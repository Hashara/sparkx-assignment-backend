package com.sparkx.model.dao;

import java.util.UUID;

public class QueuePatientSerialDAO {
    private UUID queueId;
    private UUID serialNumber;

    public UUID getQueueId() {
        return queueId;
    }

    public void setQueueId(UUID queueId) {
        this.queueId = queueId;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }
}
