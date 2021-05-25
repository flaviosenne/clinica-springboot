package com.curso.security.consulta.service;

import com.curso.security.consulta.domain.Horario;
import com.curso.security.consulta.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Transactional
    public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
        return agendamentoRepository.findByMedicoIdAndDataNotHorarioAgendado(id, data);
    }
}
