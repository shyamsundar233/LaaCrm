package com.laacrm.main;

import com.laacrm.main.framework.service.initPopulate.InitialPopulationService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class LaaCrmApplication {

	private final InitialPopulationService initPopulationService;

	@PostConstruct
	public void init() {
		initPopulationService.initPopulate();
	}

	public static void main(String[] args) {
		SpringApplication.run(LaaCrmApplication.class, args);
	}

}
