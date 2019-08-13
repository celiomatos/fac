package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.celio.fac.dao.Entities.ClienteEntry;
import com.celio.fac.dao.Entities.IngredienteEntry;
import com.celio.fac.dao.Entities.MassaEntry;
import com.celio.fac.dao.Entities.ProdutoEntry;
import com.celio.fac.dao.Entities.ReceitaEntry;
import com.celio.fac.dao.Entities.SupervisorEntry;
import com.celio.fac.dao.Entities.VendedorEntry;
import com.celio.fac.dao.Entities.TecnicoEntry;

public class FacDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "fac.db";


    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    // clientes
    private static final String SQL_CREATE_CLIENTES =
            "CREATE TABLE " + ClienteEntry.TABLE_NAME + " (" +
                    ClienteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ClienteEntry.COLUMN_NOME_CLIENTE + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_SEGMENTO + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_LOGRADOURO + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_NUMERO + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_BAIRRO + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_CIDADE + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_TELEFONE + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_CELULAR + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_NOME_CONTATO + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_USA_OUTRA_FARINHA + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_MARCA_FARINHA + TEXT_TYPE + COMMA_SEP +
                    ClienteEntry.COLUMN_ID_VENDEDOR + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY (" + ClienteEntry.COLUMN_ID_VENDEDOR +
                    ") REFERENCES " + VendedorEntry.TABLE_NAME + "(" + VendedorEntry._ID + "));";

    // vendedores
    private static final String SQL_CREATE_VENDEDORES =
            "CREATE TABLE " + VendedorEntry.TABLE_NAME + " (" +
                    VendedorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VendedorEntry.COLUMN_NOME_VENDEDOR + TEXT_TYPE + COMMA_SEP +
                    VendedorEntry.COLUMN_ID_SUPERVISOR + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY (" + VendedorEntry.COLUMN_ID_SUPERVISOR +
                    ") REFERENCES " + SupervisorEntry.TABLE_NAME + "(" + SupervisorEntry._ID + "));";


    // supervisores
    private static final String SQL_CREATE_SUPERVISORES =
            "CREATE TABLE " + SupervisorEntry.TABLE_NAME + " (" +
                    SupervisorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SupervisorEntry.COLUMN_NOME_SUPERVISOR + TEXT_TYPE + " )";

    // tecnicos
    private static final String SQL_CREATE_TECNICOS =
            "CREATE TABLE " + TecnicoEntry.TABLE_NAME + " (" +
                    TecnicoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TecnicoEntry.COLUMN_NOME_TECNICO + TEXT_TYPE + " )";

    // produtos
    private static final String SQL_CREATE_PRODUTOS =
            "CREATE TABLE " + ProdutoEntry.TABLE_NAME + " (" +
                    ProdutoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProdutoEntry.COLUMN_NOME_PRODUTO + TEXT_TYPE + " )";

    // massas
    private static final String SQL_CREATE_MASSAS =
            "CREATE TABLE " + MassaEntry.TABLE_NAME + " (" +
                    MassaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MassaEntry.COLUMN_NOME_MASSA + TEXT_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_VELOCIDADE_A + INTEGER_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_VELOCIDADE_B + INTEGER_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_TEMP_AGUA + REAL_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_TEMP_MASSA + REAL_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_TEMP_FORNO + REAL_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_FERMENTACAO + TEXT_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_TEMPO_FERMENTACAO + INTEGER_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_TEMPO_FORNEAMENTO + INTEGER_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_PESO_BOLA + REAL_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_PESO_PAO_CRU + REAL_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_PESO_PAO_ASSADO + REAL_TYPE + COMMA_SEP +
                    MassaEntry.COLUMN_QTD_PAES_BOLA + INTEGER_TYPE + " )";

    // ingredientes
    private static final String SQL_CREATE_INGREDIENTES =
            "CREATE TABLE " + IngredienteEntry.TABLE_NAME + " (" +
                    IngredienteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    IngredienteEntry.COLUMN_NOME_INGREDIENTE + TEXT_TYPE + " )";

    // receitas
    private static final String SQL_CREATE_RECEITAS =
            "CREATE TABLE " + ReceitaEntry.TABLE_NAME + " (" +
                    ReceitaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ReceitaEntry.COLUMN_PORCENTAGEM + REAL_TYPE + COMMA_SEP +
                    ReceitaEntry.COLUMN_ID_MASSA + INTEGER_TYPE + COMMA_SEP +
                    ReceitaEntry.COLUMN_ID_INGREDIENTE + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY (" + ReceitaEntry.COLUMN_ID_MASSA +
                    ") REFERENCES " + MassaEntry.TABLE_NAME + "(" + MassaEntry._ID + ")," +
                    " FOREIGN KEY (" + ReceitaEntry.COLUMN_ID_INGREDIENTE +
                    ") REFERENCES " + IngredienteEntry.TABLE_NAME + "(" + IngredienteEntry._ID + "));";

    // ************** DROP TABLES *******************
    private static final String SQL_DELETE_CLIENTES =
            "DROP TABLE IF EXISTS " + ClienteEntry.TABLE_NAME;

    private static final String SQL_DELETE_VENDEDORES =
            "DROP TABLE IF EXISTS " + VendedorEntry.TABLE_NAME;

    private static final String SQL_DELETE_SUPERVISORES =
            "DROP TABLE IF EXISTS " + SupervisorEntry.TABLE_NAME;

    private static final String SQL_DELETE_TECNICOS =
            "DROP TABLE IF EXISTS " + TecnicoEntry.TABLE_NAME;

    private static final String SQL_DELETE_PRODUTOS =
            "DROP TABLE IF EXISTS " + ProdutoEntry.TABLE_NAME;

    private static final String SQL_DELETE_MASSAS =
            "DROP TABLE IF EXISTS " + MassaEntry.TABLE_NAME;

    private static final String SQL_DELETE_INGREDIENTES =
            "DROP TABLE IF EXISTS " + IngredienteEntry.TABLE_NAME;

    private static final String SQL_DELETE_RECEITAS =
            "DROP TABLE IF EXISTS " + ReceitaEntry.TABLE_NAME;

    public FacDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_SUPERVISORES);
        db.execSQL(SQL_CREATE_VENDEDORES);
        db.execSQL(SQL_CREATE_TECNICOS);
        db.execSQL(SQL_CREATE_CLIENTES);
        db.execSQL(SQL_CREATE_PRODUTOS);
        db.execSQL(SQL_CREATE_MASSAS);
        db.execSQL(SQL_CREATE_INGREDIENTES);
        db.execSQL(SQL_CREATE_RECEITAS);

        insertValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_RECEITAS);
        db.execSQL(SQL_DELETE_PRODUTOS);
        db.execSQL(SQL_DELETE_MASSAS);
        db.execSQL(SQL_DELETE_INGREDIENTES);

        db.execSQL(SQL_DELETE_CLIENTES);
        db.execSQL(SQL_DELETE_VENDEDORES);
        db.execSQL(SQL_DELETE_SUPERVISORES);
        db.execSQL(SQL_DELETE_TECNICOS);

        onCreate(db);
    }


    /**
     * @param db
     */
    private void insertValues(SQLiteDatabase db) {

        // produto
        ContentValues produtoValues = new ContentValues();
        produtoValues.put(ProdutoEntry.COLUMN_NOME_PRODUTO, "Farinha de Trigo Trigolar saca 50Kg");

        db.insert(ProdutoEntry.TABLE_NAME, null, produtoValues);


        // ingredientes
        ContentValues farinhaValues = new ContentValues();
        farinhaValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Farinha");

        ContentValues aguaValues = new ContentValues();
        aguaValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Agua");

        ContentValues geloValues = new ContentValues();
        geloValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Gelo");

        ContentValues salValues = new ContentValues();
        salValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Sal");

        ContentValues acucarValues = new ContentValues();
        acucarValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Acucar");

        ContentValues melhoradorValues = new ContentValues();
        melhoradorValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Melhorador");

        ContentValues fermentoValues = new ContentValues();
        fermentoValues.put(IngredienteEntry.COLUMN_NOME_INGREDIENTE, "Fermento");


        db.insert(IngredienteEntry.TABLE_NAME, null, farinhaValues);
        db.insert(IngredienteEntry.TABLE_NAME, null, aguaValues);
        db.insert(IngredienteEntry.TABLE_NAME, null, geloValues);
        db.insert(IngredienteEntry.TABLE_NAME, null, salValues);
        db.insert(IngredienteEntry.TABLE_NAME, null, acucarValues);
        db.insert(IngredienteEntry.TABLE_NAME, null, melhoradorValues);
        db.insert(IngredienteEntry.TABLE_NAME, null, fermentoValues);

    }
}
