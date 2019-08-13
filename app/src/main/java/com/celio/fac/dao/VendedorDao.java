package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Supervisor;
import com.celio.fac.entities.Vendedor;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Celio on 19/12/2017.
 */
public class VendedorDao {

    /**
     * @param context
     * @param vendedor
     * @return
     */
    public long createVendedor(Context context, Vendedor vendedor) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(Entities.VendedorEntry.COLUMN_NOME_VENDEDOR, vendedor.getNomeVendedor());
        if (vendedor.getSupervisor() != null && vendedor.getSupervisor().getIdsupervisor() != null) {
            values.put(Entities.VendedorEntry.COLUMN_ID_SUPERVISOR, vendedor.getSupervisor().getIdsupervisor());
        }

        long newRowId = db.insert(Entities.VendedorEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param vendedor
     * @return
     */
    public int updateVendedor(Context context, Vendedor vendedor) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(Entities.VendedorEntry.COLUMN_NOME_VENDEDOR, vendedor.getNomeVendedor());
        if (vendedor.getSupervisor() != null && vendedor.getSupervisor().getIdsupervisor() != null) {
            values.put(Entities.VendedorEntry.COLUMN_ID_SUPERVISOR, vendedor.getSupervisor().getIdsupervisor());
        } else {
            values.putNull(Entities.VendedorEntry.COLUMN_ID_SUPERVISOR);
        }


        String selection = Entities.VendedorEntry._ID + " = ?";
        String selctionArgs[] = {vendedor.getIdvendedor()};

        int count = db.update(
                Entities.VendedorEntry.TABLE_NAME,
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
    public List<Vendedor> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.VendedorEntry.COLUMN_NOME_VENDEDOR;

        Cursor c = db.query(
                Entities.VendedorEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Vendedor> vendedors = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Vendedor vendedor = new Vendedor();

            vendedor.setIdvendedor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry._ID)));
            vendedor.setNomeVendedor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry.COLUMN_NOME_VENDEDOR)));
            vendedor.setSupervisor(new Supervisor());
            vendedor.getSupervisor().setIdsupervisor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry.COLUMN_ID_SUPERVISOR)));

            vendedors.add(vendedor);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return vendedors;
    }

    /**
     * @param context
     * @param id
     * @return
     */
    public Vendedor getVendedor(Context context, String id) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selection = Entities.VendedorEntry._ID + " = ?";
        String selectionArgs[] = {id};

        String sortOrder = Entities.VendedorEntry.COLUMN_NOME_VENDEDOR;

        Cursor c = db.query(
                Entities.VendedorEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Vendedor vendedor = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            vendedor = new Vendedor();

            vendedor.setIdvendedor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry._ID)));
            vendedor.setNomeVendedor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry.COLUMN_NOME_VENDEDOR)));
            vendedor.setSupervisor(new Supervisor());
            vendedor.getSupervisor().setIdsupervisor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry.COLUMN_ID_SUPERVISOR)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return vendedor;
    }

    /**
     * @param context
     * @param nomeVendedor
     * @return
     */
    public Vendedor getVendedorByNome(Context context, String nomeVendedor) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();


        String selectionArgs[] = {Utils.removerAcento(nomeVendedor).toLowerCase()};
        String sql = "select * from vendedores where lower(nome_vendedor) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Vendedor vendedor = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            vendedor = new Vendedor();

            vendedor.setIdvendedor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry._ID)));
            vendedor.setNomeVendedor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry.COLUMN_NOME_VENDEDOR)));
            vendedor.setSupervisor(new Supervisor());
            vendedor.getSupervisor().setIdsupervisor(c.getString(c.getColumnIndexOrThrow(Entities.VendedorEntry.COLUMN_ID_SUPERVISOR)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return vendedor;
    }

}
