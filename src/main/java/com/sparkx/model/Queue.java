package com.sparkx.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Queue {
    private UUID queueId;
    private Timestamp createdTIme;


    public UUID getQueueId() {
        return queueId;
    }

    public void setQueueId(UUID queueId) {
        this.queueId = queueId;
    }

    public Timestamp getCreatedTIme() {
        return createdTIme;
    }

    public void setCreatedTIme(Timestamp createdTIme) {
        this.createdTIme = createdTIme;
    }

    @Override
    public String toString() {
        return  "queueId: " + queueId  + ", createdTime: " + createdTIme;
    }
}
