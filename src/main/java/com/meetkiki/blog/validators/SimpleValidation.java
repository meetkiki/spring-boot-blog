//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.validators;

import java.util.function.Predicate;

public class SimpleValidation<T> implements Validation<T> {
    private Predicate<T> predicate;
    private String onErrorMessage;

    public static <T> SimpleValidation<T> from(Predicate<T> predicate, String onErrorMessage) {
        return new SimpleValidation(predicate, onErrorMessage);
    }

    public ValidationResult test(T param) {
        return this.predicate.test(param) ? ValidationResult.ok() : ValidationResult.fail(this.onErrorMessage);
    }

    public SimpleValidation(Predicate<T> predicate, String onErrorMessage) {
        this.predicate = predicate;
        this.onErrorMessage = onErrorMessage;
    }
}
