package com.elotech.biblioteca_arom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaAromApplication {


	private static final ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(BibliotecaAromApplication.class);


	public static void main(String[] args) {
		LOGGER.info("Iniciando Api da Biblioteca ! ");

		SpringApplication.run(BibliotecaAromApplication.class, args);
		LOGGER.info("Api da Biblioteca iniciado com sucesso! ");

	}

}
