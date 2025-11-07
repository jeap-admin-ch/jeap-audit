package ch.admin.bit.jeap.audit.command.consume.model;

import java.util.List;

public record AuditObject(String type,
                          String id,
                          String version,
                          List<AuditObjectData> objectDataList) {
}
