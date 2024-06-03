package br.com.myfrilas.model;

public class Assessment {
//avaliacao
    private int note;
    private String comment;
    private long idAuthor;

    public Assessment() {}
    public Assessment(int note, String comment, long idAuthor) {
        this.note = note;
        this.comment = comment;
        this.idAuthor = idAuthor;
    }
    public int getNote() {
        return note;
    }
    public void setNote(int note) {
        this.note = note;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public long getIdAuthor() {
        return idAuthor;
    }
    public void setIdAuthor(long idAuthor) {
        this.idAuthor = idAuthor;
    }
}
