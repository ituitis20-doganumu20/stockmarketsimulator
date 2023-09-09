package com.example.stockmarket;

import com.example.stockmarket.services.StockMarketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.example.stockmarket.entities")
public class StockMarketApplication {

	public static void main(String[] args) {

		//SpringApplication.run(StockMarketApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(StockMarketApplication.class, args);

		// Retrieve the StockMarketService bean
		//StockMarketService stockMarketService = context.getBean(StockMarketService.class);

		// Call the simulateStockMarket function
		//stockMarketService.simulateStockMarket();

		// Close the application context
		//context.close();
	}

}
