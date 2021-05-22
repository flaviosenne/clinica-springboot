package com.curso.security.consulta.service;

import com.curso.security.consulta.domain.Especialidade;
import com.curso.security.consulta.repository.EspecialidadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EspecialidadesService {
    @Autowired
    private EspecialidadesRepository especialidadesRepository;

    @Transactional
    public void salvar(Especialidade especialidade){
        especialidadesRepository.save(especialidade);
    }
}
