package com.teste.GerenciaPessoas.exception.business;

public class NotFoundPessoaException extends RuntimeException{
    public NotFoundPessoaException(String msg) {
        super(msg);
    }
}
