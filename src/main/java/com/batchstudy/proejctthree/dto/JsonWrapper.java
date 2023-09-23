package com.batchstudy.proejctthree.dto;

public class JsonWrapper {
    private Object value;

    public JsonWrapper(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
