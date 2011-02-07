package de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.handler;

import de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.AMQPPayload;
import de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.gateways.client.ClientIudGateway;

public class ClientIudHandler {

	private final ClientIudGateway iudGateway;

	public ClientIudHandler(ClientIudGateway iudGateway) {
		this.iudGateway = iudGateway;
	}

	public void handleMessage(AMQPPayload mp) {
		iudGateway.sendIud(mp.getPayload(), mp.getHeaders());
	}
}
