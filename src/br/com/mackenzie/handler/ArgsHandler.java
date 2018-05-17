package br.com.mackenzie.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.mackenzie.model.Executor;
import br.com.mackenzie.model.Master;
import br.com.mackenzie.model.Slave;

public class ArgsHandler {

	public Executor buildExecutor(String option){
		if("-m".equals(option)){
			return new Master();
		}else if("-s".equals(option)){
			return new Slave();
		}else{
			throw new IllegalArgumentException("É necessário informar os parâmetros -m ou -s");
		}
	}
	
	public String getArgumentValue(String option){
		Pattern pattern = Pattern.compile("=(.*)");
		Matcher matcher = pattern.matcher(option);
		if (matcher.find())
		{
			return matcher.group(1);
		}
		
		return null;
	}
}
