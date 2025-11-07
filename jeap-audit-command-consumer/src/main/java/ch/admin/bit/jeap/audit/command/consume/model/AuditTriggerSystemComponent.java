package ch.admin.bit.jeap.audit.command.consume.model;

public record AuditTriggerSystemComponent(String department,
                                          String system,
                                          String component) implements AuditTrigger {

    @Override
    public AuditTriggerType type() {
        return AuditTriggerType.SYSTEM_COMPONENT;
    }
}
