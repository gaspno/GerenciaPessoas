package com.teste.GerenciaPessoas.controllers;

import com.teste.GerenciaPessoas.dtos.EnderecoDto;
import com.teste.GerenciaPessoas.dtos.PessoaDtoRequest;
import com.teste.GerenciaPessoas.dtos.PessoaDtoResponse;
import com.teste.GerenciaPessoas.entities.Pessoa;
import com.teste.GerenciaPessoas.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaDtoResponse> criarPessoa(@Valid @RequestBody PessoaDtoRequest pessoaDto){
        Pessoa pessoa=pessoaService.save(pessoaDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pessoa.getId()).toUri();
        return ResponseEntity.created(location).body(new PessoaDtoResponse(pessoa));
    }
    @PutMapping(path = "{id}")
    public ResponseEntity<PessoaDtoResponse> atualizarPessoa(@Valid  @RequestBody PessoaDtoRequest pessoaDto,@PathVariable("id") String id){
        Pessoa pessoa= pessoaService.update(pessoaDto,Long.valueOf(id));
        return ResponseEntity.ok(new PessoaDtoResponse(pessoa));
    }
    @PutMapping(path = "{id}/endereco")
    public ResponseEntity<EnderecoDto> adicionarEndereco(@Valid @RequestBody EnderecoDto enderecoDto, @PathVariable("id") String id){
        EnderecoDto endereco= new EnderecoDto(pessoaService.addEndereco(enderecoDto,Long.valueOf(id)));
        return ResponseEntity.ok((endereco));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<PessoaDtoResponse> getPessoa(@PathVariable("id") String id){
        Pessoa pessoa=pessoaService.getPessoaByID(Long.valueOf(id));
        return ResponseEntity.ok(new PessoaDtoResponse(pessoa));
    }
    @DeleteMapping(path = "{id}")
    public ResponseEntity<PessoaDtoResponse> removePessoa(@PathVariable("id") String id){
        pessoaService.delete(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<Pessoa>> getPessoas(){
        List<Pessoa> pessoas=pessoaService.getAllPessoa();
        return ResponseEntity.ok(pessoas);
    }
}
