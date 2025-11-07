package ch.admin.bit.jeap.audit.command.builder;

import ch.admin.bit.jeap.security.resource.authentication.ServletSimpleAuthorization;
import ch.admin.bit.jeap.security.resource.semanticAuthentication.ServletSemanticAuthorization;
import ch.admin.bit.jeap.security.resource.token.JeapAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CreateAuditRecordCommandUserInfoProvider {

    private final Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional;
    private final Optional<ServletSimpleAuthorization> simpleAuthorizationOptional;


    public CreateAuditRecordCommandUserInfoProvider(Optional<ServletSemanticAuthorization> jeapSemanticAuthorizationOptional,
                                                    Optional<ServletSimpleAuthorization> simpleAuthorizationOptional) {
        this.jeapSemanticAuthorizationOptional = jeapSemanticAuthorizationOptional;
        this.simpleAuthorizationOptional = simpleAuthorizationOptional;
        if (jeapSemanticAuthorizationOptional.isEmpty() && simpleAuthorizationOptional.isEmpty()) {
            throw new IllegalStateException("No ServletSemanticAuthorization or ServletSimpleAuthorization present");
        }
    }

    public JeapAuthenticationToken getJeapAuthenticationToken() {
        try {
            JeapAuthenticationToken jeapAuthenticationToken;
            if (jeapSemanticAuthorizationOptional.isPresent()) {
                ServletSemanticAuthorization servletSemanticAuthorization = jeapSemanticAuthorizationOptional.get();
                jeapAuthenticationToken = servletSemanticAuthorization.getAuthenticationToken();

            } else { // simple must be set in the else case because we check it in constructor
                ServletSimpleAuthorization servletSimpleAuthorization = simpleAuthorizationOptional.get();
                jeapAuthenticationToken = servletSimpleAuthorization.getJeapAuthenticationToken();
            }
            return jeapAuthenticationToken;
        } catch (Exception e) {
            throw AuditException.unsupportedAuthentication(SecurityContextHolder.getContext().getAuthentication());
        }
    }

}
