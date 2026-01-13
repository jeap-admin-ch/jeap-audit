package ch.admin.bit.jeap.audit.transactional.outbox;

import ch.admin.bit.jeap.audit.record.create.AuditEventType;
import ch.admin.bit.jeap.audit.record.create.AuditUser;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommandPayload;
import ch.admin.bit.jeap.messaging.avro.AvroMessagePublisher;
import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.messaging.kafka.test.KafkaIntegrationTestBase;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationContext;
import ch.admin.bit.jeap.security.test.jws.JwsBuilderFactory;
import ch.admin.bit.jeap.security.test.resource.configuration.JeapOAuth2IntegrationTestResourceConfiguration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestApp.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "jeap.audit.transactional-outbox.topic=mysystem-audit",
                "jeap.messaging.kafka.service-name=todo",
                "jeap.messaging.kafka.system-name=test-system"
        }
)
@Import({JeapOAuth2IntegrationTestResourceConfiguration.class, AuditWithUserSimpleIT.TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuditWithUserSimpleIT extends KafkaIntegrationTestBase {

    private static final String SIMPLE_AUTH_READ_ROLE = "authentication:read";

    private static final String SUBJECT = "69368608-D736-43C8-5F76-55B7BF168299";
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
    void auditUserTriggeredEvent_fromToken() {
        String token = createJeapTokenWithUserRoles(SIMPLE_AUTH_READ_ROLE);
        given()
                .spec(auditBaseUrlSpec)
                .auth().oauth2(token)
                .when()
                .put("/audit")
                .then()
                .statusCode(HttpStatus.OK.value());

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> auditCommandConsumer.hasConsumedCommand());

        CreateAuditRecordCommand command = auditCommandConsumer.getFirstCommand();
        assertNotNull(command);
        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        Object trigger = payload.getTrigger();
        assertThat(trigger).isInstanceOf(AuditUser.class);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(SUBJECT, auditUser.getId());
        assertEquals("http://localhost/auth", auditUser.getIdentityProvider());

        AvroMessagePublisher publisher = command.getPublisher();
        assertNotNull(publisher);
        assertEquals(kafkaProperties.getSystemName(), publisher.getSystem());
        assertEquals(kafkaProperties.getServiceName(), publisher.getService());
    }

    @Test
    void auditUserTriggeredEvent_withExplicitParameters() {
        String token = createJeapTokenWithUserRoles(SIMPLE_AUTH_READ_ROLE);
        String serviceName = UUID.randomUUID().toString();
        String systemName = UUID.randomUUID().toString();
        AuditDto auditDto = new AuditDto(serviceName, systemName);


        given()
                .spec(auditBaseUrlSpec)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(auditDto)
                .when()
                .put("/audit")
                .then()
                .statusCode(HttpStatus.OK.value());

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> auditCommandConsumer.hasConsumedCommand());

        CreateAuditRecordCommand command = auditCommandConsumer.getFirstCommand();
        assertNotNull(command);
        CreateAuditRecordCommandPayload payload = command.getPayload();
        assertNotNull(payload);
        Object trigger = payload.getTrigger();
        assertThat(trigger).isInstanceOf(AuditUser.class);
        AuditUser auditUser = (AuditUser) trigger;
        assertEquals(SUBJECT, auditUser.getId());
        assertEquals("http://localhost/auth", auditUser.getIdentityProvider());

        AvroMessagePublisher publisher = command.getPublisher();
        assertNotNull(publisher);
        assertEquals(systemName, publisher.getSystem());
        assertEquals(serviceName, publisher.getService());
    }


    private String createJeapTokenWithUserRoles(String... roles) {
        return jwsBuilderFactory.createValidForFixedLongPeriodBuilder(SUBJECT, JeapAuthenticationContext.SYS).
                withUserRoles(roles).
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
        void putAudit(@RequestBody(required = false) AuditWithSystemSemanticIT.AuditDto auditDto) {
            if (auditDto == null || auditDto.getServiceName() == null || auditDto.getSystemName() == null) {
                log.info("Will send without user defined service name and system name");
                sender.auditUserTriggeredEvent(Instant.now(),
                        // Example on how to use the builder
                        builder -> {
                            builder.setEventType(AuditEventType.CREATED);
                            builder.setContext(USE_CASE, PROCESS_ID);
                        }
                );
            } else {
                log.info("Will send with user defined service name and system name");
                sender.auditUserTriggeredEvent(auditDto.getServiceName(), auditDto.getSystemName(), Instant.now(),
                        // Example on how to use the builder
                        builder -> {
                            builder.setEventType(AuditEventType.CREATED);
                            builder.setContext(USE_CASE, PROCESS_ID);
                        }
                );
            }
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @lombok.Data
    public static class AuditDto {
        String serviceName;
        String systemName;
    }

}
