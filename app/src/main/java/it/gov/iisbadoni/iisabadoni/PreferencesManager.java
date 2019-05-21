package it.gov.iisbadoni.iisabadoni;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public final static String CAMPO_CATEGORIA="categoria";
    public final static String CAMPO_NOTIFICHE="notifiche";
    public final static String CAMPO_CIRCOLARE="ultima_circolare";
    public final static String CAMPO_PRIMO_AVVIO="primo_avvio";
    public final static String PREF_NAME = "iisBadoni";

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getString(CAMPO_CATEGORIA, "studenti").equals(null)) {
            editor.putBoolean(CAMPO_NOTIFICHE, true);
            editor.commit();

            editor.putString(CAMPO_CATEGORIA, "studenti");
            editor.commit();
        }
    }

    public String getCategoria() {
        return prefs.getString(CAMPO_CATEGORIA, "studenti");
    }

    public String getCircolare() {
        return prefs.getString(CAMPO_CIRCOLARE, null);
    }

    public boolean getPrimoAvvio() {
        return prefs.getBoolean(CAMPO_PRIMO_AVVIO, true);
    }

    public boolean getNotifiche() {
        return prefs.getBoolean(CAMPO_NOTIFICHE, true);
    }

    public void setCategoria(String categoria) {
        editor.putString(CAMPO_CATEGORIA, categoria);
        editor.commit();
    }

    public void setCircolare(String circolare) {
        editor.putString(CAMPO_CIRCOLARE, circolare);
        editor.commit();
    }

    public void setPrimoAvvio(boolean primoAvvio) {
        editor.putBoolean(CAMPO_PRIMO_AVVIO, primoAvvio);
        editor.commit();
    }

    public void setNotifiche(boolean notifiche) {
        editor.putBoolean(CAMPO_NOTIFICHE, notifiche);
        editor.commit();
    }
}
