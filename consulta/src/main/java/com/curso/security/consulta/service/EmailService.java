package com.curso.security.consulta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TemplateEngine template;

    public void enviarPedidoDeconfirmacaoDeCadastro(String destino, String codigo) throws MessagingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            Context context = new Context();
            context.setVariable("titulo", "Bem vindo a clinica Spring Security");
            context.setVariable("texto","Precisamos que confirme seu cadastro, clicando no link abaixo");
            context.setVariable("linkConfirmacao", "http://localhost:8081/u/confirmacao/cadastro?codigo="+codigo);
            context.setVariable("verificador", "");

            String html = template.process("emailConfirmacao", context);
            helper.setTo(destino);
            helper.setText(html, true);
            helper.setSubject("Confirmação de cadastro");
            helper.setFrom("não-responder@clinica.com.br");

            helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

            mailSender.send(message);
        } catch (Exception e) {
            throw new InternalError("falha no envio de email");
        }
    }

    public void enviarPedidoRedefinicaoSenha(String destino, String verificador) throws MessagingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            Context context = new Context();
            context.setVariable("titulo", "Redefinição de Senha");
            context.setVariable("texto","Para definição de senha, use o " +
                    "código de verificação quando exigido no formulário");
            context.setVariable("verificador", verificador);

            String html = template.process("emailConfirmacao", context);
            helper.setTo(destino);
            helper.setText(html, true);
            helper.setSubject("Redefinição de senhao");
            helper.setFrom("não-responder@clinica.com.br");


            mailSender.send(message);
        } catch (Exception e) {
            throw new InternalError("falha no envio de email");
        }
    }

}
