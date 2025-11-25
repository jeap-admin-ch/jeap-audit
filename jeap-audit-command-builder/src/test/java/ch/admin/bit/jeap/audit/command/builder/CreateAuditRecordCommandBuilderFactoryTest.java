package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.audit.record.create.AuditSystemComponent;
import ch.admin.bit.jeap.audit.record.create.AuditUser;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.domainevent.DomainEventIdentity;
import ch.admin.bit.jeap.domainevent.avro.AvroDomainEvent;
import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.messaging.model.MessagePublisher;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAuditRecordCommandBuilderFactoryTest {

    private static final String SYSTEM_NAME = "MY_SYSTEM";
    private static final String SERVICE_NAME = "MY_SERVICE";
    private static final Instant TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String USER = "User1";
    private static final String IDENTITY_PROVIDER = "https://example.com/path/to/resource";

    private static final String SYSTEM_NAME_OTHER = "MY_SYSTEM_OTHER";
    private static final String SERVICE_NAME_OTHER = "MY_SERVICE_OTHER";

    @Test
    void userFromAuthorizationSystemServiceFromKafkaProperties() {
        KafkaProperties kafkaProperties = mock(KafkaProperties.class);
        when(kafkaProperties.getSystemName()).thenReturn(SYSTEM_NAME);
        when(kafkaProperties.getServiceName()).thenReturn(SERVICE_NAME);

        CreateAuditRecordCommandTriggerUserProvider triggerUserProvider = mock(CreateAuditRecordCommandTriggerUserProvider.class);
        // Mock the provideTriggerUser method to set the user information on the builder
        doAnswer(invocation -> {
            CreateAuditRecordCommandBuilder builder = invocation.getArgument(0);
            builder.setTriggerUser(USER, IDENTITY_PROVIDER);
            return null;
        }).when(triggerUserProvider).provideTriggerUser(any(CreateAuditRecordCommandBuilder.class));

        Optional<CreateAuditRecordCommandTriggerUserProvider> userInfoProviderOptional = Optional.of(triggerUserProvider);

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(userInfoProviderOptional, kafkaProperties);

        CreateAuditRecordCommandBuilder builder = factory.createWithUserTrigger(TIMESTAMP);
        CreateAuditRecordCommand command = builder.build();

        Object trigger = command.getPayload().getTrigger();
        assertInstanceOf(AuditUser.class, trigger);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(USER, auditUser.getId());
        assertEquals(IDENTITY_PROVIDER, auditUser.getIdentityProvider());

        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());
    }

    @Test
    void userFromAuthorization() {
        CreateAuditRecordCommandTriggerUserProvider triggerUserProvider = mock(CreateAuditRecordCommandTriggerUserProvider.class);
        // Mock the provideTriggerUser method to set the user information on the builder
        doAnswer(invocation -> {
            CreateAuditRecordCommandBuilder builder = invocation.getArgument(0);
            builder.setTriggerUser(USER, IDENTITY_PROVIDER);
            return null;
        }).when(triggerUserProvider).provideTriggerUser(any(CreateAuditRecordCommandBuilder.class));

        Optional<CreateAuditRecordCommandTriggerUserProvider> userInfoProviderOptional = Optional.of(triggerUserProvider);

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(userInfoProviderOptional, null);

        CreateAuditRecordCommandBuilder builder = factory.createWithUserTrigger(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        CreateAuditRecordCommand command = builder.build();

        Object trigger = command.getPayload().getTrigger();
        assertInstanceOf(AuditUser.class, trigger);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(USER, auditUser.getId());
        assertEquals(IDENTITY_PROVIDER, auditUser.getIdentityProvider());

        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());
    }

    @Test
    void systemFromEvent() {
        String department = "my-department";

        Optional<CreateAuditRecordCommandTriggerUserProvider> userInfoProviderOptional = Optional.empty();

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(userInfoProviderOptional, null);

        MessagePublisher publisher = mock(MessagePublisher.class);
        when(publisher.getSystem()).thenReturn(SYSTEM_NAME_OTHER);
        when(publisher.getService()).thenReturn(SERVICE_NAME_OTHER);

        DomainEventIdentity identity = mock(DomainEventIdentity.class);
        when(identity.getIdempotenceId()).thenReturn("jodeli");

        AvroDomainEvent event = mock(AvroDomainEvent.class);
        when(event.getPublisher()).thenReturn(publisher);
        when(event.getIdentity()).thenReturn(identity);

        CreateAuditRecordCommandBuilder builder = factory.createWithSystemTriggerFromMessage(SERVICE_NAME, SYSTEM_NAME, department, TIMESTAMP, event);
        CreateAuditRecordCommand command = builder.build();

        Object trigger = command.getPayload().getTrigger();
        assertInstanceOf(AuditSystemComponent.class, trigger);
        AuditSystemComponent auditSystemComponent = (AuditSystemComponent) trigger;
        assertEquals(SYSTEM_NAME_OTHER, auditSystemComponent.getSystem());
        assertEquals(SERVICE_NAME_OTHER, auditSystemComponent.getComponent());
        assertEquals(department, auditSystemComponent.getDepartment());

        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());
    }

    @Test
    void throwAuditExceptionInCaseOfCallingUserWithoutHavingUserInfoProvider() {
        KafkaProperties kafkaProperties = mock(KafkaProperties.class);
        when(kafkaProperties.getSystemName()).thenReturn(SYSTEM_NAME);
        when(kafkaProperties.getServiceName()).thenReturn(SERVICE_NAME);

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(Optional.empty(), kafkaProperties);
        assertThrows(AuditException.class, () -> factory.createWithUserTrigger(TIMESTAMP));
    }


}
