package ch.admin.bit.jeap.audit.transactional.outbox;

import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilder;
import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilderFactory;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.messaging.model.Message;
import ch.admin.bit.jeap.messaging.transactionaloutbox.outbox.TransactionalOutbox;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class CreateAuditRecordCommandTransactionOutboxSender {

    private final TransactionalOutbox transactionalOutbox;
    private final CreateAuditRecordCommandBuilderFactory builderFactory;
    private final String topic;

    /**
     * Convenience method which sets a user as trigger with userId (will be PAMS-Id) and identity provider from token
     * and sends a command to the configured topic (through transactional outbox).
     * System and service name are taken from the Kafka properties.
     *
     * @param timestamp              the timestamp of the command.
     * @param commandConsumerBuilder the builder consumer
     *
     */
    @Transactional
    public void auditUserTriggeredEvent(Instant timestamp, Consumer<CreateAuditRecordCommandBuilder> commandConsumerBuilder) {
        CreateAuditRecordCommandBuilder builder = builderFactory.createWithUserTrigger(timestamp);
        commandConsumerBuilder.accept(builder);

        CreateAuditRecordCommand command = builder.build();
        send(command);
    }

    /**
     * Convenience method which sets a user as trigger with userId (will be PAMS-Id) and identity provider from token
     * and sends a command to the configured topic (through transactional outbox).
     *
     * @param serviceName the serviceName of the command
     * @param systemName  the systemName of the command
     * @param timestamp   the timestamp of the command
     */
    @Transactional
    public void auditUserTriggeredEvent(String serviceName, String systemName, Instant timestamp, Consumer<CreateAuditRecordCommandBuilder> commandConsumerBuilder) {
        CreateAuditRecordCommandBuilder builder = builderFactory.createWithUserTrigger(serviceName, systemName, timestamp);
        commandConsumerBuilder.accept(builder);

        CreateAuditRecordCommand command = builder.build();
        send(command);
    }

    /**
     * Convenience method which sets a system as trigger with component and system taken from the message
     * and sends a command to the configured topic (through transactional outbox).
     *
     * @param serviceName                 the serviceName of the command
     * @param systemName                  the systemName of the command
     * @param triggeringServiceDepartment the department of the triggering service
     * @param timestamp                   the timestamp of the command
     * @param message                     the message
     * @param commandConsumerBuilder      the builder consumer
     *
     */
    @Transactional
    public void auditMessageTriggeredSystemEvent(String serviceName, String systemName, String triggeringServiceDepartment, Instant timestamp, Message message, Consumer<CreateAuditRecordCommandBuilder> commandConsumerBuilder) {
        CreateAuditRecordCommandBuilder builder = builderFactory.createWithSystemTriggerFromMessage(serviceName, systemName, triggeringServiceDepartment, timestamp, message);
        commandConsumerBuilder.accept(builder);

        CreateAuditRecordCommand command = builder.build();
        send(command);
    }

    /**
     * Convenience method which sets a system as trigger with component and system taken from the message
     * and sends a command to the configured topic (through transactional outbox).
     * System and service name are taken from the Kafka properties.
     *
     * @param triggeringServiceDepartment the department of the triggering service
     * @param timestamp                   the timestamp of the command
     * @param message                     the message
     * @param commandConsumerBuilder      the builder consumer
     *
     */
    @Transactional
    public void auditMessageTriggeredSystemEvent(String triggeringServiceDepartment, Instant timestamp, Message message, Consumer<CreateAuditRecordCommandBuilder> commandConsumerBuilder) {
        CreateAuditRecordCommandBuilder builder = builderFactory.createWithSystemTriggerFromMessage(triggeringServiceDepartment, timestamp, message);
        commandConsumerBuilder.accept(builder);

        CreateAuditRecordCommand command = builder.build();
        send(command);
    }

    /**
     * Sends a command to the configured topic (through transactional outbox).
     *
     * @param command the command to send
     */
    @Transactional
    public void auditEvent(CreateAuditRecordCommand command) {
        send(command);
    }

    private void send(CreateAuditRecordCommand command) {
        transactionalOutbox.sendMessage(command, topic);
    }
}
