package br.com.myfrilas.enums;

public enum StatusProject {
    
    Open(0, "Aberto"),
    IN_PROGRESS (1, "Em andamento"),
    DONE (2, "Concluído"),
    CANCELED (3, "Cancelado");

    private int type;
    private String description;

    StatusProject(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
