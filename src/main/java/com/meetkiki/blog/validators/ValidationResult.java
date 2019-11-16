//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.validators;


import com.meetkiki.blog.exception.ValidatorException;

import java.util.Objects;

public class ValidationResult {
    private boolean valid;
    private String message;
    private String code;

    public static ValidationResult ok() {
        return new ValidationResult(true, (String)null, (String)null);
    }

    public static ValidationResult ok(String code) {
        return new ValidationResult(true, (String)null, code);
    }

    public static ValidationResult fail(String message) {
        return new ValidationResult(false, message, (String)null);
    }

    public static ValidationResult fail(String code, String message) {
        return new ValidationResult(false, message, code);
    }

    public void throwIfInvalid() {
        this.throwMessage(this.getMessage());
    }

    public void throwIfInvalid(String fieldName) {
        if (!this.isValid()) {
            throw new ValidatorException(fieldName + " " + this.getMessage());
        }
    }

    public void throwMessage(String msg) {
        if (!this.isValid()) {
            throw new ValidatorException(msg);
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }


    protected boolean canEqual(Object other) {
        return other instanceof ValidationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return valid == that.valid &&
                Objects.equals(message, that.message) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valid, message, code);
    }

    public String toString() {
        return "ValidationResult(valid=" + this.isValid() + ", message=" + this.getMessage() + ", code=" + this.getCode() + ")";
    }

    public ValidationResult(boolean valid, String message, String code) {
        this.valid = valid;
        this.message = message;
        this.code = code;
    }
}
