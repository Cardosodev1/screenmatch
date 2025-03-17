package br.com.alura.screenmatch.Main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.Season;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ApiConsumption;
import br.com.alura.screenmatch.service.ConvertsData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ApiConsumption apiConsumption = new ApiConsumption();
    private ConvertsData converter = new ConvertsData();
    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void displayMenu() {
        System.out.println("Digite o nome da série para buscar");
        var serieName = scanner.nextLine();
        var json = apiConsumption.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);
        Serie serie = converter.getData(json, Serie.class);
        System.out.println(serie);

        List<Season> seasons = new ArrayList<>();

        for (int i = 1; i <= serie.totalSeasons(); i++) {
            json = apiConsumption.getData(ADDRESS + serieName.replace(" ", "+") + "&season=" + i + API_KEY);
            Season season = converter.getData(json, Season.class);
            seasons.add(season);
        }
        seasons.forEach(System.out::println);

//        for (int i = 0; i < serie.totalSeasons(); i++) {
//            List<Episode> episodes = seasons.get(i).episodes();
//            for (int j = 0; j < episodes.size(); j++) {
//                System.out.println(episodes.get(j).title());
//            }
//        }
        seasons.forEach(s -> s.episodeData().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodeData = seasons.stream()
                .flatMap(s -> s.episodeData().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 5 episodios: ");
//        episodeData.stream()
//                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.title().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodeData().stream()
                        .map(d -> new Episode(s.number(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

//        System.out.println("Digite um trecho do título do episódio");
//        var excerptTitle = scanner.nextLine();
//        Optional<Episode> episodeSearched = episodes.stream()
//                .filter(e -> e.getTitle().toUpperCase().contains(excerptTitle.toUpperCase()))
//                .findFirst();
//        if (episodeSearched.isPresent()) {
//            System.out.println("Episódio encontrado!");
//            System.out.println("Temporada: " + episodeSearched.get().getSeason());
//        } else {
//            System.out.println("Episódio não encontrado!");
//        }
//
//        System.out.println("A partir de qual ano você deseja ver os episódios? ");
//        var year = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate searchDate = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getReleaseData() != null && e.getReleaseData().isAfter(searchDate))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getSeason() +
//                        " Episódio: " + e.getTitle() +
//                        " Data lançamento: " + e.getReleaseData().format(formatter)
//                ));

        Map<Integer, Double> ratingPerSeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));
        System.out.println(ratingPerSeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}
