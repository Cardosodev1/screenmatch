package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> getSeries() {
        return service.getSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> getTop5Series() {
        return service.getTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> getLancamentos() {
        return service.getReleases();
    }

    @GetMapping("/{id}")
    public SerieDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodeDTO> getAllSeasons(@PathVariable Long id) {
        return service.getAllSeasons(id);
    }

    @GetMapping("/{id}/temporadas/{number}")
    public List<EpisodeDTO> getSeasonsByNumber(@PathVariable Long id, @PathVariable Long number) {
        return service.getSeasonsByNumber(id, number);
    }

    @GetMapping("/categoria/{nameGenre}")
    public List<SerieDTO> getSeriesByCategory(@PathVariable String nameGenre) {
        return service.getSeriesByCategory(nameGenre);
    }
}
