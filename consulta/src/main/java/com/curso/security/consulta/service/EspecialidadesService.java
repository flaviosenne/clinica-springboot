package com.curso.security.consulta.service;

import com.curso.security.consulta.datatables.Datatables;
import com.curso.security.consulta.datatables.DatatablesColunas;
import com.curso.security.consulta.domain.Especialidade;
import com.curso.security.consulta.repository.EspecialidadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;

@Service
public class EspecialidadesService {
    @Autowired
    private EspecialidadesRepository especialidadesRepository;
    @Autowired
    private Datatables datatables;

    @Transactional
    public void salvar(Especialidade especialidade){
        especialidadesRepository.save(especialidade);
    }
    @Transactional
    public Map<String, Object> buscarEspecialidades(HttpServletRequest request){
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
        Page<?> page =  datatables.getSearch().isEmpty()
                ? especialidadesRepository.findAll(datatables.getPageable())
        : especialidadesRepository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());
                return datatables.getResponse(page);
    }
}
