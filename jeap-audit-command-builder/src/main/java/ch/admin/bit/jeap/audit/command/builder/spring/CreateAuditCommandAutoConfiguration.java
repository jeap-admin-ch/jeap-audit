package ch.admin.bit.jeap.audit.command.builder.spring;

import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandBuilderFactory;
import ch.admin.bit.jeap.audit.command.builder.CreateAuditRecordCommandUserInfoProvider;
import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.security.resource.authentication.ServletSimpleAuthorization;
import ch.admin.bit.jeap.security.resource.semanticAuthentication.ServletSemanticAuthorization;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

public class CreateAuditCommandAutoConfiguration {

    /**
     * @param jeapSemanticAuthorizationOptional the jeapSemanticAuthorization is set when semantic authorization is used
     * @param simpleAuthorizationOptional       the simpleAuthorization is set when simple authorization is used
     */
    @Bean
    @ConditionalOnClass(JeapAuthenticationToken.class) // only if jeap-security is in classpath
    public CreateAuditRecordCommandUserInfoProvider createAuditRecordCommandUserProvider(
            Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional,
            Optional<ServletSimpleAuthorization> simpleAuthorizationOptional
    ) {
        return new CreateAuditRecordCommandUserInfoProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);
    }

    /**
     * @param userInfoProvider the user info provider
     * @param kafkaProperties  the KafkaProperties
     */
    @Bean
    public CreateAuditRecordCommandBuilderFactory auditRecordCommandBuilderFactory(
            Optional<CreateAuditRecordCommandUserInfoProvider> userInfoProvider,
            KafkaProperties kafkaProperties) {
        return new CreateAuditRecordCommandBuilderFactory(userInfoProvider, kafkaProperties);
    }
}
