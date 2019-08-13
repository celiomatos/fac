package com.celio.fac.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.celio.fac.entities.Produto;
import com.celio.fac.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {

    /**
     * @param context
     * @param produto
     * @return
     */
    public long createProduto(Context context, Produto produto) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.ProdutoEntry.COLUMN_NOME_PRODUTO, produto.getNomeProduto());

        long newRowId = db.insert(Entities.ProdutoEntry.TABLE_NAME, null, values);

        db.close();
        facDbHelper.close();

        return newRowId;
    }

    /**
     * @param context
     * @param produto
     * @return
     */
    public int updateProduto(Context context, Produto produto) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entities.ProdutoEntry.COLUMN_NOME_PRODUTO, produto.getNomeProduto());

        String selection = Entities.ProdutoEntry._ID + " = ?";
        String selectionArgs[] = {produto.getIdproduto()};

        int count = db.update(
                Entities.ProdutoEntry.TABLE_NAME,
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
    public List<Produto> getAll(Context context) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String sortOrder = Entities.ProdutoEntry.COLUMN_NOME_PRODUTO;

        Cursor c = db.query(
                Entities.ProdutoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Produto> produtos = new ArrayList<>();

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            Produto produto = new Produto();

            produto.setIdproduto(c.getString(c.getColumnIndexOrThrow(Entities.ProdutoEntry._ID)));
            produto.setNomeProduto(c.getString(c.getColumnIndexOrThrow(Entities.ProdutoEntry.COLUMN_NOME_PRODUTO)));

            produtos.add(produto);

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return produtos;
    }

    /**
     * @param context
     * @param nomeProduto
     * @return
     */
    public Produto getProdutoByNome(Context context, String nomeProduto) {

        FacDbHelper facDbHelper = new FacDbHelper(context);
        SQLiteDatabase db = facDbHelper.getReadableDatabase();

        String selectionArgs[] = {Utils.removerAcento(nomeProduto).toLowerCase()};
        String sql = "select * from produtos where lower(nome_produto) = ? limit 1";

        Cursor c = db.rawQuery(sql, selectionArgs);

        Produto produto = null;

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            produto = new Produto();

            produto.setIdproduto(c.getString(c.getColumnIndexOrThrow(Entities.ProdutoEntry._ID)));
            produto.setNomeProduto(c.getString(c.getColumnIndexOrThrow(Entities.ProdutoEntry.COLUMN_NOME_PRODUTO)));

            c.moveToNext();
        }

        c.close();
        db.close();
        facDbHelper.close();

        return produto;
    }

}
