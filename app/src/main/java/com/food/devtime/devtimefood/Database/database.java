package com.food.devtime.devtimefood.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lerron on 07/07/2016.
 */
public class database extends SQLiteOpenHelper
{
    public database(Context context)
    {
        super(context, "devtimefood", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
      //  db.execSQL(ScriptSQL.getCreateProdutos());
        //db.execSQL(ScriptSQL.getCreateClientes());
        db.execSQL(ScriptSQL.getCreateConfiguracao());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
