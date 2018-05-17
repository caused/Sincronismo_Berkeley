package br.com.mackenzie.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import br.com.mackenzie.handler.ArgsHandler;
import br.com.mackenzie.handler.IOHandler;
import br.com.mackenzie.handler.TimeHandler;
import br.com.mackenzie.view.Log;

public class Master implements Executor {



	@Override
	public void execute(String [] args) {
		IOHandler ioHandler = new IOHandler();
		ArgsHandler argsHandler = new ArgsHandler();
		TimeHandler timeHandler = new TimeHandler();

		Long tolerance = Long.parseLong(argsHandler.getArgumentValue(args[2]));
		String slavesFile = argsHandler.getArgumentValue(args[3]);
		String logFile = argsHandler.getArgumentValue(args[4]);


		List<IpAddress> addressList = ioHandler.readFile(slavesFile);

		DatagramSocket clientSocket = null;
		try{
			clientSocket = new DatagramSocket();
		}catch(SocketException e){
			Log.error(logFile,"Erro ao utilizar socket");
			System.exit(1);
		}
		DatagramPacket sendPacket;
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		InetAddress address = null;


		//Instrução a ser enviada para os slaves
		String comando = "";
		long id = 0;
		//Loop para ler os slaves
		Long masterTime = timeHandler.getTime(argsHandler.getArgumentValue(args[1]));
		while(true){
			id++;
			//array para juntar os atrasos dos slaves
			Long[] slavesTime = new Long[addressList.size()];
			
			Log.info(logFile, "**********Iniciando log master********");
			for(int i=0; i < addressList.size(); i++){
				//Obtem o nome do slave
				Log.info(logFile, "**************Slave: "+i+"**********");
				try {
					clientSocket.setSoTimeout(5000);
					

					//Obtendo host do slave pelo endereco IP
					Log.info(logFile, "Obtendo endereco de IP do slave: "+addressList.get(i).getIp());
					address = InetAddress.getByName(addressList.get(i).getIp());
					comando = "obterHora:"+id;

					sendData = comando.getBytes();

					//Instanciar pacote que será enviado ao slave
					sendPacket = new DatagramPacket(sendData, sendData.length, address, addressList.get(i).getPort());

					//Enviar requisição
					Log.info(logFile, "Enviando requisição...");
					clientSocket.send(sendPacket);

					//Instanciar pacote que receberá resposta dos slaves
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

					//Receber hora do slave
					Log.info(logFile, "Recebendo resposta");
					clientSocket.receive(receivePacket);
					String resposta = new String(receivePacket.getData());

					//Escrever operações realizadas no log
					Log.info(logFile, "Foi recebido do slave "+addressList.get(i).toString()+ " a mensagem: "+resposta.trim());

					slavesTime[i] = Long.parseLong(resposta.trim());

					Log.info(logFile, "Diferença entre master e slave ( "+ addressList.get(i).toString()+" ) de " + String.valueOf(masterTime - Long.valueOf(resposta.trim())) + " milissegundos");

				} catch (IOException e) {
					if(e instanceof SocketTimeoutException ){
						Log.info(logFile, "Tempo de espera máximo excedido");
						slavesTime[i] = null;
					}else{
						Log.error(logFile,"Erro ao escrever no arquivo: "+e);
						System.exit(0);
					}

				}

			}
			Long[] differencesArray = timeHandler.generateDifferences(masterTime, slavesTime);
			Long timeAverage = timeHandler.getTimeAverage(differencesArray, tolerance);
			Long[] fixedTimesIntervals = timeHandler.getFixedTimesIntervals(timeAverage, differencesArray);

			//Exibir horário atual
			Log.info(logFile, "Hora atual: " + timeHandler.getFormattedTime(masterTime) + "%n\n");

			//Exibe novo horário do mestre
			Log.info(logFile, "Hora após atualização: " + timeHandler.getFormattedTime(masterTime+timeAverage) + "%n\n");



			//envia correção para os slaves
			for(int i =0; i < addressList.size(); i++){
				if(fixedTimesIntervals[i] == null){
					fixedTimesIntervals[i] = 0L;
				}
				Log.info(logFile, "Enviando para o slave " + addressList.get(i).toString() + " correção de " + fixedTimesIntervals[i] + " milisegundos%n\n");
				comando = "corrigeHora:" + fixedTimesIntervals[i];
				sendData = comando.getBytes();
				try {
					address = InetAddress.getByName(addressList.get(i).getIp());

					sendPacket = new DatagramPacket(sendData, sendData.length, address, addressList.get(i).getPort());
					clientSocket.send(sendPacket);
				}catch (IOException e) {
					Log.error(logFile, "Erro ao enviar pacote para slaves: "+e);
					clientSocket.close();
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
