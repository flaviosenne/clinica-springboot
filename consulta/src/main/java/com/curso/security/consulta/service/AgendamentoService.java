package com.curso.security.consulta.service;

import com.curso.security.consulta.domain.Agendamento;
import com.curso.security.consulta.domain.Horario;
import com.curso.security.consulta.datatables.Datatables;
import com.curso.security.consulta.datatables.DatatablesColunas;
import com.curso.security.consulta.repository.AgendamentoRepository;
import com.curso.security.consulta.repository.projection.HistoricoPaciente;
import com.curso.security.consulta.security.exception.AccessDenidedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private Datatables datatables;

    @Transactional
    public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
        return agendamentoRepository.findByMedicoIdAndDataNotHorarioAgendado(id, data);
    }
    @Transactional
    public void salvar(Agendamento agendamento){
        agendamentoRepository.save(agendamento);
    }
    @Transactional
    public Map<String, Object> buscarHistoricoPorMedicoEmail(String email, HttpServletRequest request){
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
        Page<HistoricoPaciente> page = agendamentoRepository.findHistoricoByMedicoEmail(email, datatables.getPageable());
        return datatables.getResponse(page);
    }
    @Transactional
    public Map<String, Object> buscarHistoricoPorPacienteEmail(String email, HttpServletRequest request){
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
        Page<HistoricoPaciente> page = agendamentoRepository.findHistoricoByPacienteEmail(email, datatables.getPageable());
        return datatables.getResponse(page);
    }
    @Transactional
    public Agendamento buscarPorId(Long id) {
         return agendamentoRepository.findById(id).get();
    }
    @Transactional
    public void editar(Agendamento agendamento, String email) {
        Agendamento age = buscarPorIdEUsuario(agendamento.getId(), email);
        age.setDataConsulta(agendamento.getDataConsulta());
        age.setEspecialidade(agendamento.getEspecialidade());
        age.setHorario(agendamento.getHorario());
        age.setMedico(agendamento.getMedico());
    }

    @Transactional
    public Agendamento buscarPorIdEUsuario(Long id, String email) {
        return agendamentoRepository.findByIdAndPacientOrMedicoEmail(id, email)
                .orElseThrow(() -> new AccessDenidedException("Acesso negado ao usuario: "+ email));
    }
}
