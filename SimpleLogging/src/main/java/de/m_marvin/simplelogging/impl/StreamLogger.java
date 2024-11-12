package de.m_marvin.simplelogging.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import de.m_marvin.simplelogging.LogLevel;

public class StreamLogger extends SimpleLogger {
	
	public final OutputStream stream;
	public final Charset charset;

	public StreamLogger(OutputStream stream) {
		this(stream, StandardCharsets.UTF_8);
	}
	
	public StreamLogger(OutputStream stream, Charset charset) {
		this.stream = stream;
		this.charset = charset;
	}
	
	public OutputStream getStream() {
		return stream;
	}

	public Charset getCharset() {
		return charset;
	}
	
	@Override
	public void print(LogLevel level, String msg) {
		try {
			this.stream.write(msg.getBytes(this.charset));
			this.stream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
