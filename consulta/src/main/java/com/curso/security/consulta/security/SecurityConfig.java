package com.curso.security.consulta.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/webjars/**","/css/**","/image/**","/js/**").permitAll()
                .antMatchers("/", "/home").permitAll()

                .anyRequest().authenticated()
        .and()
            .formLogin()
            .loginPage("/login") // qual o recurso que vai ser controlado
            .defaultSuccessUrl("/", true) //caso de certo quala apgina que será redirecionada
            .failureUrl("/login-error") // caso dê errado, será direcionado para esse recurso
            .permitAll()
        .and()
            .logout()
            .logoutSuccessUrl("/");
    }
}
