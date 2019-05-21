package it.gov.iisbadoni.iisabadoni;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OrarioAdapter extends RecyclerView.Adapter<OrarioAdapter.OrarioViewHolder> {

    private List<Materia> materiaList;

    public OrarioAdapter(List<Materia> materiaList) {
        this.materiaList = materiaList;
    }

    @Override
    public int getItemCount() {
        return materiaList.size();
    }

    @Override
    public void onBindViewHolder(OrarioViewHolder orarioViewHolder, int i) {
        Materia materia = materiaList.get(i);
        orarioViewHolder.materia.setText(materia.getMateria());
        orarioViewHolder.descrzione.setText(materia.getDescrizione());
        orarioViewHolder.orario.setText(materia.getOrario());
        orarioViewHolder.modulo.setText(materia.getModulo());
        orarioViewHolder.color.setBackgroundColor(Color.parseColor(materia.getColor()));
    }

    @Override
    public OrarioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_materia, viewGroup, false);

        return new OrarioViewHolder(itemView);
    }

    public static class OrarioViewHolder extends RecyclerView.ViewHolder {
        protected TextView materia;
        protected TextView descrzione;
        protected TextView orario;
        protected TextView modulo;
        protected View color;

        public OrarioViewHolder(View v) {
            super(v);
            materia =  (TextView) v.findViewById(R.id.materia);
            descrzione = (TextView)  v.findViewById(R.id.desc);
            orario = (TextView)  v.findViewById(R.id.orario);
            modulo = (TextView)  v.findViewById(R.id.modulo);
            color = v.findViewById(R.id.coloreMateria);
        }
    }
}