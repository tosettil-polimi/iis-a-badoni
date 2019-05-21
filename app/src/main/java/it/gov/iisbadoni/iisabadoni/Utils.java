package it.gov.iisbadoni.iisabadoni;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Lorenzo on 17/05/2015.
 */
public final class Utils {

    public static boolean haveInternetConnection(Context contesto) {

        ConnectivityManager cm = (ConnectivityManager) contesto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null) {
            return netInfo.isConnected();
        }

        return false;
    }

    /**
     *
     *
     * @param linkCirc
     * @return
     */
    public static String formattaPerDownload(String linkCirc) {
        String split[];
        split=linkCirc.split("/");
        return split[split.length-1];
    }

    public static boolean dialogHome(final Activity activity, Context context) {

        if(!haveInternetConnection(context))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Attenzione!")
                    .setMessage("\nConnessione ad internet assente!")
                    .setPositiveButton("ESCI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            activity.finish();
                        }
                    })
                    .show();
            return false;
        }
        return true;
    }

    /**
     * Richiede come parametro un <code>ArrayList</code> di <code>OraClasseJSON</code>, questo metodo
     * converte l'ArrayList in un'altro ArrayList di <code>Materia</code> che ci permette di essere
     * usato dall'applicazione Java
     *
     * @param giornoJSON
     * @return ArrayList
     */
    public static ArrayList<Materia> creaGiorno(ArrayList<OraClasseJSON> giornoJSON, Context context) {

        ArrayList<Materia> giornata = new ArrayList<>();
        Materia materia;
        int i = 0;
        boolean isPrimo = true;
        OraClasseJSON oraSingola = null;

        do {
            if(isPrimo) {
                oraSingola = giornoJSON.get(i);
                String[] array = creaOrarioMateria(oraSingola.getIdGiorno(), oraSingola.getIdOra(), oraSingola.getDurata());
                materia = new Materia(oraSingola.getNomeMateria(), oraSingola.getNomeDocente(), array[0], coloreMateria(oraSingola.getNomeMateria(), context), array[1]);
                giornata.add(materia);
                isPrimo = false;
            }
            else if(oraSingola.getIdOra() != giornoJSON.get(i).getIdOra()) {
                oraSingola = giornoJSON.get(i);
                String[] array = creaOrarioMateria(oraSingola.getIdGiorno(), oraSingola.getIdOra(), oraSingola.getDurata());
                materia = new Materia(oraSingola.getNomeMateria(), oraSingola.getNomeDocente(), array[0], coloreMateria(oraSingola.getNomeMateria(), context), array[1]);
                giornata.add(materia);
            }
            i++;
        }
        while(i < giornoJSON.size());

        return giornata;

    }

    /**
     * <code>creaOrarioMateria</code> è un metodo privato chiamato da <a href="creaGiorno">creaGiorno()</a>
     * che torna una stringa che converte i codici dell'ora in numeri. Es.: idGiorno = 0, idOra = 0, durata = 120
     * "8.05 - 9.55".
     *
     * @param idGiorno
     * @param idOra
     * @param durata
     * @return String
     */
    private static String[] creaOrarioMateria(int idGiorno, int idOra, int durata) {

        String orario = "";
        String modulo = "";
        String[] array;

        switch (idOra) {

            case 0:
                if(idGiorno != 5) {
                    switch (durata) {

                        case 60:
                            orario = Materia.PRIMOMODULO + " - " + Materia.SECONDOMODULO;
                            modulo = "1° modulo";
                            break;

                        case 120:
                            orario = Materia.PRIMOMODULO + " - " + Materia.TERZOMODULO;
                            modulo = "1° - 2° modulo";
                            break;

                        case 180:
                            orario = Materia.PRIMOMODULO + " - " + Materia.QUARTOMODULOINTERVALLO;
                            modulo = "1° - 2° - 3° modulo";
                            break;

                    }
                }
                else {
                    switch (durata) {

                        case 60:
                            orario = Materia.PRIMOMODULO + " - " + Materia.SECONDOMODULO;
                            modulo = "1° modulo";
                            break;

                        case 120:
                            orario = Materia.PRIMOMODULO + " - " + Materia.TERZOMODULO;
                            modulo = "1° - 2° modulo";
                            break;

                        case 180:
                            orario = Materia.PRIMOMODULO + " - " + Materia.QUARTOMODULOSABATO;
                            modulo = "1° - 2° - 3° modulo";
                            break;

                    }
                }
                break;

            case 1:
                if(idGiorno != 5) {
                    switch (durata) {

                        case 60:
                            orario = Materia.SECONDOMODULO + " - " + Materia.TERZOMODULO;
                            modulo = "2° modulo";
                            break;

                        case 120:
                            orario = Materia.SECONDOMODULO + " - " + Materia.QUARTOMODULOINTERVALLO;
                            modulo = "2° - 3° modulo";
                            break;

                        case 180:
                            orario = Materia.SECONDOMODULO + " - " + Materia.QUINTOMODULO;
                            modulo = "2° - 3° - 4° modulo";
                            break;

                    }
                }
                else {
                    switch (durata) {

                        case 60:
                            orario = Materia.SECONDOMODULO + " - " + Materia.TERZOMODULO;
                            modulo = "2° modulo";
                            break;

                        case 120:
                            orario = Materia.SECONDOMODULO + " - " + Materia.QUARTOMODULOSABATO;
                            modulo = "2° - 3° modulo";
                            break;

                        case 180:
                            orario = Materia.SECONDOMODULO + " - " + Materia.QUINTOMODULOSABATO;
                            modulo = "2° - 3° - 4° modulo";
                            break;

                    }
                }
                break;

            case 2:
                if(idGiorno != 5) {
                    switch (durata) {

                        case 60:
                            orario = Materia.TERZOMODULO + " - " + Materia.QUARTOMODULOINTERVALLO;
                            modulo = "3° modulo";
                            break;

                        case 120:
                            orario = Materia.TERZOMODULO + " - " + Materia.QUINTOMODULO;
                            modulo = "3° - 4° modulo";
                            break;

                        case 180:
                            orario = Materia.TERZOMODULO + " - " + Materia.SESTOMODULO;
                            modulo = "3° - 4° - 5° modulo";
                            break;

                    }
                }
                else {
                    switch (durata) {

                        case 60:
                            orario = Materia.TERZOMODULOSABATO + " - " + Materia.QUARTOMODULOSABATO;
                            modulo = "3° modulo";
                            break;

                        case 120:
                            orario = Materia.TERZOMODULOSABATO + " - " + Materia.QUINTOMODULOSABATO;
                            modulo = "3° - 4° modulo";
                            break;

                    }
                }
                break;

            case 3:
                if(idGiorno != 5) {
                    switch (durata) {

                        case 60:
                            orario = Materia.QUARTOMODULOINTERVALLOFINITO + " - " + Materia.QUINTOMODULO;
                            modulo = "4° modulo";
                            break;

                        case 120:
                            orario = Materia.QUARTOMODULOINTERVALLOFINITO + " - " + Materia.SESTOMODULO;
                            modulo = "4° - 5° modulo";
                            break;

                        case 180:
                            orario = Materia.QUARTOMODULOINTERVALLOFINITO + " - " + Materia.SETTIMOMODULO;
                            modulo = "4° - 5° - 6° modulo";
                            break;

                    }
                }
                else {
                    orario = Materia.QUARTOMODULOSABATO + " - " + Materia.QUINTOMODULOSABATO;
                    modulo = "4° modulo";
                }
                break;

            case 4:
                switch (durata) {

                    case 60:
                        orario = Materia.QUINTOMODULO + " - " + Materia.SESTOMODULO;
                        modulo = "5° modulo";
                        break;

                    case 120:
                        orario = Materia.QUINTOMODULO + " - " + Materia.SETTIMOMODULO;
                        modulo = "5° - 6° modulo";

                }
                break;

            case 5:
                orario = Materia.SESTOMODULO + " - " + Materia.SETTIMOMODULO;
                modulo = "6° modulo";
                break;

        }

        array = new String[2];
        array[0] = orario;
        array[1] = modulo;

        return array;

    }

    /**
     *
     * @param orarioCompleto
     * @return
     */
    public static ArrayList<ArrayList<Materia>> creaOrarioCompleto(ArrayList<OraClasseJSON> orarioCompleto, Context context) {

        ArrayList<ArrayList<Materia>> sett = new ArrayList<>();
        ArrayList<OraClasseJSON> giorno;

        for (int i=0;i<orarioCompleto.size();i++) {
            int idGiorno = orarioCompleto.get(i).getIdGiorno();
            giorno = new ArrayList<>();
            boolean cicloFatto = false;         //serve per far diventare l'arraylist null se non entra nel for, in teoria non potrebbe entrarci solo il sabato (idGiorno = 5)
            int j;

            for(j = i; j < orarioCompleto.size() && orarioCompleto.get(j).getIdGiorno() == idGiorno; j++) {
                giorno.add(orarioCompleto.get(j));
                cicloFatto = true;
            }

            i = j - 1;      //per non far rifare il for sullo stesso idGiorno, sposto l'indice al giorno successivo

            if(cicloFatto)
                sett.add(creaGiorno(giorno, context));
        }

        return sett;

    }

    public static String coloreMateria(String materia, Context context) {

        BufferedReader reader = null;
        String json = null;
        String colore = "#FFFFFF";

        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("coloriMaterie.json")));

            String mLine;
            json = "";

            while ((mLine = reader.readLine()) != null) {
                json += mLine;
            }

        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

        }


        try {

            JSONArray array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(0);

            for(int i = 1;!obj.getString("materia").equals(materia); i++ )
                obj = array.getJSONObject(i);

            colore = obj.getString("colore");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return colore;
    }

    public static String[] classiFromAssets(Context context) {

        ArrayList<String> arrayList = new ArrayList<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        BufferedReader reader = null;
        String json = "";

        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("elencoClassi.json")));

            String mLine;

            while ((mLine = reader.readLine()) != null) {
                json += mLine;
            }

        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

        }

        try {

            JSONArray array = new JSONArray(json);
            JSONObject obj;
            String classePref = sp.getString("classePref", "1AEE");
            arrayList.add(classePref);

            for(int i = 0;(obj = array.getJSONObject(i)) != null; i++ ) {
                String nomeClasse = obj.getString("nomeclasse");
                if(!classePref.equals(nomeClasse))
                    arrayList.add(nomeClasse);
            }


        } catch (JSONException e) {
        }


        return arrayList.toArray(new String[0]);

    }

    public static String[] docentiFromAssets(Context context) {

        ArrayList<String> arrayList = new ArrayList<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        BufferedReader reader = null;
        String json = "";

        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("elencoDocenti.json")));

            String mLine;

            while ((mLine = reader.readLine()) != null) {
                json += mLine;
            }

        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }

        }

        try {

            JSONArray array = new JSONArray(json);
            JSONObject obj;
            String profPref = sp.getString("profPref", "Achler");
            arrayList.add(profPref);

            for(int i = 0;(obj = array.getJSONObject(i)) != null; i++ ) {
                String nomedocente = obj.getString("nomedocente");
                if(!profPref.equals(nomedocente))
                    arrayList.add(nomedocente);
            }


        } catch (JSONException e) {
        }


        return arrayList.toArray(new String[0]);

    }

    private static String tornaNomeGiorno(int idGiorno) {
        switch (idGiorno) {
            case 0:
                return "LUNEDI";
            case 1:
                return "MARTEDI";
            case 2:
                return "MERCOLEDI";
            case 3:
                return "GIOVEDI";
            case 4:
                return "VENERDI";
            case 5:
                return "SABATO";
        }

        return null;
    }

    public static String tornaGiornoEsatto(ArrayList<OraClasseJSON> arrayList, int position) {

        int nGiorno = 0;
        int j;

        j = arrayList.get(0).getIdGiorno();

        for(int i=1;i<=arrayList.size();i++) {
            if(nGiorno == position) {
                return tornaNomeGiorno(j);
            }
            try {
                if (!(j == arrayList.get(i).getIdGiorno())) {
                    nGiorno++;
                    j = arrayList.get(i).getIdGiorno();
                }
            }
            catch (IndexOutOfBoundsException e) {}
        }

        return tornaNomeGiorno(j);

    }
}
