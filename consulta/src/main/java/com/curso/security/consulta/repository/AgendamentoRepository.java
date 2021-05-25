package com.curso.security.consulta.repository;

import com.curso.security.consulta.domain.Agendamento;
import com.curso.security.consulta.domain.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

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
}
