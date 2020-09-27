package com.food.devtime.devtimefood.Dominio.Entidade;

/**
 * Created by Lerron on 14/07/2016.
 */
public class Pedidos {

    public static String ID = "_id";
    public static String OBSERVACAO = "OBSERVACAO";

    private long id;
    private String Descricao;

    public Pedidos(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }
}
