package com.orgo.passkey.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PasskeyResource {

    @JsonProperty("account_id")
    @NotNull
    private String accountId;

    @JsonProperty("username")
    @NotNull
    private String username;

    @JsonProperty("passkeys")
    @NotNull
    private Collection<? extends FidoDevice> passkeys;

    public PasskeyResource(
        String accountId,
        String username,
        Collection<Passkey>  passkeys
    ) {
        this.accountId = accountId;
        this.username = username;
        this.passkeys = passkeys;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public Collection<? extends FidoDevice> getPasskeys() {
        return passkeys;
    }
}