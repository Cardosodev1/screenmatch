package br.com.alura.screenmatch;

import br.com.alura.screenmatch.Main.Main;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class ScreenmatchApplicationWithoutWeb implements CommandLineRunner {
//
//	@Autowired
//	private SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenmatchApplicationWithoutWeb.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		Main main = new Main(repository);
//		main.displayMenu();
//	}
//}