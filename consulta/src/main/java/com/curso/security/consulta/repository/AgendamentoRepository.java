package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Agendamento;
import com.curso.security.consulta.domain.Horario;
import com.curso.security.consulta.repository.projection.HistoricoPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("select h " +
            "from Horario h " +
            "where not exists(" +
                " select a.horario.id " +
                " from Agendamento a " +
                " where " +
                    " a.medico.id = :id " +
                    " and a.dataConsulta = :data " +
                    " and a.horario.id = h.id" +
            ") " +
            " order by h.horaMinuto asc")
    List<Horario> findByMedicoIdAndDataNotHorarioAgendado(Long id, LocalDate data);

    @Query("select a.id as id, " +
            "a.paciente as paciente, " +
            "concat(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta, " +
            "a.medico as medico, " +
            "a.especialidade as especialidade " +
            "from Agendamento a " +
            "where  a.paciente.usuario.email like :email")
    Page<HistoricoPaciente> findHistoricoByPacienteEmail(String email, Pageable pageable);

    @Query("select a.id as id, " +
            "a.paciente as paciente, " +
            "concat(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta, " +
            "a.medico as medico, " +
            "a.especialidade as especialidade " +
            "from Agendamento a " +
            "where  a.medico.usuario.email like :email")
    Page<HistoricoPaciente> findHistoricoByMedicoEmail(String email, Pageable pageable);

    @Query("select a from Agendamento a " +
            "where " +
            "(a.id = :id and a.paciente.usuario.email like :email) " +
            "or " +
            "(a.id = :id and a.medico.usuario.email like :email)")
    Optional<Agendamento> findByIdAndPacientOrMedicoEmail(Long id, String email);
}
