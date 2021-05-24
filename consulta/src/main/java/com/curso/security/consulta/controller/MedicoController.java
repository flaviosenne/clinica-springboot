package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Medico;
import com.curso.security.consulta.domain.Usuario;
import com.curso.security.consulta.service.MedicoService;
import com.curso.security.consulta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/dados"})
    public String abrirPorMedico(Medico medico, ModelMap
            model, @AuthenticationPrincipal User user){
        if(medico.hasNotId()){
            medico = medicoService.buscarPorEmail(user.getUsername());
            model.addAttribute("medico", medico);
        }
        return "medico/cadastro";
    }

    @PostMapping({"/salvar"})
    public String salvar(Medico medico, RedirectAttributes redirect,
                         @AuthenticationPrincipal User user
                        ){

        if(medico.hasNotId() && medico.getUsuario().hasNotId()){
            Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
            medico.setUsuario(usuario);
        }


        medicoService.salvar(medico);
        redirect.addFlashAttribute("sucesso", "Operação realizada com sucesso");
        redirect.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    @PostMapping({"/editar"})
    public String editar(Medico medico, RedirectAttributes redirect){
        medicoService.editar(medico);

        redirect.addFlashAttribute("sucesso", "Operação realizada com sucesso");
        redirect.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    @GetMapping({"/id/{idMed}/excluir/especializacao/{idEsp}"})
    public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed,
                         @PathVariable("idEsp") Long idEsp,
                         RedirectAttributes redirect){
        medicoService.excluirEspecialidadePorMedico(idMed, idEsp);

        redirect.addFlashAttribute("sucesso", "Especialidade Removida com sucesso");
        return "redirect:/medicos/dados";
    }
}
