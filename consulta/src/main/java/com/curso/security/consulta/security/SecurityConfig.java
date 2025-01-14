package com.curso.security.consulta.security;

import com.curso.security.consulta.domain.PerfilTipo;
import com.curso.security.consulta.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableGlobalMethodSecurity(prePostEnabled = true)
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
            .antMatchers("/", "/home", "/expired").permitAll()
                .antMatchers(
                        "/u/novo/cadastro",
                        "/u/cadastro/realizado",
                        "/u/cadastro/paciente/salvar",
                        "/u/confirmacao/cadastro",
                        "/u/p/**").permitAll()

                // accessos privado admin
                .antMatchers("/u/editar/senha","/u/confirmar/senha").hasAnyAuthority(PACIENTE,MEDICO) // essa autoridade tem que ser conforme está no banco
                .antMatchers("/u/**").hasAuthority(ADMIN) // essa autoridade tem que ser conforme está no banco

                // accessos privado medicos
                .antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(MEDICO, PACIENTE)
                .antMatchers("/medicos/dados", "/medicos/salvar","/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
                .antMatchers("/medicos/**").hasAuthority(MEDICO)

                // accessos privado paciente
                .antMatchers("/pacientes/**").hasAuthority(PACIENTE)


                // accessos privado especialidade
                .antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
                .antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
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
        .and()
            .rememberMe();

        http.sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/expired")
//                .maxSessionsPreventsLogin(true) // quando estiver logado em um dispositivo, não logará no outro
                .maxSessionsPreventsLogin(false) // quando estiver logado em um dispositivo, caso ocorra outro o login, a sessão anterioir será expirada
                .sessionRegistry(sessionRegister());

        http.sessionManagement()
                .sessionFixation().newSession()
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy());
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(){
        return new RegisterSessionAuthenticationStrategy(sessionRegister());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SessionRegistry sessionRegister(){
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<?> servletListenerRegistrationBean(){
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}
