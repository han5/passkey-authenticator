package com.orgo.passkey.model;

public interface FidoDevice {

    String getFidoDeviceId();
    String getUserAgent();
    String getAttributes();
    Long createdAt();
    Long lastUsed();

}
