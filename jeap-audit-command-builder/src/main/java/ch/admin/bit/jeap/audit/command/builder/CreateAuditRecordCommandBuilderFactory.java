package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.messaging.model.Message;
import ch.admin.bit.jeap.messaging.model.MessagePublisher;

import java.time.Instant;
import java.util.Optional;

public class CreateAuditRecordCommandBuilderFactory {

    private final Optional<CreateAuditRecordCommandTriggerUserProvider> triggerUserProvider;
    private final KafkaProperties kafkaProperties;

    public CreateAuditRecordCommandBuilderFactory(Optional<CreateAuditRecordCommandTriggerUserProvider> triggerUserProvider,
                                                  KafkaProperties kafkaProperties) {
        this.triggerUserProvider = triggerUserProvider;
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Convenience method which sets a user as trigger with the userId given by the token subject (will be the PAMS ID in the context of the ePortal) and identity provider from token.
     * System and service name are taken from the kafka properties.
     *
     * @param timestamp the timestamp of the command
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithUserTrigger(Instant timestamp) {
        return createWithUserTrigger(kafkaProperties.getServiceName(), kafkaProperties.getSystemName(), timestamp);
    }

    /**
     * Convenience method which sets a user as trigger with the userId given by the token subject (will be the PAMS ID in the context of the ePortal) and identity provider from token.
     *
     * @param serviceName the serviceName of the command
     * @param systemName  the systemName of the command
     * @param timestamp   the timestamp of the command
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithUserTrigger(String serviceName, String systemName, Instant timestamp) {
        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(serviceName, systemName, timestamp);
        addUserTrigger(builder);
        return builder;
    }


    /**
     * Convenience method which sets a system as trigger with component and system taken from the message.
     * System and service name are taken from the kafka properties.
     *
     * @param triggeringServiceDepartment the department of the triggering service
     * @param timestamp                   the timestamp of the command
     * @param message                     the message
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithSystemTriggerFromMessage(String triggeringServiceDepartment, Instant timestamp, Message message) {
        return createWithSystemTriggerFromMessage(kafkaProperties.getServiceName(), kafkaProperties.getSystemName(), triggeringServiceDepartment, timestamp, message);
    }

    /**
     * Convenience method which sets a system as trigger with component and system taken from the message.
     *
     * @param serviceName                 the serviceName of the command
     * @param systemName                  the systemName of the command
     * @param triggeringServiceDepartment the department of the triggering service
     * @param timestamp                   the timestamp of the command
     * @param message                     the message
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithSystemTriggerFromMessage(String serviceName, String systemName, String triggeringServiceDepartment, Instant timestamp, Message message) {
        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(serviceName, systemName, timestamp);
        addSystemTrigger(builder, triggeringServiceDepartment, message);
        builder.idempotenceId("audit-" + message.getIdentity().getIdempotenceId());

        return builder;
    }

    private void addUserTrigger(CreateAuditRecordCommandBuilder builder) {
        if (triggerUserProvider.isEmpty()) {
            throw AuditException.unsupportedWithoutJeapSecurity();
        }
        triggerUserProvider.get().provideTriggerUser(builder);
    }

    private void addSystemTrigger(CreateAuditRecordCommandBuilder builder, String department, Message message) {
        MessagePublisher publisher = message.getPublisher();
        String system = publisher.getSystem();
        String component = publisher.getService();
        builder.setTriggerSystem(department, system, component);
    }
}
