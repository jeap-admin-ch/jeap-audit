package ch.admin.bit.jeap.audit.command.consume.model;

public record AuditTriggerUser(String id,
                               String identityProvider) implements AuditTrigger {

    @Override
    public AuditTriggerType type() {
        return AuditTriggerType.USER;
    }
}
