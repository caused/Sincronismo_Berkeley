package br.com.mackenzie.view;

import br.com.mackenzie.handler.IOHandler;

public class Log {

	private static IOHandler ioHandler = new IOHandler();
	
	public static void info(String fileName, String content){
		System.out.println("[INFO] "+content);
		ioHandler.writeInFile(fileName, "[INFO] "+content);
	}
	
	public static void error(String fileName, String content){
		System.out.println("[ERROR] "+content);
		ioHandler.writeInFile(fileName, "[ERROR] "+content);
	}
}
