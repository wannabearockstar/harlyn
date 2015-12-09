package com.harlyn.exception;

/**
 * Created by wannabe on 08.12.15.
 */
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category with id " + id + " not found");
    }
}
