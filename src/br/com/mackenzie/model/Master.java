package br.com.mackenzie.model;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import br.com.mackenzie.handler.SocketHandler;

import br.com.mackenzie.handler.ArgsHandler;
import br.com.mackenzie.handler.IOHandler;
import br.com.mackenzie.handler.TimeHandler;
import br.com.mackenzie.view.Log;

public class Master implements Executor {



	@Override
	public void execute(String [] args) {
		IOHandler ioHandler = new IOHandler();
		ArgsHandler argsHandler = new ArgsHandler();
		TimeHandler timeHandler = new TimeHandler(argsHandler.getArgumentValue(args[2]));

		Long tolerance = Long.parseLong(argsHandler.getArgumentValue(args[3]));
		String slavesFile = argsHandler.getArgumentValue(args[4]);
		String logFile = argsHandler.getArgumentValue(args[5]);


		List<IpAddress> addressList = ioHandler.readFile(slavesFile);

		SocketHandler socket = new SocketHandler(logFile);

		//Instrução a ser enviada para os slaves
		String comando = "";
		long id = 0;
		//Loop para ler os slaves
		while(true){
			id++;
			
			//array para juntar os atrasos dos slaves
			Long[] differencesArray = new Long[addressList.size()];
			
			Log.info(logFile, "**********Iniciando log master********");
			Log.info(logFile, "**************LOOP: "+id+"**********");
			for(int i=0; i < addressList.size(); i++){
				//Obtem o nome do slave
				
				try {
					
					//Obtendo host do slave pelo endereco IP
					Log.info(logFile, "Obtendo endereco de IP do slave: "+addressList.get(i).getIp());
					
					comando = "obterHora:"+id;

					//Enviando chamada para obter hora dos slaves
					socket.sendPacket(comando, addressList.get(i).getIp(), addressList.get(i).getPort(), logFile);

					//Receber retorno dos slaves
					String resposta = socket.receivePacket();

					//Escrever operações realizadas no log
					Log.info(logFile, "Foi recebido do slave "+addressList.get(i).toString()+ " a mensagem: "+resposta.trim());

					differencesArray[i] = timeHandler.generateDifferences(timeHandler.getCurrentTime(), Long.parseLong(resposta.trim()));

					Log.info(logFile, "Diferença entre master e slave ( "+ addressList.get(i).toString()+" ) de " + String.valueOf(timeHandler.getCurrentTime() - Long.valueOf(resposta.trim())) + " milissegundos");

				} catch (IOException e) {
					if(e instanceof SocketTimeoutException ){
						Log.info(logFile, "Tempo de espera máximo excedido");
						differencesArray[i] = null;
					}else{
						Log.error(logFile,"Erro ao escrever no arquivo: "+e);
						System.exit(0);
					}

				}

			}
			Long timeAverage = timeHandler.getTimeAverage(differencesArray, tolerance);
			Long[] fixedTimesIntervals = timeHandler.getFixedTimesIntervals(timeAverage, differencesArray);

			//Exibir horário atual
			Log.info(logFile, "Hora atual: " + timeHandler.getFormattedTime(timeHandler.getCurrentTime()));

			//Exibe novo horário do mestre
			timeHandler.updateLocalDifferences(timeAverage);
			Log.info(logFile, "Hora após atualização: " + timeHandler.getFormattedTime(timeHandler.getCurrentTime()));



			//envia correção para os slaves
			for(int i =0; i < addressList.size(); i++){
				if(fixedTimesIntervals[i] == null){
					fixedTimesIntervals[i] = 0L;
				}
				Log.info(logFile, "Enviando para o slave " + addressList.get(i).toString() + " correção de " + fixedTimesIntervals[i] + " milisegundos");
				comando = "corrigeHora:" + fixedTimesIntervals[i];
				try {
					socket.sendPacket(comando, addressList.get(i).getIp(), addressList.get(i).getPort(), logFile);
				}catch (IOException e) {
					Log.error(logFile, "Erro ao enviar pacote para slaves: "+e);
					System.exit(0);
				}
			}
			try{
				Thread.sleep(5000);
			}catch(Exception e){
				System.out.println("Erro ao aguardar processamento");
				System.exit(0);
			}
			
			
		}


	}

}
