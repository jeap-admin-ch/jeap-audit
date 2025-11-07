package ch.admin.bit.jeap.audit.command.consume.model;

import ch.admin.bit.jeap.audit.record.create.AuditObjectDataRole;

import java.nio.ByteBuffer;

public class AuditObjectDataJSON extends AuditObjectData {

    private final ByteBuffer jsonAsUtf8;

    public AuditObjectDataJSON(String name, AuditObjectDataRole role, ByteBuffer jsonAsUtf8) {
        super(name, role);
        this.jsonAsUtf8 = jsonAsUtf8;
    }

    @Override
    public AuditObjectDataType type() {
        return AuditObjectDataType.JSON;
    }

    public ByteBuffer jsonAsUtf8() {
        return jsonAsUtf8;
    }
}
