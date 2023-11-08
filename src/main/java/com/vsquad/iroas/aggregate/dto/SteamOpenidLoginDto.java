package com.vsquad.iroas.aggregate.dto;


import org.hibernate.validator.constraints.URL;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

public class SteamOpenidLoginDto {

    @URL
    @NotBlank
    private String ns;

    @URL
    @NotBlank
    private String op_endpoint;

    @URL
    @NotBlank
    private String claimed_id;

    @URL
    @NotBlank
    private String identity;

    @URL
    @NotBlank
    private String return_to;

    @NotBlank
    private String response_nonce;

    @NotBlank
    private String assoc_handle;

    @NotBlank
    @Pattern(regexp = "^\\w+(?:,\\w+)*$")
    private String signed;

    @NotBlank
    private String sig;

    public SteamOpenidLoginDto() {
    }

    public SteamOpenidLoginDto(String ns, String op_endpoint, String claimed_id, String identity, String return_to, String response_nonce, String assoc_handle, String signed, String sig) {
        this.ns = ns;
        this.op_endpoint = op_endpoint;
        this.claimed_id = claimed_id;
        this.identity = identity;
        this.return_to = return_to;
        this.response_nonce = response_nonce;
        this.assoc_handle = assoc_handle;
        this.signed = signed;
        this.sig = sig;

        this.validate();
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getOp_endpoint() {
        return op_endpoint;
    }

    public void setOp_endpoint(String op_endpoint) {
        this.op_endpoint = op_endpoint;
    }

    public String getClaimed_id() {
        return claimed_id;
    }

    public void setClaimed_id(String claimed_id) {
        this.claimed_id = claimed_id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getReturn_to() {
        return return_to;
    }

    public void setReturn_to(String return_to) {
        this.return_to = return_to;
    }

    public String getResponse_nonce() {
        return response_nonce;
    }

    public void setResponse_nonce(String response_nonce) {
        this.response_nonce = response_nonce;
    }

    public String getAssoc_handle() {
        return assoc_handle;
    }

    public void setAssoc_handle(String assoc_handle) {
        this.assoc_handle = assoc_handle;
    }

    public String getSigned() {
        return signed;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    private void validate() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator  = factory.getValidator();
            Set<ConstraintViolation<SteamOpenidLoginDto>> violations = validator.validate(this);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }
}
