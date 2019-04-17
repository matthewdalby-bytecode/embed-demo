package com.bytecodeio.demo.embed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.bytecodeio"})
public class EmbedDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmbedDemoApplication.class, args);
	}

}
