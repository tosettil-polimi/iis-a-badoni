package it.gov.iisbadoni.iisabadoni;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Materia {

    public static final String PRIMOMODULO = "8.05";
    public static final String SECONDOMODULO = "9.00";
    public static final String TERZOMODULO = "9.55";
    public static final String QUARTOMODULOINTERVALLO = "10.50";
    public static final String QUARTOMODULOINTERVALLOFINITO = "11.05";
    public static final String QUINTOMODULO = "12.00";
    public static final String SESTOMODULO = "12.55";
    public static final String SETTIMOMODULO = "13.50";

    public static final String TERZOMODULOSABATO = "10.05";
    public static final String QUARTOMODULOSABATO = "11.00";
    public static final String QUINTOMODULOSABATO = "11.55";

    private String materia;
    private String descrizione;
    private String orario;
    private String color;
    private String modulo;
    private Context context;

    public Materia(String materia, String descrizione, String orario, String color) {
        this.materia = materia;
        this.descrizione = descrizione;   //Professore o Classe
        this.orario = orario;   //es.: 8.05 - 9.55 (PRIMOMODULO + " - " + TERZOMODULO)
        this.color = color;
        this.modulo = "";
    }

    public Materia(String materia, String descrizione, String orario, String color, String modulo) {
        this.materia = materia;
        this.descrizione = descrizione;   //Professore o Classe
        this.orario = orario;   //es.: 8.05 - 9.55 (PRIMOMODULO + " - " + TERZOMODULO)
        this.color = color;
        this.modulo = modulo;
    }

    public Materia(String materia, String descrizione, String orario, Context context) {
        this.materia = materia;
        this.descrizione = descrizione;
        this.orario = orario;
        this.modulo = "";
        this.color = "#FFFFFF";
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrzione) {
        this.descrizione = descrzione;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "materia = '" + materia + '\'' +
                ", descrizione = '" + descrizione + '\'' +
                ", orario = '" + orario + '\'' +
                ", color = '" + color + '\'' +
                '}';
    }
}
