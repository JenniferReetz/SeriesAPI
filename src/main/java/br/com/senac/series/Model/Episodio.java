package br.com.senac.series.Model;

public class Episodio {

    private int id;
    private String titulo;
    private int duracao;
    private int temporada;
    private int idSerie;
    public Episodio() {
    }

    public Episodio(int id, String titulo, int duracao, int temporada, int idSerie) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.temporada = temporada;
        this.idSerie = idSerie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public int getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }
}
 