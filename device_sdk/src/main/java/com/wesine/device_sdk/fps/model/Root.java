package com.wesine.device_sdk.fps.model;

/**
 * Created by doug on 18-2-28.
 */

public class Root {
    private String type;

    private String from;

    private String ts;

    private Content content;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return this.from;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getTs() {
        return this.ts;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return this.content;
    }
}
