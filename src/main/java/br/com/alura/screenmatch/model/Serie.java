package br.com.alura.screenmatch.model;

import java.util.OptionalDouble;

public class Serie {

    private String title;
    private Integer totalSeasons;
    private Double rating;
    private ECategory genre;
    private String actors;
    private String poster;
    private String plot;

    public Serie(DataSerie dataSerie) {
        this.title = dataSerie.title();
        this.totalSeasons = dataSerie.totalSeasons();
        this.rating = OptionalDouble.of(Double.valueOf(dataSerie.rating())).orElse(0);
        this.genre = ECategory.fromString(dataSerie.genre().split(",")[0].trim());
        this.actors = dataSerie.actors();
        this.poster = dataSerie.poster();
        this.plot = dataSerie.plot();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ECategory getGenre() {
        return genre;
    }

    public void setGenre(ECategory genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return "genero=" + genre +
                ", titulo='" + title + '\'' +
                ", totalTemporadas=" + totalSeasons +
                ", avaliacao=" + rating +
                ", atores='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + plot + '\'';
    }
}
