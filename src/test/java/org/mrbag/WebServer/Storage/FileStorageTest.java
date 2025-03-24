package org.mrbag.WebServer.Storage;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileStorageTest {
	
	final static Logger log = Logger.getLogger(FileStorageTest.class);
	
	@BeforeAll
	static void configure() {
		FileStorage.setmFile(FileStorageTest.class.getResource("/dynamicDir").getFile());
	}
	
	/**
	 * Это конечно не правильный подход, но тесты будут прогоняться торлько при сборке проекта а если что это всегда может быть переписанно 
	 * 
	 */
	@Test
	@DisplayName("Test file server static")
	public void tetsStaticDir() {
		File file = new File(
				FileStorage.class.getResource("/static").getFile()
				);
		log.info(String.format("Static File dir for tets [%s]", file.getAbsolutePath()));
		for(String name: file.list()) {
			log.debug(String.format("Testing file name[%s]", name));
			Assertions.assertTrue(FileStorage.isStaticDir("/" + name));
		}
		
	}
	
	@Test
	@DisplayName("Test file server dynamic")
	public void testDynamicDir() {
		File file = new File(
				FileStorageTest.class.getResource("/dynamicDir").getFile()
				);
		log.info(String.format("Static File dir for tets [%s]", FileStorage.getmFile()));
		
		for(String name: file.list()) {
			log.debug(String.format("[DYNAMIC]Testing file name[%s]", name));
			Assertions.assertTrue(FileStorage.isDynamicDir("/" + name));
		}
	}
	
}
