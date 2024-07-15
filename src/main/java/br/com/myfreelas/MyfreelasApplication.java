package br.com.myfreelas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Myfreelas API", version = "1.0.0", 
		description = "API desenvolvida para gerenciamento da rede de freelancers Myfreelas, a api faz registro de usuarios e projetos, com suas habilidades e dependencias que sao gerenciaveis pelos usuarios"))
@SpringBootApplication
public class MyfreelasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyfreelasApplication.class, args);
	}

}
