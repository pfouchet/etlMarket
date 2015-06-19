package com.groupeseb.etlmarket;

import com.groupeseb.etlmarket.service.CommonApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class EtlMarket implements CommandLineRunner {

	@Autowired
	private CommonApiService commonApiService;


	public static void main(String[] args) {
		SpringApplication.run(EtlMarket.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		commonApiService.createVariants();
	}
}
