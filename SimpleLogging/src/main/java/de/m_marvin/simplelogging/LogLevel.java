package de.m_marvin.simplelogging;

import java.util.HashMap;
import java.util.Map;

public class LogLevel {

	private static final Map<String, LogLevel> levels = new HashMap<>();
	private static int levelLen = 0;

	public static final LogLevel DEBUG = LogLevel.make("DEBUG", "\033[38;5;109m");
	public static final LogLevel INFO = LogLevel.make("INFO", "\033[38;5;231m");
	public static final LogLevel WARN = LogLevel.make("WARN", "\033[38;5;190m");
	public static final LogLevel ERROR = LogLevel.make("ERROR", "\033[38;5;196m");
	
	private String name;
	private String format;
	
	private LogLevel(String name, String format) {
		this.name = name;
		this.format = format;
		if (this.name.length() > levelLen) levelLen = this.name.length();
	}

	public static LogLevel make(String name, String format) {
		if (!levels.containsKey(name)) {
			levels.put(name, new LogLevel(name, format));
		}
		return levels.get(name);
	}
	
	public static LogLevel of(String name) {
		if (!levels.containsKey(name)) {
			levels.put(name, new LogLevel(name, "\033[38;5;231m"));
		}
		return levels.get(name);
	}
	
	public String getFormat() {
		return format;
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
