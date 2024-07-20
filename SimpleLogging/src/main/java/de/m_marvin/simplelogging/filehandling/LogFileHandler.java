package de.m_marvin.simplelogging.filehandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import de.m_marvin.simplelogging.printing.Logger;

public class LogFileHandler {
	
	public static final String LATEST_LOG_NAME = "latest";
	public static final String LOG_FILE_FORMAT = "log";
	
	protected File logFileFolder;
	protected String logApplicationName;
	
	protected Logger logger;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;
	
	public LogFileHandler(File logFileFolder, String applicationName) {
		this.logFileFolder = logFileFolder;
		this.logApplicationName = applicationName;
	}
	
	public LogFileHandler stopBeforeExit() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { if (this.isLogging()) this.endLogging(); }));
		return this;
	}
	
	public File getLogFileFolder() {
		return logFileFolder;
	}
	
	public String getLogApplicationName() {
		return logApplicationName;
	}
	
	public LocalDateTime getLogStartTime() {
		return startTime;
	}
	
	public LocalDateTime getLogEndTime() {
		return endTime;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public boolean isLogging() {
		return this.logger != null;
	}
	
	public Logger beginLogging() {
		if (this.logger != null) throw new IllegalStateException("A log-session is already started!");
		this.startTime = LocalDateTime.now();
		this.logger = new Logger(getCurrentIncompleteLogFile(), getLatestLogFile());
		return this.logger;
	}
	
	public void endLogging() {
		if (this.logger == null) throw new IllegalStateException("No log-session is active!");
		this.logger.close();
		this.logger = null;
		this.endTime = LocalDateTime.now();
		storeLatestFile();
	}
	
	public File getLatestLogFile() {
		return new File(this.logFileFolder, "/" + LATEST_LOG_NAME + "." + LOG_FILE_FORMAT);
	}
	
	public File getCurrentIncompleteLogFile() {
		return new File(this.logFileFolder, "/" + logApplicationName + "-" + timeFormated(this.startTime) + "-INCOMPLETED." + LOG_FILE_FORMAT);
	}
	
	public static String timeFormated(LocalDateTime time) {
		return String.format("%02d.%02d.%04d_%02d.%02d.%02d", 
				time.getDayOfMonth(), 
				time.getMonthValue(), 
				time.getYear(), 
				time.getHour(), 
				time.getMinute(), 
				time.getSecond());
	}
	
	protected void storeLatestFile() {
		try {
			String fileName = logApplicationName + "-" + timeFormated(startTime) + "-" + timeFormated(endTime) + "." + LOG_FILE_FORMAT;
			Files.move(getLatestLogFile().toPath(), new File(getLogFileFolder(), "/" + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.delete(getCurrentIncompleteLogFile().toPath());
		} catch (IOException e) {
			System.err.println("Failed to rename latest.log!");
			e.printStackTrace();
		}
	}
	
}
