package com.celio.fac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.celio.fac.R;
import com.celio.fac.entities.Vendedor;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class VendedoresAdapter extends ArrayAdapter<Vendedor> {

    Context context;
    int resource, textViewResourceId;
    List<Vendedor> vendedores, tmpVendedores, suggestions;

    public VendedoresAdapter(Context context, int resource, int textViewResourceId, List<Vendedor> vendedores) {
        super(context, resource, textViewResourceId, vendedores);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.vendedores = vendedores;
        tmpVendedores = new ArrayList<>(vendedores); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autocomplete_cliente, parent, false);
        }
        String nome = vendedores.get(position).getNomeVendedor();
        if (nome != null) {
            TextView lblName = view.findViewById(R.id.lbl_name);
            if (lblName != null) {
                lblName.setText(nome);
            }
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Vendedor) resultValue).getNomeVendedor();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Vendedor vendedor : tmpVendedores) {
                    if (vendedor.getNomeVendedor().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(vendedor);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                List<Vendedor> filterList = (ArrayList<Vendedor>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Vendedor vendedor : filterList) {
                        add(vendedor);
                        notifyDataSetChanged();
                    }
                }
            } catch (ConcurrentModificationException e) {
                Toast.makeText(getContext(), "Apague pausadamente", Toast.LENGTH_LONG).show();
            }
        }
    };
}
