package com.celio.fac;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.celio.fac.adapters.SectionsPagerAdapter;
import com.celio.fac.dao.ClienteDao;
import com.celio.fac.dao.IngredienteDao;
import com.celio.fac.dao.MassaDao;
import com.celio.fac.dao.ProdutoDao;
import com.celio.fac.dao.ReceitaDao;
import com.celio.fac.dao.SupervisorDao;
import com.celio.fac.dao.TecnicoDao;
import com.celio.fac.dao.VendedorDao;
import com.celio.fac.entities.Clientes;
import com.celio.fac.entities.Ficha;
import com.celio.fac.entities.Ingrediente;
import com.celio.fac.entities.Massa;
import com.celio.fac.entities.Produto;
import com.celio.fac.entities.Receita;
import com.celio.fac.entities.Supervisor;
import com.celio.fac.entities.Tecnico;
import com.celio.fac.entities.Vendedor;
import com.celio.fac.utils.Mask;
import com.celio.fac.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Boolean isFabOpen = false;
    private FloatingActionButton fab;
    private LinearLayout fab1, fab2, fab3, fab4, fab5;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    private ViewPager mViewPager;

    private final int FOTOS_PADARIA = 1;
    private final int FOTOS_MAQUINARIO = 2;
    private final int FOTOS_PRODUTO = 3;
    private final int FOTOS_CONCLUSAO = 4;

    public final int FIELD_TYPE_RADIO = 2;
    public final int FIELD_TYPE_CHECKBOX = 3;

    private List<Uri> lstPadaria;
    private List<Uri> lstMaquinario;
    private List<Uri> lstProduto;
    private List<Uri> lstConclusao;

    private ProgressDialog progressDoalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        init();

    }

    /**
     *
     */
    private void init() {
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        mViewPager.setAdapter(pagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab5 = findViewById(R.id.fab5);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        fab5.setOnClickListener(this);

        lstPadaria = new LinkedList<>();
        lstMaquinario = new LinkedList<>();
        lstProduto = new LinkedList<>();
        lstConclusao = new LinkedList<>();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                createPdf();
                break;
            case R.id.fab2:
                setImages(R.id.fab2);
                break;
            case R.id.fab3:
                setImages(R.id.fab3);
                break;
            case R.id.fab4:
                setImages(R.id.fab4);
                break;
            case R.id.fab5:
                setImages(R.id.fab5);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * @param id
     */
    public void setImages(int id) {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        switch (id) {
            case R.id.fab2: {
                lstPadaria.clear();
                startActivityForResult(i, FOTOS_PADARIA);
                break;
            }
            case R.id.fab3: {
                lstMaquinario.clear();
                startActivityForResult(i, FOTOS_MAQUINARIO);
                break;
            }
            case R.id.fab4: {
                lstProduto.clear();
                startActivityForResult(i, FOTOS_PRODUTO);
                break;
            }
            case R.id.fab5: {
                lstConclusao.clear();
                startActivityForResult(i, FOTOS_CONCLUSAO);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (data.getClipData() != null) {

                int count = data.getClipData().getItemCount();
                int currentItem = 0;

                switch (requestCode) {

                    case FOTOS_PADARIA: {
                        while (currentItem < count) {
                            Uri imgUri = data.getClipData().getItemAt(currentItem).getUri();
                            lstPadaria.add(imgUri);
                            currentItem++;
                        }
                        break;
                    }

                    case FOTOS_MAQUINARIO: {
                        while (currentItem < count) {
                            Uri imgUri = data.getClipData().getItemAt(currentItem).getUri();
                            lstMaquinario.add(imgUri);
                            currentItem++;
                        }
                        break;
                    }

                    case FOTOS_PRODUTO: {
                        while (currentItem < count) {
                            Uri imgUri = data.getClipData().getItemAt(currentItem).getUri();
                            lstProduto.add(imgUri);
                            currentItem++;
                        }
                        break;
                    }

                    case FOTOS_CONCLUSAO: {
                        while (currentItem < count) {
                            Uri imgUri = data.getClipData().getItemAt(currentItem).getUri();
                            lstConclusao.add(imgUri);
                            currentItem++;
                        }
                        break;
                    }
                }
            } else if (data.getData() != null) {

                switch (requestCode) {

                    case FOTOS_PADARIA: {
                        Uri imgUri = data.getData();
                        lstPadaria.add(imgUri);
                        break;
                    }

                    case FOTOS_MAQUINARIO: {
                        Uri imgUri = data.getData();
                        lstMaquinario.add(imgUri);
                        break;
                    }

                    case FOTOS_PRODUTO: {
                        Uri imgUri = data.getData();
                        lstProduto.add(imgUri);
                        break;
                    }

                    case FOTOS_CONCLUSAO: {
                        Uri imgUri = data.getData();
                        lstConclusao.add(imgUri);
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void animateFAB() {

        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab5.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            fab5.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab5.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            fab5.setClickable(true);
            isFabOpen = true;
        }
    }

    /**
     *
     */
    public void createPdf() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setIndeterminate(true);
        progressDoalog.setMessage("Aguarde!!!, preparando o documento...");
        progressDoalog.setTitle("Processando");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        new MyTask().execute(0);
    }

    /**
     *
     */
    private void preparaFicha() {
        // DADOS DO CLIENTE
        EditText edtcliente = mViewPager.findViewById(R.id.edtcliente);
        EditText edtlogradouto = mViewPager.findViewById(R.id.edtlogradouto);
        EditText edtnumero = mViewPager.findViewById(R.id.edtnumero);
        EditText edtbairro = mViewPager.findViewById(R.id.edtbairro);
        EditText edtcidade = mViewPager.findViewById(R.id.edtcidade);
        EditText edttelefone = mViewPager.findViewById(R.id.edttelefone);
        EditText edtcelular = mViewPager.findViewById(R.id.edtcelular);
        EditText edtemail = mViewPager.findViewById(R.id.edtemail);
        EditText edtvendedor = mViewPager.findViewById(R.id.edtvendedor);
        EditText edtsupervidor = mViewPager.findViewById(R.id.edtsupervidor);
        RadioGroup rgmarca = mViewPager.findViewById(R.id.rgmarca);
        EditText edtmarca = mViewPager.findViewById(R.id.edtmarca);
        EditText edtmotivo = mViewPager.findViewById(R.id.edtmotivo);
        EditText edtpaorealizado = mViewPager.findViewById(R.id.edtpaorealizado);
        EditText edtproduto = mViewPager.findViewById(R.id.edtproduto);
        EditText edtqtdadquirida = mViewPager.findViewById(R.id.edtqtdadquirida);
        EditText edtqtdreclamada = mViewPager.findViewById(R.id.edtqtdreclamada);
        EditText edtdtvalidade = mViewPager.findViewById(R.id.edtdtvalidade);
        EditText edtlote = mViewPager.findViewById(R.id.edtlote);
        EditText edtdtatendimento = mViewPager.findViewById(R.id.edtdtatendimento);
        EditText edtdtfinalatend = mViewPager.findViewById(R.id.edtdtfinalatend);
        RadioGroup rgreacao = mViewPager.findViewById(R.id.rgreacao);
        RadioGroup rgsegmento = mViewPager.findViewById(R.id.rgsegmento);
        RadioGroup rgvendas = mViewPager.findViewById(R.id.rgvendas);
        RadioGroup rgtecnico = mViewPager.findViewById(R.id.rgtecnico);
        EditText edttecnico = mViewPager.findViewById(R.id.edttecnico);
        EditText edtdatahora = mViewPager.findViewById(R.id.edtdatahora);
        EditText edtnomecontato = mViewPager.findViewById(R.id.edtnomecontato);
        EditText edtdescinforprestadas = mViewPager.findViewById(R.id.edtdescinforprestadas);
        RadioGroup rgresolveram = mViewPager.findViewById(R.id.rgresolveram);
        EditText edtprovtomadas = mViewPager.findViewById(R.id.edtprovtomadas);
        EditText edtmassa = mViewPager.findViewById(R.id.edtmassa);
        TableLayout tbreceitacliente = mViewPager.findViewById(R.id.tbreceitacliente);
        TableLayout tbreceitasugerida = mViewPager.findViewById(R.id.tbreceitasugerida);
        CheckBox ckbconvencional = mViewPager.findViewById(R.id.ckbconvencional);
        CheckBox ckbrapida = mViewPager.findViewById(R.id.ckbrapida);
        CheckBox ckbsemirapida = mViewPager.findViewById(R.id.ckbsemirapida);
        CheckBox ckbcilindro = mViewPager.findViewById(R.id.ckbcilindro);
        EditText edtmist1veloc = mViewPager.findViewById(R.id.edtmist1veloc);
        EditText edtmist2veloc = mViewPager.findViewById(R.id.edtmist2veloc);
        EditText edttempagua = mViewPager.findViewById(R.id.edttempagua);
        EditText edttempmassa = mViewPager.findViewById(R.id.edttempmassa);
        EditText edttempforno = mViewPager.findViewById(R.id.edttempforno);
        RadioGroup rgfermentacao = mViewPager.findViewById(R.id.rgfermentacao);
        EditText edttempoferm = mViewPager.findViewById(R.id.edttempoferm);
        EditText edttempoforne = mViewPager.findViewById(R.id.edttempoforne);
        EditText edtpesobola = mViewPager.findViewById(R.id.edtpesobola);
        EditText edtpesopaocru = mViewPager.findViewById(R.id.edtpesopaocru);
        EditText edtpesopaoassado = mViewPager.findViewById(R.id.edtpesopaoassado);
        EditText edtqtdpaesbola = mViewPager.findViewById(R.id.edtqtdpaesbola);
        EditText edtqtdpaessaca = mViewPager.findViewById(R.id.edtqtdpaessaca);
        EditText edtconclusao = mViewPager.findViewById(R.id.edtconclusao);
        EditText edtobs = mViewPager.findViewById(R.id.edtobs);
        RadioGroup rgavaliacao = mViewPager.findViewById(R.id.rgavaliacao);

        String strSegmento = "";
        int segmento = rgsegmento.getCheckedRadioButtonId();
        if (segmento == R.id.rbpanificacao) {
            strSegmento = "panificacao";
        } else if (segmento == R.id.rbconfeitaria) {
            strSegmento = "confeitaria";
        } else if (segmento == R.id.rbindustria) {
            strSegmento = "industria";
        }

        String boomarca = "";
        int idmarca = rgmarca.getCheckedRadioButtonId();
        if (idmarca == R.id.rgmarcasim) {
            boomarca = "true";
        } else if (idmarca == R.id.rgmarcanao) {
            boomarca = "false";
        }

        String booreacao = "";
        int idreacao = rgreacao.getCheckedRadioButtonId();
        if (idreacao == R.id.aceitou) {
            booreacao = "aceitou";
        } else if (idreacao == R.id.recusou) {
            booreacao = "recusou";
        }

        String boovenda = "";
        int idvenda = rgvendas.getCheckedRadioButtonId();
        if (idvenda == R.id.rgvendassim) {
            boovenda = "true";
        } else if (idvenda == R.id.rgvendasnao) {
            boovenda = "false";
        }


        String bootecnico = "";
        int idtecnico = rgtecnico.getCheckedRadioButtonId();
        if (idtecnico == R.id.rbtecnicosim) {
            bootecnico = "true";
        } else if (idtecnico == R.id.rbtecniconao) {
            bootecnico = "false";
        }

        String booresolveram = "";
        int idresolveram = rgresolveram.getCheckedRadioButtonId();
        if (idresolveram == R.id.rbresolveramsim) {
            booresolveram = "true";
        } else if (idresolveram == R.id.rbresolveramnao) {
            booresolveram = "false";
        }


        // RECEITAS
        List<String[]> receitaC = new ArrayList<>();
        Double totalC = 0D;
        for (int i = 1; i < tbreceitacliente.getChildCount() - 1; i++) {
            TableRow row = (TableRow) tbreceitacliente.getChildAt(i);
            TextView txvingrediente = (TextView) row.getChildAt(0);
            TextView txvqtde = (TextView) row.getChildAt(1);
            TextView txvporc = (TextView) row.getChildAt(2);

            String vt[] = new String[3];
            vt[0] = txvingrediente.getText().toString().trim();
            vt[1] = txvqtde.getText().toString().trim();
            vt[2] = txvporc.getText().toString().trim();

            totalC += Double.parseDouble(vt[1]);

            receitaC.add(vt);
        }

        List<String[]> receitaS = new ArrayList<>();
        Double totalS = 0D;
        for (int i = 1; i < tbreceitasugerida.getChildCount() - 1; i++) {
            TableRow row = (TableRow) tbreceitasugerida.getChildAt(i);
            TextView txvingrediente = (TextView) row.getChildAt(0);
            TextView txvqtde = (TextView) row.getChildAt(1);
            TextView txvporc = (TextView) row.getChildAt(2);

            String vt[] = new String[3];
            vt[0] = txvingrediente.getText().toString().trim();
            vt[1] = txvqtde.getText().toString().trim();
            vt[2] = txvporc.getText().toString().trim();

            totalS += Double.parseDouble(vt[1]);

            receitaS.add(vt);
        }

        String boofermentacao = "";
        int idfermentacao = rgfermentacao.getCheckedRadioButtonId();
        if (idfermentacao == R.id.rbcurta) {
            boofermentacao = "curta";
        } else if (idfermentacao == R.id.rblonga) {
            boofermentacao = "longa";
        }

        String booavaliacao = "";
        int idavaliacao = rgavaliacao.getCheckedRadioButtonId();
        if (idavaliacao == R.id.rbsatisfeito) {
            booavaliacao = "satisfeito";
        } else if (idavaliacao == R.id.rbinsatisfeito) {
            booavaliacao = "insatisfeito";
        }

        Ficha ficha = new Ficha();
        ficha.setSegmento(Utils.getSemento(strSegmento));
        ficha.setNomeCliente(edtcliente.getText().toString().trim());
        ficha.setLogradouro(edtlogradouto.getText().toString().trim());
        ficha.setNumero(edtnumero.getText().toString().trim());
        ficha.setBairro(edtbairro.getText().toString().trim());
        ficha.setCidade(edtcidade.getText().toString().trim());
        ficha.setTelefone(edttelefone.getText().toString().trim());
        ficha.setCelular(edtcelular.getText().toString().trim());
        ficha.setEmail(edtemail.getText().toString().trim());
        ficha.setVendedor(edtvendedor.getText().toString().trim());
        ficha.setSupervisor(edtsupervidor.getText().toString().trim());
        ficha.setOutraFarinha(Utils.getSimNao(boomarca));
        ficha.setNomeMarca(edtmarca.getText().toString().trim());
        ficha.setMotivoSilicitacao(edtmotivo.getText().toString().trim());
        ficha.setTiposPaes(edtpaorealizado.getText().toString().trim());
        ficha.setProduto(edtproduto.getText().toString().trim());
        ficha.setQtdAdquirida(edtqtdadquirida.getText().toString().trim());
        ficha.setQtdReclamada(edtqtdreclamada.getText().toString().trim());
        ficha.setValidadeLote(edtdtvalidade.getText().toString().trim() + " "
                + edtlote.getText().toString().trim());
        ficha.setDtAtendimento(edtdtatendimento.getText().toString().trim());
        ficha.setDtFinalizacao(edtdtfinalatend.getText().toString().trim());
        ficha.setReacaoAssistencia(booreacao);
        ficha.setVisitaTecnica(Utils.getSimNao(bootecnico));
        ficha.setVisitaVenda(Utils.getSimNao(boovenda));
        ficha.setNomeTecnico(edttecnico.getText().toString().trim());
        ficha.setDhTenico(edtdatahora.getText().toString().trim());
        ficha.setNomeContato(edtnomecontato.getText().toString().trim());
        ficha.setDescInformacoes(edtdescinforprestadas.getText().toString().trim());
        ficha.setResolveramProblema(Utils.getSimNao(booresolveram));
        ficha.setProvidencias(edtprovtomadas.getText().toString().trim());
        ficha.setMassa(edtmassa.getText().toString().trim());
        ficha.setReceitaCliente(receitaC);
        ficha.setTotalCliente(Utils.aredondaValor(totalC.toString(), 3));
        ficha.setReceitaSugerida(receitaS);
        ficha.setTotalSugerida(Utils.aredondaValor(totalS.toString(), 3));
        ficha.setConvencional(ckbconvencional.isChecked());
        ficha.setSemirapida(ckbsemirapida.isChecked());
        ficha.setRapida(ckbrapida.isChecked());
        ficha.setCilindro(ckbcilindro.isChecked());
        ficha.setVelocidadeA(edtmist1veloc.getText().toString().trim());
        ficha.setVelocidadeB(edtmist2veloc.getText().toString().trim());
        ficha.setTempAgua(edttempagua.getText().toString().trim());
        ficha.setTempMassa(edttempmassa.getText().toString().trim());
        ficha.setTempForno(edttempforno.getText().toString().trim());
        ficha.setFermentacao(boofermentacao);
        ficha.setTempoFermentacao(edttempoferm.getText().toString().trim());
        ficha.setTempoForneamento(edttempoforne.getText().toString().trim());
        ficha.setPesoBola(edtpesobola.getText().toString().trim());
        ficha.setPesoPaoCru(edtpesopaocru.getText().toString().trim());
        ficha.setPesoPaoAssado(edtpesopaoassado.getText().toString().trim());
        ficha.setQtdPaesSaca(edtqtdpaessaca.getText().toString().trim());
        ficha.setConclusao(edtconclusao.getText().toString().trim());
        ficha.setObservacao(edtobs.getText().toString().trim());
        ficha.setAvaliacao(booavaliacao);
        ficha.setQtdePaesBola(edtqtdpaesbola.getText().toString().trim());

        persist(ficha);
        setDocument(ficha);
    }

    /**
     * persist os dados no banco
     *
     * @param ficha
     */
    private void persist(Ficha ficha) {
        long idsupervisor = 0;

        if (!ficha.getSupervisor().isEmpty()) {
            SupervisorDao supervisorDao = new SupervisorDao();
            String nomeSupervisor = ficha.getSupervisor();
            Supervisor supervisor = supervisorDao.getSupervisorByNome(this, nomeSupervisor);

            if (supervisor == null) {
                supervisor = new Supervisor();
                supervisor.setNomeSupervisor(Utils.removerAcento(nomeSupervisor));
                idsupervisor = supervisorDao.createSupervidor(this, supervisor);
            } else {
                idsupervisor = Long.parseLong(supervisor.getIdsupervisor());
            }
        }

        long idvendedor = 0;

        if (!ficha.getVendedor().isEmpty()) {
            VendedorDao vendedorDao = new VendedorDao();
            String nomeVendedor = ficha.getVendedor();
            Vendedor vendedor = vendedorDao.getVendedorByNome(this, nomeVendedor);
            if (vendedor == null) {
                vendedor = new Vendedor();
                if (idsupervisor > 0) {
                    vendedor.setSupervisor(new Supervisor());
                    vendedor.getSupervisor().setIdsupervisor(String.valueOf(idsupervisor));
                }
                vendedor.setNomeVendedor(Utils.removerAcento(nomeVendedor));
                idvendedor = vendedorDao.createVendedor(this, vendedor);
            } else {
                idvendedor = Long.parseLong(vendedor.getIdvendedor());
                if (idsupervisor > 0) {
                    vendedor.setSupervisor(new Supervisor());
                    vendedor.getSupervisor().setIdsupervisor(String.valueOf(idsupervisor));
                } else {
                    vendedor.setSupervisor(null);
                }
                vendedorDao.updateVendedor(this, vendedor);
            }

        }

        if (!ficha.getNomeCliente().isEmpty()) {
            ClienteDao clienteDao = new ClienteDao();
            String nomeCliente = ficha.getNomeCliente();
            Clientes cliente = clienteDao.getClienteByName(this, nomeCliente);
            boolean isCriar = false;
            if (cliente == null) {
                isCriar = true;
                cliente = new Clientes();
                cliente.setNomeCliente(Utils.removerAcento(nomeCliente));
            }
            cliente.setSegmento(Utils.getSementoBD(ficha.getSegmento()));
            cliente.setLogradouro(ficha.getLogradouro());
            cliente.setNumero(ficha.getNumero());
            cliente.setBairro(ficha.getBairro());
            cliente.setCidade(ficha.getCidade());
            cliente.setTelefone(Mask.unmask(ficha.getTelefone()));
            cliente.setCelular(Mask.unmask(ficha.getCelular()));
            cliente.setEmail(ficha.getEmail());

            if (idvendedor > 0) {
                cliente.setVendedor(new Vendedor());
                cliente.getVendedor().setIdvendedor(String.valueOf(idvendedor));
            } else {
                cliente.setVendedor(null);
            }
            cliente.setUsaOutraFarinha(Utils.getTrueFalse(ficha.getOutraFarinha()));
            cliente.setMarcaFarinha(ficha.getNomeMarca());
            cliente.setNomeContato(ficha.getNomeContato());
            if (isCriar) {
                clienteDao.createCliente(this, cliente);
            } else {
                clienteDao.updateCliente(this, cliente);
            }
        }

        // produto
        if (!ficha.getProduto().isEmpty()) {
            ProdutoDao produtoDao = new ProdutoDao();
            String nomeProduto = ficha.getProduto();
            Produto produto = produtoDao.getProdutoByNome(this, nomeProduto);
            if (produto == null) {
                produto = new Produto();
                produto.setNomeProduto(Utils.removerAcento(nomeProduto));
                produtoDao.createProduto(this, produto);
            }
        }

        // tecnico
        if (!ficha.getNomeTecnico().isEmpty()) {
            TecnicoDao tecnicoDao = new TecnicoDao();
            String nomeTecnico = ficha.getNomeTecnico();
            Tecnico tecnico = tecnicoDao.getTecnicoByNome(this, nomeTecnico);
            if (tecnico == null) {
                tecnico = new Tecnico();
                tecnico.setNomeTecnico(Utils.removerAcento(nomeTecnico));
                tecnicoDao.createTecnico(this, tecnico);
            }
        }

        // massa
        if (!ficha.getMassa().isEmpty()) {
            MassaDao massaDao = new MassaDao();
            String nomeMassa = ficha.getMassa();
            Massa massa = massaDao.getMassaByNome(this, nomeMassa);

            long idmassa = 0;
            boolean isExiste = true;
            if (massa == null) {
                isExiste = false;
                massa = new Massa();
                massa.setNomeMassa(Utils.removerAcento(nomeMassa));
            }
            massa.setVelocidadeA(ficha.getVelocidadeA());
            massa.setVelocidadeB(ficha.getVelocidadeB());
            massa.setTempAgua(ficha.getTempAgua());
            massa.setTempMassa(ficha.getTempMassa());
            massa.setTempForno(ficha.getTempForno());
            massa.setFermentacao(ficha.getFermentacao());
            massa.setTempoFermentacao(ficha.getTempoFermentacao());
            massa.setTempoForneamento(ficha.getTempoForneamento());
            massa.setPesoBola(ficha.getPesoBola());
            massa.setPesoPaoCru(ficha.getPesoPaoCru());
            massa.setPesoPaoAssado(ficha.getPesoPaoAssado());
            massa.setQtdePaesBola(ficha.getQtdePaesBola());
            if (!isExiste) {
                idmassa = massaDao.createMassa(this, massa);
            } else {
                massaDao.updateMassa(this, massa);
                idmassa = Long.parseLong(massa.getIdmassa());
            }

            //ingrediente e receita
            List<String[]> receitaS = ficha.getReceitaSugerida();
            if (!receitaS.isEmpty() && idmassa > 0) {

                IngredienteDao ingredienteDao = new IngredienteDao();
                ReceitaDao receitaDao = new ReceitaDao();

                List<Receita> lst = receitaDao.getReceitaByMassa(this, nomeMassa);
                for (int i = 0; i < receitaS.size(); i++) {
                    // verifico se ja existe na receita
                    boolean achou = false;
                    String novo = receitaS.get(i)[0];
                    if (!novo.equalsIgnoreCase("farinha")) {
                        for (int j = 0; j < lst.size(); j++) {
                            String old = lst.get(j).getIngrediente().getNomeIngrediente();
                            if (old.equalsIgnoreCase(novo)) {
                                achou = true;
                                Receita receita = lst.get(j);
                                receita.setPorcentagem(receitaS.get(i)[2]);
                                receitaDao.updateReceita(this, receita);
                                lst.remove(j);
                                break;
                            }
                        }
                        if (!achou) {
                            Ingrediente ingrediente = ingredienteDao.getIngredienteByNome(this, novo);
                            long idingrediente;
                            if (ingrediente == null) {
                                ingrediente = new Ingrediente();
                                ingrediente.setNomeIngrediente(Utils.removerAcento(novo));
                                idingrediente = ingredienteDao.createIngrediente(this, ingrediente);
                            } else {
                                idingrediente = Long.parseLong(ingrediente.getIdingrediente());
                            }
                            if (idmassa > 0 && idingrediente > 0) {
                                Receita receita = new Receita();
                                receita.setMassa(new Massa());
                                receita.getMassa().setIdmassa(String.valueOf(idmassa));
                                receita.setIngrediente(new Ingrediente());
                                receita.getIngrediente().setIdingrediente(String.valueOf(idingrediente));
                                receita.setPorcentagem(receitaS.get(i)[2]);

                                receitaDao.createReceita(this, receita);
                            }
                        }
                    }
                }
                if (!lst.isEmpty()) {
                    for (int r = 0; r < lst.size(); r++) {
                        receitaDao.deleteReceita(this, lst.get(r).getIdreceita());
                    }
                }
            }
        }

    }

    /**
     * gerando o pdf
     */
    private void setDocument(Ficha ficha) {
        try {

            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File f = new File(dir, "formulario.pdf");

            if (f.exists()) {
                f.delete();
            }

            Document document = new Document();
            document.setMargins(9, 9, 9, 9);
            //Step 2
            FileOutputStream output = new FileOutputStream(f);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, output);
            //Step 3
            document.open();
            // metadados do documento
            document.addTitle("Visita Técnica");
            document.addAuthor("Jerry Martins");
            document.addSubject("Formulário de Visita Técnica");
            document.addCreator("iText");

            Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

            PdfPTable table = new PdfPTable(39);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            Drawable drawable = ResourcesCompat.getDrawableForDensity(getResources(),
                    R.mipmap.ic_ocrim, DisplayMetrics.DENSITY_XXXHIGH, getTheme());

            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bmp = bitmapDrawable.getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = Image.getInstance(stream.toByteArray());
            img.scaleAbsolute(50f, 50f);

            PdfPCell cell = new PdfPCell(img);
            cell.setColspan(7);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("FAC – Ficha de Atendimento ao Cliente"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(32);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" "));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("DADOS DO CLIENTE"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(39);
            table.addCell(cell);

            ///////// segmento
            cell = new PdfPCell(new Paragraph("Segmento do Cliente", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            PdfFormField radiogroupField = PdfFormField.createRadioButton(pdfWriter, true);
            radiogroupField.setFieldName("radioGroup");
            radiogroupField.setFieldFlags(PdfFormField.FF_READ_ONLY);

            String segmentos[] = {"Panificação", "Confeitaria", "Indústria"};

            for (int i = 0; i < 3; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getSegmento().equalsIgnoreCase(segmentos[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(segmentos[i], font));
                cell.setColspan(8);
                cell.setBorderWidthLeft(-1);
                if (i < 2) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// fim segmento

            cell = new PdfPCell(new Paragraph("Cliente", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(9);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getNomeCliente(), font));
            cell.setColspan(30);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Endereço completo"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Logradouro", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getLogradouro(), font));
            cell.setColspan(25);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Nº", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getNumero(), font));
            cell.setColspan(7);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Bairro", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getBairro(), font));
            cell.setColspan(16);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Cidade", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getCidade(), font));
            cell.setColspan(15);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("DDD + Telefone", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTelefone(), font));
            cell.setColspan(12);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("DDD + Celular", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getCelular(), font));
            cell.setColspan(13);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("email", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getEmail(), font));
            cell.setColspan(34);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Vendedor", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getVendedor(), font));
            cell.setColspan(13);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Supervisor", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getSupervisor(), font));
            cell.setColspan(12);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            //// utiliza farinha de outra marca
            cell = new PdfPCell(new Paragraph("Utiliza farinha de outra marca?", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            String yesno[] = {"Sim", "Não"};

            for (int i = 0; i < 2; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getOutraFarinha().equalsIgnoreCase(yesno[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(yesno[i], font));
                cell.setColspan(3);
                cell.setBorderWidthLeft(-1);
                if (i < 1) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// yesno

            cell = new PdfPCell(new Paragraph("Nome da marca – farinha utilizada", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getNomeMarca(), font));
            cell.setColspan(9);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Motivo da solicitação", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setFixedHeight(38f);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getMotivoSilicitacao(), font));
            cell.setColspan(28);
            cell.setFixedHeight(38f);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("Tipo(s) de pão(es) realizado(s)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(11);
            cell.setFixedHeight(38f);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTiposPaes(), font));
            cell.setColspan(28);
            cell.setFixedHeight(38f);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("IDENTIFICAÇÃO DO PRODUTO"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Produto", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(9);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getProduto(), font));
            cell.setColspan(30);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantidade adquirida", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getQtdAdquirida(), font));
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantidade reclamada", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getQtdReclamada(), font));
            cell.setColspan(4);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Date de validade / Lote", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getValidadeLote(), font));
            cell.setColspan(10);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ATENDIMENTO"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Nº do atendimento", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(6);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Data do atendimento", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(8);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getDtAtendimento(), font));
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Data finalização do atendimento", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(10);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getDtFinalizacao(), font));
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("(SAC) Reação do cliente à ", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setBorderWidthBottom(-1);
            cell.setColspan(13);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                    "radioGroupCell0", FIELD_TYPE_RADIO,
                    ficha.getReacaoAssistencia().equalsIgnoreCase("Aceitou")));
            cell.setBorderWidthRight(-1);
            cell.setBorderWidthBottom(-1);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Aceitou", font));
            cell.setBorderWidthBottom(-1);
            cell.setColspan(7);
            cell.setBorderWidthLeft(-1);
            cell.setBorderWidthRight(-1);
            cell.setBorderWidthBottom(-1);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Solicita visita técnica?", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(10);
            table.addCell(cell);


            for (int i = 0; i < 2; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getVisitaTecnica().equalsIgnoreCase(yesno[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(yesno[i], font));
                cell.setColspan(3);
                cell.setBorderWidthLeft(-1);
                if (i < 1) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// yesno

            cell = new PdfPCell(new Paragraph("assistência", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_TOP);
            cell.setBorderWidthTop(-1);
            cell.setColspan(13);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                    "radioGroupCell1", FIELD_TYPE_RADIO,
                    ficha.getReacaoAssistencia().equalsIgnoreCase("Recusou")));
            cell.setBorderWidthRight(-1);
            cell.setBorderWidthTop(-1);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Recusou", font));
            cell.setColspan(7);
            cell.setBorderWidthLeft(-1);
            cell.setBorderWidthRight(-1);
            cell.setBorderWidthTop(-1);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Solicita visita de vendas?", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(10);
            table.addCell(cell);


            for (int i = 0; i < 2; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getVisitaVenda().equalsIgnoreCase(yesno[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(yesno[i], font));
                cell.setColspan(3);
                cell.setBorderWidthLeft(-1);
                if (i < 1) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// yesno

            cell = new PdfPCell(new Paragraph("Técnico", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(6);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getNomeTecnico(), font));
            cell.setColspan(16);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Data/Hora", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getDhTenico(), font));
            cell.setColspan(13);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Nome do contato", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(9);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getNomeContato(), font));
            cell.setColspan(30);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Descrição das informações prestadas", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(12);
            cell.setFixedHeight(38);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getDescInformacoes(), font));
            cell.setColspan(27);
            cell.setFixedHeight(38);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Instruções passadas resolveram o problema?", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(23);
            table.addCell(cell);


            for (int i = 0; i < 2; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getResolveramProblema().equalsIgnoreCase(yesno[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(yesno[i], font));
                cell.setColspan(7);
                cell.setBorderWidthLeft(-1);
                if (i < 1) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// yesno

            cell = new PdfPCell(new Paragraph("Providências tomadas", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(12);
            cell.setFixedHeight(38);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getProvidencias(), font));
            cell.setColspan(27);
            cell.setFixedHeight(38);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("RECEITAS"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Produto", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(9);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getMassa(), font));
            cell.setColspan(30);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Receita do Cliente", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(19);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Receita Sugerida", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(20);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Ingredientes", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantidade", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("%", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Ingredientes", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(13);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Quantidade", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("%", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);

            List<String[]> rc = ficha.getReceitaCliente();
            int idxCliente = 0;
            List<String[]> rs = ficha.getReceitaSugerida();
            int idxSugerida = 0;

            for (int i = 0; i < 10; i++) {

                if (idxCliente >= rc.size()) {
                    // cliente
                    cell = new PdfPCell(new Paragraph(" ", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(12);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(" ", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(" ", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(3);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Paragraph(rc.get(idxCliente)[0], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(12);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(rc.get(idxCliente)[1], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(rc.get(idxCliente)[2], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(3);
                    table.addCell(cell);
                    idxCliente++;
                }
                if (idxSugerida >= rs.size()) {
                    // sugerida
                    cell = new PdfPCell(new Paragraph(" ", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(13);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(" ", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(" ", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(3);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Paragraph(rs.get(idxSugerida)[0], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(13);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(rs.get(idxSugerida)[1], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(4);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(rs.get(idxSugerida)[2], font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(3);
                    table.addCell(cell);
                    idxSugerida++;
                }
            }
            cell = new PdfPCell(new Paragraph("Total", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTotalCliente(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Total", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(13);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTotalSugerida(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("PROCESSO APLICADO"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Tipo de masseira", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            String masseiras[] = {"Convencional", "Semi-rápida", "Rápida", "Cilindro"};
            boolean checados[] = {ficha.isConvencional(), ficha.isSemirapida(), ficha.isRapida(), ficha.isCilindro()};

            for (int i = 0; i < 4; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_CHECKBOX, checados[i]));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(masseiras[i], font));
                cell.setColspan(6);
                cell.setBorderWidthLeft(-1);
                if (i < 3) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// fim masseira
            // primiera linha
            cell = new PdfPCell(new Paragraph("Mistura – 1ª velocidade", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getVelocidadeA(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Minutos", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Tempo de fermentação", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTempoFermentacao(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Horas", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
            // fim primeira linha

            // segunda linha
            cell = new PdfPCell(new Paragraph("Mistura – 2ª velocidade", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getVelocidadeB(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Minutos", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Tempo de forneamento", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTempoForneamento(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Minutos", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
            // fim segunda linha

            // terceira linha
            cell = new PdfPCell(new Paragraph("Temperatura da água", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTempAgua(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ºC", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Peso da bola", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getPesoBola(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Kg", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
            // fim terceira linha
            // quarta linha
            cell = new PdfPCell(new Paragraph("Temperatura da massa", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTempMassa(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ºC", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Peso do pão cru", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getPesoPaoCru(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("gr", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
            // fim quarta linha
            // quinta linha
            cell = new PdfPCell(new Paragraph("Temperatura do forno", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getTempForno(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ºC", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Peso do pão assado", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getPesoPaoAssado(), font));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("gr", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
            // fim quinta linha
            // ultima linha
            cell = new PdfPCell(new Paragraph("Fermentação", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(11);
            table.addCell(cell);

            String fermentacao[] = {"Curta", "Longa"};

            for (int i = 0; i < 2; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getFermentacao().equalsIgnoreCase(fermentacao[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(fermentacao[i], font));
                cell.setColspan(3);
                cell.setBorderWidthLeft(-1);
                if (i < 1) {
                    cell.setBorderWidthRight(-1);
                }
                table.addCell(cell);
            }
            ///////// curtalonga

            cell = new PdfPCell(new Paragraph("Quantidade de pães por saca de 50kg", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(12);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getQtdPaesSaca(), font));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("un", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(3);
            table.addCell(cell);
            // fim ultima linha

            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("CONCLUSÃO TÉCNICA"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(39);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getConclusao(), font));
            cell.setColspan(39);
            cell.setFixedHeight(38);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);

            // fim table
            document.add(table);
            pdfWriter.addAnnotation(radiogroupField);
            //Step 4 Add content
            document.newPage();
            document.add(getTableImages(lstPadaria, "FOTOS DA FACHADA"));
            document.add(getTableImages(lstMaquinario, "FOTOS DOS MAQUINÁRIOS"));
            document.add(getTableImages(lstProduto, "FOTOS DOS PRODUTOS"));
            document.add(getTableImages(lstConclusao, "FOTOS DA CONCLUSÃO TÉCNICA"));

            PdfPTable avaliacao = new PdfPTable(39);
            avaliacao.setWidthPercentage(100);
            avaliacao.setHorizontalAlignment(Element.ALIGN_LEFT);

            cell = new PdfPCell(new Paragraph("AVALIAÇÃO DO CLIENTE"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(23);
            avaliacao.addCell(cell);

            String avaliacaocliente[] = {"Satisfeito", "Insatisfeito"};

            for (int i = 0; i < 2; i++) {
                cell = new PdfPCell();
                cell.setCellEvent(new MyCellField(pdfWriter, radiogroupField,
                        "radioGroupCell" + i, FIELD_TYPE_RADIO,
                        ficha.getAvaliacao().equalsIgnoreCase(avaliacaocliente[i])));
                cell.setBorderWidthRight(-1);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                }
                avaliacao.addCell(cell);

                cell = new PdfPCell(new Paragraph(avaliacaocliente[i], font));
                cell.setColspan(7);
                cell.setBorderWidthLeft(-1);
                if (i < 1) {
                    cell.setBorderWidthRight(-1);
                }
                avaliacao.addCell(cell);
            }
            ///////// curtalonga
            cell = new PdfPCell(new Paragraph(" ", font));
            cell.setFixedHeight(2f);
            cell.setColspan(39);
            avaliacao.addCell(cell);

            cell = new PdfPCell(new Paragraph("Observações"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(new BaseColor(0, 153, 153));
            cell.setColspan(13);
            cell.setFixedHeight(38);
            avaliacao.addCell(cell);

            cell = new PdfPCell(new Paragraph(ficha.getObservacao(), font));
            cell.setColspan(26);
            cell.setFixedHeight(38);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            avaliacao.addCell(cell);

            document.add(avaliacao);
            pdfWriter.addAnnotation(radiogroupField);

            // Close the document
            document.close();
            // enviando documento via whatsapp
            Uri u = FileProvider.getUriForFile(MainActivity.this, "com.celio.fac.fileprovider", f);
            sendDocument(u);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * enviando documento via whatsapp
     *
     * @param u
     */
    private void sendDocument(Uri u) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_STREAM, u);
        i.setType("application/pdf");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setPackage("com.whatsapp");
        startActivity(i);
    }

    /**
     * @param lst
     * @return
     * @throws IOException
     * @throws BadElementException
     */
    private PdfPTable getTableImages(List<Uri> lst, String titulo) throws IOException, BadElementException {

        int cols = lst.size();

        if (cols > 4) {
            cols = 4;
        } else if (cols == 0) {
            cols = 1;
        }

        PdfPTable t = new PdfPTable(cols);
        t.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Paragraph(titulo));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(0, 153, 153));
        cell.setColspan(cols);
        t.addCell(cell);

        if (lst.size() == 0) {
            cell = new PdfPCell(new Paragraph(" "));
            cell.setFixedHeight(165);

            t.addCell(cell);
        } else {
            for (int i = 0; i < cols; i++) {

                Uri u1 = lst.get(i);

                InputStream is = this.getContentResolver().openInputStream(u1);
                Bitmap b = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 30, s);
                is.close();

                Image img = Image.getInstance(s.toByteArray());
                if (img.getWidth() > img.getHeight()) {
                    img.scaleAbsolute(154, 120);
                } else {
                    img.scaleAbsolute(120, 154);
                }
                cell = new PdfPCell(img);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setFixedHeight(165);
                if (i > 0) {
                    cell.setBorderWidthLeft(-1);
                    if (i < cols - 1) {
                        cell.setBorderWidthRight(-1);
                    }
                } else {
                    cell.setBorderWidthRight(-1);
                }

                t.addCell(cell);
            }
        }
        return t;
    }

    /**
     *
     */
    class MyTask extends AsyncTask<Integer, Integer, String> {


        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
                preparaFicha();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDoalog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDoalog.setMessage("Aguarde!!!, preparando o documento...");
        }

//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            txvfotos.setText("Running..." + values[0]);
//            progressBar.setProgress(values[0]);
//        }
    }

    /**
     * classe de criacao de radiobuton e checkbox
     */
    class MyCellField implements PdfPCellEvent {

        private PdfFormField parent;
        private String partialFieldName;
        private PdfWriter writer;
        private int type;
        private boolean checked;

        /**
         * @param writer
         * @param parent
         * @param name
         * @param type
         * @param checked
         */
        public MyCellField(PdfWriter writer, PdfFormField parent, String name, int type, boolean checked) {
            this.writer = writer;
            this.parent = parent;
            this.partialFieldName = name;
            this.type = type;
            this.checked = checked;
        }


        /**
         * @param cell
         * @param rect
         * @param cb
         */
        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {
            try {
                if (FIELD_TYPE_RADIO == type) {
                    createRadioField(rect);
                } else if (FIELD_TYPE_CHECKBOX == type) {
                    createCheckboxField(rect);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        /**
         * @param rect
         * @throws IOException
         * @throws DocumentException
         */
        private void createRadioField(Rectangle rect) throws IOException, DocumentException {
            RadioCheckField rf = new RadioCheckField(writer, new Rectangle(rect.getLeft(2),
                    rect.getBottom(2), rect.getRight(2), rect.getTop(2)),
                    partialFieldName, "on");
            rf.setChecked(checked);
            rf.setBorderColor(GrayColor.GRAYBLACK);
            rf.setBackgroundColor(GrayColor.GRAYWHITE);
            rf.setCheckType(RadioCheckField.TYPE_CIRCLE);
            parent.addKid(rf.getRadioField());
        }

        /**
         * @param rect
         * @throws IOException
         * @throws DocumentException
         */
        private void createCheckboxField(Rectangle rect) throws IOException, DocumentException {

            RadioCheckField rf = new RadioCheckField(writer, new Rectangle(rect.getLeft(2),
                    rect.getBottom(2), rect.getRight(2), rect.getTop(2)),
                    partialFieldName, "on");

            rf.setChecked(checked);
            rf.setBorderColor(GrayColor.GRAYBLACK);
            rf.setBackgroundColor(GrayColor.GRAYWHITE);
            rf.setCheckType(RadioCheckField.TYPE_CHECK);
            parent.addKid(rf.getCheckField());
        }
    }
}
