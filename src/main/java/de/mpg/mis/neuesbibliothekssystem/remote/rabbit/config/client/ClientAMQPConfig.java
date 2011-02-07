package de.mpg.mis.neuesbibliothekssystem.remote.rabbit.config.client;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mpg.mis.neuesbibliothekssystem.remote.rabbit.config.AbstractAMQPConfig;

@Configuration
public class ClientAMQPConfig extends AbstractAMQPConfig {

	@Value("${DBMaster.activemq.doActualTopic}.*")
	private String iudRoutingKey;

	@Bean
	public Queue doActualQueue() {
		return amqpAdmin().declareQueue();
	}

	/**
	 * Binds to the market data exchange. Interested in any stock quotes.
	 */
	@Bean
	public Binding doActualBinding() {
		return BindingBuilder.from(doActualQueue()).to(doActualIUDExchange())
				.with(iudRoutingKey);
	}

}
