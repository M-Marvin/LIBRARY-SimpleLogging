package de.m_marvin.simplelogging.impl;

import de.m_marvin.simplelogging.LogLevel;

public class SystemLogger extends SimpleLogger {

	@Override
	public void print(LogLevel level, String msg) {
		if (level == LogLevel.ERROR || level == LogLevel.WARN) {
			System.err.println(msg);
		} else {
			System.out.println(msg);
		}
	}

}
