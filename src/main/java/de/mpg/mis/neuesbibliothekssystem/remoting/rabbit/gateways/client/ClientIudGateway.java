package de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.gateways.client;

import java.util.Map;

import org.springframework.integration.annotation.Headers;

public interface ClientIudGateway {

	public void sendIud(Object iud, @Headers Map<String, Object> headers);

}
