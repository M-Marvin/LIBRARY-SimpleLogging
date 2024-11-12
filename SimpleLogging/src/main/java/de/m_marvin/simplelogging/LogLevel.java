package de.m_marvin.simplelogging;

import java.util.HashMap;
import java.util.Map;

public class LogLevel {

	private static final Map<String, LogLevel> levels = new HashMap<>();
	private static int levelLen = 0;

	public static final LogLevel DEBUG = LogLevel.of("DEBUG");
	public static final LogLevel INFO = LogLevel.of("INFO");
	public static final LogLevel WARN = LogLevel.of("WARN");
	public static final LogLevel ERROR = LogLevel.of("ERROR");
	
	private String name;
	
	private LogLevel(String name) {
		this.name = name;
		if (this.name.length() > levelLen) levelLen = this.name.length();
	}
	
	public static LogLevel of(String name) {
		if (!levels.containsKey(name)) {
			levels.put(name, new LogLevel(name));
		}
		return levels.get(name);
	}
	
	@Override
	public String toString() {
		return String.format("%1$" + levelLen + "s", this.name);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}
	
}
