package de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.stub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.support.MessageBuilder;

import de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.MessagePayload;
import de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.gateways.client.ClientToRemoteGateway;

public class Client {

	@Autowired
	private ClientToRemoteGateway toRemoteGateway;

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath*:META-INF/context.xml");

		Client c = (Client) ctx.getBean("client");
		c.test();
	}

	public void test() {
		MessagePayload payload = new MessagePayload();
		payload.add("demo2", "demo");

		System.out.println(toRemoteGateway.sendAndReceive(MessageBuilder
				.withPayload(payload).setHeader("serviceMethod", "test2")
				.build()));
	}

}
