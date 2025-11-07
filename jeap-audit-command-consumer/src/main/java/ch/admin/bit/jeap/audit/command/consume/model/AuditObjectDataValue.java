package ch.admin.bit.jeap.audit.command.consume.model;

import ch.admin.bit.jeap.audit.record.create.AuditObjectDataRole;

public class AuditObjectDataValue extends AuditObjectData {

    private final String value;

    public AuditObjectDataValue(String name, AuditObjectDataRole role, String value) {
        super(name, role);
        this.value = value;
    }

    @Override
    public AuditObjectDataType type() {
        return AuditObjectDataType.VALUE;
    }

    public String value() {
        return value;
    }
}
