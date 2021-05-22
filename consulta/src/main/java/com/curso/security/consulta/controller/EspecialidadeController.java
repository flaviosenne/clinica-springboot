package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Especialidade;
import com.curso.security.consulta.service.EspecialidadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {
    @Autowired
    private EspecialidadesService especialidadesService;

    @GetMapping({"","/"})
    public String abrir(Especialidade especialidade){
        return "especialidade/especialidade";
    }

    @PostMapping("/salvar")
    public String salvar(Especialidade especialidade, RedirectAttributes redirect){
        especialidadesService.salvar(especialidade);
        redirect.addFlashAttribute("sucesso","Operação realizada com sucesso");
        return "redirect:/especialidades";
    }

    @GetMapping("/datatables/server")
    public ResponseEntity<?> getEspecialidades(HttpServletRequest request){
        return ResponseEntity.ok(especialidadesService.buscarEspecialidades(request));
    }
}
