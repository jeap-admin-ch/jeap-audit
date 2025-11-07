package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.domainevent.avro.AvroDomainEvent;
import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.messaging.model.MessagePublisher;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationToken;

import java.time.Instant;
import java.util.Optional;

public class CreateAuditRecordCommandBuilderFactory {

    private final Optional<CreateAuditRecordCommandUserInfoProvider> userInfoProvider;
    private final KafkaProperties kafkaProperties;

    public CreateAuditRecordCommandBuilderFactory(Optional<CreateAuditRecordCommandUserInfoProvider> userInfoProvider,
                                                  KafkaProperties kafkaProperties) {
        this.userInfoProvider = userInfoProvider;
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Convenience method which sets a user as trigger with the userId given by the token subject (will be the Pams ID in the context of the ePortal) and identity provider from token.
     * System and service name are taken from the kafka properties.
     *
     * @param timestamp the timestamp of the command.
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithUserTrigger(Instant timestamp) {
        return createWithUserTrigger(kafkaProperties.getServiceName(), kafkaProperties.getSystemName(), timestamp);
    }

    /**
     * Convenience method which sets a user as trigger with the userId given by the token subject (will be the Pams ID in the context of the ePortal) and identity provider from token.
     *
     * @param serviceName the serviceName of the command.
     * @param systemName  the systemName of the command.
     * @param timestamp   the timestamp of the command.
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithUserTrigger(String serviceName, String systemName, Instant timestamp) {
        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(serviceName, systemName, timestamp);
        addUserTrigger(builder);
        return builder;
    }

    /**
     * Convenience method which sets a system as trigger with component and system taken from the event.
     *
     * @param serviceName the serviceName of the command.
     * @param systemName  the systemName of the command.
     * @param timestamp   the timestamp of the command.
     * @return the command builder to set other attributes
     */
    public CreateAuditRecordCommandBuilder createWithSystemTriggerFromEvent(String serviceName, String systemName, String department, Instant timestamp, AvroDomainEvent avroDomainEvent) {
        CreateAuditRecordCommandBuilder builder = CreateAuditRecordCommandBuilder.createCommandBuilder(serviceName, systemName, timestamp);
        addSystemTrigger(builder, department, avroDomainEvent);
        builder.idempotenceId("audit-" + avroDomainEvent.getIdentity().getIdempotenceId());

        return builder;
    }

    private void addUserTrigger(CreateAuditRecordCommandBuilder builder) {
        JeapAuthenticationToken jeapAuthenticationToken = getJeapAuthenticationToken();
        String userId = jeapAuthenticationToken.getTokenSubject();
        String identityProvider = jeapAuthenticationToken.getToken().getIssuer().toString();
        builder.setTriggerUser(userId, identityProvider);
    }

    private JeapAuthenticationToken getJeapAuthenticationToken() {
        if (!userInfoProvider.isPresent()) {
            throw AuditException.unsupportedWithoutJeapSecurity();
        }
        return userInfoProvider.get().getJeapAuthenticationToken();
    }

    private void addSystemTrigger(CreateAuditRecordCommandBuilder builder, String department, AvroDomainEvent avroDomainEvent) {
        MessagePublisher publisher = avroDomainEvent.getPublisher();
        String system = publisher.getSystem();
        String component = publisher.getService();
        builder.setTriggerSystem(department, system, component);
    }
}
