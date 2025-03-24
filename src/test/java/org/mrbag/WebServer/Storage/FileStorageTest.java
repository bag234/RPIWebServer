package org.mrbag.WebServer.Storage;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FileStorageTest {
	
	@Test
	@DisplayName("Test file server static")
	public void tets() {
		File file = new File(
				FileStorage.class.getResource("/static").getFile()
				);
		
		for(String name: file.list()) {
			Assertions.assertTrue(FileStorage.isStaticDir("/" + name));
		}
		
	}
	
}
