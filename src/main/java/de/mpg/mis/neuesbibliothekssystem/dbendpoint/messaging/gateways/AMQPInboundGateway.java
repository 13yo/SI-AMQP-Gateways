package de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.gateways;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;

import de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.AMQPPayload;

public class AMQPInboundGateway extends SimpleMessageListenerContainer

{

	// public AMQPInboundGateway(ConnectionFactory connectionFactory) {
	// super(connectionFactory);
	// }

	public AMQPInboundGateway() {
		this.messagingTemplate = new MessagingTemplate();
	}

	private MessageChannel requestChannel;
	private MessagingTemplate messagingTemplate;

	public MessagingTemplate getMessagingTemplate() {
		return messagingTemplate;
	}

	public void setMessagingTemplate(MessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.rabbitTemplate.setMessageConverter(messageConverter);
	}

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private RabbitTemplate rabbitTemplate;

	/**
	 * Set the Rabbit connection factory to be used by the gateway. Will
	 * automatically create a RabbitTemplate for the given ConnectionFactory.
	 * 
	 * @see #createRabbitTemplate
	 * @see #setConnectionFactory(org.springframework.amqp.rabbit.connection.ConnectionFactory)
	 * @param connectionFactory
	 */
	@Override
	public final void setConnectionFactory(ConnectionFactory connectionFactory) {
		super.setConnectionFactory(connectionFactory);
		this.rabbitTemplate = createRabbitTemplate(connectionFactory);
	}

	/**
	 * Create a RabbitTemplate for the given ConnectionFactory. Only invoked if
	 * populating the gateway with a ConnectionFactory reference.
	 * 
	 * @param connectionFactory
	 *            the Rabbit ConnectionFactory to create a RabbitTemplate for
	 * @return the new RabbitTemplate instance
	 * @see #setConnectionFactory
	 */
	protected RabbitTemplate createRabbitTemplate(
			ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	/**
	 * Return the Rabbit ConnectionFactory used by the gateway.
	 */
	@Override
	public final ConnectionFactory getConnectionFactory() {
		return (this.rabbitTemplate != null ? this.rabbitTemplate
				.getConnectionFactory() : null);
	}

	/**
	 * Set the RabbitTemplate for the gateway.
	 * 
	 * @param rabbitTemplate
	 * @see #setConnectionFactory(org.springframework.amqp.rabbit.connection.ConnectionFactory)
	 */
	public final void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	/**
	 * Return the RabbitTemplate for the gateway.
	 */
	public final RabbitTemplate getRabbitTemplate() {
		return this.rabbitTemplate;
	}

	public void onInit() throws IllegalArgumentException,
			BeanInitializationException {
		if (this.rabbitTemplate == null) {
			throw new IllegalArgumentException(
					"'connectionFactory' or 'rabbitTemplate' is required");
		}
		try {
			initGateway();
		} catch (Exception ex) {
			throw new BeanInitializationException(
					"Initialization of Rabbit gateway failed: "
							+ ex.getMessage(), ex);
		}
	}

	/**
	 * Subclasses can override this for custom initialization behavior. Gets
	 * called after population of this instance's bean properties.
	 * 
	 * @throws java.lang.Exception
	 *             if initialization fails
	 */
	protected void initGateway() throws Exception {
		this.setMessageListener(new MessageListenerAdapter(
				new InboundHandler(), this.rabbitTemplate.getMessageConverter()));
	}

	public MessageChannel getRequestChannel() {
		return requestChannel;
	}

	public void setRequestChannel(MessageChannel requestChannel) {
		this.requestChannel = requestChannel;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(this.requestChannel, "requestChannel must not be null");

		onInit();

		messagingTemplate.setDefaultChannel(requestChannel);
	}

	private class InboundHandler {
		@SuppressWarnings("unchecked")
		public AMQPPayload handleMessage(AMQPPayload mp) {
			@SuppressWarnings("rawtypes")
			org.springframework.integration.Message<?> answer = messagingTemplate
					.sendAndReceive(AMQPPayload.toMessage(mp));
			return AMQPPayload.fromMessage(answer);
		}

		public void handleMessage(String str) {
			messagingTemplate.send(MessageBuilder.withPayload(str).build());
		}
	}
}
