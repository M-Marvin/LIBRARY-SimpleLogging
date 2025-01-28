package de.m_marvin.simplelogging.impl;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.Logger;

public class SynchronizedLogger  extends SimpleLogger {
	
	protected final Logger logger;
	
	public SynchronizedLogger(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void logt(LogLevel level, String tag, String msg, Object... args) {
		synchronized (this) {
			this.logger.logt(level, tag, msg, args);
		}
	}

	@Override
	public void print(LogLevel level, String msg) {}
	
}
