package com.danielbostwick.stopwatch.app.stopwatch

import com.danielbostwick.stopwatch.ext.toTimeElapsedString
import com.danielbostwick.stopwatch.core.event.StopwatchPaused
import com.danielbostwick.stopwatch.core.event.StopwatchStarted
import com.danielbostwick.stopwatch.core.event.StopwatchWasReset
import com.danielbostwick.stopwatch.core.manager.StopwatchManager
import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState
import com.danielbostwick.stopwatch.core.service.StopwatchService
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.joda.time.DateTime

class StopwatchPresenter(val view: StopwatchContract.View,
                         val eventBus: EventBus,
                         val stopwatchManager: StopwatchManager,
                         val stopwatchService: StopwatchService) : StopwatchContract.Presenter {
    var stopwatch: Stopwatch

    init {
        stopwatch = stopwatchManager.getStopwatch()
    }

    override fun onResume() {
        eventBus.register(this)
        stopwatch = stopwatchManager.getStopwatch()

        when (stopwatch.state) {
            StopwatchState.PAUSED -> view.showStartResetButtons()
            StopwatchState.STARTED -> view.showPauseButton()
        }
    }

    override fun onPause() {
        eventBus.unregister(this)
    }

    override fun onStopwatchStartClicked() {
        stopwatchService.start(stopwatch, DateTime.now())
    }

    override fun onStopwatchPauseClicked() {
        stopwatchService.pause(stopwatch, DateTime.now())
    }

    override fun onStopwatchResetClicked() {
        stopwatchService.reset(stopwatch)
    }

    override fun getStopwatchTimeElapsed() =
        stopwatchService.timeElapsed(stopwatch, DateTime.now()).toTimeElapsedString()

    @Subscribe fun onStopwatchStarted(event: StopwatchStarted) {
        this.stopwatch = event.stopwatch
        view.showPauseButton()
    }

    @Subscribe fun onStopwatchPaused(event: StopwatchPaused) {
        this.stopwatch = event.stopwatch
        view.showStartResetButtons()
    }

    @Subscribe fun onStopwatchReset(event: StopwatchWasReset) {
        this.stopwatch = event.stopwatch
        view.showStartResetButtons()
    }
}
