package com.agileoperations.webvideoconverter;

import java.io.File;

public class FileFactory {
	
	public FileFactory(){
	}
	
	public File getTransferedFile(String multiPartFilename){
		return new File("/tmp/" + multiPartFilename);
	}

}
