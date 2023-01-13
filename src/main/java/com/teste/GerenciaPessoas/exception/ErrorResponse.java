package com.teste.GerenciaPessoas.exception;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<String> errors=new ArrayList<>();
    public ErrorResponse(String msg) {
        this.errors.add(msg);
    }
    public ErrorResponse(List<String> msgs) {
        msgs.stream().forEach(e->this.errors.add(e));
    }
    public List<String> getErrors(){
        return errors;
    }

}
