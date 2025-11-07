package ch.admin.bit.jeap.audit.command.consume.model;

import ch.admin.bit.jeap.audit.record.create.AuditObjectDataRole;

public class AuditObjectDataS3 extends AuditObjectData {

    private final String objectReference;

    public AuditObjectDataS3(String name, AuditObjectDataRole role, String objectReference) {
        super(name, role);
        this.objectReference = objectReference;
    }

    @Override
    public AuditObjectDataType type() {
        return AuditObjectDataType.S3_REFERENCE;
    }

    public String objectReference() {
        return objectReference;
    }
}
