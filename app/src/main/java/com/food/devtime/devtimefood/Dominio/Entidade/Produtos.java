package com.food.devtime.devtimefood.Dominio.Entidade;

import java.io.Serializable;

/**
 * Created by Lerron on 07/07/2016.
 */
public class Produtos implements Serializable {

    public static String ID = "_id";
    public static String DESCRICAO = "DESCRICAO";
    public static String PRECOVENDA = "PRECOVENDA";
    public static String PRECOCUSTO = "PRECOCUSTO";
    public static String NCM = "NCM";
    public static String CEST = "CEST";

    private long id;
    private String Descricao;
    private String PrecoVenda;
    private String PrecoCusto;
    private String Ncm;
    private String Cest;

    public Produtos()
    {
        setId(0);
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

    public String getPrecoVenda() {
        return PrecoVenda;
    }

    public void setPrecoVenda(String precoVenda) {
        PrecoVenda = precoVenda;
    }

    public String getPrecoCusto() {
        return PrecoCusto;
    }

    public void setPrecoCusto(String precoCusto) {
        PrecoCusto = precoCusto;
    }

    public String getNcm() {
        return Ncm;
    }

    public void setNcm(String ncm) {
        Ncm = ncm;
    }

    public String getCest() {
        return Cest;
    }

    public void setCest(String cest) {
        Cest = cest;
    }

    @Override
    public String toString()
    {
        return Descricao + " - " + Ncm + " - " + Cest;
    }
}
