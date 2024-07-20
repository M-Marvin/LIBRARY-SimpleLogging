package de.m_marvin.simplelogging;

import java.io.File;

import de.m_marvin.simplelogging.filehandling.LogFileHandler;
import de.m_marvin.simplelogging.printing.LogType;
import de.m_marvin.simplelogging.printing.Logger;

public class Test {
	
	public static void main(String[] args) {
		
		File runDir = new File(new File(Test.class.getClassLoader().getResource("").getPath().substring(1)).getParentFile().getParentFile(), "run");
		System.out.println("Run/Log-Folder: " + runDir);
		
		LogFileHandler logManager = new LogFileHandler(runDir, "ExampleApp").stopBeforeExit();
		Logger logger = logManager.beginLogging();
		logger.catchSystemStreams();
		
		Logger.setDefaultLogger(logger);
		
		logger.println(LogType.INFO, "Test Info");
		logger.println(LogType.ERROR, "Test error");
		logger.println(LogType.WARN, "Test warn");
		
		
		
		System.out.println("TEST 11");
		
//		System.exit(-1);
		
		logger.log(LogType.INFO, "Test1", "INFO");
		logger.log(LogType.ERROR, "Test1", "INFO");
		
		logManager.endLogging();
		
		System.out.println("TEST2");
		
	}
	
}
