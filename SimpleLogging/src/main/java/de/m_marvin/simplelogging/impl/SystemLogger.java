package de.m_marvin.simplelogging.impl;

import de.m_marvin.simplelogging.LogLevel;

public class SystemLogger extends SimpleLogger {

	protected final boolean colored;
	
	public SystemLogger(boolean colored) {
		this.colored = colored;
	}
	
	public SystemLogger() {
		this(true);
	}
	
	@Override
	public void print(LogLevel level, String msg) {
		if (level == LogLevel.ERROR || level == LogLevel.WARN) {
			System.err.println((this.colored ? level.getFormat() : "") + msg);
		} else {
			System.out.println((this.colored ? level.getFormat() : "") + msg);
		}
	}

}
