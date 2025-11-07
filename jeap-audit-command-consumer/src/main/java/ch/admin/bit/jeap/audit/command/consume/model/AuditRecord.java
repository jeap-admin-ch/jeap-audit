package ch.admin.bit.jeap.audit.command.consume.model;

import java.time.Instant;

public record AuditRecord(String serviceName,
                          String systemName,
                          Instant timestamp,
                          AuditEvent auditEvent,
                          AuditTrigger trigger,
                          AuditObject auditedData) {
}
