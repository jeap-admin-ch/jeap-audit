package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.messaging.kafka.properties.KafkaProperties;
import ch.admin.bit.jeap.security.resource.authentication.ServletSimpleAuthorization;
import ch.admin.bit.jeap.security.resource.semanticAuthentication.ServletSemanticAuthorization;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateAuditRecordCommandUserInfoProviderTest {

    private static final String USER = "User1";
    private static final String IDENTITY_PROVIDER = "https://example.com/path/to/resource";

    @Test
    void tokenFromSimpleAuthorization() throws Exception {
        JeapAuthenticationToken token = mock(JeapAuthenticationToken.class);

        ServletSimpleAuthorization simpleAuthorization = mock(ServletSimpleAuthorization.class);
        when(simpleAuthorization.getJeapAuthenticationToken()).thenReturn(token);

        Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional = Optional.empty();
        Optional<ServletSimpleAuthorization> simpleAuthorizationOptional = Optional.of(simpleAuthorization);

        CreateAuditRecordCommandUserInfoProvider userInfoProvider = new CreateAuditRecordCommandUserInfoProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);
        assertEquals(token, userInfoProvider.getJeapAuthenticationToken());
    }

    @Test
    void tokenFromSemanticAuthorization() {
        JeapAuthenticationToken token = mock(JeapAuthenticationToken.class);

        ServletSemanticAuthorization jeapSemanticAuthorization = mock(ServletSemanticAuthorization.class);
        when(jeapSemanticAuthorization.getAuthenticationToken()).thenReturn(token);

        Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional = Optional.of(jeapSemanticAuthorization);
        Optional<ServletSimpleAuthorization> simpleAuthorizationOptional = Optional.empty();

        CreateAuditRecordCommandUserInfoProvider userInfoProvider = new CreateAuditRecordCommandUserInfoProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);
        assertEquals(token, userInfoProvider.getJeapAuthenticationToken());
    }

    @Test
    void throwAuditExceptionInCaseOfUnsupportedAuthentication() throws Exception {
         ServletSemanticAuthorization jeapSemanticAuthorization = mock(ServletSemanticAuthorization.class);
        when(jeapSemanticAuthorization.getAuthenticationToken()).thenThrow(new RuntimeException("Uiuiui"));

        Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional = Optional.of(jeapSemanticAuthorization);
        Optional<ServletSimpleAuthorization> simpleAuthorizationOptional = Optional.empty();

        CreateAuditRecordCommandUserInfoProvider userInfoProvider = new CreateAuditRecordCommandUserInfoProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);
        assertThrows(AuditException.class,() -> userInfoProvider.getJeapAuthenticationToken());
    }

    @Test
    void failOnCreationIfNoAuthorizationSet() {
        Executable creation = () ->
                new CreateAuditRecordCommandUserInfoProvider(Optional.empty(), Optional.empty());

        assertThrows(IllegalStateException.class, creation);
    }
}
