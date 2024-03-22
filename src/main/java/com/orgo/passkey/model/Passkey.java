package com.orgo.passkey.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Passkey implements FidoDevice {

    private final String fidoDeviceId;
    private final String userAgent;
    private final String attributes;
    private final Long createdAt;
    private final Long lastUsed;

    public Passkey(
        @JsonProperty("fidoDeviceId")
        String fidoDeviceId,
        @JsonProperty("userAgent")
        String userAgent,
        @JsonProperty("attributes")
        String attributes,
        @JsonProperty("createdAt")
        Long createdAt,
        @JsonProperty("lastUsed")
        Long lastUsed
    ) {
        this.fidoDeviceId = fidoDeviceId;
        this.userAgent = userAgent;
        this.attributes = attributes;
        this.createdAt = createdAt;
        this.lastUsed = lastUsed;
    }

    public String getFidoDeviceId() {
        return fidoDeviceId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAttributes() {
        return attributes;
    }

    public Long createdAt() {
        return createdAt;
    }

    public Long lastUsed() {
        return lastUsed;
    }
}
