package com.celio.fac;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.celio.fac.adapters.MassasAdapter;
import com.celio.fac.dao.IngredienteDao;
import com.celio.fac.dao.MassaDao;
import com.celio.fac.dao.ReceitaDao;
import com.celio.fac.entities.Ingrediente;
import com.celio.fac.entities.Massa;
import com.celio.fac.entities.Receita;
import com.celio.fac.utils.Utils;

import java.math.BigDecimal;
import java.util.List;

public class FragmentThree extends Fragment {

    private EditText edtqtdcliente, edtporccliente, edtqtdsugerida, edtporcsugerida;
    private TextView qtdtotalcliente, qtdtotalsugerida;
    private AutoCompleteTextView edtmassa, edtingrediente;
    private TableLayout tbreceitacliente, tbreceitasugerida;
    private ImageButton btadd;
    private int qtdFarinhaCliente, qtdFarinhaSugerida, idxTotalCliente = 1, idxTotalSugerida = 1;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup vg, Bundle b) {
        View v = i.inflate(R.layout.fragment_three, vg, false);
        init(v);
        return v;
    }

    /**
     * @param v
     */
    private void init(View v) {

        edtmassa = v.findViewById(R.id.edtmassa);
        edtingrediente = v.findViewById(R.id.edtingrediente);
        edtqtdcliente = v.findViewById(R.id.edtqtdcliente);
        edtporccliente = v.findViewById(R.id.edtporccliente);
        edtqtdsugerida = v.findViewById(R.id.edtqtdsugerida);
        edtporcsugerida = v.findViewById(R.id.edtporcsugerida);
        tbreceitacliente = v.findViewById(R.id.tbreceitacliente);
        tbreceitasugerida = v.findViewById(R.id.tbreceitasugerida);
        qtdtotalcliente = v.findViewById(R.id.qtdtotalcliente);
        qtdtotalsugerida = v.findViewById(R.id.qtdtotalsugerida);

        edtqtdcliente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && qtdFarinhaCliente > 0) {
                    if (!edtqtdcliente.getText().toString().trim().isEmpty()) {
                        String qtd = edtqtdcliente.getText().toString().trim();
                        edtporccliente.setText(getPorcentagem(qtd, qtdFarinhaCliente));
                    }
                }
            }
        });

        edtporccliente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && qtdFarinhaCliente > 0) {
                    if (!edtporccliente.getText().toString().trim().isEmpty()) {
                        String porc = edtporccliente.getText().toString().trim();
                        edtqtdcliente.setText(getQuantidade(porc, qtdFarinhaCliente));
                    }
                }
            }
        });

        edtqtdsugerida.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && qtdFarinhaSugerida > 0) {
                    if (!edtqtdsugerida.getText().toString().trim().isEmpty()) {
                        String qtd = edtqtdsugerida.getText().toString().trim();
                        edtporcsugerida.setText(getPorcentagem(qtd, qtdFarinhaSugerida));
                    }
                }
            }
        });

        edtporcsugerida.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && qtdFarinhaSugerida > 0) {
                    if (!edtporcsugerida.getText().toString().trim().isEmpty()) {
                        String porc = edtporcsugerida.getText().toString().trim();
                        edtqtdsugerida.setText(getQuantidade(porc, qtdFarinhaSugerida));
                    }
                }
            }
        });

        // botao addingrediente
        btadd = v.findViewById(R.id.btadd);
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReceita();
            }
        });

        autoCompleteMassa();
        autoCompleteIngrediente();
    }

    /**
     * @param porc
     * @param qdtFarinha
     * @return
     */
    private String getQuantidade(String porc, int qdtFarinha) {

        try {
            Double porcD = Double.parseDouble(porc);
            BigDecimal qtd = new BigDecimal(qdtFarinha * (porcD / 100));
            porc = Utils.aredondaValor(qtd.toString(), 3);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.falha_calc_porc, Toast.LENGTH_LONG).show();
        }
        return porc;
    }

    /**
     * @param qtd
     * @param qdtFarinha
     * @return
     */
    private String getPorcentagem(String qtd, int qdtFarinha) {

        try {
            Double qdtD = Double.parseDouble(qtd);
            BigDecimal porc = new BigDecimal((qdtD / qdtFarinha) * 100);
            qtd = Utils.aredondaValor(porc.toString(), 2);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.falha_calc_porc, Toast.LENGTH_LONG).show();
        }
        return qtd;
    }

    /**
     *
     */
    private void setReceita() {

        String ingrediente = edtingrediente.getText().toString().trim();
        String qtCl = edtqtdcliente.getText().toString().trim();
        String porcCl = edtporccliente.getText().toString().trim();
        String qtSug = edtqtdsugerida.getText().toString().trim();
        String porcSug = edtporcsugerida.getText().toString().trim();

        if (!qtCl.isEmpty()) {
            qtCl = Utils.aredondaValor(qtCl, 3);
        }
        if (!porcCl.isEmpty()) {
            porcCl = Utils.aredondaValor(porcCl, 2);
        }
        if (!qtSug.isEmpty()) {
            qtSug = Utils.aredondaValor(qtSug, 3);
        }
        if (!porcSug.isEmpty()) {
            porcSug = Utils.aredondaValor(porcSug, 2);
        }

        setReceita(ingrediente, qtCl, porcCl, qtSug, porcSug);

        if (!edtmassa.getText().toString().trim().isEmpty()
                && ingrediente.equalsIgnoreCase("farinha")
                && qtdFarinhaCliente > 0
                && qtdFarinhaSugerida > 0) {

            ReceitaDao receitaDao = new ReceitaDao();

            String massa = edtmassa.getText().toString().trim();

            List<Receita> receita = receitaDao.getReceitaByMassa(getContext(), massa);

            if (!receita.isEmpty()) {
                for (Receita r : receita) {
                    ingrediente = r.getIngrediente().getNomeIngrediente();
                    qtCl = getQuantidade(r.getPorcentagem(), qtdFarinhaCliente);
                    porcCl = r.getPorcentagem();
                    qtSug = getQuantidade(r.getPorcentagem(), qtdFarinhaSugerida);
                    setReceita(ingrediente, qtCl, porcCl, qtSug, porcCl);
                }
            }
        }
    }

    /**
     *
     */
    private void setReceita(String ingrediente, String qtCl, String porcCl, String qtSug, String porcSug) {

        if (!ingrediente.isEmpty()) {
            // cliente
            boolean achouC = false;

            // se ja foi lancando remove ou altera
            for (int i = 1; i < tbreceitacliente.getChildCount() - 1; i++) {
                TableRow row = (TableRow) tbreceitacliente.getChildAt(i);
                TextView txv = (TextView) row.getChildAt(0);
                String ing = txv.getText().toString();
                if (ing.equalsIgnoreCase(ingrediente)) {
                    achouC = true;
                    if (qtCl.isEmpty() || porcCl.isEmpty()) {
                        tbreceitacliente.removeViewAt(i);
                        idxTotalCliente--;
                        break;
                    } else {
                        TextView txvqtd = (TextView) row.getChildAt(1);
                        txvqtd.setText(qtCl);
                        TextView txvporc = (TextView) row.getChildAt(2);
                        txvporc.setText(porcCl);
                        break;
                    }
                }
            }

            if (!qtCl.isEmpty() && !porcCl.isEmpty() && !achouC) {

                TextView ic = new TextView(getContext());
                ic.setText(ingrediente);
                ic.setTextColor(Color.WHITE);

                TextView qc = new TextView(getContext());
                qc.setText(qtCl);
                qc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                qc.setTextColor(Color.WHITE);

                if (ingrediente.equalsIgnoreCase("farinha")) {
                    qtdFarinhaCliente = Integer.parseInt(qtCl);
                }

                TextView pc = new TextView(getContext());
                pc.setText(porcCl);
                pc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                pc.setTextColor(Color.WHITE);

                TableRow rc = new TableRow(getContext());
                rc.addView(ic);
                rc.addView(qc);
                rc.addView(pc);

                tbreceitacliente.addView(rc, idxTotalCliente++);

            }

            Double totalC = 0D;
            for (int i = 1; i < tbreceitacliente.getChildCount() - 1; i++) {
                TableRow row = (TableRow) tbreceitacliente.getChildAt(i);
                TextView txv = (TextView) row.getChildAt(1);
                String qtd = txv.getText().toString();

                totalC += Double.parseDouble(qtd);
            }
            if (totalC == 0D) {
                qtdFarinhaCliente = 0;
            }
            qtdtotalcliente.setText(Utils.aredondaValor(totalC.toString(), 3));
            setPaesSaca(totalC);
            // sugerida
            boolean achouS = false;

            for (int i = 1; i < tbreceitasugerida.getChildCount() - 1; i++) {
                TableRow row = (TableRow) tbreceitasugerida.getChildAt(i);
                TextView txv = (TextView) row.getChildAt(0);
                String ing = txv.getText().toString();
                if (ing.equalsIgnoreCase(ingrediente)) {
                    achouS = true;
                    if (qtSug.isEmpty() || porcSug.isEmpty()) {
                        tbreceitasugerida.removeViewAt(i);
                        idxTotalSugerida--;
                        break;
                    } else {
                        TextView txvqtd = (TextView) row.getChildAt(1);
                        txvqtd.setText(qtSug);
                        TextView txvporc = (TextView) row.getChildAt(2);
                        txvporc.setText(porcSug);
                        break;
                    }
                }
            }

            // sugerida
            if (!qtSug.isEmpty() && !porcSug.isEmpty() && !achouS) {

                TextView is = new TextView(getContext());
                is.setText(ingrediente);
                is.setTextColor(Color.WHITE);

                TextView qs = new TextView(getContext());
                qs.setText(qtSug);
                qs.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                qs.setTextColor(Color.WHITE);

                if (ingrediente.equalsIgnoreCase("farinha")) {
                    qtdFarinhaSugerida = Integer.parseInt(qtSug);
                }
                TextView ps = new TextView(getContext());
                ps.setText(porcSug);
                ps.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                ps.setTextColor(Color.WHITE);

                TableRow rs = new TableRow(getContext());
                rs.addView(is);
                rs.addView(qs);
                rs.addView(ps);

                tbreceitasugerida.addView(rs, idxTotalSugerida++);
            }

            Double totalS = 0D;
            for (int i = 1; i < tbreceitasugerida.getChildCount() - 1; i++) {
                TableRow row = (TableRow) tbreceitasugerida.getChildAt(i);
                TextView txv = (TextView) row.getChildAt(1);
                String qtd = txv.getText().toString();
                totalS += Double.parseDouble(qtd);
            }
            if (totalS == 0D) {
                qtdFarinhaSugerida = 0;
            }
            qtdtotalsugerida.setText(Utils.aredondaValor(totalS.toString(), 3));
        }
        edtingrediente.setText("");
        edtqtdcliente.setText("");
        edtporccliente.setText("");
        edtqtdsugerida.setText("");
        edtporcsugerida.setText("");
        edtingrediente.requestFocus();
    }

    /**
     * @param total
     */
    private void setPaesSaca(Double total) {

        // total / edtpesobola * edtqtdpaesbola

        EditText edtpesobola = getActivity().findViewById(R.id.edtpesobola);
        if (!edtpesobola.getText().toString().trim().isEmpty()) {
            String pesoBola = edtpesobola.getText().toString().trim();
            EditText edtqtdpaesbola = getActivity().findViewById(R.id.edtqtdpaesbola);
            if (!edtqtdpaesbola.getText().toString().trim().isEmpty()) {
                String qtdPaesBola = edtqtdpaesbola.getText().toString().trim();

                Double pesoBolaD = Double.parseDouble(pesoBola);
                Double qtdPaesBolaD = Double.parseDouble(qtdPaesBola);

                Double qtdPaesPorMassa = (total / pesoBolaD) * qtdPaesBolaD;
                // 50 / farinha * result
                Double qtFarinhaD = Double.parseDouble(String.valueOf(qtdFarinhaCliente));
                Double qtdPaesPorSaca = (50D / qtFarinhaD) * qtdPaesPorMassa;

                EditText edtqtdpaessaca = getActivity().findViewById(R.id.edtqtdpaessaca);
                edtqtdpaessaca.setText(String.valueOf(qtdPaesPorSaca.intValue()));
            }
        }
    }

    /**
     * auto completa massa
     */
    private void autoCompleteMassa() {
        MassaDao massaDao = new MassaDao();
        List<Massa> lst = massaDao.getAll(getContext());

        final MassasAdapter massasAdapter = new MassasAdapter(
                getContext(),
                R.layout.fragment_one,
                R.id.lbl_name,
                lst);

        edtmassa.setDropDownBackgroundResource(R.color.colorWhite);
        edtmassa.setAdapter(massasAdapter);
        edtmassa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {

                Massa massa = massasAdapter.getItem(posicao);

                EditText edtmist1veloc = getActivity().findViewById(R.id.edtmist1veloc);
                edtmist1veloc.setText(massa.getVelocidadeA());
                EditText edtmist2veloc = getActivity().findViewById(R.id.edtmist2veloc);
                edtmist2veloc.setText(massa.getVelocidadeB());
                EditText edttempagua = getActivity().findViewById(R.id.edttempagua);
                edttempagua.setText(massa.getTempAgua());
                EditText edttempmassa = getActivity().findViewById(R.id.edttempmassa);
                edttempmassa.setText(massa.getTempMassa());
                EditText edttempforno = getActivity().findViewById(R.id.edttempforno);
                edttempforno.setText(massa.getTempForno());
                if (massa.getFermentacao() != null && !massa.getFermentacao().isEmpty()) {
                    RadioGroup rgfermentacao = getActivity().findViewById(R.id.rgfermentacao);
                    if (massa.getFermentacao().equalsIgnoreCase("curta")) {
                        rgfermentacao.check(R.id.rbcurta);
                    } else if (massa.getFermentacao().equalsIgnoreCase("longa")) {
                        rgfermentacao.check(R.id.rblonga);
                    }
                }
                EditText edttempoferm = getActivity().findViewById(R.id.edttempoferm);
                edttempoferm.setText(massa.getTempoFermentacao());
                EditText edttempoforne = getActivity().findViewById(R.id.edttempoforne);
                edttempoforne.setText(massa.getTempoForneamento());
                EditText edtpesobola = getActivity().findViewById(R.id.edtpesobola);
                edtpesobola.setText(Utils.aredondaValor(massa.getPesoBola(), 3));
                EditText edtpesopaocru = getActivity().findViewById(R.id.edtpesopaocru);
                edtpesopaocru.setText(Utils.aredondaValor(massa.getPesoPaoCru(), 3));
                EditText edtpesopaoassado = getActivity().findViewById(R.id.edtpesopaoassado);
                edtpesopaoassado.setText(Utils.aredondaValor(massa.getPesoPaoAssado(), 3));
                EditText edtqtdpaesbola = getActivity().findViewById(R.id.edtqtdpaesbola);
                edtqtdpaesbola.setText(massa.getQtdePaesBola());
                edtingrediente.requestFocus();
            }
        });
    }

    /**
     * auto completa ingredientes
     */
    private void autoCompleteIngrediente() {
        final ArrayAdapter<String> ingredientesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                getIngredientes());

        edtingrediente.setDropDownBackgroundResource(R.color.colorWhite);
        edtingrediente.setAdapter(ingredientesAdapter);
        edtingrediente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String ingrediente = ingredientesAdapter.getItem(position);
                if (ingrediente.equalsIgnoreCase("farinha")) {
                    edtporccliente.setText("100");
                    edtporcsugerida.setText("100");
                }
                edtqtdcliente.requestFocus();
            }
        });
    }

    /**
     * lista de ingredientes
     *
     * @return
     */
    private String[] getIngredientes() {

        IngredienteDao ingredienteDao = new IngredienteDao();

        List<Ingrediente> lst = ingredienteDao.getAll(getContext());

        int tam = lst.size();
        String vt[] = new String[tam];

        for (int i = 0; i < tam; i++) {
            vt[i] = lst.get(i).getNomeIngrediente();
        }

        return vt;
    }
}
