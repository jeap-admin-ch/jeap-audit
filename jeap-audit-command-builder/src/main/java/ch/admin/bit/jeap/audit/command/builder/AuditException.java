package ch.admin.bit.jeap.audit.command.builder;

public class AuditException extends RuntimeException {

    private AuditException(final String message) {
        super(message);
    }

    public static AuditException unsupportedAuthentication(final Object authentication) {
        return new AuditException(String.format("Could not determine user data from the security context. " +
                "Unsupported user Authentication: %s", authentication));
    }

    public static AuditException unsupportedWithoutJeapSecurity() {
        return new AuditException("You cannot use user convenience method without jEAP security");
    }
}
