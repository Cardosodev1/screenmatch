package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.ECategory;

public record SerieDTO(Long id,
                       String title,
                       Integer totalSeasons,
                       Double rating,
                       ECategory genre,
                       String actors,
                       String poster,
                       String plot) {
}
