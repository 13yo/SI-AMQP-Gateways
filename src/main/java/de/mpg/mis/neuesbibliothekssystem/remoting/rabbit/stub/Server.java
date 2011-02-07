package de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.stub;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.Payload;

import de.mpg.mis.neuesbibliothekssystem.remoting.rabbit.gateways.client.ClientToRemoteGateway;

public class Server {

	@Autowired
	private ClientToRemoteGateway toRemoteGateway;

	@Autowired
	private RabbitTemplate iudRabbitTemplate;

	@Value("${DBMaster.activemq.doActualTopic}")
	private String iudRoutingKey;

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath*:META-INF/context-server.xml");

		Server c = (Server) ctx.getBean("server");
	}

	public String test(@Payload Object mp,
			@Header("serviceMethod") String method) {
		String out = mp.getClass() + ":::" + mp + ":::" + method;
		System.out.println(out);
		iudRabbitTemplate.setExchange(iudRoutingKey);
		iudRabbitTemplate.convertAndSend(iudRoutingKey + ".ALL", out);
		return null;
	}

}
