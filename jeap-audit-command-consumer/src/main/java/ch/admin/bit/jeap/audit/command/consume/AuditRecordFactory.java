package ch.admin.bit.jeap.audit.command.consume;

import ch.admin.bit.jeap.audit.command.consume.model.*;
import ch.admin.bit.jeap.audit.record.create.AuditEventDetails;
import ch.admin.bit.jeap.audit.record.create.AuditSystemComponent;
import ch.admin.bit.jeap.audit.record.create.AuditUser;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommand;
import ch.admin.bit.jeap.audit.record.create.CreateAuditRecordCommandPayload;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class AuditRecordFactory {

    public static AuditRecord createAuditRecord(CreateAuditRecordCommand command) {
        String serviceName = command.getPublisher().getService();
        String systemName = command.getPublisher().getSystem();

        CreateAuditRecordCommandPayload payload = command.getPayload();
        Instant timestamp = payload.getTimestamp();
        AuditEvent auditEvent = createEvent(payload);
        AuditTrigger trigger = createTrigger(payload);
        AuditObject auditedData = createAuditObject(payload);

        return new AuditRecord(serviceName, systemName, timestamp, auditEvent, trigger, auditedData);
    }

    private static AuditEvent createEvent(CreateAuditRecordCommandPayload payload) {
        AuditEventDetails event = payload.getEvent();
        AuditContext context = createContext(event.getContext());
        List<AuditEventDataElement> eventDataElements = createDataElements(event.getEventData());
        return new AuditEvent(event.getType(), context, eventDataElements);
    }

    private static AuditContext createContext(ch.admin.bit.jeap.audit.record.create.AuditContext context) {
        if (context == null) {
            return null;
        }
        return new AuditContext(context.getUseCase(), context.getProcessId());
    }

    private static List<AuditEventDataElement> createDataElements(List<ch.admin.bit.jeap.audit.record.create.AuditEventDataElement> eventData) {
        if (eventData == null || eventData.isEmpty()) {
            return new ArrayList<>();
        }
        List<AuditEventDataElement> dataElements = new ArrayList<>();
        eventData.forEach(
                eventDataElement -> dataElements.add(new AuditEventDataElement(eventDataElement.getKey(), eventDataElement.getValue()))
        );

        return dataElements;
    }

    private static AuditTrigger createTrigger(CreateAuditRecordCommandPayload payload) {
        Object trigger = payload.getTrigger();
        if (trigger instanceof AuditUser user) {
            return new AuditTriggerUser(user.getId(), user.getIdentityProvider());
        } else if (trigger instanceof AuditSystemComponent systemComponent) {
            return new AuditTriggerSystemComponent(systemComponent.getDepartment(), systemComponent.getSystem(), systemComponent.getComponent());
        }
        throw new IllegalStateException("Trigger is of unknown type: " + trigger.getClass());
    }

    private static AuditObject createAuditObject(CreateAuditRecordCommandPayload payload) {
        ch.admin.bit.jeap.audit.record.create.AuditObject auditedData = payload.getAuditedData();
        if (auditedData == null) {
            return null;
        }
        List<AuditObjectData> dataList = createAuditObjectDataList(auditedData.getObjectData());

        return new AuditObject(auditedData.getType(), auditedData.getId(), auditedData.getVersion(), dataList);
    }

    private static List<AuditObjectData> createAuditObjectDataList(List<Object> objectData) {
        if (objectData == null || objectData.isEmpty()) {
            return new ArrayList<>();
        }
        List<AuditObjectData> dataList = new ArrayList<>();
        objectData.forEach(objectDataElement -> {
            AuditObjectData auditObjectData = createAuditObjectData(objectDataElement);
            dataList.add(auditObjectData);
        });
        return dataList;
    }

    private static AuditObjectData createAuditObjectData(Object objectDataElement) {
        if (objectDataElement instanceof ch.admin.bit.jeap.audit.record.create.AuditObjectDataValue value) {
            return new AuditObjectDataValue(value.getName(), value.getRole(), value.getValue());
        } else if (objectDataElement instanceof ch.admin.bit.jeap.audit.record.create.AuditObjectDataJSON json) {
            return new AuditObjectDataJSON(json.getName(), json.getRole(), json.getJsonAsUTF8());
        } else if (objectDataElement instanceof ch.admin.bit.jeap.audit.record.create.AuditObjectDataS3 s3) {
            return new AuditObjectDataS3(s3.getName(), s3.getRole(), s3.getObjectReference());
        }
        throw new IllegalStateException("Object data element is of unknown type: " + objectDataElement.getClass());
    }
}
