package de.m_marvin.simplelogging;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.m_marvin.simplelogging.api.Logger;
import de.m_marvin.simplelogging.impl.FilteredLogger;
import de.m_marvin.simplelogging.impl.MultiLogger;
import de.m_marvin.simplelogging.impl.StacktraceLogger;
import de.m_marvin.simplelogging.impl.StreamLogger;
import de.m_marvin.simplelogging.impl.SystemLogger;
import de.m_marvin.simplelogging.misc.ReadBackLogFileProvider;

public class Test {
	
	public static void main(String[] args) {
		
		File runDir = new File(new File(Test.class.getClassLoader().getResource("").getPath().substring(1)).getParentFile().getParentFile(), "run");
		System.out.println("Run/Log-Folder: " + runDir);
		
		ReadBackLogFileProvider logManager = new ReadBackLogFileProvider(runDir).setName("ExampleLog").stopBeforeExit();
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
		
		Throwable e = new Exception("test");
		
		logger.warnPrinterRaw().println("TEST TEST WARN");

		logger.log(LogLevel.INFO, "Test1");

		logger.warnPrinter().print("TEST");
		logger.warnPrinter().println(" TEST WARN");
		logger.warnPrinter().println("TEST W1\nTEST W2");

		var p = logger.warnPrinterRaw();
		p.write('A');
		p.write('B');
		p.write('V');
		p.write('\n');
		p.write('S');
		p.flush();
		
		logger.log(LogLevel.INFO, "Test1");
		logger.log(LogLevel.ERROR, "Test1", e);
		
		try {
			List<String> logs = logManager.readBackLogs();
			for (String l : logs) {
				System.out.println("> " + l);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		logManager.endLogging();
		
	}
	
}
