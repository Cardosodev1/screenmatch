package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private Integer totalSeasons;
    private Double rating;

    @Enumerated(EnumType.STRING)
    private ECategory genre;

    private String actors;
    private String poster;
    private String plot;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episode> episodes = new ArrayList<>();

    public Serie() {
    }

    public Serie(DataSerie dataSerie) {
        this.title = dataSerie.title();
        this.totalSeasons = dataSerie.totalSeasons();
        this.rating = OptionalDouble.of(Double.valueOf(dataSerie.rating())).orElse(0);
        this.genre = ECategory.fromString(dataSerie.genre().split(",")[0].trim());
        this.actors = dataSerie.actors();
        this.poster = dataSerie.poster();
        this.plot = dataSerie.plot();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        episodes.forEach(e -> e.setSerie(this));
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "genero=" + genre +
                ", titulo='" + title + '\'' +
                ", totalTemporadas=" + totalSeasons +
                ", avaliacao=" + rating +
                ", atores='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + plot + '\'' +
                ", episodios='" + episodes + '\'';
    }
}
