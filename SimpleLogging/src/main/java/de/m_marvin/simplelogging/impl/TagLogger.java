package de.m_marvin.simplelogging.impl;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.Logger;

public class TagLogger  extends SimpleLogger {
	
	@FunctionalInterface
	public static interface LogFilter {
		public boolean test(LogLevel level, String tag);
	}
	
	protected final Logger logger;
	protected final String tag;
	protected final boolean isSuffix;

	public TagLogger(Logger logger, String tag) {
		this(logger, tag, false);
	}
	
	public TagLogger(Logger logger, String tag, boolean isSuffix) {
		this.logger = logger;
		this.tag = tag;
		this.isSuffix = isSuffix;
	}
	
	@Override
	public void logt(LogLevel level, String tag, String msg, Object... args) {
		String ntag = this.isSuffix ? tag + this.tag : this.tag + tag;
		this.logger.logt(level, ntag, msg, args);
	}

	@Override
	public void print(LogLevel level, String msg) {}
	
}
