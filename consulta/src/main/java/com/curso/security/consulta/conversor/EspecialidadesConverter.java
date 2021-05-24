package com.curso.security.consulta.conversor;

import com.curso.security.consulta.domain.Especialidade;
import com.curso.security.consulta.domain.Perfil;
import com.curso.security.consulta.service.EspecialidadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> {
    @Autowired
    private EspecialidadesService especialidadesService;

    @Override
    public Set<Especialidade> convert(String[] titulo) {

        Set<Especialidade> especialidades = new HashSet<>();

        if(titulo != null && titulo.length > 0){
            especialidades.addAll(especialidadesService.buscarPortTitulo(titulo));
        }

        return especialidades;
    }
}
