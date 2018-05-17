package br.com.mackenzie.handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHandler {

	public Long getTime(String time){
		String[] horarioDecomposto = time.split(":");

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horarioDecomposto[0]));
		calendar.set(Calendar.MINUTE,Integer.parseInt(horarioDecomposto[1]));
		calendar.set(Calendar.SECOND,Integer.parseInt(horarioDecomposto[2]));
		
		return calendar.getTimeInMillis();
	}

	public String getFormattedTime(Long milliseconds){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

		return format.format(new Date(milliseconds));
	}

	public Long getTimeAverage(Long[] differencesArray, long tolerance){
		Long acumulador = 0L;
		Long contador = 0L;
		for(int i=0; i < differencesArray.length; i++){
			if(differencesArray[i] != null){
				if(Math.abs(differencesArray[i]) < tolerance){
					acumulador += differencesArray[i];
					contador++;
				}
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
