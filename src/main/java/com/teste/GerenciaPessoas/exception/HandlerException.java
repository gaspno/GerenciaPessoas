package com.teste.GerenciaPessoas.exception;

import com.teste.GerenciaPessoas.exception.business.DateFormatException;
import com.teste.GerenciaPessoas.exception.business.NotFoundEnderecoException;
import com.teste.GerenciaPessoas.exception.business.NotFoundPessoaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(NotFoundPessoaException.class)
    public ResponseEntity notFoundPessoaException(NotFoundPessoaException exception){
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
    @ExceptionHandler(NotFoundEnderecoException.class)
    public ResponseEntity notFoundEnderecoException(NotFoundEnderecoException exception){
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
    @ExceptionHandler(DateFormatException.class)
    public ResponseEntity dateFormatException(DateFormatException exception){
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity notValidArgumentException(MethodArgumentNotValidException exception){
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getBindingResult().getFieldErrors().stream().map(e->e.getField()+" esta errado ou nulo").collect(Collectors.toList())));
    }
}
