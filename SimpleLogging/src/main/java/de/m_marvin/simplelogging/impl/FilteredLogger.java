package de.m_marvin.simplelogging.impl;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.Logger;

public class FilteredLogger  extends SimpleLogger {
	
	@FunctionalInterface
	public static interface LogFilter {
		public boolean test(LogLevel level, String tag);
	}
	
	protected final Logger logger;
	protected final LogFilter filter;
	
	public FilteredLogger(Logger logger, LogFilter filter) {
		this.logger = logger;
		this.filter = filter;
	}
	
	@Override
	public void logt(LogLevel level, String tag, String msg, Object... args) {
		if (this.filter.test(level, tag)) this.logger.logt(level, tag, msg, args);
	}

	@Override
	public void print(LogLevel level, String msg) {
		if (this.filter.test(level, "")) this.logger.print(level, msg);
	}
	
}
