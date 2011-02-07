package de.mpg.mis.neuesbibliothekssystem.remote.rabbit;

import org.springframework.integration.jms.DefaultJmsHeaderMapper;

/**
 * Pre-defined names and prefixes to be used for setting and/or retrieving JMS
 * attributes from/to integration Message Headers.
 * 
 * @see(JmsHeaders)
 * 
 * @author Mark Fisher
 * @author Tobias Kaatz
 */

public abstract class AmqpHeaders {

	/**
	 * Prefix used for JMS API related headers in order to distinguish from
	 * user-defined headers and other internal headers (e.g. correlationId).
	 * 
	 * @see DefaultJmsHeaderMapper
	 */
	public static final String PREFIX = "jms_";

	public static final String MESSAGE_ID = PREFIX + "messageId";

	public static final String CORRELATION_ID = PREFIX + "correlationId";

	public static final String REPLY_TO = PREFIX + "replyTo";

	public static final String REDELIVERED = PREFIX + "redelivered";

	public static final String TYPE = PREFIX + "type";

}
