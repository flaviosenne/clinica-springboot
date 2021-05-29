package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Agendamento;
import com.curso.security.consulta.domain.Paciente;
import com.curso.security.consulta.domain.PerfilTipo;
import com.curso.security.consulta.domain.Especialidade;
import com.curso.security.consulta.service.AgendamentoService;
import com.curso.security.consulta.service.PacienteService;
import com.curso.security.consulta.service.EspecialidadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EspecialidadesService especialidadeService;


    @GetMapping({"/agendar"})
    public String agendarConsulta(Agendamento agendamento){
        return "agendamento/cadastro";
    }

    @GetMapping("/horario/medico/{id}/data/{data}")
    public ResponseEntity<?> getHorarios(@PathVariable("id")Long id,
                                         @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data ){

        return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
    }

    @PostMapping({"/salvar"})
    public String salvar(Agendamento agendamento, RedirectAttributes redirect, @AuthenticationPrincipal UserDetails user){
        Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
        String titulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService
                .buscarPortTitulo(new String[] {titulo})
                .stream().findFirst().get();

        agendamento.setEspecialidade(especialidade);
        agendamento.setPaciente(paciente);
        agendamentoService.salvar(agendamento);

        redirect.addFlashAttribute("sucesso", "sua consilta foi agendada com sucesso");

        return "redirect:/agendamentos/agendar";
    }

    @GetMapping({"/historico/paciente","historico/consultas"})
    public String historico(){

        return "agendamento/historico-paciente";
    }

    @GetMapping("/datatables/server/historico")
    public ResponseEntity<?> historicoAgendamentoPorPaciente(HttpServletRequest request, @AuthenticationPrincipal UserDetails user){

        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))){
            return ResponseEntity.ok(agendamentoService.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
        }
        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))){
            return ResponseEntity.ok(agendamentoService.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
        }
        return ResponseEntity.notFound().build();
    }
}
