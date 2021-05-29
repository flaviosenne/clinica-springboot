package com.curso.security.consulta.repository.projection;

import com.curso.security.consulta.domain.Paciente;
import com.curso.security.consulta.domain.Medico;
import com.curso.security.consulta.domain.Especialidade;

public interface HistoricoPaciente {
    Long getId();
    Paciente getPaciente();
    String getDataConsulta();
    Medico getMedico();
    Especialidade getEspecialidade();

}
