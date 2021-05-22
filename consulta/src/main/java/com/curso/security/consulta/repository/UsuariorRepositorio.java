package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuariorRepositorio extends JpaRepository<Usuario, Long> {
    @Query("select u from Usuario u where u.email like :email")
    Usuario findByEmail(@Param("email")String email);

    @Query("select u from Usuario u " +
            "join u.perfis p " +
            "where u.email like :search% or " +
            "p.desc like :search%")
    Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);
}
