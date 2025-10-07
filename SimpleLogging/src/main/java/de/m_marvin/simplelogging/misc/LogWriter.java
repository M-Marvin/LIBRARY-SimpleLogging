package de.m_marvin.simplelogging.misc;

import java.io.IOException;
import java.io.Writer;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.Logger;

public class LogWriter extends Writer {

	protected final Logger logger;
	protected final LogLevel level;
	protected final boolean raw;
	protected final String tag;
	
	protected char lastChar = '\n';
	
	public LogWriter(Logger logger, LogLevel level, String tag) {
		this.logger = logger;
		this.level = level;
		this.tag = tag;
		this.raw = false;
	}

	public LogWriter(Logger logger, LogLevel level) {
		this.logger = logger;
		this.level = level;
		this.tag = null;
		this.raw = true;
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		if (this.raw) {
			this.logger.print(level, String.copyValueOf(cbuf, off, len));
		} else {
			this.logger.lognlt(level, tag, "%s", String.copyValueOf(cbuf, off, len));
		}
		this.lastChar = cbuf[off + len - 1];
	}

	@Override
	public void flush() throws IOException {
		if (this.lastChar != '\n' || this.lastChar == '\r') {
			this.logger.print(this.level, "\n");
		}
	}
	
	@Override
	public void close() throws IOException {
		this.flush();
	}

}
