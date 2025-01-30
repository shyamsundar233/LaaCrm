package com.laacrm.main;

import com.laacrm.main.core.service.TemplateDataPopulate;
import com.laacrm.main.framework.service.initPopulate.InitialPopulationService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@AllArgsConstructor
@PropertySource("classpath:feature-limit.properties")
public class LaaCrmApplication {

	private final InitialPopulationService initPopulationService;
	private final TemplateDataPopulate templateDataPopulate;

	@PostConstruct
	public void init() throws Exception {
		initPopulationService.initPopulate();
		templateDataPopulate.populateFieldPropertiesRef();
		templateDataPopulate.populateRecordsTable();
	}

	public static void main(String[] args) {
		SpringApplication.run(LaaCrmApplication.class, args);
	}

}
