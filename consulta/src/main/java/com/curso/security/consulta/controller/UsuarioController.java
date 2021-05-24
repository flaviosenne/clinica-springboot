package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Medico;
import com.curso.security.consulta.domain.Perfil;
import com.curso.security.consulta.domain.PerfilTipo;
import com.curso.security.consulta.domain.Usuario;
import com.curso.security.consulta.service.MedicoService;
import com.curso.security.consulta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("u")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/novo/cadastro/usuario")
    public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario){
        return "usuario/cadastro";
    }

    @GetMapping("/lista")
    public String listarUsuarios(){
        return "usuario/lista";
    }

    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request){

        return ResponseEntity.ok(usuarioService.buscarTodos(request));
    }

    // asalvar por administrador
    @PostMapping("/cadastro/salvar")
    public String salvarUsuarios(Usuario usuario, RedirectAttributes redirect){
        List<Perfil> perfis = usuario.getPerfis();

        if(perfis.size() > 2 ||
        perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L ))) ||
        perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L )))){
            redirect.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou médico");
            redirect.addFlashAttribute("usuario", usuario);
        }
        else{
            try{
                usuarioService.salvarUsuario(usuario);
                redirect.addFlashAttribute("sucesso", "Operação realizada co sucesso");
            }catch (DataIntegrityViolationException exception){
                redirect.addFlashAttribute("falha", "Cadastro não realizado, Email já existente");
            }

        }
        return "redirect:/u/novo/cadastro/usuario";
    }

    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView preEditarCredenciais(@PathVariable("id") Long id){

        return new ModelAndView("usuario/cadastro", "usuario", usuarioService.buscarPorId(id));
    }

    @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView preEditarCredenciais(@PathVariable("id") Long usuarioId,
                                             @PathVariable("perfis") Long[] perfisId){

        Usuario usuario = usuarioService.buscarPorIdEPerfis(usuarioId, perfisId);

        if(usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
                !usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

            return new ModelAndView("usuario/cadastro", "usuario",usuario);

        }else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){

            Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
            return medico.hasNotId()?
                    new ModelAndView("medico/cadastro", "medico",
                            new Medico(new Usuario(usuarioId)))
                    : new ModelAndView("medico/cadastro", "medico", medico);

        }else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))){
            ModelAndView model = new ModelAndView("error");
            model.addObject("status", 403);
            model.addObject("error", "Area restrita");
            model.addObject("message", "Os dados de paciente são restrito a ele");
            return model;
        }

        return new ModelAndView("redirect:/u/lista");
    }
}
