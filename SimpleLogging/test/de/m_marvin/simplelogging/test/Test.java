package de.m_marvin.simplelogging.test;

import java.io.File;
import java.io.IOException;

import de.m_marvin.simplelogging.filehandling.LogFileHandler;
import de.m_marvin.simplelogging.printing.LogType;
import de.m_marvin.simplelogging.printing.Logger;

public class Test {
	
	public static void main(String... args) throws IOException {
		
		System.out.println("Begin of Test");
		
		LogFileHandler handler = new LogFileHandler(new File(Test.class.getResource("").getPath()), "testapp");
		
		Logger logger = handler.beginLogging();
		Logger.setDefaultLogger(logger);
		
		try {

			Logger.defaultLogger().logWarn("TEST");
			Logger.defaultLogger().logWarn("test1", "TEST");
			Logger.defaultLogger().logInfo("TEST");
			
			int i = 42 / 2;
			Logger.defaultLogger().logInfo("Value: " + i);
			
			Logger.defaultLogger().logInfo("test1", "TEST");
			Logger.defaultLogger().logError("TEST");
			Logger.defaultLogger().logError("test1", "TEST");
			
		} catch (Exception e) {
			
			Logger.defaultLogger().printException(LogType.ERROR, e);
			
		}
		
		handler.endLogging();
		
		System.out.println("End of Test");
		
	}
	
}
