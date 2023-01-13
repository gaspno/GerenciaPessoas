package com.teste.GerenciaPessoas.exception.business;

import java.text.ParseException;

public class DateFormatException extends RuntimeException {
    public DateFormatException(ParseException e) {
        super(e.getMessage());
    }
}
