package com.deliver.backend;

import org.springframework.boot.SpringApplication;

public class TestDeliverBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(DeliverBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
