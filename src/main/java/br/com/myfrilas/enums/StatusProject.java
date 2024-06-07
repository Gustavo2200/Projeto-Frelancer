package br.com.myfrilas.enums;

public enum StatusProject {
    OPEN(0, "ABERTO"),
    IN_PROGRESS(1, "EM_ANDAMENTO"),
    DONE(2, "CONCLUIDO");

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

    public static StatusProject fromDescription(String description) {
        for (StatusProject status : StatusProject.values()) {
            if (status.getDescription().equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }
}
