//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.validators;

@FunctionalInterface
public interface Validation<T> {
    ValidationResult test(T var1);

    default Validation<T> and(Validation<T> other) {
        return (param) -> {
            ValidationResult firstResult = this.test(param);
            return !firstResult.isValid() ? firstResult : other.test(param);
        };
    }

    default Validation<T> or(Validation<T> other) {
        return (param) -> {
            ValidationResult firstResult = this.test(param);
            return firstResult.isValid() ? firstResult : other.test(param);
        };
    }
}
