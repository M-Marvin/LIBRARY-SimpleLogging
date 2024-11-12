package de.m_marvin.simplelogging.api;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDateTime;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.LogWriter;

public interface Logger {

	public default void warnt(String tag, String msg, Object... args) { logt(LogLevel.WARN, tag, msg, args); }
	public default void warn(String msg, Object... args) { log(LogLevel.WARN, msg, args); }

	public default PrintWriter warnPrinter() { return printer(LogLevel.WARN); }
	public default PrintWriter warnPrinter(String tag) { return printer(LogLevel.WARN, tag); }
	
	public default Writer warnWriter() { return writer(LogLevel.WARN); }
	public default Writer warnWriter(String tag) { return writer(LogLevel.WARN, tag); }
	
	
	public default void infot(String tag, String msg, Object... args) { logt(LogLevel.INFO, tag, msg, args); }
	public default void info(String msg, Object... args) { log(LogLevel.INFO, msg, args); }

	public default PrintWriter infoPrinter() { return printer(LogLevel.INFO); }
	public default PrintWriter infoPrinter(String tag) { return printer(LogLevel.INFO, tag); }
	
	public default Writer infoWriter() { return writer(LogLevel.INFO); }
	public default Writer infoWriter(String tag) { return writer(LogLevel.INFO, tag); }
	
	
	public default void errort(String tag, String msg, Object... args) { logt(LogLevel.ERROR, tag, msg, args); }
	public default void error(String msg, Object... args) { log(LogLevel.ERROR, msg, args); }

	public default PrintWriter errorPrinter() { return printer(LogLevel.ERROR); }
	public default PrintWriter errorPrinter(String tag) { return printer(LogLevel.ERROR, tag); }
	
	public default Writer errorWriter() { return writer(LogLevel.ERROR); }
	public default Writer errorWriter(String tag) { return writer(LogLevel.ERROR, tag); }
	
	
	public default void debugt(String tag, String msg, Object... args) { logt(LogLevel.DEBUG, tag, msg, args); }
	public default void debug(String msg, Object... args) { log(LogLevel.DEBUG, msg, args); }

	public default PrintWriter debugPrinter() { return printer(LogLevel.DEBUG); }
	public default PrintWriter debugPrinter(String tag) { return printer(LogLevel.DEBUG, tag); }
	
	public default Writer debugWriter() { return writer(LogLevel.DEBUG); }
	public default Writer debugWriter(String tag) { return writer(LogLevel.DEBUG, tag); }
	
	
	public void logt(LogLevel level, String tag, String msg, Object... args);
	public default void log(LogLevel level, String msg, Object... args) { logt(level, "", msg, args); }
	
	public default PrintWriter printer(LogLevel level) { return new PrintWriter(writer(level)); }
	public default PrintWriter printer(LogLevel level, String tag) { return new PrintWriter(writer(level, tag)); }
	
	public default Writer writer(LogLevel level) { return writer(level, ""); }
	public default Writer writer(LogLevel level, String tag) { return new LogWriter(this, level, tag); }
	
	public default LocalDateTime logTime() { return LocalDateTime.now(); }
	public String logTimeStr();
	
}
