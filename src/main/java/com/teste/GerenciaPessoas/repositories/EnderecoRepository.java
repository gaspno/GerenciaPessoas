package com.teste.GerenciaPessoas.repositories;

import com.teste.GerenciaPessoas.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}