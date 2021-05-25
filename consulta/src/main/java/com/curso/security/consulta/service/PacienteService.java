package com.curso.security.consulta.service;

import com.curso.security.consulta.domain.Paciente;
import com.curso.security.consulta.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public Paciente buscarPorUsuarioEmail(String email){
        return  pacienteRepository.findByUsuarioEmail(email).orElse(new Paciente());
    }
}
