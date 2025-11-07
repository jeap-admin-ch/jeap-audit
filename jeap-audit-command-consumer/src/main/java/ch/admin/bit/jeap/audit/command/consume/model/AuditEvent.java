package ch.admin.bit.jeap.audit.command.consume.model;

import ch.admin.bit.jeap.audit.record.create.AuditEventType;

import java.util.List;

public record AuditEvent(AuditEventType eventType,
                         AuditContext context,
                         List<AuditEventDataElement> eventDataElements) {
}
