package com.naveen.learning;

import com.naveen.learning.model.User;
import com.naveen.learning.model.audit.SecurityAuditorAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLearningApplication.class, args);
	}

}
