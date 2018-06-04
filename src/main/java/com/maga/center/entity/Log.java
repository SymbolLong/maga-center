package com.maga.center.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Log extends BaseEntity {

    private String accessKey;
    private String api;
    @Lob
    private String result;
    private String time;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
