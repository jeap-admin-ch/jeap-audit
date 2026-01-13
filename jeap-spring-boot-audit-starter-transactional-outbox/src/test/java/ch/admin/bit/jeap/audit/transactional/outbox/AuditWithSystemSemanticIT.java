package ch.admin.bit.jeap.audit.transactional.outbox;

import ch.admin.bit.jeap.audit.record.create.AuditEventType;
import ch.admin.bit.jeap.audit.record.create.AuditSystemComponent;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommandPayload;
import ch.admin.bit.jeap.domainevent.DomainEventIdentity;
import ch.admin.bit.jeap.domainevent.avro.AvroDomainEvent;
import ch.admin.bit.jeap.messaging.avro.AvroMessagePublisher;
import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.messaging.kafka.test.KafkaIntegrationTestBase;
import ch.admin.bit.jeap.messaging.model.MessagePublisher;
import ch.admin.bit.jeap.security.resource.semanticAuthentication.SemanticApplicationRole;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationContext;
import ch.admin.bit.jeap.security.test.jws.JwsBuilderFactory;
import ch.admin.bit.jeap.security.test.resource.configuration.JeapOAuth2IntegrationTestResourceConfiguration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestApp.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "jeap.audit.transactional-outbox.topic=mysystem-audit",
                "jeap.messaging.kafka.service-name=todo",
                "jeap.messaging.kafka.system-name=test-system"
        }
)
@Import({JeapOAuth2IntegrationTestResourceConfiguration.class, AuditWithSystemSemanticIT.TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuditWithSystemSemanticIT extends KafkaIntegrationTestBase {

    private static final SemanticApplicationRole WRITE_ROLE = SemanticApplicationRole.builder()
            .system("test-system")
            .resource("audit")
            .operation("write")
            .build();

    private static final String SUBJECT = "69368608-D736-43C8-5F76-55B7BF168299";
    private static final JeapAuthenticationContext CONTEXT = JeapAuthenticationContext.SYS;
    private static final String USE_CASE = "my-usecase";
    private static final String PROCESS_ID = "my-processId";

    @LocalServerPort
    private int port;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private JwsBuilderFactory jwsBuilderFactory;

    private RequestSpecification auditBaseUrlSpec;

    @Autowired
    private AuditCommandConsumer auditCommandConsumer;

    @Autowired
    private KafkaProperties kafkaProperties;

    @BeforeEach
    void setUp() {
        String baseUrl = "/" + applicationName + "/api";
        auditBaseUrlSpec = new RequestSpecBuilder().setBasePath(baseUrl).setPort(port).build();
    }

    @Test
    void auditMessageTriggeredSystemEvent() {
        String token = createAuthTokenForUserRoles(WRITE_ROLE);
        String serviceName = UUID.randomUUID().toString();
        String systemName = UUID.randomUUID().toString();
        String department = UUID.randomUUID().toString();
        String triggerSystem = UUID.randomUUID().toString();
        String triggerComponent = UUID.randomUUID().toString();

        AuditDto auditDto = new AuditDto(serviceName, systemName, department, triggerSystem, triggerComponent);

        given()
                .spec(auditBaseUrlSpec)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(auditDto)
                .when()
                .put("/audit")
                .then()
                .statusCode(HttpStatus.OK.value());

        assertAuditEvent(triggerComponent, triggerSystem, department, systemName, serviceName);
    }

    @Test
    void auditMessageTriggeredSystemEventFromSystem() {
        String token = createAuthTokenForUserRoles(WRITE_ROLE);
        String serviceName = kafkaProperties.getServiceName();
        String systemName = kafkaProperties.getSystemName();
        String department = UUID.randomUUID().toString();
        String triggerSystem = kafkaProperties.getSystemName();
        String triggerComponent = kafkaProperties.getServiceName();

        given()
                .spec(auditBaseUrlSpec)
                .auth().oauth2(token)
                .when()
                .put("/audit/from-system?department=" + department + "&system=" + systemName + "&service=" + serviceName)
                .then()
                .statusCode(HttpStatus.OK.value());

        assertAuditEvent(triggerComponent, triggerSystem, department, systemName, serviceName);
    }

    private void assertAuditEvent(String triggerComponent, String triggerSystem, String department, String systemName, String serviceName) {
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> auditCommandConsumer.hasConsumedCommand());

        CreateAuditRecordCommand command = auditCommandConsumer.getFirstCommand();
        assertNotNull(command);
        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        Object trigger = payload.getTrigger();
        assertThat(trigger).isInstanceOf(AuditSystemComponent.class);
        AuditSystemComponent auditSystemComponent = (AuditSystemComponent) trigger;
        assertEquals(triggerComponent, auditSystemComponent.getComponent());
        assertEquals(triggerSystem, auditSystemComponent.getSystem());
        assertEquals(department, auditSystemComponent.getDepartment());

        AvroMessagePublisher publisher = command.getPublisher();
        assertNotNull(publisher);
        assertEquals(systemName, publisher.getSystem());
        assertEquals(serviceName, publisher.getService());
    }

    private String createAuthTokenForUserRoles(SemanticApplicationRole... userroles) {
        return jwsBuilderFactory.createValidForFixedLongPeriodBuilder(SUBJECT, CONTEXT).
                withUserRoles(userroles).
                build().serialize();
    }

    public static class TestConfig {

        @Bean
        public FakeRestController fakeRestController(CreateAuditRecordCommandTransactionOutboxSender sender) {
            return new FakeRestController(sender);
        }

        @Bean
        public AuditCommandConsumer auditCommandConsumer() {
            return new AuditCommandConsumer();
        }
    }


    @RestController
    @RequestMapping(value = "/api/audit")
    @RequiredArgsConstructor
    @Slf4j
    public static class FakeRestController {

        private final CreateAuditRecordCommandTransactionOutboxSender sender;

        @PutMapping
        void putAudit(@RequestBody AuditDto auditDto) {
            MessagePublisher publisher = mock(MessagePublisher.class);
            when(publisher.getSystem()).thenReturn(auditDto.getTriggerSystem());
            when(publisher.getService()).thenReturn(auditDto.getTriggerComponent());

            DomainEventIdentity identity = mock(DomainEventIdentity.class);
            when(identity.getIdempotenceId()).thenReturn("jodeli");

            AvroDomainEvent avroDomainEvent = mock(AvroDomainEvent.class);
            when(avroDomainEvent.getPublisher()).thenReturn(publisher);
            when(avroDomainEvent.getIdentity()).thenReturn(identity);

            sender.auditMessageTriggeredSystemEvent(auditDto.getServiceName(), auditDto.getSystemName(), auditDto.getDepartment(),
                    Instant.now(), avroDomainEvent,
                    // Example on how to use the builder
                    builder -> {
                        builder.setEventType(AuditEventType.CREATED);
                        builder.setContext(USE_CASE, PROCESS_ID);
                    });
        }

        @PutMapping(path = "/from-system")
        void putAudit(@RequestParam String department, @RequestParam String system, @RequestParam String service) {
            MessagePublisher publisher = mock(MessagePublisher.class);
            when(publisher.getSystem()).thenReturn(system);
            when(publisher.getService()).thenReturn(service);

            DomainEventIdentity identity = mock(DomainEventIdentity.class);
            when(identity.getIdempotenceId()).thenReturn("jodeli");

            AvroDomainEvent avroDomainEvent = mock(AvroDomainEvent.class);
            when(avroDomainEvent.getPublisher()).thenReturn(publisher);
            when(avroDomainEvent.getIdentity()).thenReturn(identity);
            sender.auditMessageTriggeredSystemEvent(department,
                    Instant.now(), avroDomainEvent,
                    // Example on how to use the builder
                    builder -> {
                        builder.setEventType(AuditEventType.CREATED);
                        builder.setContext(USE_CASE, PROCESS_ID);
                    });
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuditDto {
        String serviceName;
        String systemName;
        String department;
        String triggerSystem;
        String triggerComponent;
    }
}
