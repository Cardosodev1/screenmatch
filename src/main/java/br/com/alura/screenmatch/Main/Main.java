package br.com.alura.screenmatch.Main;

import br.com.alura.screenmatch.model.DataSeason;
import br.com.alura.screenmatch.model.DataSerie;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.ConvertsData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ApiConsumption apiConsumption = new ApiConsumption();
    private ConvertsData converter = new ConvertsData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DataSerie> dataSeries = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Main(SerieRepository repository) {
        this.repository = repository;
    }

    public void displayMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                1. Buscar séries
                2. Buscar episódios
                3. Listar séries buscadas
                
                0. Sair
                """;
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1)
                searchWebSerie();
            else if (option == 2)
                searchEpisodeBySerie();
            else if (option == 3)
                listSearchedSeries();
            else if (option == 0)
                System.out.println("Saindo...");
            else {
                System.out.println("Opção inválida");
            }
        }
    }


    private void searchWebSerie() {
        DataSerie data = getDataSerie();
        Serie serie = new Serie(data);
        //dataSeries.add(data);
        repository.save(serie);
        System.out.println(data);
    }

    private DataSerie getDataSerie() {
        System.out.println("Digite o nome da série para busca");
        var serieName = scanner.nextLine();
        var json = apiConsumption.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);
        DataSerie data = converter.getData(json, DataSerie.class);
        return data;
    }

    private void searchEpisodeBySerie() {
        listSearchedSeries();
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = scanner.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(serieName.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var foundSerie = serie.get();
            List<DataSeason> seasons = new ArrayList<>();

            for (int i = 1; i <= foundSerie.getTotalSeasons(); i++) {
                var json = apiConsumption.getData(ADDRESS + foundSerie.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                DataSeason dataSeason = converter.getData(json, DataSeason.class);
                seasons.add(dataSeason);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.datumEpisodes().stream()
                            .map(e -> new Episode(d.number(), e)))
                    .collect(Collectors.toList());
            foundSerie.setEpisodes(episodes);
            repository.save(foundSerie);
        } else {
            System.out.println("Série não encontrada!");
        }
    }


    private void listSearchedSeries() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
