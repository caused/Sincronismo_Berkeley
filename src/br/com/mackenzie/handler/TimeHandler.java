package br.com.mackenzie.handler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class TimeHandler {

	public  Long getTime(){
		return System.currentTimeMillis();
	}
	
	public String getFormattedTime(Long milliseconds){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		return format.format(new Date(milliseconds));
	}
	
	public Long getTimeAverage(Long[] differencesArray, long tolerance){
		Long acumulador = 0L;
		Long contador = 0L;
		for(int i=0; i < differencesArray.length; i++){
			if(Math.abs(differencesArray[i]) < tolerance){
				acumulador += differencesArray[i];
				contador++;
			}
		}
		
		return acumulador/(contador+1);
	}
	
	public Long[] generateDifferences(Long masterTime, Long[] slavesTime){
		
		Long[] differencesArray = new Long[slavesTime.length];
		
		for(int i =0; i < slavesTime.length; i++){
			if(slavesTime[i] != null){
				differencesArray[i] = slavesTime[i] - masterTime;
			}
		}
		return differencesArray;
	}
	
	public Long[] getFixedTimesIntervals(Long average, Long[] differencesArray){
		
		Long[] fixedIntervals = new Long[differencesArray.length];
		
		for(int i=0; i < differencesArray.length; i++){
			if(differencesArray[i] != null){
				fixedIntervals[i] = average - differencesArray[i];
			}
		}
		
		return fixedIntervals;
	}
	
}
