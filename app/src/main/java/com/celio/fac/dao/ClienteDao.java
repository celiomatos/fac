package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Clientes;
import com.celio.fac.entities.Vendedor;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    /**
     * @param context
     * @param cliente
     * @return
     */
    public long createCliente(Context context, Clientes cliente) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.ClienteEntry.COLUMN_NOME_CLIENTE, cliente.getNomeCliente());
        values.put(Entities.ClienteEntry.COLUMN_SEGMENTO, cliente.getSegmento());
        values.put(Entities.ClienteEntry.COLUMN_LOGRADOURO, cliente.getLogradouro());
        values.put(Entities.ClienteEntry.COLUMN_NUMERO, cliente.getNumero());
        values.put(Entities.ClienteEntry.COLUMN_BAIRRO, cliente.getBairro());
        values.put(Entities.ClienteEntry.COLUMN_CIDADE, cliente.getCidade());
        values.put(Entities.ClienteEntry.COLUMN_CELULAR, cliente.getCelular());
        values.put(Entities.ClienteEntry.COLUMN_TELEFONE, cliente.getTelefone());
        values.put(Entities.ClienteEntry.COLUMN_EMAIL, cliente.getEmail());
        values.put(Entities.ClienteEntry.COLUMN_USA_OUTRA_FARINHA, cliente.getUsaOutraFarinha());
        values.put(Entities.ClienteEntry.COLUMN_MARCA_FARINHA, cliente.getMarcaFarinha());
        values.put(Entities.ClienteEntry.COLUMN_NOME_CONTATO, cliente.getNomeContato());
        if (cliente.getVendedor() != null && cliente.getVendedor().getIdvendedor() != null) {
            values.put(Entities.ClienteEntry.COLUMN_ID_VENDEDOR, cliente.getVendedor().getIdvendedor());
        }


        long newRowId = db.insert(Entities.ClienteEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param cliente
     * @return
     */
    public int updateCliente(Context context, Clientes cliente) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.ClienteEntry.COLUMN_NOME_CLIENTE, cliente.getNomeCliente());
        values.put(Entities.ClienteEntry.COLUMN_SEGMENTO, cliente.getSegmento());
        values.put(Entities.ClienteEntry.COLUMN_LOGRADOURO, cliente.getLogradouro());
        values.put(Entities.ClienteEntry.COLUMN_NUMERO, cliente.getNumero());
        values.put(Entities.ClienteEntry.COLUMN_BAIRRO, cliente.getBairro());
        values.put(Entities.ClienteEntry.COLUMN_CIDADE, cliente.getCidade());
        values.put(Entities.ClienteEntry.COLUMN_CELULAR, cliente.getCelular());
        values.put(Entities.ClienteEntry.COLUMN_TELEFONE, cliente.getTelefone());
        values.put(Entities.ClienteEntry.COLUMN_EMAIL, cliente.getEmail());
        values.put(Entities.ClienteEntry.COLUMN_USA_OUTRA_FARINHA, cliente.getUsaOutraFarinha());
        values.put(Entities.ClienteEntry.COLUMN_MARCA_FARINHA, cliente.getMarcaFarinha());
        values.put(Entities.ClienteEntry.COLUMN_NOME_CONTATO, cliente.getNomeContato());

        if (cliente.getVendedor() != null && cliente.getVendedor().getIdvendedor() != null) {
            values.put(Entities.ClienteEntry.COLUMN_ID_VENDEDOR, cliente.getVendedor().getIdvendedor());
        } else {
            values.putNull(Entities.ClienteEntry.COLUMN_ID_VENDEDOR);
        }

        String selection = Entities.ClienteEntry._ID + " = ?";
        String selctionArgs[] = {cliente.getIdcliente()};

        int count = db.update(
                Entities.ClienteEntry.TABLE_NAME,
                values,
                selection,
                selctionArgs);

        db.close();
        facDbHelper.close();

        return count;
    }

    /**
     * @param context
     * @return
     */
    public List<Clientes> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.ClienteEntry.COLUMN_NOME_CLIENTE;

        Cursor c = db.query(
                Entities.ClienteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Clientes> clientes = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Clientes cliente = new Clientes();

            cliente.setIdcliente(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry._ID)));
            cliente.setNomeCliente(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_NOME_CLIENTE)));
            cliente.setSegmento(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_SEGMENTO)));
            cliente.setLogradouro(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_LOGRADOURO)));
            cliente.setNumero(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_NUMERO)));
            cliente.setBairro(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_BAIRRO)));
            cliente.setCidade(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_CIDADE)));
            cliente.setCelular(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_CELULAR)));
            cliente.setTelefone(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_TELEFONE)));
            cliente.setEmail(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_EMAIL)));
            cliente.setUsaOutraFarinha(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_USA_OUTRA_FARINHA)));
            cliente.setMarcaFarinha(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_MARCA_FARINHA)));
            cliente.setNomeContato(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_NOME_CONTATO)));
            cliente.setVendedor(new Vendedor());
            cliente.getVendedor().setIdvendedor(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_ID_VENDEDOR)));

            clientes.add(cliente);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return clientes;
    }

    /**
     * @param context
     * @param nomeCliente
     * @return
     */
    public Clientes getClienteByName(Context context, String nomeCliente) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();


        String selectionArgs[] = { Utils.removerAcento(nomeCliente).toLowerCase() };

        String sql = "select * from clientes where lower(nome_cliente) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Clientes cliente = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            cliente = new Clientes();

            cliente.setIdcliente(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry._ID)));
            cliente.setNomeCliente(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_NOME_CLIENTE)));
            cliente.setSegmento(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_SEGMENTO)));
            cliente.setLogradouro(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_LOGRADOURO)));
            cliente.setNumero(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_NUMERO)));
            cliente.setBairro(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_BAIRRO)));
            cliente.setCidade(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_CIDADE)));
            cliente.setCelular(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_CELULAR)));
            cliente.setTelefone(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_TELEFONE)));
            cliente.setEmail(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_EMAIL)));
            cliente.setUsaOutraFarinha(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_USA_OUTRA_FARINHA)));
            cliente.setMarcaFarinha(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_MARCA_FARINHA)));
            cliente.setNomeContato(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_NOME_CONTATO)));
            cliente.setVendedor(new Vendedor());
            cliente.getVendedor().setIdvendedor(c.getString(c.getColumnIndexOrThrow(Entities.ClienteEntry.COLUMN_ID_VENDEDOR)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return cliente;
    }
}
