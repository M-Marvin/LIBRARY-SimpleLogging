package de.m_marvin.simplelogging.impl;

import java.util.function.Consumer;

import de.m_marvin.simplelogging.LogLevel;

public class LineConsumerLogger extends SimpleLogger {

	protected final boolean colored;
	protected final Consumer<String> consumer;
	
	public LineConsumerLogger(Consumer<String> consumer) {
		this(consumer, false);
	}

	public LineConsumerLogger(Consumer<String> consumer, boolean colored) {
		this.consumer = consumer;
		this.colored = colored;
	}
	
	public Consumer<String> getConsumer() {
		return consumer;
	}
	
	@Override
	public void print(LogLevel level, String msg) {
		StringBuffer buf = new StringBuffer();
		if (this.colored) buf.append(level.getFormat());
		buf.append(msg);
		if (this.colored) buf.append(level.getFormatReset());
		this.consumer.accept(buf.toString());
	}
	
}
