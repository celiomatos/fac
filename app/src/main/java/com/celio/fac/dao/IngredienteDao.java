package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Ingrediente;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class IngredienteDao {

    /**
     * @param context
     * @param ingrediente
     * @return
     */
    public long createIngrediente(Context context, Ingrediente ingrediente) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.IngredienteEntry.COLUMN_NOME_INGREDIENTE, ingrediente.getNomeIngrediente());

        long newRowId = db.insert(Entities.IngredienteEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param ingrediente
     * @return
     */
    public int updateIngrediente(Context context, Ingrediente ingrediente) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.IngredienteEntry.COLUMN_NOME_INGREDIENTE, ingrediente.getNomeIngrediente());

        String selection = Entities.IngredienteEntry._ID + " = ?";
        String selectionArgs[] = {ingrediente.getIdingrediente()};

        int count = db.update(
                Entities.IngredienteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();
        facDbHelper.close();

        return count;
    }

    /**
     * @param context
     * @return
     */
    public List<Ingrediente> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.IngredienteEntry.COLUMN_NOME_INGREDIENTE;

        Cursor c = db.query(
                Entities.IngredienteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Ingrediente> ingredientes = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Ingrediente ingrediente = new Ingrediente();

            ingrediente.setIdingrediente(c.getString(c.getColumnIndexOrThrow(Entities.IngredienteEntry._ID)));
            ingrediente.setNomeIngrediente(c.getString(c.getColumnIndexOrThrow(Entities.IngredienteEntry.COLUMN_NOME_INGREDIENTE)));

            ingredientes.add(ingrediente);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return ingredientes;
    }

    /**
     * @param context
     * @param nomeIngrediente
     * @return
     */
    public Ingrediente getIngredienteByNome(Context context, String nomeIngrediente) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selectionArgs[] = {Utils.removerAcento(nomeIngrediente).toLowerCase()};
        String sql = "select * from ingredientes where lower(nome_ingrediente) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Ingrediente ingrediente = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            ingrediente = new Ingrediente();

            ingrediente.setIdingrediente(c.getString(c.getColumnIndexOrThrow(Entities.IngredienteEntry._ID)));
            ingrediente.setNomeIngrediente(c.getString(c.getColumnIndexOrThrow(Entities.IngredienteEntry.COLUMN_NOME_INGREDIENTE)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return ingrediente;
    }

}
