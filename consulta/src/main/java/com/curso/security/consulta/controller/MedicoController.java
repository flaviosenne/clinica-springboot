package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Medico;
import com.curso.security.consulta.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @GetMapping({"/dados"})
    public String abrirPorMedico(Medico medico, ModelMap model){
        return "medico/cadastro";
    }

    @GetMapping({"/salvar"})
    public String salvar(Medico medico, RedirectAttributes redirect){
        medicoService.salvar(medico);
        redirect.addFlashAttribute("sucesso", "Operação realizada com sucesso");
        redirect.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    @GetMapping({"/editar"})
    public String editar(Medico medico, RedirectAttributes redirect){
        medicoService.editar(medico);

        redirect.addFlashAttribute("sucesso", "Operação realizada com sucesso");
        redirect.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }
}
