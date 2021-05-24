package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Especialidade;
import com.curso.security.consulta.service.EspecialidadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, ModelMap model){
        model.addAttribute("especialidade",especialidadesService.buscaporId(id));
        return "especialidade/especialidade";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect){
        especialidadesService.remover(id);
        redirect.addFlashAttribute("sucesso","Operação realizada com sucesso");
        return "redirect:/especialidades";
    }

    @GetMapping("/titulo")
    public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam("termo") String termo){
        List<String> especialidades = especialidadesService.buscarEspecialidadeByTermo(termo);
        return ResponseEntity.ok(especialidades);
    }

    @GetMapping("/datatables/server/medico/{id}")
    public ResponseEntity<?> getEspecialidadesPorMedico(@PathVariable("id") Long id,
                                                        HttpServletRequest request){

        return ResponseEntity.ok(especialidadesService.buscarEspecialidadePorMedico(id, request));
    }
}
