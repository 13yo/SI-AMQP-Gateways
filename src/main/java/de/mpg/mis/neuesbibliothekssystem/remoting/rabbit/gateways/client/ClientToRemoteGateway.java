package de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.gateways.client;

import org.springframework.integration.Message;

public interface ClientToRemoteGateway {

	public void send(Message<?> m);

	public Object sendAndReceive(Message<?> m);

}
