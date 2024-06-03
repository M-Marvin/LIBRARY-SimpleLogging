package de.m_marvin.simplelogging.printing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import de.m_marvin.simplelogging.filehandling.LogFileHandler;

public class Logger {
	
	private static Logger defaultLogger = new Logger();
	
	public static void setDefaultLogger(Logger logger) {
		defaultLogger = logger;
	}
	
	public static Logger defaultLogger() {
		return defaultLogger;
	}
	
	protected Function<LocalDateTime, String> dateTimeFormat;
	protected OutputStream[] infoStream;
	protected OutputStream[] errorStream;
	protected OutputStream[] warnStream;
	protected Runnable closeActions;

	public Logger() {
		this(
				new OutputStream[] {System.out}, 
				new OutputStream[] {System.err}, 
				new OutputStream[] {System.err}
			);
	}
	
	public Logger(File... logFiles) {
		OutputStream[] logFileStreams = Stream.of(logFiles).map(file -> {
			try {
				return new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				System.err.println("Failed to open log file!");
				e.printStackTrace();
				return OutputStream.nullOutputStream();
			}
		}).toArray(i -> new OutputStream[i]);
		this.infoStream = Arrays.copyOf(logFileStreams, logFileStreams.length + 1);
		this.infoStream[logFileStreams.length] = System.out;
		this.warnStream = Arrays.copyOf(logFileStreams, logFileStreams.length + 1);
		this.warnStream[logFileStreams.length] = System.err;
		this.errorStream = Arrays.copyOf(logFileStreams, logFileStreams.length + 1);
		errorStream[logFileStreams.length] = System.err;
		this.closeActions = () -> {
			for (OutputStream fileStream : logFileStreams) {
				try {
					fileStream.close();
				} catch (IOException e) {
					System.err.println("Failed to close log output stream!");
					e.printStackTrace();
				}
			}
		};
		this.dateTimeFormat = LogFileHandler::fileNameFormatted;
	}
	
	public Logger(OutputStream[] infoStream, OutputStream[] warnStream, OutputStream[] errorStream) {
		this(infoStream, warnStream, errorStream, () -> {});
	}

	public Logger(OutputStream[] infoStream, OutputStream[] warnStream, OutputStream[] errorStream, Runnable closeActions) {
		this.infoStream = infoStream;
		this.warnStream = warnStream;
		this.errorStream = errorStream;
		this.closeActions = closeActions;
		this.dateTimeFormat = LogFileHandler::fileNameFormatted;
	}
	
	public String getTimeString() {
		return this.dateTimeFormat.apply(LocalDateTime.now());
	}
	
	public void setDateTimeFormat(Function<LocalDateTime, String> dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}
	
	public void close() {
		releaseSystemStreams();
		closeActions.run();
	}
	
	public void logInfo(String msg) {
		log(LogType.INFO, null, msg);
	}
	public void logInfo(String tag, String msg) {
		log(LogType.INFO, tag, msg);
	}
	public void logInfo(String tag, String format, Object... arg) {
		log(LogType.INFO, tag, format, arg);
	}
	
	public void logWarn(String msg) {
		log(LogType.WARN, null, msg);
	}
	public void logWarn(String tag, String msg) {
		log(LogType.WARN, tag, msg);
	}
	public void logWarn(String tag, String format, Object... arg) {
		log(LogType.WARN, tag, format, arg);
	}
	
	public void logError(String msg) {
		log(LogType.ERROR, null, msg);
	}
	public void logError(String tag, String msg) {
		log(LogType.ERROR, tag, msg);
	}
	public void logError(String tag, String format, Object... arg) {
		log(LogType.ERROR, tag, format, arg);
	}
	
	public void log(LogType target, String tag, String format, Object... arg) {
		log(target, tag, String.format(format, arg));
	}
	
	public void log(LogType target, String tag, String msg) {
		println(target, getTimeString() + ": [" + target.name() + (tag != null ? "/" + tag : "") + "] " + msg);
	}
	
	public void println(LogType target, String msg) {
		print(target, msg + "\n");
	}
	
	public void print(LogType target, String msg) {
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
			System.err.println("Failed to write log string to streams!");
			e.printStackTrace();
		}
	}
	
	public void printException(LogType target, Throwable e) {
		printException(target, null, e);
	}
	
	public void printException(LogType target, String tag, Throwable e) {
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
	
	public void printExceptionInfo(Throwable e) {
		printException(LogType.INFO, e);
	}
	public void printExceptionInfo(String tag, Throwable e) {
		printException(LogType.INFO, tag, e);
	}

	public void printExceptionWarn(Throwable e) {
		printException(LogType.WARN, e);
	}
	public void printExceptionWarn(String tag, Throwable e) {
		printException(LogType.WARN, tag, e);
	}
	
	public void printExceptionError(Throwable e) {
		printException(LogType.ERROR, e);
	}
	public void printExceptionError(String tag, Throwable e) {
		printException(LogType.ERROR, tag, e);
	}
	
	public PrintStream outPrintStream() {
		return new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				for (OutputStream os : Logger.this.infoStream) os.write(b);
			}
		});
	}

	public PrintStream errPrintStream() {
		return new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				for (OutputStream os : Logger.this.errorStream) os.write(b);
			}
		});
	}
	
	protected PrintStream standardOutStream;
	protected PrintStream standardErrStream;
	
	public void catchSystemStreams() {
		if (this.standardOutStream == null) {
			this.standardOutStream = System.out;
			this.standardErrStream = System.err;
			System.setOut(outPrintStream());
			System.setErr(errPrintStream());
		}
	}
	
	public void releaseSystemStreams() {
		if (this.standardOutStream != null) {
			System.setOut(standardOutStream);
			System.setErr(standardErrStream);
			this.standardOutStream = null;
			this.standardErrStream = null;
		}
	}
	
}
