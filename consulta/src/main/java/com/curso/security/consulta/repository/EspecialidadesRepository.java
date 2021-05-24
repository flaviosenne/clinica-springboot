package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface EspecialidadesRepository extends JpaRepository<Especialidade, Long> {
    @Query("select e from Especialidade e where e.titulo like :search%")
    Page<Especialidade> findAllByTitulo(String search, Pageable pageable);

    @Query("select e.titulo from Especialidade e where e.titulo like :termo%")
    List<String> findEspecialidadesBuTermo(String termo);

    @Query("select e from Especialidade e where e.titulo in :titulo")
    Set<Especialidade> findByTitulos(String[] titulo);

    @Query("select e from Especialidade e " +
            "join e.medicos m " +
            "where m.id = :id")
    Page<Especialidade> findByIdMedico(Long id, Pageable pageable);
}
