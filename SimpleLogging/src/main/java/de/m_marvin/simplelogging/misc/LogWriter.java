package de.m_marvin.simplelogging.misc;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import de.m_marvin.simplelogging.LogLevel;
import de.m_marvin.simplelogging.api.Logger;

public class LogWriter extends Writer {

	protected final Logger logger;
	protected final LogLevel level;
	protected final boolean raw;
	protected final String tag;
	
	String lastLine = null;
	
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
		String s = new String(Arrays.copyOfRange(cbuf, off, off + len)).replace("\r\n", "\n").replace('\r', '\n');
		int i;
		while ((i = s.indexOf('\n')) != -1) {
			if (this.raw) {
				this.logger.print(this.level, (this.lastLine != null ? this.lastLine : "") + s.substring(0, i));
			} else {
				this.logger.logt(this.level, this.tag, "%s", (this.lastLine != null ? this.lastLine : "") + s.substring(0, i));
			}
			s = s.substring(i + 1);
			this.lastLine = null;
		}
		if (!s.isEmpty()) {
			this.lastLine = s;
		}
	}

	@Override
	public void flush() throws IOException {
		if (this.lastLine != null) {
			if (this.raw) {
				this.logger.print(this.level, this.lastLine);
			} else {
				this.logger.logt(this.level, this.tag, this.lastLine);
			}
			this.lastLine = null;
		}
	}

	@Override
	public void close() throws IOException {
		this.flush();
	}

}
