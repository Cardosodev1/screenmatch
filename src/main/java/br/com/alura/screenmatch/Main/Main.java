package br.com.alura.screenmatch.Main;

import br.com.alura.screenmatch.model.DataSeason;
import br.com.alura.screenmatch.model.DataSerie;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.ConvertsData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ApiConsumption apiConsumption = new ApiConsumption();
    private ConvertsData converter = new ConvertsData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DataSerie> dataSeries = new ArrayList<>();

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
        dataSeries.add(data);
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
        DataSerie dataSerie = getDataSerie();
        List<DataSeason> seasons = new ArrayList<>();

        for (int i = 1; i <= dataSerie.totalSeasons(); i++) {
            var json = apiConsumption.getData(ADDRESS + dataSerie.title().replace(" ", "+") + "&season=" + i + API_KEY);
            DataSeason dataSeason = converter.getData(json, DataSeason.class);
            seasons.add(dataSeason);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchedSeries() {
        List<Serie> series = new ArrayList<>();
        series = dataSeries.stream()
                  .map(d -> new Serie(d))
                  .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
