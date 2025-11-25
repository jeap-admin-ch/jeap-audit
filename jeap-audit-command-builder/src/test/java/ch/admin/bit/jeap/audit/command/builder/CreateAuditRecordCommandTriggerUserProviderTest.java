package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.security.resource.authentication.ServletSimpleAuthorization;
import ch.admin.bit.jeap.security.resource.semanticAuthentication.ServletSemanticAuthorization;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateAuditRecordCommandTriggerUserProviderTest {

    private static final String USER_ID = "test-user-id";
    private static final String ISSUER = "https://test-issuer.com";

    @Test
    void tokenFromSimpleAuthorization() throws Exception {
        JeapAuthenticationToken token = createMockToken(USER_ID, ISSUER);

        ServletSimpleAuthorization simpleAuthorization = mock(ServletSimpleAuthorization.class);
        when(simpleAuthorization.getJeapAuthenticationToken()).thenReturn(token);

        Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional = Optional.empty();
        Optional<ServletSimpleAuthorization> simpleAuthorizationOptional = Optional.of(simpleAuthorization);

        CreateAuditRecordCommandTriggerUserProvider triggerUserProvider = new CreateAuditRecordCommandTriggerUserProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);

        CreateAuditRecordCommandBuilder builder = mock(CreateAuditRecordCommandBuilder.class);
        triggerUserProvider.provideTriggerUser(builder);

        verify(builder).setTriggerUser(USER_ID, ISSUER);
    }

    @Test
    void tokenFromSemanticAuthorization() throws Exception {
        JeapAuthenticationToken token = createMockToken(USER_ID, ISSUER);

        ServletSemanticAuthorization jeapSemanticAuthorization = mock(ServletSemanticAuthorization.class);
        when(jeapSemanticAuthorization.getAuthenticationToken()).thenReturn(token);

        Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional = Optional.of(jeapSemanticAuthorization);
        Optional<ServletSimpleAuthorization> simpleAuthorizationOptional = Optional.empty();

        CreateAuditRecordCommandTriggerUserProvider triggerUserProvider = new CreateAuditRecordCommandTriggerUserProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);

        CreateAuditRecordCommandBuilder builder = mock(CreateAuditRecordCommandBuilder.class);
        triggerUserProvider.provideTriggerUser(builder);

        verify(builder).setTriggerUser(USER_ID, ISSUER);
    }

    @Test
    void throwAuditExceptionInCaseOfUnsupportedAuthentication() {
        ServletSemanticAuthorization jeapSemanticAuthorization = mock(ServletSemanticAuthorization.class);
        when(jeapSemanticAuthorization.getAuthenticationToken()).thenThrow(new RuntimeException("Uiuiui"));

        Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional = Optional.of(jeapSemanticAuthorization);
        Optional<ServletSimpleAuthorization> simpleAuthorizationOptional = Optional.empty();

        CreateAuditRecordCommandTriggerUserProvider triggerUserProvider = new CreateAuditRecordCommandTriggerUserProvider(jeapSemanticAuthorizationOptional, simpleAuthorizationOptional);

        CreateAuditRecordCommandBuilder builder = mock(CreateAuditRecordCommandBuilder.class);
        assertThrows(AuditException.class, () -> triggerUserProvider.provideTriggerUser(builder));
    }

    @Test
    void failOnCreationIfNoAuthorizationSet() {
        Executable creation = () ->
                new CreateAuditRecordCommandTriggerUserProvider(Optional.empty(), Optional.empty());

        assertThrows(IllegalStateException.class, creation);
    }

    private JeapAuthenticationToken createMockToken(String userId, String issuer) throws Exception {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getIssuer()).thenReturn(java.net.URI.create(issuer).toURL());
        JeapAuthenticationToken token = mock(JeapAuthenticationToken.class);
        when(token.getTokenSubject()).thenReturn(userId);
        when(token.getToken()).thenReturn(jwt);
        return token;
    }
}
