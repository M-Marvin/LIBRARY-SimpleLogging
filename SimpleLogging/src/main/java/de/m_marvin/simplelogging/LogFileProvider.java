package de.m_marvin.simplelogging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import de.m_marvin.simplelogging.impl.SimpleLogger;

public class LogFileProvider {
	
	protected File logFileFolder;
	protected String name = "log";
	protected String timeFormat = SimpleLogger.DEFAULT_TIME_FORMAT.replace(':', '.');
	protected String format = "%1$s-%2$s-%3$s.log";
	
	protected LocalDateTime startTime = null;
	protected OutputStream logStream = null;
	protected LocalDateTime endTime = null;
	
	public LogFileProvider(File logFileFolder) {
		this.logFileFolder = logFileFolder;
	}
	
	public LogFileProvider stopBeforeExit() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { if (this.isLogging()) this.endLogging(); }));
		return this;
	}
	
	public LogFileProvider setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
		return this;
	}
	
	public LogFileProvider setFileNameFormat(String format) {
		this.format = format;
		return this;
	}
	
	public LogFileProvider setName(String prefix) {
		this.name = prefix;
		return this;
	}
	
	public String getTimeFormat() {
		return timeFormat;
	}
	
	public String getFileNameFormat() {
		return format;
	}
	
	public String getName() {
		return name;
	}
	
	public File getLogFileFolder() {
		return logFileFolder;
	}
	
	
	
	public LocalDateTime getLogStartTime() {
		return startTime;
	}
	
	public LocalDateTime getLogEndTime() {
		return endTime;
	}
	
	public boolean isLogging() {
		return this.startTime != null;
	}
	
	public OutputStream beginLogging() throws IOException {
		if (this.startTime != null) throw new IllegalStateException("a log-session is already started!");
		this.startTime = LocalDateTime.now();
		try {
			this.logStream = new FileOutputStream(getCurrentLogFile());
		} catch (IOException e) {
			this.startTime = null;
			throw new IOException("faild to create log file!", e);
		}
		return this.logStream;
	}
	
	public void endLogging() {
		if (this.startTime == null) return;
		try {
			this.logStream.close();
			this.logStream = null;
		} catch (IOException e) {}
		this.endTime = LocalDateTime.now();
		storeLatestFile();
		this.startTime = null;
		this.endTime = null;
	}
	
	public File getCurrentLogFile() {
		return new File(this.logFileFolder, String.format(this.format, this.name, timeString(this.startTime), "INCOMPLETE"));
	}
	
	public String timeString(LocalDateTime time) {
		return String.format(this.timeFormat, time.getYear(), time.getMonthValue(), time.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond());
	}
	
	protected void storeLatestFile() {
		try {
			String fileName = String.format(this.format, this.name, timeString(this.startTime), timeString(this.endTime));
			Files.move(getCurrentLogFile().toPath(), new File(getLogFileFolder(), "/" + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println(String.format("Failed to rename %s!", getCurrentLogFile().getName()));
			e.printStackTrace();
		}
	}
	
}
