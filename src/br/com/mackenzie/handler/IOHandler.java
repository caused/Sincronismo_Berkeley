package br.com.mackenzie.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.mackenzie.model.IpAddress;

public class IOHandler {

	public List<IpAddress> readFile(String slavesFile){
		List<IpAddress> lista = new ArrayList<IpAddress>();
		try {
			BufferedReader arquivo = new BufferedReader(new FileReader(slavesFile));
			
			String linha = arquivo.readLine();
			IpAddress ipAddress = null;
			while(linha != null){
				String[] ipPort = linha.split(":");
				ipAddress = new IpAddress(ipPort[0], Integer.parseInt(ipPort[1]));
				lista.add(ipAddress);
				linha = arquivo.readLine();
			}
			arquivo.close();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			System.out.println("Não foi possível abrir o arquivo");
			System.exit(0);
		}
		return lista;
	}
	
	public void writeInFile(String fileName, String content){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(content);
			writer.flush();
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Não foi possível abrir o arquivo: "+fileName);
			System.exit(0);
		}
	}
}
