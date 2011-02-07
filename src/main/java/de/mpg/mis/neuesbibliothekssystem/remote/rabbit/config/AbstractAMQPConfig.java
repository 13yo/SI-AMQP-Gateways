package de.mpg.mis.neuesbibliothekssystem.remote.rabbit.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.AbstractRabbitConfiguration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SingleConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class AbstractAMQPConfig extends AbstractRabbitConfiguration {

	@Value("${DBMaster.activemq.doActualTopic}")
	protected String DO_ACTUAL_IUD_EXCHANGE;

	@Value("${DBMaster.activemq.requestQueue}")
	protected String DBMASTER_REQUEST_QUEUE;

	@Bean
	public ConnectionFactory connectionFactory() {
		SingleConnectionFactory connectionFactory = new SingleConnectionFactory(
				"localhost");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}

	@Override
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}

	@Bean
	public Queue dbmasterRequestQueue() {
		return new Queue(DBMASTER_REQUEST_QUEUE);
	}

	@Bean
	public TopicExchange doActualIUDExchange() {
		return new TopicExchange(DO_ACTUAL_IUD_EXCHANGE);
	}

}
