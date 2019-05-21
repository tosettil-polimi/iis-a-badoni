package it.gov.iisbadoni.iisabadoni;

/**
 * Created by Lorenzo on 03/02/2016.
 */
public class OraClasseJSON {

    private String nomeMateria;
    private String nomeDocente;
    private int durata;
    private int idGiorno;
    private int idOra;

    public OraClasseJSON(String nomeMateria, String nomeDocente, int durata, int idGiorno, int idOra) {
        this.nomeMateria = nomeMateria;
        this.nomeDocente = nomeDocente;
        this.durata = durata;
        this.idGiorno = idGiorno;
        this.idOra = idOra;
    }

    public OraClasseJSON() { }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }

    public String getNomeDocente() {
        return nomeDocente;
    }

    public void setNomeDocente(String nomeDocente) {
        this.nomeDocente = nomeDocente;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public int getIdGiorno() {
        return idGiorno;
    }

    public void setIdGiorno(int idGiorno) {
        this.idGiorno = idGiorno;
    }

    public int getIdOra() {
        return idOra;
    }

    public void setIdOra(int idOra) {
        this.idOra = idOra;
    }
}
