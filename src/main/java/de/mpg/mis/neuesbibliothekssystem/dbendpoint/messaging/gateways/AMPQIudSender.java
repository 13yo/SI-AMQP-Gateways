package de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.gateways;

import org.springframework.integration.Message;

import de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.AMQPPayload;
import de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging.MessagePayload;

public class AMPQIudSender extends AMQPOutboundGateway {

	private String iudTopicName;

	public String getIudTopicName() {
		return iudTopicName;
	}

	public void setIudTopicName(String iudTopicName) {
		this.iudTopicName = iudTopicName;
	}

	@Override
	protected Message<?> handleRequestMessage(Message<?> arg0) {
		String channel = (arg0.getPayload() instanceof MessagePayload && ((MessagePayload) arg0
				.getPayload()).typeMap.get("dbObject").endsWith("DTO")) ? ((MessagePayload) arg0
				.getPayload()).typeMap.get("dbObject") : "ALL";

		getRabbitTemplate().convertAndSend(iudTopicName,
				iudTopicName + "." + channel, AMQPPayload.fromMessage(arg0));
		return null;
	}
}
