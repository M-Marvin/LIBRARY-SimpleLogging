package de.m_marvin.simplelogging.filehandling;

import java.io.File;
import java.io.FileNotFoundException;
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
	
	public File getLogFileFolder() {
		return logFileFolder;
	}
	
	public File getLatestLogFile() {
		return new File(this.logFileFolder, "/" + LATEST_LOG_NAME + "." + LOG_FILE_FORMAT);
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
	
	public Logger beginLogging() throws FileNotFoundException {
		if (this.logger != null) throw new IllegalStateException("A log-session is already started!");
		this.startTime = LocalDateTime.now();
		this.logger = new Logger(getLatestLogFile());
		return this.logger;
	}
	
	public void endLogging() throws IOException {
		if (this.logger == null) throw new IllegalStateException("No log-session is active!");
		this.logger.close();
		this.logger = null;
		this.endTime = LocalDateTime.now();
		storeLatestFile();
	}
	
	public static String fileNameFormatted(LocalDateTime time) {
		return time.getDayOfMonth() + "." + time.getMonthValue() + "." + time.getYear() + "_" + String.format("%02d", time.getHour()) + "." + String.format("%02d", time.getMinute()) + "." + String.format("%02d", time.getSecond());
	}
	
	protected void storeLatestFile() throws IOException {
		String fileName = logApplicationName + "-" + fileNameFormatted(startTime) + "-" + fileNameFormatted(endTime) + "." + LOG_FILE_FORMAT;
		Files.move(getLatestLogFile().toPath(), new File(getLogFileFolder(), "/" + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
}
