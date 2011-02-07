package de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.gateways;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.integration.Message;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.util.Assert;

import de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.AMQPPayload;

//public class AMQPOutboundGateway extends RabbitGatewaySupport {
public class AMQPOutboundGateway extends AbstractReplyProducingMessageHandler {
	// public class AMQPOutboundGateway extends MessagingGatewaySupport {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private RabbitTemplate rabbitTemplate;

	private String routingKey;

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	/**
	 * Set the Rabbit connection factory to be used by the gateway. Will
	 * automatically create a RabbitTemplate for the given ConnectionFactory.
	 * 
	 * @see #createRabbitTemplate
	 * @see #setConnectionFactory(org.springframework.amqp.rabbit.connection.ConnectionFactory)
	 * @param connectionFactory
	 */
	public final void setConnectionFactory(ConnectionFactory connectionFactory) {
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

	public void setMessageConverter(MessageConverter messageConverter) {
		this.rabbitTemplate.setMessageConverter(messageConverter);
	}

	@Override
	public void onInit() throws IllegalArgumentException,
			BeanInitializationException {
		if (this.rabbitTemplate == null) {
			throw new IllegalArgumentException(
					"'connectionFactory' or 'rabbitTemplate' is required");
		}
		Assert.notNull(this.routingKey, "routingKey must not be null");
		try {
			super.onInit();
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
		this.rabbitTemplate.setRoutingKey(this.routingKey);
	}

	@Override
	protected Message<?> handleRequestMessage(Message<?> arg0) {
		// Object requestValue = arg0.getPayload();
		// Map<String, Object> headers = arg0.getHeaders();
		// AMQPPayload ap = new AMQPPayload(requestValue);
		// System.out.println("Sende MEssage");
		// logger.debug("sende Nachricht");

		return AMQPPayload.toMessage((AMQPPayload) rabbitTemplate
				.convertSendAndReceive(AMQPPayload.fromMessage(arg0)));
	}

	// @Override
	// public void handleMessage(Message<?> arg0) throws MessagingException {
	// AMQPPayload ap = new AMQPPayload(arg0.getPayload());
	// System.out.println(arg0.getHeaders());
	// }

}
