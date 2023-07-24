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
	
	private static Logger defaultLogger;
	
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
				new OutputStream[] {System.err}, 
				() -> {}
			);
	}

	public Logger(File... logFiles) {
		this(Stream.of(logFiles).map(file -> {
			try {
				return new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				System.err.println("Failed to open log file!");
				e.printStackTrace();
				return null;
			}
		}).toArray(i -> new OutputStream[i]));
	}
	
	public Logger(OutputStream... logFileStreams) {
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
	
	public void close() throws IOException {
		releaseSystemStreams();
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
