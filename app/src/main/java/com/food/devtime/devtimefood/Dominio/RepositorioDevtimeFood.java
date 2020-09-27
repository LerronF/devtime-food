package com.food.devtime.devtimefood.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.food.devtime.devtimefood.Database.ConnectionRemote;
import com.food.devtime.devtimefood.Dominio.Entidade.Clientes;
import com.food.devtime.devtimefood.Dominio.Entidade.Configuracao;

/**
 * Created by Lerron on 07/07/2016.
 */
public class RepositorioDevtimeFood {

    private SQLiteDatabase conn;

    private ConnectionRemote connectionRemote;

    public RepositorioDevtimeFood(SQLiteDatabase conn)
    {
        this.conn=conn;
    }

    //CLASSE PEDIDOS
    private ContentValues preencheClienteContentValues(Clientes clientes)
    {
        ContentValues values = new ContentValues();

        values.put("NOME",clientes.getNome());
        values.put("NROINSC",clientes.getNroInsc() );
        values.put("ENDERECO",clientes.getEndereco());
        values.put("EMAIL",clientes.getEmail());
        values.put("TEL",clientes.getTel());

        return values;
    }

    public void inserirClientes(Clientes clientes)
    {
        ContentValues values  = preencheClienteContentValues(clientes);

        conn.insertOrThrow("CLIENTES",null,values);
    }

    public void alterarClientes(Clientes clientes)
    {
        ContentValues values  = preencheClienteContentValues(clientes);

        conn.update("CLIENTES",values," _id = ? ",new String[]{String.valueOf(clientes.getId())});
    }

    public void excluirClientes(long id)
    {
        conn.delete("CLIENTES"," _id = ? ",new String[]{String.valueOf(id)});
    }

     public void testeInserirClientes()
      {
          for (int i = 0; i<5; i++)
          {
              ContentValues values = new ContentValues();
              values.put("NOME","Lerron");
              conn.insertOrThrow("CLIENTES",null,values);
          }
       }


    public ArrayAdapter<Clientes> buscaClientes(Context context)
    {
        ArrayAdapter<Clientes> adpClientes = new ArrayAdapter<Clientes>(context,android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CLIENTES",null,null,null,null,null,null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            do
            {
                Clientes clientes = new Clientes();

                clientes.setId(cursor.getLong(cursor.getColumnIndex(Clientes.ID)));
                clientes.setNome(cursor.getString(cursor.getColumnIndex(Clientes.NOME)));
                clientes.setNroInsc(cursor.getString(cursor.getColumnIndex(Clientes.NROINSC)));
                clientes.setEndereco(cursor.getString(cursor.getColumnIndex(Clientes.ENDERECO)));
                clientes.setEmail(cursor.getString(cursor.getColumnIndex(Clientes.EMAIL)));
                clientes.setTel(cursor.getString(cursor.getColumnIndex(Clientes.TEL)));

                adpClientes.add(clientes);

            }while(cursor.moveToNext());
        }

        return adpClientes;
    }

    //CLASSE CONFIGURACAO
    private ContentValues preencheConfiguracaoContentValues(Configuracao configuracao)
    {
        ContentValues values = new ContentValues();

        values.put("Ip",configuracao.getIp());
        values.put("Driver",configuracao.getDriver() );
        values.put("BancoDados",configuracao.getBancoDados());
        values.put("Usuario",configuracao.getUsuario());
        values.put("Senha",configuracao.getSenha());

        return values;
    }

    public void inserirConfiguracao(Configuracao configuracao)
    {
        ContentValues values  = preencheConfiguracaoContentValues(configuracao);

        conn.insertOrThrow("CONFIGURACAOBD",null,values);
    }

    public void alterarConfiguracao(Configuracao configuracao)
    {
        ContentValues values  = preencheConfiguracaoContentValues(configuracao);

        conn.update("CONFIGURACAOBD",values," _id = ? ",new String[]{String.valueOf(configuracao.getId())});
    }

    public void excluirConfiguracao(long id)
    {
        conn.delete("CONFIGURACAOBD"," _id = ? ",new String[]{String.valueOf(id)});
    }

    public void testeInserirConfiguracao()
    {
        ContentValues values = new ContentValues();
        values.put("Ip","0.0.0.0");
        values.put("Driver","net.sourceforge.jtds.jdbc.Driver");
        values.put("BancoDados","DTPadrao");
        values.put("Usuario","sa");
        values.put("Senha","Apollo28am");
        conn.insertOrThrow("CONFIGURACAOBD",null,values);
    }


    public ArrayAdapter<Configuracao> buscaConfiguracao(Context context)
    {
        ArrayAdapter<Configuracao> adpConfiguracao = new ArrayAdapter<Configuracao>(context,android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CONFIGURACAOBD",null,null,null,null,null,null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            do
            {
                Configuracao configuracao = new Configuracao();

                configuracao.setId(cursor.getLong(cursor.getColumnIndex(Configuracao.ID)));
                configuracao.setIp(cursor.getString(cursor.getColumnIndex(Configuracao.IP)));
                configuracao.setDriver(cursor.getString(cursor.getColumnIndex(Configuracao.DRIVER)));
                configuracao.setBancoDados(cursor.getString(cursor.getColumnIndex(Configuracao.BANCODADOS)));
                configuracao.setUsuario(cursor.getString(cursor.getColumnIndex(Configuracao.USUARIO)));
                configuracao.setSenha(cursor.getString(cursor.getColumnIndex(Configuracao.SENHA)));

                adpConfiguracao.add(configuracao);

            }while(cursor.moveToNext());
        }

        return adpConfiguracao;
    }

}
