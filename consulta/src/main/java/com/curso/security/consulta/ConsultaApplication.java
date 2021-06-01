package com.curso.security.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ConsultaApplication implements CommandLineRunner {

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("12345"));
		SpringApplication.run(ConsultaApplication.class, args);
	}

	@Autowired
	JavaMailSender sender;

	@Override
	public void run(String... args) throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("joaodev3@gmail.com");
		message.setText("test 1");
		message.setSubject("test 1");

		sender.send(message);
	}
}
