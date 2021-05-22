package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadesRepository extends JpaRepository<Especialidade, Long> {
}
