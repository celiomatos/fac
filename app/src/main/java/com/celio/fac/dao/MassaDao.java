package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Massa;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MassaDao {

    /**
     * @param context
     * @param massa
     * @return
     */
    public long createMassa(Context context, Massa massa) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.MassaEntry.COLUMN_NOME_MASSA, massa.getNomeMassa());
        values.put(Entities.MassaEntry.COLUMN_VELOCIDADE_A, massa.getVelocidadeA());
        values.put(Entities.MassaEntry.COLUMN_VELOCIDADE_B, massa.getVelocidadeB());
        values.put(Entities.MassaEntry.COLUMN_TEMP_AGUA, massa.getTempAgua());
        values.put(Entities.MassaEntry.COLUMN_TEMP_MASSA, massa.getTempMassa());
        values.put(Entities.MassaEntry.COLUMN_TEMP_FORNO, massa.getTempForno());
        values.put(Entities.MassaEntry.COLUMN_FERMENTACAO, massa.getFermentacao());
        values.put(Entities.MassaEntry.COLUMN_TEMPO_FERMENTACAO, massa.getTempoFermentacao());
        values.put(Entities.MassaEntry.COLUMN_TEMPO_FORNEAMENTO, massa.getTempoForneamento());
        values.put(Entities.MassaEntry.COLUMN_PESO_BOLA, massa.getPesoBola());
        values.put(Entities.MassaEntry.COLUMN_PESO_PAO_CRU, massa.getPesoPaoCru());
        values.put(Entities.MassaEntry.COLUMN_PESO_PAO_ASSADO, massa.getPesoPaoAssado());
        values.put(Entities.MassaEntry.COLUMN_QTD_PAES_BOLA, massa.getQtdePaesBola());

        long newRowId = db.insert(Entities.MassaEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param massa
     * @return
     */
    public int updateMassa(Context context, Massa massa) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.MassaEntry.COLUMN_NOME_MASSA, massa.getNomeMassa());
        values.put(Entities.MassaEntry.COLUMN_VELOCIDADE_A, massa.getVelocidadeA());
        values.put(Entities.MassaEntry.COLUMN_VELOCIDADE_B, massa.getVelocidadeB());
        values.put(Entities.MassaEntry.COLUMN_TEMP_AGUA, massa.getTempAgua());
        values.put(Entities.MassaEntry.COLUMN_TEMP_MASSA, massa.getTempMassa());
        values.put(Entities.MassaEntry.COLUMN_TEMP_FORNO, massa.getTempForno());
        values.put(Entities.MassaEntry.COLUMN_FERMENTACAO, massa.getFermentacao());
        values.put(Entities.MassaEntry.COLUMN_TEMPO_FERMENTACAO, massa.getTempoFermentacao());
        values.put(Entities.MassaEntry.COLUMN_TEMPO_FORNEAMENTO, massa.getTempoForneamento());
        values.put(Entities.MassaEntry.COLUMN_PESO_BOLA, massa.getPesoBola());
        values.put(Entities.MassaEntry.COLUMN_PESO_PAO_CRU, massa.getPesoPaoCru());
        values.put(Entities.MassaEntry.COLUMN_PESO_PAO_ASSADO, massa.getPesoPaoAssado());
        values.put(Entities.MassaEntry.COLUMN_QTD_PAES_BOLA, massa.getQtdePaesBola());

        String selection = Entities.MassaEntry._ID + " = ?";
        String selectionArgs[] = {massa.getIdmassa()};

        int count = db.update(
                Entities.MassaEntry.TABLE_NAME,
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
    public List<Massa> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.MassaEntry.COLUMN_NOME_MASSA;

        Cursor c = db.query(
                Entities.MassaEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Massa> massas = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Massa massa = new Massa();

            massa.setIdmassa(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry._ID)));
            massa.setNomeMassa(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_NOME_MASSA)));
            massa.setQtdePaesBola(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_QTD_PAES_BOLA)));
            massa.setVelocidadeA(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_VELOCIDADE_A)));
            massa.setVelocidadeB(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_VELOCIDADE_B)));
            massa.setTempAgua(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMP_AGUA)));
            massa.setTempMassa(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMP_MASSA)));
            massa.setTempForno(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMP_FORNO)));
            massa.setFermentacao(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_FERMENTACAO)));
            massa.setTempoFermentacao(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMPO_FERMENTACAO)));
            massa.setTempoForneamento(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMPO_FORNEAMENTO)));
            massa.setPesoBola(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_PESO_BOLA)));
            massa.setPesoPaoCru(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_PESO_PAO_CRU)));
            massa.setPesoPaoAssado(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_PESO_PAO_ASSADO)));
            massa.setQtdePaesBola(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_QTD_PAES_BOLA)));

            massas.add(massa);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return massas;
    }

    /**
     * @param context
     * @return
     */
    public Massa getMassaByNome(Context context, String nomeMassa) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selectionArgs[] = {Utils.removerAcento(nomeMassa).toLowerCase()};
        String sql = "select * from massas where lower(nome_massa) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Massa massa = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            massa = new Massa();

            massa.setIdmassa(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry._ID)));
            massa.setNomeMassa(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_NOME_MASSA)));
            massa.setQtdePaesBola(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_QTD_PAES_BOLA)));
            massa.setVelocidadeA(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_VELOCIDADE_A)));
            massa.setVelocidadeB(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_VELOCIDADE_B)));
            massa.setTempAgua(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMP_AGUA)));
            massa.setTempMassa(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMP_MASSA)));
            massa.setTempForno(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMP_FORNO)));
            massa.setFermentacao(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_FERMENTACAO)));
            massa.setTempoFermentacao(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMPO_FERMENTACAO)));
            massa.setTempoForneamento(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_TEMPO_FORNEAMENTO)));
            massa.setPesoBola(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_PESO_BOLA)));
            massa.setPesoPaoCru(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_PESO_PAO_CRU)));
            massa.setPesoPaoAssado(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_PESO_PAO_ASSADO)));
            massa.setQtdePaesBola(c.getString(c.getColumnIndexOrThrow(Entities.MassaEntry.COLUMN_QTD_PAES_BOLA)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return massa;
    }

}
