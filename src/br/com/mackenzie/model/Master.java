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

public class Master implements Executor {

	@Override
	public void execute(String [] args) {
		IOHandler ioHandler = new IOHandler();
		ArgsHandler argsHandler = new ArgsHandler();
		TimeHandler timeHandler = new TimeHandler();
		
		String slavesFile = argsHandler.getArgumentValue(args[3]);
		String logFile = argsHandler.getArgumentValue(args[4]);

		List<IpAddress> addressList = ioHandler.readFile(slavesFile);
		
		DatagramSocket clientSocket = null;
		try{
			clientSocket = new DatagramSocket();
		}catch(SocketException e){
			System.out.println("Erro ao utilizar socket");
			System.exit(1);
		}
		DatagramPacket sendPacket;
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		

		//Instrução a ser enviada para os slaves
		String comando = "";
		long id = 0;
		//Loop para ler os slaves
		while(true){
			id++;
			//array para juntar os atrasos dos slaves
			Long[] atrasos = new Long[addressList.size()];
			
			for(int i=0; i < addressList.size(); i++){
				//Obtem o nome do slave
				
				try {
					clientSocket.setSoTimeout(2000);
					ioHandler.writeInFile(logFile, "**********Iniciando log master********");
					
					//Obtendo host do slave pelo endereco IP
					InetAddress address = InetAddress.getByName(addressList.get(i).getIp());
					comando = "obtemHora";
					
					sendData = comando.getBytes();
					
					//Instanciar pacote que será enviado ao slave
					sendPacket = new DatagramPacket(sendData, sendData.length, address, addressList.get(i).getPort());
					
					//Enviar requisição
					clientSocket.send(sendPacket);
					
					//Instanciar pacote que receberá resposta dos slaves
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					
					//Receber hora do slave
					clientSocket.receive(receivePacket);
					String resposta = new String(receivePacket.getData());
					
					//Escrever operações realizadas no log
					ioHandler.writeInFile(logFile, "Foi recebido do slave "+addressList.get(i).toString()+ " a mensagem: "+resposta);
					
					atrasos[i] = Long.parseLong(resposta.trim());
					ioHandler.writeInFile(logFile, "Diferença entre master e slave ( "+ addressList.get(i).toString()+" ) de " + String.valueOf(timeHandler.getTime() - Long.valueOf(resposta.trim())) + " milissegundos");
					
				} catch (IOException e) {
					if(e instanceof SocketTimeoutException ){
						atrasos[i] = 0L;
					}else{
						System.out.println("Erro ao escrever no arquivo");
						System.exit(0);
					}
					
				}
				
			}
			
			
			clientSocket.close();
			
			try{
				Thread.sleep(5000);
			}catch(Exception e){
				System.out.println("Erro ao aguardar processamento");
				System.exit(0);
			}
		}


	}

}
