package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Ingrediente;
import com.celio.fac.entities.Massa;
import com.celio.fac.entities.Receita;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReceitaDao {

    /**
     * @param context
     * @param receita
     * @return
     */
    public long createReceita(Context context, Receita receita) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.ReceitaEntry.COLUMN_ID_MASSA, receita.getMassa().getIdmassa());
        values.put(Entities.ReceitaEntry.COLUMN_ID_INGREDIENTE, receita.getIngrediente().getIdingrediente());
        values.put(Entities.ReceitaEntry.COLUMN_PORCENTAGEM, receita.getPorcentagem());

        long newRowId = db.insert(Entities.ReceitaEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param receita
     * @return
     */
    public int updateReceita(Context context, Receita receita) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.ReceitaEntry.COLUMN_ID_MASSA, receita.getMassa().getIdmassa());
        values.put(Entities.ReceitaEntry.COLUMN_ID_INGREDIENTE, receita.getIngrediente().getIdingrediente());
        values.put(Entities.ReceitaEntry.COLUMN_PORCENTAGEM, receita.getPorcentagem());

        String selection = Entities.ReceitaEntry._ID + " = ?";
        String selectionArgs[] = {receita.getIdreceita()};

        int count = db.update(
                Entities.ReceitaEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();
        facDbHelper.close();

        return count;
    }

    /**
     * @param context
     * @param idreceita
     * @return
     */
    public int deleteReceita(Context context, String idreceita) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String whereClause = Entities.ReceitaEntry._ID + " = ?";
        String whereArgs[] = {idreceita};

        int count = db.delete(Entities.ReceitaEntry.TABLE_NAME, whereClause, whereArgs);

        db.close();
        facDbHelper.close();

        return count;
    }

    /**
     * @param context
     * @return
     */
    public List<Receita> getReceitaByMassa(Context context, String massa) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selectionArgs[] = {Utils.removerAcento(massa).toLowerCase()};

        StringBuilder sql = new StringBuilder();
        sql.append("select r._id, nome_ingrediente, porcentagem, idmassa, idingrediente ");
        sql.append("from receitas r ");
        sql.append("inner join massas m on(r.idmassa = m._id) ");
        sql.append("inner join ingredientes i on(r.idingrediente = i._id) ");
        sql.append("where lower(m.nome_massa) = ? ");

        Cursor c = db.rawQuery(sql.toString(), selectionArgs);

        List<Receita> receitas = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Receita receita = new Receita();

            receita.setIdreceita(c.getString(c.getColumnIndexOrThrow(Entities.ReceitaEntry._ID)));
            receita.setIngrediente(new Ingrediente());
            receita.getIngrediente().setIdingrediente(
                    c.getString(c.getColumnIndexOrThrow(Entities.ReceitaEntry.COLUMN_ID_INGREDIENTE)));
            receita.getIngrediente().setNomeIngrediente(c.getString(
                    c.getColumnIndexOrThrow(Entities.IngredienteEntry.COLUMN_NOME_INGREDIENTE)));
            receita.setPorcentagem(c.getString(c.getColumnIndexOrThrow(Entities.ReceitaEntry.COLUMN_PORCENTAGEM)));
            receita.setMassa(new Massa());
            receita.getMassa().setIdmassa(c.getString(c.getColumnIndexOrThrow(Entities.ReceitaEntry.COLUMN_ID_MASSA)));

            receitas.add(receita);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return receitas;
    }

}
