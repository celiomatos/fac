package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Tecnico;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TecnicoDao {

    /**
     * @param context
     * @param tecnico
     * @return
     */
    public long createTecnico(Context context, Tecnico tecnico) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.TecnicoEntry.COLUMN_NOME_TECNICO, tecnico.getNomeTecnico());

        long newRowId = db.insert(Entities.TecnicoEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param tecnico
     * @return
     */
    public int updateTecnico(Context context, Tecnico tecnico) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.TecnicoEntry.COLUMN_NOME_TECNICO, tecnico.getNomeTecnico());

        String selection = Entities.TecnicoEntry._ID + " = ?";
        String selectionArgs[] = {tecnico.getIdtecnico()};

        int count = db.update(
                Entities.TecnicoEntry.TABLE_NAME,
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
    public List<Tecnico> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.TecnicoEntry.COLUMN_NOME_TECNICO;

        Cursor c = db.query(
                Entities.TecnicoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Tecnico> tecnicos = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Tecnico tecnico = new Tecnico();

            tecnico.setIdtecnico(c.getString(c.getColumnIndexOrThrow(Entities.TecnicoEntry._ID)));
            tecnico.setNomeTecnico(c.getString(c.getColumnIndexOrThrow(Entities.TecnicoEntry.COLUMN_NOME_TECNICO)));

            tecnicos.add(tecnico);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return tecnicos;
    }

    /**
     * @param context
     * @param nomeTecnico
     * @return
     */
    public Tecnico getTecnicoByNome(Context context, String nomeTecnico) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selectionArgs[] = {Utils.removerAcento(nomeTecnico).toLowerCase()};
        String sql = "select * from tecnicos where lower(nome_tecnico) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Tecnico tecnico = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            tecnico = new Tecnico();

            tecnico.setIdtecnico(c.getString(c.getColumnIndexOrThrow(Entities.TecnicoEntry._ID)));
            tecnico.setNomeTecnico(c.getString(c.getColumnIndexOrThrow(Entities.TecnicoEntry.COLUMN_NOME_TECNICO)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return tecnico;
    }

}
