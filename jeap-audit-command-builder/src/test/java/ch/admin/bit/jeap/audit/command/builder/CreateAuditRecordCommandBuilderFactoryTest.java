package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.audit.record.create.AuditSystemComponent;
import ch.admin.bit.jeap.audit.record.create.AuditUser;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.domainevent.DomainEventIdentity;
import ch.admin.bit.jeap.domainevent.avro.AvroDomainEvent;
import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.messaging.model.MessagePublisher;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationToken;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void userFromAuthorizationSystemServiceFromKafkaProperties() throws Exception {
        URL url = URI.create(IDENTITY_PROVIDER).toURL();
        Jwt jwtToken = mock(Jwt.class);
        when(jwtToken.getIssuer()).thenReturn(url);

        KafkaProperties kafkaProperties = mock(KafkaProperties.class);
        when(kafkaProperties.getSystemName()).thenReturn(SYSTEM_NAME);
        when(kafkaProperties.getServiceName()).thenReturn(SERVICE_NAME);

        JeapAuthenticationToken token = mock(JeapAuthenticationToken.class);
        when(token.getTokenSubject()).thenReturn(USER);
        when(token.getToken()).thenReturn(jwtToken);

        CreateAuditRecordCommandUserInfoProvider userInfoProvider = mock(CreateAuditRecordCommandUserInfoProvider.class);
        when(userInfoProvider.getJeapAuthenticationToken()).thenReturn(token);

        Optional<CreateAuditRecordCommandUserInfoProvider> userInfoProviderOptional = Optional.of(userInfoProvider);

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(userInfoProviderOptional, kafkaProperties);

        CreateAuditRecordCommandBuilder builder = factory.createWithUserTrigger(TIMESTAMP);
        CreateAuditRecordCommand command = builder.build();

        Object trigger = command.getPayload().getTrigger();
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(USER, auditUser.getId());
        assertEquals(IDENTITY_PROVIDER, auditUser.getIdentityProvider());

        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());
    }

    @Test
    void userFromAuthorization() throws Exception {
        URL url = URI.create(IDENTITY_PROVIDER).toURL();
        Jwt jwtToken = mock(Jwt.class);
        when(jwtToken.getIssuer()).thenReturn(url);

        JeapAuthenticationToken token = mock(JeapAuthenticationToken.class);
        when(token.getTokenSubject()).thenReturn(USER);
        when(token.getToken()).thenReturn(jwtToken);

        CreateAuditRecordCommandUserInfoProvider userInfoProvider = mock(CreateAuditRecordCommandUserInfoProvider.class);
        when(userInfoProvider.getJeapAuthenticationToken()).thenReturn(token);

        Optional<CreateAuditRecordCommandUserInfoProvider> userInfoProviderOptional = Optional.of(userInfoProvider);

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(userInfoProviderOptional, null);

        CreateAuditRecordCommandBuilder builder = factory.createWithUserTrigger(SERVICE_NAME, SYSTEM_NAME, TIMESTAMP);
        CreateAuditRecordCommand command = builder.build();

        Object trigger = command.getPayload().getTrigger();
        assertTrue(trigger instanceof AuditUser);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(USER, auditUser.getId());
        assertEquals(IDENTITY_PROVIDER, auditUser.getIdentityProvider());

        assertEquals(SYSTEM_NAME, command.getPublisher().getSystem());
        assertEquals(SERVICE_NAME, command.getPublisher().getService());
    }

    @Test
    void systemFromEvent() {
        String department = "my-department";

        Optional<CreateAuditRecordCommandUserInfoProvider> userInfoProviderOptional = Optional.empty();

        CreateAuditRecordCommandBuilderFactory factory = new CreateAuditRecordCommandBuilderFactory(userInfoProviderOptional, null);

        MessagePublisher publisher = mock(MessagePublisher.class);
        when(publisher.getSystem()).thenReturn(SYSTEM_NAME_OTHER);
        when(publisher.getService()).thenReturn(SERVICE_NAME_OTHER);

        DomainEventIdentity identity = mock(DomainEventIdentity.class);
        when(identity.getIdempotenceId()).thenReturn("jodeli");

        AvroDomainEvent event = mock(AvroDomainEvent.class);
        when(event.getPublisher()).thenReturn(publisher);
        when(event.getIdentity()).thenReturn(identity);

        CreateAuditRecordCommandBuilder builder = factory.createWithSystemTriggerFromEvent(SERVICE_NAME, SYSTEM_NAME, department, TIMESTAMP, event);
        CreateAuditRecordCommand command = builder.build();

        Object trigger = command.getPayload().getTrigger();
        assertTrue(trigger instanceof AuditSystemComponent);
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
