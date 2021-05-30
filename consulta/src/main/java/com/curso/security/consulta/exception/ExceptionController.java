package com.curso.security.consulta.exception;

import com.curso.security.consulta.security.exception.AccessDenidedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException exception){
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 404);
        model.addObject("error", "Opração Não pode ser realizada");
        model.addObject("message", exception.getMessage());
        return model;
    }

    @ExceptionHandler(AccessDenidedException.class)
    public ModelAndView accessDeniededException(AccessDenidedException exception){
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 403);
        model.addObject("error", "Opração Não pode ser realizada");
        model.addObject("message", exception.getMessage());
        return model;
    }
}
