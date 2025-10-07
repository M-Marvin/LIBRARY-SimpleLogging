package de.m_marvin.simplelogging.impl;

import java.time.LocalDateTime;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.FormatLogger;

public abstract class SimpleLogger implements FormatLogger {
	
	public static final String DEFAULT_TIME_FORMAT = "%3$02d.%2$02d.%1$04d_%4$02d:%5$02d:%6$02d";
	public static final String DEFAULT_FORMAT = "%1$s: [%2$s] %3$s: %4$s";
	
	protected String format = DEFAULT_FORMAT;
	protected String timeFormat = DEFAULT_TIME_FORMAT;
	
	protected boolean continueLine = false;
	
	@Override
	public void logt(LogLevel level, String tag, String msg, Object... args) {
		print(level, String.format(this.format, logTimeStr(), level.toString(), tag, String.format(msg, args)) + "\n");
	}
	
	@Override
	public void lognlt(LogLevel level, String tag, String msg, Object... args) {
		String s = String.format(msg, args);
		String[] lines = s.split("(\n|\r\n)", -1);
		boolean endsWithNewLine = lines.length > 1 && lines[lines.length - 1].isEmpty();
		for (int line = 0; line < (endsWithNewLine ? lines.length - 1 : lines.length); line++) {
			boolean lastLine = line == (endsWithNewLine ? lines.length - 1 : lines.length) - 1;
			boolean printNewLine = !lastLine || endsWithNewLine;
			if (this.continueLine) {
				print(level, printNewLine ? lines[line] + "\n" : lines[line]);
				this.continueLine = false;
			} else {
				print(level, String.format(this.format, logTimeStr(), level.toString(), tag, printNewLine ? lines[line] + "\n" : lines[line]));
			}
		}
		this.continueLine = !endsWithNewLine;
	}
	
	@Override
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public void setTimeFormat(String format) {
		this.timeFormat = format;
	}
	
	@Override
	public String logTimeStr() {
		LocalDateTime time = logTime();
		return String.format(this.timeFormat, time.getYear(), time.getMonthValue(), time.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond());
	}
	
}
