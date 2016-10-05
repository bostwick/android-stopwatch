package com.redballoonsoftware.stopwatch;

import java.util.ArrayList;
import java.util.List;

public class Stopwatch {
	
	/**
	 * Implements a method that returns the current time, in milliseconds.
	 * Used for testing
	 */
	public interface GetTime {
		public long now();
	}
	
	/**
	 * Default way to get time. Just use the system clock.
	 */
	private GetTime SystemTime = new GetTime() {
		@Override
		public long now() {	return System.currentTimeMillis(); }
	};
	
	/**
	 * What is the stopwatch doing?
	 */
	public enum State { PAUSED, RUNNING };
	
	private GetTime m_time;
	private long m_startTime;
	private long m_stopTime;
	private long m_pauseOffset;
	private List<Long> m_laps = new ArrayList<Long>();
	private State m_state;
	
	public Stopwatch() {
		m_time = SystemTime;
		reset();
	}
	public Stopwatch(GetTime time) {
		m_time = time;
		reset();
	}
	
	/**
	 * Start the stopwatch running. If the stopwatch is already running, this
	 * does nothing. 
	 */
	public void start() {
		if ( m_state == State.PAUSED ) {
			m_pauseOffset = getElapsedTime();
			m_stopTime = 0;
			m_startTime = m_time.now();
			m_state = State.RUNNING;
		}
	}

	/***
	 * Pause the stopwatch. If the stopwatch is already running, do nothing.
	 */
	public void pause() {
		if ( m_state == State.RUNNING ) {
			m_stopTime = m_time.now();
			m_state = State.PAUSED;
		}
	}

	/**
	 * Reset the stopwatch to the initial state, clearing all stored times. 
	 */
	public void reset() {
		m_state = State.PAUSED;
		m_startTime 	= 0;
		m_stopTime 		= 0;
		m_pauseOffset 	= 0;
		m_laps.clear();
	}
	
	/**
	 * Record a lap at the current time.
	 */
	public void lap() {
		m_laps.add(getElapsedTime());
	}
	
	/***
	 * @return The amount of time recorded by the stopwatch, in milliseconds
	 */
	public long getElapsedTime() {
		if ( m_state == State.PAUSED ) {
			return (m_stopTime - m_startTime) + m_pauseOffset;
		} else {
			return (m_time.now() - m_startTime) + m_pauseOffset;
		}
	}
	
	/**
	 * @return A list of the laps recorded. Each lap is given as a millisecond
	 * 		   value from when the stopwatch began running. 
	 */
	public List<Long> getLaps() {
		return m_laps;
	}
	
	/**
	 * @return true if the stopwatch is currently running and recording
	 * 		   time, false otherwise.
	 */
	public boolean isRunning() {
		return (m_state == State.RUNNING);
	}
}
