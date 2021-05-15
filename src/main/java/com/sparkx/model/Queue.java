package com.sparkx.model;

import com.sparkx.model.Types.StatusType;

public class Queue{
    private int queueId;
    private StatusType status;


    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}
