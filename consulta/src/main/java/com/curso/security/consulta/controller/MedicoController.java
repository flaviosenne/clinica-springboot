package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Medico;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("medicos")
public class MedicoController {
    @GetMapping({"/dados"})
    public String abrirPorMedico(Medico medico, ModelMap model){
        return "medico/cadastro";
    }
}
