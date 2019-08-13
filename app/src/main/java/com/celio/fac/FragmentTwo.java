package com.celio.fac;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.celio.fac.dao.ProdutoDao;
import com.celio.fac.dao.TecnicoDao;
import com.celio.fac.entities.Produto;
import com.celio.fac.entities.Tecnico;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FragmentTwo extends Fragment implements View.OnClickListener {


    private DatePickerDialog dtValidadeDialog, dtDateTecnicoDialog;
    private DatePickerDialog dtAtendimentoDialog, dtFinalAtendDialog;
    private TimePickerDialog dtTimeTecnicoDialog;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private EditText edtdtvalidade, edtdtatendimento, edtdtfinalatend;
    private EditText edtdatahora;

    private AutoCompleteTextView edtproduto, edttecnico;
    private RadioGroup rgtecnico;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup vg, Bundle b) {

        View v = i.inflate(R.layout.fragment_two, vg, false);

        init(v);

        return v;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == edtdtvalidade.getId()) {
            dtValidadeDialog.show();
        } else if (view.getId() == edtdtatendimento.getId()) {
            dtAtendimentoDialog.show();
        } else if (view.getId() == edtdtfinalatend.getId()) {
            dtFinalAtendDialog.show();
        } else if (view.getId() == edtdatahora.getId()) {
            dtTimeTecnicoDialog.show();
            dtDateTecnicoDialog.show();
        }
    }

    /**
     * @param v
     */
    private void init(View v) {

        edtproduto = v.findViewById(R.id.edtproduto);
        edtdtvalidade = v.findViewById(R.id.edtdtvalidade);
        edtdtvalidade.setOnClickListener(this);
        edtdtatendimento = v.findViewById(R.id.edtdtatendimento);
        edtdtatendimento.setOnClickListener(this);
        edtdtfinalatend = v.findViewById(R.id.edtdtfinalatend);
        edtdtfinalatend.setOnClickListener(this);
        edttecnico = v.findViewById(R.id.edttecnico);
        edtdatahora = v.findViewById(R.id.edtdatahora);
        edtdatahora.setOnClickListener(this);
        rgtecnico = v.findViewById(R.id.rgtecnico);

        autoCompleteProduto();
        autoCompleteTecnico();
        setDataValidade();
        setDataAtendimento();
        setDataFinalAtendimento();
        setDataTimeTecnico();
        habilitaTecnico();
    }

    /**
     * auto complete tecnicos
     */
    private void autoCompleteTecnico() {

        ArrayAdapter<String> tecnicosAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                getTecnicos());

        edttecnico.setDropDownBackgroundResource(R.color.colorWhite);
        edttecnico.setAdapter(tecnicosAdapter);
    }

    /**
     * @return
     */
    private String[] getTecnicos() {

        TecnicoDao tecnicoDao = new TecnicoDao();

        List<Tecnico> lst = tecnicoDao.getAll(getContext());

        int tam = lst.size();
        String vt[] = new String[tam];

        for (int i = 0; i < tam; i++) {
            vt[i] = lst.get(i).getNomeTecnico();
        }

        return vt;
    }

    /**
     * auto complete produtos
     */
    private void autoCompleteProduto() {

        ArrayAdapter<String> produtos = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                getProdutos());

        edtproduto.setDropDownBackgroundResource(R.color.colorWhite);
        edtproduto.setAdapter(produtos);

    }

    /**
     * set data validade
     */
    private void setDataValidade() {
        Calendar c = Calendar.getInstance();

        dtValidadeDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar d = Calendar.getInstance();
                d.set(year, month, day);
                edtdtvalidade.setText(sdf.format(d.getTime()));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * data atendimento
     */
    private void setDataAtendimento() {
        Calendar b = Calendar.getInstance();

        dtAtendimentoDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar d = Calendar.getInstance();
                d.set(year, month, day);
                edtdtatendimento.setText(sdf.format(d.getTime()));
            }
        }, b.get(Calendar.YEAR), b.get(Calendar.MONTH), b.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * set data final atendimento
     */
    private void setDataFinalAtendimento() {

        Calendar e = Calendar.getInstance();

        dtFinalAtendDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar d = Calendar.getInstance();
                d.set(year, month, day);
                edtdtfinalatend.setText(sdf.format(d.getTime()));
            }
        }, e.get(Calendar.YEAR), e.get(Calendar.MONTH), e.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * set data e hora do tecnico
     */
    private void setDataTimeTecnico() {

        Calendar dt = Calendar.getInstance();

        // date
        dtDateTecnicoDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar d = Calendar.getInstance();
                d.set(year, month, day);
                edtdatahora.setText(sdf.format(d.getTime()));
            }
        }, dt.get(Calendar.YEAR), dt.get(Calendar.MONTH), dt.get(Calendar.DAY_OF_MONTH));

        // time
        dtTimeTecnicoDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String strH = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String strM = minute < 10 ? "0" + minute : "" + minute;

                String dtt = edtdatahora.getText().toString() + " " + strH + ":" + strM;
                edtdatahora.setText(dtt);
            }
        }, dt.get(Calendar.HOUR_OF_DAY), dt.get(Calendar.MINUTE), true);

    }

    /**
     * habilita campo tecnico
     */
    private void habilitaTecnico() {

        rgtecnico.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbtecnicosim:
                        edtdatahora.setEnabled(true);
                        edttecnico.setEnabled(true);
                        edttecnico.requestFocus();
                        break;
                    case R.id.rbtecniconao:
                        edttecnico.setText("");
                        edtdatahora.setText("");
                        edtdatahora.setEnabled(false);
                        edttecnico.setEnabled(false);
                        break;
                }
            }
        });
    }

    /**
     * lista de produtos
     *
     * @return
     */
    private String[] getProdutos() {

        ProdutoDao produtoDao = new ProdutoDao();

        List<Produto> lst = produtoDao.getAll(getContext());

        int tam = lst.size();
        String vt[] = new String[tam];

        for (int i = 0; i < tam; i++) {
            vt[i] = lst.get(i).getNomeProduto();
        }

        return vt;
    }

}
