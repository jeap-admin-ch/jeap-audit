package ch.admin.bit.jeap.audit.command.consume.model;

import ch.admin.bit.jeap.audit.record.create.AuditObjectDataRole;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AuditObjectData {

    private final String name;
    private final AuditObjectDataRole role;

    public String name() {
        return name;
    }

    public AuditObjectDataRole role() {
        return role;
    }

    public abstract AuditObjectDataType type();

    public enum AuditObjectDataType {
        VALUE, JSON, S3_REFERENCE
    }
}
