package com.sparkx.model;

import java.sql.Timestamp;

public class Queue {
    private int queueId;
    private Timestamp createdTIme;


    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
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
