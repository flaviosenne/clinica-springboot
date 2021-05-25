package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
