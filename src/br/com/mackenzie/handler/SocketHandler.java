package br.com.mackenzie.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import br.com.mackenzie.view.Log;

public class SocketHandler {

	private DatagramSocket clientSocket;
	private String logFile;

	public SocketHandler(String logFile){
		this.logFile = logFile;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			Log.error(logFile,"Erro ao utilizar socket");
			System.exit(1);
		}
	}

	public void sendPacket(String comando, String ip, int port, String logFile ) throws IOException{

		DatagramPacket sendPacket;
		byte[] sendData = new byte[1024];

		sendData = comando.getBytes();
		this.clientSocket.setSoTimeout(5000);
		InetAddress address; address = InetAddress.getByName(ip);

		//Instanciar pacote que será enviado ao slave
		sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

		//Enviar requisição
		Log.info(logFile, "Enviando mensagem...");
		clientSocket.send(sendPacket);


	}

	public String receivePacket() throws IOException{
		//Instanciar pacote que receberá resposta dos slaves
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		//Receber hora do slave
		Log.info(logFile, "Recebendo resposta");
		this.clientSocket.receive(receivePacket);

		return new String(receivePacket.getData());
	}

}
