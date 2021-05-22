package com.curso.security.consulta.service;

import com.curso.security.consulta.domain.Perfil;
import com.curso.security.consulta.domain.Usuario;
import com.curso.security.consulta.repository.UsuariorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuariorRepositorio usuariorRepositorio;

    @Transactional
    public Usuario buscarPorEmail(String email){
        return usuariorRepositorio.findByEmail(email);
    }

    @Override @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Usuario usuario = buscarPorEmail(userName);
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
}
