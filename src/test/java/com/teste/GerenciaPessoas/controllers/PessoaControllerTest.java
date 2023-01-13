package com.teste.GerenciaPessoas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.GerenciaPessoas.dtos.EnderecoDto;
import com.teste.GerenciaPessoas.dtos.PessoaDtoRequest;
import com.teste.GerenciaPessoas.entities.Endereco;
import com.teste.GerenciaPessoas.entities.Pessoa;
import com.teste.GerenciaPessoas.service.EnderecoService;
import com.teste.GerenciaPessoas.service.PessoaService;
import com.teste.GerenciaPessoas.utils.DateUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class PessoaControllerTest {

    private static final String API="/api/pessoa";

    @Autowired
    MockMvc mvc;

    @MockBean
    PessoaService pessoaService;

    @MockBean
    EnderecoService enderecoService;

    @Test
    @DisplayName("Deve cadastrar uma pessoa")
    public void cadastrarPessoa() throws Exception {

        PessoaDtoRequest pessoaDto=new PessoaDtoRequest("Roberto","12/01/1995",null);
        Pessoa pessoa=new Pessoa();
        pessoa.setNome(pessoaDto.getNome());
        pessoa.setData(DateUtil.dateFromString(pessoaDto.getData()));
        pessoa.setId(1L);
        BDDMockito.given(pessoaService.save(Mockito.any(PessoaDtoRequest.class))).willReturn(pessoa);
        String json=new ObjectMapper( ).writeValueAsString(pessoaDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status()
                        .isCreated())
                .andExpect(header().string("Location","http://localhost/api/pessoa/1"))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("nome").value("Roberto"))
                .andExpect(jsonPath("data").value("12/01/1995"))
                .andExpect(jsonPath("enderecos").isEmpty());

    }
    @Test
    @DisplayName("Deve retornar uma pessoa salva com endereços")
    public void obterPessoa() throws Exception {


        Pessoa pessoa=new Pessoa();
        pessoa.setNome("Roberto");
        pessoa.setData(DateUtil.dateFromString("12/01/1995"));
        pessoa.setId(4L);
        List<Endereco> enderecos= Arrays.asList(new Endereco(1L, "casa", "58941510", 312, "city 1", false, pessoa),
                new Endereco(2L, "apartamento", "58100010", 512, "city 2", true, pessoa));
        pessoa.setEnderecos(enderecos);
        BDDMockito.given(pessoaService.getPessoaByID(4L)).willReturn(pessoa);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(API+"/4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("id").value(4L))
                .andExpect(jsonPath("nome").value("Roberto"))
                .andExpect(jsonPath("data").value("12/01/1995"))
                .andExpect(jsonPath("enderecos").isNotEmpty())
                .andExpect(jsonPath("enderecos").isArray())
                .andExpect(jsonPath("enderecos", Matchers.hasSize(2)))
                .andExpect(jsonPath("enderecos[0].logradouro").value("casa"))
                .andExpect(jsonPath("enderecos[0].cep").value("58941510"))
                .andExpect(jsonPath("enderecos[0].numero").value(312))
                .andExpect(jsonPath("enderecos[0].cidade").value("city 1"))
                .andExpect(jsonPath("enderecos[0].isPrincipal").value(false))
                .andExpect(jsonPath("enderecos[1].logradouro").value("apartamento"))
                .andExpect(jsonPath("enderecos[1].cep").value("58100010"))
                .andExpect(jsonPath("enderecos[1].numero").value(512))
                .andExpect(jsonPath("enderecos[1].cidade").value("city 2"))
                .andExpect(jsonPath("enderecos[1].isPrincipal").value(true));

    }

    @Test
    @DisplayName("Deve atualizar uma pessoa com novos enderecos")
    public void atualizarPessoa() throws Exception {

        PessoaDtoRequest pessoaDto=new PessoaDtoRequest("Roberto","12/01/1995",null);
        Pessoa pessoa=new Pessoa();
        pessoa.setNome(pessoaDto.getNome());
        pessoa.setData(DateUtil.dateFromString(pessoaDto.getData()));
        pessoa.setId(1L);
        BDDMockito.given(pessoaService.update(Mockito.any(PessoaDtoRequest.class),Mockito.eq(1L) )).willReturn(pessoa);
        String json=new ObjectMapper( ).writeValueAsString(pessoaDto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(API+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("nome").value("Roberto"))
                .andExpect(jsonPath("data").value("12/01/1995"))
                .andExpect(jsonPath("enderecos").isEmpty())
                .andExpect(jsonPath("enderecos").isArray());

        PessoaDtoRequest pessoaDtoAtualizacao=new PessoaDtoRequest("Roberto","12/01/1995", List.of(new EnderecoDto("casa", "5120000", 412, "croacia", true)));
        pessoa.updatePessoa(pessoaDtoAtualizacao);
        pessoa.setEnderecos(pessoaDtoAtualizacao.getEnderecos().stream().map(Endereco::new).collect(Collectors.toList()));
        BDDMockito.given(pessoaService.update(pessoaDtoAtualizacao,1L)).willReturn(pessoa);

        mvc.perform(request)
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("nome").value("Roberto"))
                .andExpect(jsonPath("data").value("12/01/1995"))
                .andExpect(jsonPath("enderecos").isNotEmpty())
                .andExpect(jsonPath("enderecos").isArray())
                .andExpect(jsonPath("enderecos", Matchers.hasSize(1)))
                .andExpect(jsonPath("enderecos[0].logradouro").value("casa"))
                .andExpect(jsonPath("enderecos[0].cep").value("5120000"))
                .andExpect(jsonPath("enderecos[0].numero").value(412))
                .andExpect(jsonPath("enderecos[0].cidade").value("croacia"))
                .andExpect(jsonPath("enderecos[0].isPrincipal").value(true));

    }

    @Test
    @DisplayName("Deve adicionar  um endereco a pessoa")
    public void removerPessoa() throws Exception {

        PessoaDtoRequest pessoaDto=new PessoaDtoRequest("Roberto","12/01/1995",null);
        Pessoa pessoa=new Pessoa();
        pessoa.setNome(pessoaDto.getNome());
        pessoa.setData(DateUtil.dateFromString(pessoaDto.getData()));
        pessoa.setId(1L);
        BDDMockito.given(pessoaService.save(Mockito.any(PessoaDtoRequest.class))).willReturn(pessoa);
        Endereco endereco=new Endereco(1L,"apartamento","512341",12,"florian",false,pessoa);
        BDDMockito.given(pessoaService.addEndereco(Mockito.any(EnderecoDto.class),Mockito.eq(1L))).willReturn(endereco);
        String json=new ObjectMapper( ).writeValueAsString(pessoaDto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status()
                        .isCreated())
                .andExpect(header().string("Location","http://localhost/api/pessoa/1"))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("nome").value("Roberto"))
                .andExpect(jsonPath("data").value("12/01/1995"))
                .andExpect(jsonPath("enderecos").isEmpty());
        EnderecoDto dto=new EnderecoDto(endereco);
        json=new ObjectMapper( ).writeValueAsString(dto);
        request = MockMvcRequestBuilders.put(API+"/1/endereco")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("logradouro").value("apartamento"))
                .andExpect(jsonPath("cep").value("512341"))
                .andExpect(jsonPath("numero").value(12))
                .andExpect(jsonPath("cidade").value("florian"))
                .andExpect(jsonPath("isPrincipal").value(false));

    }
}
