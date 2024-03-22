package com.orgo.passkey.model;

import java.util.Base64;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yubico.webauthn.data.ByteArray;

public class CredentialModel {

    public static ByteArray toByteArray(List<Integer> input) {
        byte[] bs = new byte[input.size()];
        for(int i=0; i<input.size(); i++) {
            bs[i] = input.get(i).byteValue();
        }
        return ByteArray.fromBase64(Base64.getEncoder().encodeToString(bs));
    }

    public static class CredentialOptions {
        @JsonProperty("challenge")
        public List<Integer> challenge;
        @JsonProperty("rpId")
        public String rpId;
        @JsonProperty("userVerification")
        public String userVerification;
        @JsonProperty("timeout")
        public long timeout;
        @JsonProperty("allowCredentials")
        public List<CredentialDescriptor> allowCredentials;
    }

    public static class CredentialCreateOptions {
        @JsonProperty("attestation")
        public String attestation;
        @JsonProperty("challenge")
        public List<Integer> challenge;
        @JsonProperty("authenticatorSelection")
        public AuthenticatorSelection authenticatorSelection;
        @JsonProperty("user")
        public User user;
        @JsonProperty("rp")
        public Rp rp;
        @JsonProperty("timeout")
        public int timeout;
        @JsonProperty("excludeCredentials")
        public List<ExcludeCredential> excludeCredentials;
        @JsonProperty("pubKeyCredParams")
        public List<PubKeyCredParam> pubKeyCredParams;
    }

    public static class AuthenticatorSelection {
        @JsonProperty("residentKey")
        public String residentKey;
        @JsonProperty("requireResidentKey")
        public boolean requireResidentKey;
        @JsonProperty("userVerification")
        public String userVerification;
    }

    public static class User {
        @JsonProperty("id")
        public List<Integer> id;
        @JsonProperty("displayName")
        public String displayName;
        public String name;
    }

    public static class Rp {
        public String id;
        public String name;
    }

    public static class ExcludeCredential {
        public String type;
        public List<Integer> id;
    }

    public static class PubKeyCredParam {
        public String type;
        public String alg;
    }

    public static class CredentialDescriptor {
        public String type;
        public List<Integer> id;
    }
}
