package de.m_marvin.simplelogging;

import java.io.File;
import java.io.IOException;

import de.m_marvin.simplelogging.api.Logger;
import de.m_marvin.simplelogging.impl.FilteredLogger;
import de.m_marvin.simplelogging.impl.MultiLogger;
import de.m_marvin.simplelogging.impl.StacktraceLogger;
import de.m_marvin.simplelogging.impl.StreamLogger;
import de.m_marvin.simplelogging.impl.SystemLogger;

public class Test {
	
	public static void main(String[] args) {
		
		File runDir = new File(new File(Test.class.getClassLoader().getResource("").getPath().substring(1)).getParentFile().getParentFile(), "run");
		System.out.println("Run/Log-Folder: " + runDir);
		
		LogFileProvider logManager = new LogFileProvider(runDir).setName("ExampleLog").stopBeforeExit();
		Logger logger;
		try {
			logger = new StacktraceLogger(new MultiLogger(new FilteredLogger(new StreamLogger(logManager.beginLogging()), (level, tag) -> level != LogLevel.DEBUG), new SystemLogger()));
		} catch (IOException e) {
			logger = Log.defaultLogger();
			logger.warnt("init", "failed to create log file", e);
		}
		
		Log.setDefaultLogger(logger);
		
		logger.infot("test/test", "test info %s", "1");
		logger.debugt("test/test", "test debug %s", "2");
		logger.errort("test/test", "test error %s", "3");
		logger.warnt("test/test", "test warn %s", "4");
		
//		System.exit(-1);
		
		Throwable e = new Exception("test");
		
		logger.log(LogLevel.INFO, "Test1");
		logger.log(LogLevel.ERROR, "Test1", e);
		
		logManager.endLogging();
		
	}
	
}
