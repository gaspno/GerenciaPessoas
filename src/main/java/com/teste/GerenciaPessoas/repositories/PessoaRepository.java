package com.teste.GerenciaPessoas.repositories;

import com.teste.GerenciaPessoas.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}