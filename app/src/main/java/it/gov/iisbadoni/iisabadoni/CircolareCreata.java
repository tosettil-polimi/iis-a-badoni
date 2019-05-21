package it.gov.iisbadoni.iisabadoni;

import java.io.Serializable;

/**
 * Created by Lorenzo on 17/05/2015.
 */
public class CircolareCreata implements Serializable {

    private String contenuto;
    private String nProto;
    private String links[];
    private String formati[];

    public CircolareCreata() {

    }


    public CircolareCreata(String contenuto, String nProto, String[] links, String[] formati) {
        this.contenuto = contenuto;
        this.nProto = nProto;
        this.links = links;
        this.formati = formati;
    }

    public String getnProto()
    {
        return nProto;
    }

    public String getContenuto()
    {
        return contenuto;
    }

    public String[] getLinks() {
        return links;
    }

    public String[] getFormati() {
        return formati;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public void setnProto(String nProto) {
        this.nProto = nProto;
    }

    public void setLinks(String[] links) {
        this.links = links;
    }

    public void setFormati(String[] formati) {
        this.formati = formati;
    }
}
