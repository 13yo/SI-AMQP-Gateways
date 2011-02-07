package de.mpg.mis.neuesbibliothekssystem.remote.rabbit.config.server;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.mpg.mis.neuesbibliothekssystem.remote.rabbit.config.AbstractAMQPConfig;

@Configuration
public class ServerAMQPConfig extends AbstractAMQPConfig {

	@Bean
	public RabbitTemplate iudRabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	// @Bean
	// public DirectExchange dbmasterRequestExchange() {
	// return new DirectExchange(DBMASTER_REQUEST_QUEUE);
	// }
	//
	// @Bean
	// public Binding dbmasterRequestBinding() {
	// return BindingBuilder.from(dbmasterRequestQueue())
	// .to(dbmasterRequestExchange()).with(DBMASTER_REQUEST_QUEUE);
	// }

}
