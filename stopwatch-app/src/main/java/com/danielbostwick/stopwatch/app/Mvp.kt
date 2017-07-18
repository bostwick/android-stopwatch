package com.danielbostwick.stopwatch.app

interface MvpPresenter

interface MvpView<in P : MvpPresenter> {
    fun setPresenter(presenter: P)
}
