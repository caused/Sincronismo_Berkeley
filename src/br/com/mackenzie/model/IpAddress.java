package br.com.mackenzie.model;

public class IpAddress {
	
	private String ip;
	private Integer port;
	
	public IpAddress(String ip, Integer port){
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.ip.concat(":").concat(this.port.toString());
	}

}
