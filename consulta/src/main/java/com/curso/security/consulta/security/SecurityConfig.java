package com.curso.security.consulta.security;

import com.curso.security.consulta.domain.PerfilTipo;
import com.curso.security.consulta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
    private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
    private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/webjars/**","/css/**","/image/**","/js/**").permitAll()
                .antMatchers("/", "/home").permitAll()

                // accessos privado admin
                .antMatchers("/u/**").hasAuthority(ADMIN) // essa autoridade tem que ser conforme está no banco

                // accessos privado medicos
                .antMatchers("/medicos/dados", "/medicos/salvar","/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
                .antMatchers("/medicos/**").hasAuthority(MEDICO)

                // accessos privado paciente
                .antMatchers("/pacientes/**").hasAuthority(PACIENTE)

                .antMatchers("/especialidades/**").hasAuthority(MEDICO)
                .anyRequest().authenticated()
        .and()
            .formLogin()
            .loginPage("/login") // qual o recurso que vai ser controlado
            .defaultSuccessUrl("/", true) //caso de certo quala apgina que será redirecionada
            .failureUrl("/login-error") // caso dê errado, será direcionado para esse recurso
            .permitAll()
        .and()
            .logout()
            .logoutSuccessUrl("/")
        .and()
        .exceptionHandling()
        .accessDeniedPage("/acesso-negado")
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
