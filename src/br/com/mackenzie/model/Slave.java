package br.com.mackenzie.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import br.com.mackenzie.handler.ArgsHandler;
import br.com.mackenzie.handler.TimeHandler;
import br.com.mackenzie.view.Log;

public class Slave implements Executor {

	@Override
	public void execute(String [] args) {
		ArgsHandler argsHandler = new ArgsHandler();
		TimeHandler timeHandler = new TimeHandler(argsHandler.getArgumentValue(args[2]));

		String logFile = argsHandler.getArgumentValue(args[3]);

		int port = Integer.parseInt(argsHandler.getArgumentValue(args[1]).split(":")[1]);
		DatagramSocket serverSocket = null;
		InetAddress address = null;

		try{
			serverSocket = new DatagramSocket(port);
		}catch(SocketException e){
			Log.error(logFile,"Erro ao utilizar socket");
			System.exit(1);
		}
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		String comando;

		Log.info(logFile, "+------ Log Slave -------+");
		while(true){

			//Preparar recebimento da mensagem do master
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try{
				serverSocket.receive(receivePacket);

				comando = new String(receivePacket.getData()).trim();
				receiveData = new byte[1024];

				//Obtendo endereco do master
				address = receivePacket.getAddress();
				int porta = receivePacket.getPort();

				if(comando.contains("obterHora")){
					Log.info(logFile, "**************ID: "+comando.replace("obterHora:", "")+"**********");
					Log.info(logFile, "Master pedindo horário");
					comando = Long.toString(timeHandler.getCurrentTime());
					sendData = comando.getBytes();
					
					//Envia resposta ao master
					Log.info(logFile, "Retornando resposta");
					DatagramPacket packetRespostaHorario = new DatagramPacket(sendData, sendData.length,address, porta );
					serverSocket.send(packetRespostaHorario);
				}else if(comando.contains("corrigeHora")){
					
					long correcao = Long.parseLong(comando.replace("corrigeHora:", ""));
					
					Log.info(logFile, "Correção recebida do master: "+correcao);
					
					//Exibir horário atual
					Log.info(logFile, "Hora atual: " + timeHandler.getFormattedTime(timeHandler.getCurrentTime()) + "");

					//Exibe novo horário do slave
					timeHandler.updateLocalDifferences(correcao);
					Log.info(logFile, "Hora após atualização: " + timeHandler.getFormattedTime(timeHandler.getCurrentTime()) + "\n");

					
				}
			}catch(IOException e){
				Log.error(logFile, "Ocorreu um erro ao processar o arquivo");
				serverSocket.close();
				System.exit(0);
			}
			
		}

	}

}
