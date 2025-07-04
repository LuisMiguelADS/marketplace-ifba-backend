package com.marketplace.ifba.model;

import java.util.UUID;

public class Tag {
    private UUID idTag;
    private String tag;

    public Tag(UUID idTag, String tag) {
        this.idTag = idTag;
        this.tag = tag;
    }

    public UUID getIdTag() {
        return idTag;
    }

    public void setIdTag(UUID idTag) {
        this.idTag = idTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
