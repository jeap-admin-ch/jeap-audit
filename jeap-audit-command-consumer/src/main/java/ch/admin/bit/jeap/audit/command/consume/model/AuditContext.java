package ch.admin.bit.jeap.audit.command.consume.model;

public record AuditContext(String useCase,
                           String processId) {
}
