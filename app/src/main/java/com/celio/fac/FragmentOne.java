package com.celio.fac;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.celio.fac.adapters.ClientesAdapter;
import com.celio.fac.adapters.VendedoresAdapter;
import com.celio.fac.dao.ClienteDao;
import com.celio.fac.dao.SupervisorDao;
import com.celio.fac.dao.VendedorDao;
import com.celio.fac.entities.Clientes;
import com.celio.fac.entities.Supervisor;
import com.celio.fac.entities.Vendedor;

import java.util.List;

import static com.celio.fac.utils.Mask.insert;

public class FragmentOne extends Fragment {

    private EditText edttelefone, edtcelular, edtemail;
    private AutoCompleteTextView edtcliente, edtvendedor, edtsupervidor;
    private RadioGroup rgsegmento, rgmarca;
    private EditText edtlogradouto, edtnumero, edtbairro, edtcidade;
    private EditText edtmarca;


    private ClientesAdapter clientesAdapter;
    private VendedoresAdapter vendedoresAdapter;

    private final VendedorDao vendedorDao = new VendedorDao();
    private final SupervisorDao supervisorDao = new SupervisorDao();

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup vg, Bundle b) {

        View v = i.inflate(R.layout.fragment_one, vg, false);

        init(v);

        return v;
    }

    /**
     * @param v
     */
    private void init(View v) {

        edtcliente = v.findViewById(R.id.edtcliente);
        rgsegmento = v.findViewById(R.id.rgsegmento);
        edtlogradouto = v.findViewById(R.id.edtlogradouto);
        edtnumero = v.findViewById(R.id.edtnumero);
        edtbairro = v.findViewById(R.id.edtbairro);
        edtcidade = v.findViewById(R.id.edtcidade);
        edttelefone = v.findViewById(R.id.edttelefone);
        edtcelular = v.findViewById(R.id.edtcelular);
        edtemail = v.findViewById(R.id.edtemail);
        edtvendedor = v.findViewById(R.id.edtvendedor);
        edtsupervidor = v.findViewById(R.id.edtsupervidor);
        edtmarca = v.findViewById(R.id.edtmarca);
        rgmarca = v.findViewById(R.id.rgmarca);

        setMaskFone();
        habilitaMarca();
        autoCompleteSupervisor();
        autoCompleteVendedor();
        autoCompleteCliente();
    }

    /**
     * set mascara de entrada
     */
    private void setMaskFone() {
        // maskara fones
        edttelefone.addTextChangedListener(insert("(##)####-####", edttelefone));
        edtcelular.addTextChangedListener(insert("(##)#####-####", edtcelular));
    }

    /**
     * habilita informar outra marca de farinha
     */
    private void habilitaMarca() {
        rgmarca.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rgmarcasim:
                        edtmarca.setEnabled(true);
                        break;
                    case R.id.rgmarcanao:
                        edtmarca.setText("");
                        edtmarca.setEnabled(false);
                        break;
                }
            }
        });
    }

    /**
     * auto complete clientes
     */
    private void autoCompleteCliente() {

        ClienteDao clienteDao = new ClienteDao();
        List<Clientes> list = clienteDao.getAll(getContext());

        clientesAdapter = new ClientesAdapter(
                getContext(),
                R.layout.fragment_one,
                R.id.lbl_name,
                list);

        edtcliente.setAdapter(clientesAdapter);
        edtcliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Clientes cliente = clientesAdapter.getItem(position);
                // segmento
                rgsegmento.clearCheck();
                if (cliente.getSegmento() != null) {
                    if (cliente.getSegmento().equalsIgnoreCase("panificacao")) {
                        rgsegmento.check(R.id.rbpanificacao);
                    } else if (cliente.getSegmento().equalsIgnoreCase("confeitaria")) {
                        rgsegmento.check(R.id.rbconfeitaria);
                    } else if (cliente.getSegmento().equalsIgnoreCase("industria")) {
                        rgsegmento.check(R.id.rbindustria);
                    }
                }
                //endereco
                edtlogradouto.setText(cliente.getLogradouro());
                edtnumero.setText(cliente.getNumero());
                edtbairro.setText(cliente.getBairro());
                edtcidade.setText(cliente.getCidade());
                edttelefone.setText("");
                edttelefone.setText(cliente.getTelefone());
                edtcelular.setText("");
                edtcelular.setText(cliente.getCelular());
                edtemail.setText(cliente.getEmail());
                // vendedor
                if (cliente.getVendedor().getIdvendedor() == null) {
                    edtvendedor.setText("");
                    edtsupervidor.setText("");
                } else {
                    String idvend = cliente.getVendedor().getIdvendedor();
                    Vendedor vendedor = vendedorDao.getVendedor(getContext(), idvend);
                    edtvendedor.setText(vendedor.getNomeVendedor());

                    if (vendedor.getSupervisor().getIdsupervisor() != null) {
                        String idsup = vendedor.getSupervisor().getIdsupervisor();
                        setSupervisor(idsup);
                    }
                }
                rgmarca.clearCheck();
                edtmarca.setText("");
                edtmarca.setEnabled(false);
                if (cliente.getUsaOutraFarinha() != null) {
                    if (cliente.getUsaOutraFarinha().equalsIgnoreCase("true")) {
                        rgmarca.check(R.id.rgmarcasim);
                        edtmarca.setEnabled(true);
                        edtmarca.setText(cliente.getMarcaFarinha());
                    } else if (cliente.getUsaOutraFarinha().equalsIgnoreCase("false")) {
                        rgmarca.check(R.id.rgmarcanao);
                        edtmarca.setText("");
                        edtmarca.setEnabled(false);
                    }
                }
                EditText edtnomecontato = getActivity().findViewById(R.id.edtnomecontato);
                edtnomecontato.setText(cliente.getNomeContato());
            }
        });
    }

    /**
     * auto complete vendedores
     */
    private void autoCompleteVendedor() {

        List<Vendedor> vendedores = vendedorDao.getAll(getContext());

        vendedoresAdapter = new VendedoresAdapter(
                getContext(),
                R.layout.fragment_one,
                R.id.lbl_name,
                vendedores);

        edtvendedor.setAdapter(vendedoresAdapter);
        edtvendedor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Vendedor vendedor = vendedoresAdapter.getItem(position);
                if (vendedor.getSupervisor().getIdsupervisor() != null) {
                    String idsup = vendedor.getSupervisor().getIdsupervisor();
                    setSupervisor(idsup);
                } else {
                    edtsupervidor.setText("");
                }
            }
        });
    }

    /**
     * auto complete supervisores
     */
    private void autoCompleteSupervisor() {

        ArrayAdapter<String> supervisoresAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                getSupervisores());

        edtsupervidor.setDropDownBackgroundResource(R.color.colorWhite);
        edtsupervidor.setAdapter(supervisoresAdapter);
    }

    /**
     * @return
     */
    private String[] getSupervisores() {

        SupervisorDao supervisorDao = new SupervisorDao();

        List<Supervisor> lst = supervisorDao.getAll(getContext());

        int tam = lst.size();
        String vt[] = new String[tam];

        for (int i = 0; i < tam; i++) {
            vt[i] = lst.get(i).getNomeSupervisor();
        }

        return vt;
    }

    /**
     * @param idsupervisor
     */
    private void setSupervisor(String idsupervisor) {
        Supervisor supervisor = supervisorDao.getSupervisor(getContext(), idsupervisor);
        edtsupervidor.setText(supervisor.getNomeSupervisor());
    }
}
