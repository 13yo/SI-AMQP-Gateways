package de.mpg.mis.neuesbibliothekssystem.remote.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.integration.mapping.HeaderMapper;

/**
 * Strategy interface for mapping integration Message headers to an outbound
 * AMQP Message (e.g. to configure AMQP properties) or extracting integration
 * header values from an inbound AMQP Message.
 * 
 * @author Tobias Kaatz
 */
public interface AmqpHeaderMapper extends HeaderMapper<Message> {
}
