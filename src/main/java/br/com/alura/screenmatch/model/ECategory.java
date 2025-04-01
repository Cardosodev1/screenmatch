package br.com.alura.screenmatch.model;

public enum ECategory {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    private String categoryOmdb;

    private String categoryPortuguese;

    ECategory(String categoryOmdb, String categoryPortuguese) {
        this.categoryOmdb = categoryOmdb;
        this.categoryPortuguese = categoryPortuguese;
    }

    public static ECategory fromString(String text) {
        for (ECategory eCategory : ECategory.values()) {
            if (eCategory.categoryOmdb.equalsIgnoreCase(text)) {
                return eCategory;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static ECategory fromPortuguese(String text) {
        for (ECategory eCategory : ECategory.values()) {
            if (eCategory.categoryPortuguese.equalsIgnoreCase(text)) {
                return eCategory;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
