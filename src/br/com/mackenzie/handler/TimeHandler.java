package br.com.mackenzie.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHandler {

	public  Long getTime(){
		return System.currentTimeMillis();
	}
	
	public String getFormattedTime(Long milliseconds){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		return format.format(new Date(milliseconds));
	}
}
