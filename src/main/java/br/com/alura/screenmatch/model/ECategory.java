package br.com.alura.screenmatch.model;

public enum ECategory {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String categoryOmdb;

    ECategory(String categoryOmdb) {
        this.categoryOmdb = categoryOmdb;
    }

    public static ECategory fromString(String text) {
        for (ECategory eCategory : ECategory.values()) {
            if (eCategory.categoryOmdb.equalsIgnoreCase(text)) {
                return eCategory;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
