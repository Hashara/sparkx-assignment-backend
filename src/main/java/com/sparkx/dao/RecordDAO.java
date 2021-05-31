package com.sparkx.dao;

import com.sparkx.model.Record;
import com.sparkx.model.Severity;

import java.util.List;
import java.util.UUID;

public class RecordDAO extends Record {
    private List<Severity> severityList;

    public List<Severity> getSeverityList() {
        return severityList;
    }

    public void setSeverityList(List<Severity> severityList) {
        this.severityList = severityList;
    }


}
