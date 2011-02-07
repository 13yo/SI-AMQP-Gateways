package de.mpg.mis.neuesbibliothekssystem.dbendpoint.messaging;

import java.util.HashMap;
import java.util.Map;

import org.springframework.integration.support.MessageBuilder;

public class AMQPPayload {

	private Map<String, Object> headers;

	private Object payload;

	public AMQPPayload() {
		this.headers = new HashMap<String, Object>();
	}

	public AMQPPayload(Object payload) {
		this.headers = new HashMap<String, Object>();
		this.payload = payload;
	}

	public AMQPPayload(Object payload, Map<String, Object> headers) {
		this.headers = new HashMap<String, Object>();
		this.headers.putAll(headers);
		this.headers.remove("errorChannel");
		this.headers.remove("replyChannel");
		this.payload = payload;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public void addHeader(String key, Object value) {
		this.headers.put(key, value);
	}

	public static AMQPPayload fromMessage(
			org.springframework.integration.Message<?> message) {
		if (message == null)
			return null;
		return new AMQPPayload(message.getPayload(), message.getHeaders());
	}

	@SuppressWarnings("unchecked")
	public static org.springframework.integration.Message<?> toMessage(
			AMQPPayload payload) {
		if (payload == null)
			return null;
		@SuppressWarnings("rawtypes")
		MessageBuilder mb = MessageBuilder.withPayload(payload.getPayload());
		mb.copyHeadersIfAbsent(payload.getHeaders());
		return mb.build();
	}

	@Override
	public String toString() {
		return "AMQPPayload::Payload:" + this.payload.toString() + "::"
				+ this.headers.toString() + "::";
	}

}
