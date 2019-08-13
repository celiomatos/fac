package com.celio.fac;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentFour extends Fragment {

    EditText edtqtdpaessaca;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup vg, Bundle b) {
        View v = i.inflate(R.layout.fragment_four, vg, false);
        init(v);
        return v;
    }

    /**
     * @param v
     */
    private void init(final View v) {
        edtqtdpaessaca = v.findViewById(R.id.edtqtdpaessaca);
        edtqtdpaessaca.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    setPaesSaca(v);
                }
            }
        });
    }

    private void setPaesSaca(View v) {

        // total / edtpesobola * edtqtdpaesbola

        EditText edtpesobola = v.findViewById(R.id.edtpesobola);
        if (!edtpesobola.getText().toString().trim().isEmpty()) {
            String pesoBola = edtpesobola.getText().toString().trim();
            EditText edtqtdpaesbola = v.findViewById(R.id.edtqtdpaesbola);
            if (!edtqtdpaesbola.getText().toString().trim().isEmpty()) {
                String qtdPaesBola = edtqtdpaesbola.getText().toString().trim();

                Double pesoBolaD = Double.parseDouble(pesoBola);
                Double qtdPaesBolaD = Double.parseDouble(qtdPaesBola);


                Double totalC = 0D;
                Double qtdFarinhaCliente = 0D;
                TableLayout tbreceitacliente = getActivity().findViewById(R.id.tbreceitacliente);

                for (int i = 1; i < tbreceitacliente.getChildCount() - 1; i++) {
                    TableRow row = (TableRow) tbreceitacliente.getChildAt(i);
                    TextView txv = (TextView) row.getChildAt(1);
                    String qtd = txv.getText().toString();

                    totalC += Double.parseDouble(qtd);
                    TextView txving = (TextView) row.getChildAt(0);
                    if (txving.getText().toString().trim().equalsIgnoreCase("farinha")) {
                        qtdFarinhaCliente = Double.parseDouble(qtd);
                    }
                }

                if (totalC != 0D && qtdFarinhaCliente != 0D) {

                    Double qtdPaesPorMassa = (totalC / pesoBolaD) * qtdPaesBolaD;
                    // 50 / farinha * result
                    Double qtFarinhaD = Double.parseDouble(String.valueOf(qtdFarinhaCliente));
                    Double qtdPaesPorSaca = (50D / qtFarinhaD) * qtdPaesPorMassa;
                    edtqtdpaessaca.setText(String.valueOf(qtdPaesPorSaca.intValue()));
                }
            }
        }
    }
}
