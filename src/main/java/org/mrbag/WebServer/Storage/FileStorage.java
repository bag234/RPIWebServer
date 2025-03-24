package org.bag.WebServer.Storage;

import java.io.File;

import org.bag.WebServer.Response.ContetTypes;

public class FileStorage {

	private static File mFile = new File("./WebServer/");
	
	final static private File static_dir = new File(
				(FileStorage.class.getResource("/static")).getFile()
			);
	
	public static void setmFile(String file) {
		FileStorage.mFile = new File(file);
	}
	
	public FileStorage() {
		if(!mFile.exists())
			mFile.mkdir();
		
	}
	
	public static boolean isStaticDir(String path) {
		return new File(static_dir.getAbsolutePath() + path).isFile();
	}
	
	public static boolean isDynamicDir(String path) {
		return new File(mFile.getAbsolutePath() + path).isFile();
	}
	
	public File getFile(String path) {
		if(isStaticDir(path))
			return new File(static_dir.getAbsolutePath() + path);
		if(isDynamicDir(path))
			return new File(mFile.getAbsolutePath() + path);
		
		return new File(static_dir.getAbsolutePath() + "/NotFile.html");
	}
	
	public static ContetTypes getTypesFile(File file) {
		if (!file.isFile())
			return null;
		
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
