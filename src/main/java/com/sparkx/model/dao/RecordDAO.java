package com.sparkx.model.dao;

import com.sparkx.model.Record;
import com.sparkx.model.Severity;

import java.util.List;

public class RecordDAO extends Record {
    private List<Severity> severityList;

    public List<Severity> getSeverityList() {
        return severityList;
    }

    public void setSeverityList(List<Severity> severityList) {
        this.severityList = severityList;
    }


}
