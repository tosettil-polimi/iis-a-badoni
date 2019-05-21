package it.gov.iisbadoni.iisabadoni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ScegliClasse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegliclasse);
        setTitle("Orario Lezioni");

        final ArrayAdapter<CharSequence> adapterClasse = new ArrayAdapter<CharSequence>(this, R.layout.spinner_closed, R.id.titleSpinner, Utils.classiFromAssets(this));
        final ArrayAdapter<CharSequence> adapterDocenti = new ArrayAdapter<CharSequence>(this, R.layout.spinner_closed, R.id.titleSpinner, Utils.docentiFromAssets(this));

        adapterClasse.setDropDownViewResource(R.layout.spinner_item);
        adapterDocenti.setDropDownViewResource(R.layout.spinner_item);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerClassi);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupClass);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TextView textView = (TextView) findViewById(R.id.titleSpinnerClass);

                switch (checkedId) {
                    case R.id.radioButtClasse:
                        textView.setText("Seleziona la classe che vuoi visualizzare");
                        spinner.setAdapter(adapterClasse);
                        break;
                    case R.id.radioButtProf:
                        textView.setText("Seleziona il professore che vuoi visualizzare");
                        spinner.setAdapter(adapterDocenti);
                        break;
                }
            }
        });

        radioGroup.check(R.id.radioButtClasse);
    }

    public void apriOrario(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerClassi);
        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButtClasse);

        Intent intent = new Intent(this, OrarioActivity.class);
        intent.putExtra("scelta", (CharSequence) spinner.getSelectedItem());
        intent.putExtra("isStudente", radioButton.isChecked());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
