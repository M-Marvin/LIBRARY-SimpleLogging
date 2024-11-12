package de.m_marvin.simplelogging.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.FormatLogger;

public class MultiLogger extends SimpleLogger {

	protected final List<FormatLogger> loggers;
	
	public MultiLogger() {
		this(new ArrayList<>());
	}
	
	public MultiLogger(FormatLogger... loggers) {
		this(Arrays.asList(loggers));
	}
	
	public MultiLogger(List<FormatLogger> loggers) {
		this.loggers = loggers;
	}
	
	public List<FormatLogger> getLoggers() {
		return loggers;
	}
	
	@Override
	public void logt(LogLevel level, String tag, String msg, Object... args) {
		this.loggers.forEach(l -> l.logt(level, tag, msg, args));
	}

	@Override
	public void print(LogLevel level, String msg) {}
	
}
