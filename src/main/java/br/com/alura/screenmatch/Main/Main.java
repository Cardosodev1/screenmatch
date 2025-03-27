package br.com.alura.screenmatch.Main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
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

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> searchSerie;

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
                4. Buscar série por título
                5. Buscar séries por ator
                6. Top 5 Séries
                7. Buscar séries por categoria
                8. Buscar séries por temporadas e avaliação
                9. Buscar episódios por trecho
                10. Top 5 Episódios por série
                11. Buscar episódio a partir de uma data
                
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
            else if (option == 4)
                searchSerieByTitle();
            else if (option == 5)
                searchSerieByActor();
            else if (option == 6)
                searchTop5Series();
            else if (option == 7)
                searchSeriesByCategory();
            else if (option == 8)
                searchSeriesBySeasonAndRating();
            else if (option == 9)
                searchEpisodeByExcerpt();
            else if (option == 10)
                topEpisodesBySerie();
            else if (option == 11)
                searchEpisodeAfterDate();
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

        Optional<Serie> serie = repository.findByTitleContainingIgnoreCase(serieName);

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

    private void searchSerieByTitle() {
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = scanner.nextLine();
        searchSerie = repository.findByTitleContainingIgnoreCase(serieName);

        if (searchSerie.isPresent()) {
            System.out.println("Dados da série: " + searchSerie.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void searchSerieByActor() {
        System.out.println("Qual o nome que você busca:");
        var actorName = scanner.nextLine();
        System.out.println("Avaliações a partir de qual nota: ");
        var rating = scanner.nextDouble();

        List<Serie> foundSeries = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Séries em que " + actorName + " trabalhou: ");
        foundSeries.forEach(s -> System.out.println(s.getTitle() + " - avaliação: " + s.getRating()));
    }

    private void searchTop5Series() {
        List<Serie> serieTop = repository.findTop5ByOrderByRatingDesc();
        serieTop.forEach(s -> System.out.println(s.getTitle() + " - avaliação: " + s.getRating()));
    }

    private void searchSeriesByCategory() {
        System.out.println("Deseja buscar séries de que categoria/gênero: ");
        var genreName = scanner.nextLine();

        ECategory category = ECategory.fromPortuguese(genreName);
        List<Serie> seriesByECategory = repository.findByGenre(category);
        System.out.println("Séries da categoria: " + genreName);
        seriesByECategory.forEach(System.out::println);
    }

    private void searchSeriesBySeasonAndRating() {
        System.out.println("Qual o número maximo de temporadas que você deseja ver em uma série: ");
        var totalSeasons = scanner.nextInt();
        System.out.println("Avaliações a partir de qual nota: ");
        var rating = scanner.nextDouble();

        List<Serie> seriesByTotalSeasonsAndRating = repository.seriesBySeasonAndRating(totalSeasons, rating);
        System.out.println("Séries com até " + totalSeasons + " temporadas e avaliação maior ou igual a " + rating);
        seriesByTotalSeasonsAndRating.forEach(System.out::println);
    }

    private void searchEpisodeByExcerpt() {
        System.out.println("Qual o nome do episódio para busca:");
        var excerptEpisode = scanner.nextLine();

        List<Episode> foundEpisodes = repository.episodeByExcerpt(excerptEpisode);
        foundEpisodes.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitle(), e.getSeason(),
                        e.getEpisodeNumber(), e.getTitle()));
    }

    private void topEpisodesBySerie() {
        searchSerieByTitle();
        if (searchSerie.isPresent()) {
            Serie serie = searchSerie.get();
            List<Episode> topEpisodes = repository.topEpisodesBySerie(serie);
            topEpisodes.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitle(), e.getSeason(),
                            e.getEpisodeNumber(), e.getTitle(), e.getRating()));
        }
    }

    private void searchEpisodeAfterDate() {
        searchSerieByTitle();
        if (searchSerie.isPresent()) {
            Serie serie = searchSerie.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var yearRelease = scanner.nextInt();
            scanner.nextLine();

            List<Episode> episodesYear = repository.episodesBySerieAndYear(serie, yearRelease);
            episodesYear.forEach(System.out::println);
        }
    }
}
