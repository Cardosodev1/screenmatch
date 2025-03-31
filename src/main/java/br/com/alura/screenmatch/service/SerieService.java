package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.ECategory;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    private List<SerieDTO> convertsData(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSeries() {
        return convertsData(repository.findAll());
    }

    public List<SerieDTO> getTop5Series() {
        return convertsData(repository.findTop5ByOrderByRatingDesc());
    }

    public List<SerieDTO> getReleases() {
        return convertsData(repository.latestReleases());
    }

    public SerieDTO getById(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDTO> getSeasonsByNumber(Long id, Long number) {
        return repository.getEpisodesBySeason(id, number)
                .stream()
                .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeNumber(), e.getTitle()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSeriesByCategory(String nameGenre) {
        ECategory category = ECategory.fromPortuguese(nameGenre);
        return convertsData(repository.findByGenre(category));
    }
}
