package de.m_marvin.simplelogging.misc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class ReadBackLogFileProvider extends LogFileProvider {
	
	protected RandomAccessFile randomAccess;
	
	public ReadBackLogFileProvider(File logFileFolder) {
		super(logFileFolder);
	}
	
	public OutputStream beginLogging() throws IOException {
		if (this.startTime != null) throw new IllegalStateException("a log-session is already started!");
		this.startTime = LocalDateTime.now();
		try {
			this.randomAccess = new RandomAccessFile(getCurrentLogFile(), "rw");
			this.logStream = new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					synchronized (ReadBackLogFileProvider.this.randomAccess) {
						ReadBackLogFileProvider.this.randomAccess.write(b);
					}
				}
				@Override
				public void write(byte[] b, int off, int len) throws IOException {
					synchronized (ReadBackLogFileProvider.this.randomAccess) {
						ReadBackLogFileProvider.this.randomAccess.write(b, off, len);
					}
				}
			};
		} catch (IOException e) {
			this.startTime = null;
			throw new IOException("faild to create log file!", e);
		}
		return this.logStream;
	}
	
	public void endLogging() {
		if (this.startTime == null) return;
		try {
			synchronized (this.randomAccess) {
				this.logStream.close();
				this.randomAccess.close();
				this.logStream = null;
				this.randomAccess = null;
			}
		} catch (IOException e) {}
		this.endTime = LocalDateTime.now();
		storeLatestFile();
		this.startTime = null;
		this.endTime = null;
	}
	
	public long getFilePos() throws IOException {
		return this.randomAccess.getFilePointer();
	}
	
	public List<String> readBackLogs(long from, long to) throws IOException {
		synchronized (this.randomAccess) {
			long p1 = this.randomAccess.getFilePointer();
			this.randomAccess.seek(from);
			byte[] buffer = new byte[0x4000];
			StringBuffer textBuffer = new StringBuffer();
			long p = from;
			while (p < to) {
				int len = (int) Math.min(buffer.length, to - p);
				this.randomAccess.read(buffer, 0, len);
				textBuffer.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
				p += len;
			}
			this.randomAccess.seek(p1);
			return textBuffer.toString().lines().toList();
		}
	}
	
	public List<String> readBackLogs() throws IOException {
		return readBackLogs(0, getFilePos());
	}
	
}
