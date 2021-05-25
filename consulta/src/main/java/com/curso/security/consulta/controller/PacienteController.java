package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Paciente;
import com.curso.security.consulta.domain.Usuario;
import com.curso.security.consulta.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/dados")
    public String cadastro(Paciente paciente, ModelMap model, @AuthenticationPrincipal UserDetails user){
        paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());

        if(paciente.hasNotId()){
         paciente.setUsuario(new Usuario(user.getUsername()));
        }
        model.addAttribute("paciente", paciente);
        return "paciente/cadastro";
    }
}
