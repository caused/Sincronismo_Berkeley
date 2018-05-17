package br.com.mackenzie;

import br.com.mackenzie.handler.ArgsHandler;
import br.com.mackenzie.model.Executor;

public class Main {
	public static void main(String[] args) {
		
		ArgsHandler argsHandler = new ArgsHandler();
		
		
		Executor executor = argsHandler.buildExecutor(args[0]);
		
		executor.execute(args);
	}
}
