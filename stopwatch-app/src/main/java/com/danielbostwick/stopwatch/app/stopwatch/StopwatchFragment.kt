package com.danielbostwick.stopwatch.app.stopwatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.danielbostwick.stopwatch.R
import com.danielbostwick.stopwatch.app.BaseFragment
import com.danielbostwick.stopwatch.app.StopwatchApplication
import com.danielbostwick.stopwatch.core.manager.StopwatchManager
import com.danielbostwick.stopwatch.core.service.StopwatchService
import java.util.*

class StopwatchFragment : BaseFragment(), StopwatchContract.View {
    private val TAG = StopwatchFragment::class.java.simpleName
    private val UPDATE_DELAY: Long = 100

    private val eventBus = StopwatchApplication.instance.eventBus
    private val stopwatchService = StopwatchApplication.instance.stopwatchService
    private lateinit var updateTimer: Timer

    @BindView(R.id.fragment_stopwatch_start) lateinit var startButton: Button
    @BindView(R.id.fragment_stopwatch_reset) lateinit var resetButton: Button
    @BindView(R.id.fragment_stopwatch_pause) lateinit var pauseButton: Button
    @BindView(R.id.fragment_stopwatch_time) lateinit var timeElapsedText: TextView
    @BindView(R.id.fragment_stopwatch_start_reset_container) lateinit var startResetContainer: View
    @BindView(R.id.fragment_stopwatch_pause_container) lateinit var pauseContainer: View

    private lateinit var presenter: StopwatchContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater!!.inflate(R.layout.fragment_stopwatch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated()")

        ButterKnife.bind(this, view)
        setPresenter(StopwatchPresenter(this, eventBus, stopwatchService as StopwatchManager,
            stopwatchService as StopwatchService))

        startButton.setOnClickListener { presenter.onStopwatchStartClicked() }
        pauseButton.setOnClickListener { presenter.onStopwatchPauseClicked() }
        resetButton.setOnClickListener { presenter.onStopwatchResetClicked() }
    }

    override fun onResume() {
        super.onResume()
        updateTimer = Timer()

        presenter.onResume()
        updateTimer.scheduleAtFixedRate(updateText, 0, UPDATE_DELAY)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
        updateTimer.cancel()
    }

    override fun setPresenter(presenter: StopwatchContract.Presenter) {
        this.presenter = presenter
    }

    override fun showStartResetButtons() {
        startResetContainer.visibility = View.VISIBLE
        pauseContainer.visibility = View.GONE
    }

    override fun showPauseButton() {
        startResetContainer.visibility = View.GONE
        pauseContainer.visibility = View.VISIBLE
    }

    private val updateText = object : TimerTask() {
        override fun run() {
            activity.runOnUiThread { timeElapsedText.text = presenter.getStopwatchTimeElapsed() }
        }
    }
}
