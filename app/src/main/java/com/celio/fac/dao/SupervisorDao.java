package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Supervisor;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SupervisorDao {

    /**
     * @param context
     * @param supervisor
     * @return
     */
    public long createSupervidor(Context context, Supervisor supervisor) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR, supervisor.getNomeSupervisor());

        long newRowId = db.insert(Entities.SupervisorEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param supervisor
     * @return
     */
    public int updateSupervidor(Context context, Supervisor supervisor) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR, supervisor.getNomeSupervisor());

        String selection = Entities.SupervisorEntry._ID + " = ?";
        String selectionArgs[] = {supervisor.getIdsupervisor()};

        int count = db.update(
                Entities.SupervisorEntry.TABLE_NAME,
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
    public List<Supervisor> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR;

        Cursor c = db.query(
                Entities.SupervisorEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Supervisor> supervisors = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Supervisor supervisor = new Supervisor();

            supervisor.setIdsupervisor(c.getString(c.getColumnIndexOrThrow(Entities.SupervisorEntry._ID)));
            supervisor.setNomeSupervisor(c.getString(c.getColumnIndexOrThrow(Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR)));

            supervisors.add(supervisor);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return supervisors;
    }

    /**
     * @param context
     * @param id
     * @return
     */
    public Supervisor getSupervisor(Context context, String id) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selection = Entities.SupervisorEntry._ID + " = ?";
        String selectionArgs[] = {id};
        String sortOrder = Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR;

        Cursor c = db.query(
                Entities.SupervisorEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        Supervisor supervisor = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            supervisor = new Supervisor();

            supervisor.setIdsupervisor(c.getString(c.getColumnIndexOrThrow(Entities.SupervisorEntry._ID)));
            supervisor.setNomeSupervisor(c.getString(c.getColumnIndexOrThrow(Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return supervisor;
    }

    /**
     * @param context
     * @param nomeSupervisor
     * @return
     */
    public Supervisor getSupervisorByNome(Context context, String nomeSupervisor) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();


        String selectionArgs[] = {Utils.removerAcento(nomeSupervisor).toLowerCase()};
        String sql = "select * from supervisores where lower(nome_supervisor) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Supervisor supervisor = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            supervisor = new Supervisor();

            supervisor.setIdsupervisor(c.getString(c.getColumnIndexOrThrow(Entities.SupervisorEntry._ID)));
            supervisor.setNomeSupervisor(c.getString(c.getColumnIndexOrThrow(Entities.SupervisorEntry.COLUMN_NOME_SUPERVISOR)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return supervisor;
    }

}
