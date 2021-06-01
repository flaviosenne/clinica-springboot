package com.curso.security.consulta.service;

import com.curso.security.consulta.datatables.Datatables;
import com.curso.security.consulta.datatables.DatatablesColunas;
import com.curso.security.consulta.domain.Perfil;
import com.curso.security.consulta.domain.PerfilTipo;
import com.curso.security.consulta.domain.Usuario;
import com.curso.security.consulta.repository.UsuariorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuariorRepositorio usuariorRepositorio;

    @Autowired
    private Datatables datatables;

    @Transactional
    public Usuario buscarPorEmail(String email){
        return usuariorRepositorio.findByEmail(email);
    }

    @Override @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Usuario usuario = buscarPorEmailEAtivo(userName)
                .orElseThrow(() -> new UsernameNotFoundException("usuario "+ userName+ " não encontrado"));
        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                AuthorityUtils.createAuthorityList(
                        getAuthorities(usuario.getPerfis()))
        );
    }

    private String[] getAuthorities(List<Perfil> perfilList){
        String[] authorities = new String[perfilList.size()];
        for(int i = 0; i< perfilList.size(); i++){
            authorities[i] = perfilList.get(i).getDesc();
        }
        return  authorities;
    }

    @Transactional
    public Map<String, Object> buscarTodos(HttpServletRequest request) {
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.USUARIOS);
        Page<Usuario> page = datatables.getSearch().isEmpty()
                ? usuariorRepositorio.findAll(datatables.getPageable())
                :usuariorRepositorio.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());

        return datatables.getResponse(page);
    }

    @Transactional
    public void salvarUsuario(Usuario usuario) {
        String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(crypt);
        usuariorRepositorio.save(usuario);
    }

    @Transactional
    public Usuario buscarPorId(Long id) {
        return usuariorRepositorio.findById(id).get();
    }

    @Transactional
    public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
        return usuariorRepositorio.findByIdAndPerfis(usuarioId, perfisId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário nãp existente"));
    }

    public static boolean isSenhaCorreta(String senhaDigitada, String senhaDB) {
        return  new BCryptPasswordEncoder().matches(senhaDigitada, senhaDB);
    }

    @Transactional
    public void alterarSenha(Usuario usuario, String novaSenha) {
        usuario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
        usuariorRepositorio.save(usuario);
    }

    public void salvarCadastroPaciente(Usuario usuario) {
        String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(crypt);
        usuario.addPerfil(PerfilTipo.PACIENTE);
        usuariorRepositorio.save(usuario);
    }

    public Optional<Usuario> buscarPorEmailEAtivo(String email){
        return usuariorRepositorio.findByEmailAndAtivo();
    }
}
