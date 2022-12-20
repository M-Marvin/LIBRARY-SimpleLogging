package de.m_marvin.simplelogging.printing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	
	private static Logger defaultLogger;
	
	public static void setDefaultLogger(Logger logger) {
		defaultLogger = logger;
	}
	
	public static Logger defaultLogger() {
		return defaultLogger;
	}
	
	protected DateTimeFormatter dateTimeFormat;
	protected OutputStream[] infoStream;
	protected OutputStream[] errorStream;
	protected OutputStream[] warnStream;
	protected Runnable closeActions;

	public Logger() {
		this(
				new OutputStream[] {System.out}, 
				new OutputStream[] {System.err}, 
				new OutputStream[] {System.err}, 
				() -> {}
			);
	}

	public Logger(File logFile) throws FileNotFoundException {
		this(new FileOutputStream(logFile), true);
	}
	
	public Logger(FileOutputStream logFileStream, boolean closeFileOnEnd) {
		this(
				new OutputStream[] {System.out, logFileStream}, 
				new OutputStream[] {System.err, logFileStream}, 
				new OutputStream[] {System.err, logFileStream}, 
				() -> {
					if (closeFileOnEnd)
						try {
							logFileStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			);
	}
	
	public Logger(OutputStream[] infoStream, OutputStream[] warnStream, OutputStream[] errorStream) {
		this(infoStream, warnStream, errorStream, () -> {});
	}

	public Logger(OutputStream[] infoStream, OutputStream[] warnStream, OutputStream[] errorStream, Runnable closeActions) {
		this.infoStream = infoStream;
		this.warnStream = warnStream;
		this.errorStream = errorStream;
		this.closeActions = closeActions;
		this.dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;
	}

	public String getTimeString() {
		return LocalDateTime.now().format(this.dateTimeFormat);
	}
	
	public void setDateTimeFormat(DateTimeFormatter dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}
	
	public void close() throws IOException {
		closeActions.run();
	}
	
	public void logInfo(String msg) {
		log(LogType.INFO, null, msg);
	}
	public void logInfo(String tag, String msg) {
		log(LogType.INFO, tag, msg);
	}

	public void logWarn(String msg) {
		log(LogType.WARN, null, msg);
	}
	public void logWarn(String tag, String msg) {
		log(LogType.WARN, tag, msg);
	}

	public void logError(String msg) {
		log(LogType.ERROR, null, msg);
	}
	public void logError(String tag, String msg) {
		log(LogType.ERROR, tag, msg);
	}
	
	public void log(LogType target, String tag, String msg) {
		println(target, getTimeString() + ": [" + target.name() + (tag != null ? "\\" + tag : "") + "] " + msg);
	}
	
	public void println(LogType target, String msg) {
		print(target, msg + "\n");
	}
	
	public void print(LogType target,String msg) {
		try {
			switch (target) {
			case INFO:
				for (OutputStream os : this.infoStream) os.write(msg.getBytes());
				break;
			case WARN:
				for (OutputStream os : this.warnStream) os.write(msg.getBytes());
				break;
			case ERROR:
				for (OutputStream os : this.errorStream) os.write(msg.getBytes());
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printException(LogType target, Exception e) {
		printException(target, null, e);
	}
	
	public void printException(LogType target, String tag, Exception e) {
		log(target, tag, e.getMessage());
		switch (target) {
		case INFO:
			for (OutputStream os : this.infoStream) e.printStackTrace(new PrintStream(os));
			break;
		case WARN:
			for (OutputStream os : this.warnStream) e.printStackTrace(new PrintStream(os));
			break;
		case ERROR:
			for (OutputStream os : this.errorStream) e.printStackTrace(new PrintStream(os));
			break;
		}
	}
	
}
