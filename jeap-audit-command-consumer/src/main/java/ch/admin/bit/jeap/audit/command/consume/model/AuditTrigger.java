package ch.admin.bit.jeap.audit.command.consume.model;

public interface AuditTrigger {

    AuditTriggerType type();

    enum AuditTriggerType {
        USER, SYSTEM_COMPONENT
    }
}
