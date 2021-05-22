package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EspecialidadesRepository extends JpaRepository<Especialidade, Long> {
    @Query("select e from Especialidade e where e.titulo like :search%")
    Page<Especialidade> findAllByTitulo(String search, Pageable pageable);
}
