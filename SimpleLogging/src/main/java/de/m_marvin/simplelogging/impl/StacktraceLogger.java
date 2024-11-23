package de.m_marvin.simplelogging.impl;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.Logger;

public class StacktraceLogger extends SimpleLogger {

	protected final Logger logger;
	
	public StacktraceLogger(Logger logger) {
		this.logger = logger;
	}
	
	@Override
	public void logt(LogLevel level, String tag, String msg, Object... args) {
		this.logger.logt(level, tag, msg, args);
		boolean b = false;
		for (Object arg : args) {
			if (arg instanceof Throwable e) {
				this.logger.logt(level, tag, "");
				e.printStackTrace(this.logger.printer(level, tag));
				b = true;
			}
		}
		if (b) this.logger.logt(level, tag, "");
	}

	@Override
	public void print(LogLevel level, String msg) {}
	
}
