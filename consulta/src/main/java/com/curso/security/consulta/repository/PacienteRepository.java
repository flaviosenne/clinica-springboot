package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query("select p from Paciente p where p.usuario.email like :email")
    Optional<Paciente> findByUsuarioEmail(String email);
}
