package com.curso.security.consulta.controller;

import com.curso.security.consulta.domain.Perfil;
import com.curso.security.consulta.domain.Usuario;
import com.curso.security.consulta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        perfis.contains(Arrays.asList(new Perfil(1L), new Perfil(3L ))) ||
        perfis.contains(Arrays.asList(new Perfil(2L), new Perfil(3L )))){
            redirect.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou médico");
            redirect.addFlashAttribute("usuario", usuario);
        }
        else{
            usuarioService.salvarUsuario(usuario);
            redirect.addFlashAttribute("sucesso", "Operação realizada co sucesso");
        }
        return "redirect:/u/novo/cadastro/usuario";
    }
}
