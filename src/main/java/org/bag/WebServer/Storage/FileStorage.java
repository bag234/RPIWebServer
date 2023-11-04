package org.bag.WebServer.Storage;

import java.io.File;

import org.bag.WebServer.Response.ContetTypes;

public class FileStorage {

	final static File mFile = new File("WebServer/");
	
	public FileStorage() {
	}
	
	public static boolean isFind(String path) {
		return new File(mFile.getAbsolutePath() + path).isFile();
	}
	
	public File getFile(String path) {
		if(isFind(path))
			return new File(mFile.getAbsolutePath() + path);
		
		return new File(mFile.getAbsolutePath() + "/NotFile.html");
	}
	
	public static ContetTypes getTypesFile(File file) {
		if (!file.isFile())
			return null;
//		String[] p = file.getName().split("\\.");
		switch (file.getName().split("\\.")[1]) {
		case "html":
			return ContetTypes.HTML;
		case "js":
			return ContetTypes.JS;
		case "ico":
		case "img":
		case "png":
		case "jpg":
			return ContetTypes.IMAGE;
		default:
			return ContetTypes.OTHER;
		}
	}
	
}
