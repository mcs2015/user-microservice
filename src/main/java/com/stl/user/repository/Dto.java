package com.stl.user.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public abstract class Dto {

    @JsonIgnore
    private boolean deleted = true;

    @JsonIgnore
    private String createdTimestamp=ZonedDateTime.now(ZoneOffset.UTC).toString();

    @JsonIgnore
    private String updatedTimestamp=ZonedDateTime.now(ZoneOffset.UTC).toString();

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

}
