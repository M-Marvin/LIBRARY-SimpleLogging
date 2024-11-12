package de.m_marvin.simplelogging;

import de.m_marvin.simplelogging.api.Logger;
import de.m_marvin.simplelogging.impl.StacktraceLogger;
import de.m_marvin.simplelogging.impl.SystemLogger;

public class Log {
	
	private static Logger defaultLogger = new StacktraceLogger(new SystemLogger());
	
	public static void setDefaultLogger(Logger defaultLogger) {
		Log.defaultLogger = defaultLogger;
	}
	
	public static Logger defaultLogger() {
		return defaultLogger;
	}
	
}
