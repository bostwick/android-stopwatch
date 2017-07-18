package com.danielbostwick.stopwatch.app.stopwatch

import com.danielbostwick.stopwatch.app.MvpPresenter
import com.danielbostwick.stopwatch.app.MvpView

interface StopwatchContract {
    interface Presenter : MvpPresenter {
        fun onStopwatchStartClicked()
        fun onStopwatchPauseClicked()
        fun onStopwatchResetClicked()
        fun getStopwatchTimeElapsed(): String

        fun onResume()
        fun onPause()
    }

    interface View : MvpView<Presenter> {
        fun showStartResetButtons()
        fun showPauseButton()
    }
}
