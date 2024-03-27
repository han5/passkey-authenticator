package com.orgo.passkey;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import com.google.common.collect.ImmutableList;
import com.orgo.passkey.model.CredentialModel;
import com.orgo.passkey.util.JsonUtils;
import com.yubico.webauthn.data.AttestationConveyancePreference;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.AuthenticatorSelectionCriteria;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.COSEAlgorithmIdentifier;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.PublicKeyCredentialParameters;
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions;
import com.yubico.webauthn.data.PublicKeyCredentialType;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.yubico.webauthn.data.ResidentKeyRequirement;
import com.yubico.webauthn.data.UserIdentity;
import com.yubico.webauthn.data.UserVerificationRequirement;
import de.adesso.softauthn.Authenticators;
import de.adesso.softauthn.CredentialsContainer;
import de.adesso.softauthn.Origin;

/**
 *  PasskeyAuthenticator is a Passkey Authenticator
 *
 *  Example of a Relying Party Challenge (credential_options) :
 *       {
 *           "challenge":[-115,-6,38,78,56,-41,30,-108,-40,-118,-61,25,-112,-30,51,12,-93,-2,-123,3,110,17,-35,120,-60,83,-21,79,89,20,-36,55],
 *           "timeout":120000,
 *           "rpId":"passkey.com",
 *           "allowCredentials":[],
 *           "userVerification":"required"
 *       }
*/
public class PasskeyAuthenticator {

    private static final Function<String, CredentialModel.CredentialCreateOptions> credCreateOptsFromJson = JsonUtils.newJsonReads(CredentialModel.CredentialCreateOptions.class);
    private static final Function<String, CredentialModel.CredentialOptions> credOptsFromJson = JsonUtils.newJsonReads(CredentialModel.CredentialOptions.class);
    private static final Function<PublicKeyCredential<?,?>, String> jsonAssertCred = JsonUtils.newJsonWrites();
    private final Origin origin;
    private CredentialsContainer credentials;

    public PasskeyAuthenticator(String host) {
        this(new Origin("https", host, -1, null));
    }

    public PasskeyAuthenticator(Origin origin) {
        this.origin = origin;
        this.credentials = new CredentialsContainer(origin, ImmutableList.of(Authenticators.platform().build()));
    }

    public void clearCredentials() {
        credentials = new CredentialsContainer(origin, ImmutableList.of(Authenticators.platform().build()));
    }

    public PublicKeyCredential<?,?> newCredentialFromChallenge(String credentialOptions) {

        CredentialModel.CredentialCreateOptions sonyChallenge = null;

        try {
            sonyChallenge = credCreateOptsFromJson.apply(credentialOptions);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        RelyingPartyIdentity rP = RelyingPartyIdentity.builder()
            .id(sonyChallenge.rp.id)
            .name(sonyChallenge.rp.id)
            .build();

        ByteArray bChallenge = CredentialModel.toByteArray(sonyChallenge.challenge);
        List<Integer> userId = sonyChallenge.user.id;
        ByteArray bUserId = CredentialModel.toByteArray(userId);

        UserIdentity user = UserIdentity.builder()
            .name(sonyChallenge.user.name)
            .displayName(sonyChallenge.user.displayName)
            .id(bUserId)
            .build();

        PublicKeyCredentialParameters params = PublicKeyCredentialParameters.builder().alg(COSEAlgorithmIdentifier.ES256).type(PublicKeyCredentialType.PUBLIC_KEY).build();
        AuthenticatorSelectionCriteria criteria = AuthenticatorSelectionCriteria.builder()
            .residentKey(ResidentKeyRequirement.PREFERRED)
            .userVerification(UserVerificationRequirement.PREFERRED)
            .build();

        AttestationConveyancePreference attestation = AttestationConveyancePreference.NONE;
        /*RegistrationExtensionInputs ext = RegistrationExtensionInputs.builder().build();*/

        PublicKeyCredentialCreationOptions pKCCO = PublicKeyCredentialCreationOptions.builder()
            .rp(rP)
            .user(user)
            .challenge(bChallenge)
            .pubKeyCredParams(ImmutableList.of(params))
            .attestation(attestation)
            .authenticatorSelection(criteria)
            .excludeCredentials(Collections.emptySet())
            .build();

        return credentials.create(pKCCO);
    }

    public String credentialToAssertion(PublicKeyCredential<?,?> key) {
        return jsonAssertCred.apply(key);
    }

    public CredentialModel.CredentialOptions fromCredentialOptions(String credentialOptions) {
        try {
            return credOptsFromJson.apply(credentialOptions);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public PublicKeyCredential<?,?> newAssertionFromChallenge(String credentialOptions) {

        CredentialModel.CredentialOptions sonyChallenge = fromCredentialOptions(credentialOptions);

        ByteArray bChallenge = CredentialModel.toByteArray(sonyChallenge.challenge);

        PublicKeyCredential<?,?> keyResult = credentials.get(
            PublicKeyCredentialRequestOptions.builder()
                .challenge(bChallenge)
                .rpId(sonyChallenge.rpId)
                .timeout(sonyChallenge.timeout)
                .userVerification(UserVerificationRequirement.PREFERRED)
                .build()
        );

        return keyResult;
    }

    public AuthenticatorAssertionResponse authenticateResponse(PublicKeyCredential<?,?> pKey) {
        return (AuthenticatorAssertionResponse) pKey.getResponse();
    }

    public boolean doesCredentialChallengeHaveAllowListEntry(String credentialOptions) {
        CredentialModel.CredentialOptions sonyChallenge = fromCredentialOptions(credentialOptions);
        return !sonyChallenge.allowCredentials.isEmpty();
    }

}
