package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Perfil;
import com.curso.security.consulta.domain.Usuario;
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
}
