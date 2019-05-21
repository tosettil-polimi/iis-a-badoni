package it.gov.iisbadoni.iisabadoni;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.widget.Adapter;

import java.io.Serializable;

/**
 *
 * @author Jordino
 */
public class CircularFromDb implements Serializable {

    private String titolo;
    private String link;
    private String data;
    private String protocollo;

    public CircularFromDb(String titolo, String link, String data, String protocollo) {
        this.titolo = titolo;
        this.link = link;
        this.data = data;
        this.protocollo = protocollo;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getLink() {
        return link;
    }

    public String getData() {
        return data;
    }

    public String getProtocollo() {
        return protocollo;
    }

    @Override
    public String toString() {
        return titolo + "\nContinua a leggere...                                       \nData: " + data;
    }

}
