package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.ConvertsData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var apiConsumption = new ApiConsumption();
		var json = apiConsumption.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		ConvertsData converter = new ConvertsData();
		Serie serie = converter.getData(json, Serie.class);
		System.out.println(serie);
	}
}
