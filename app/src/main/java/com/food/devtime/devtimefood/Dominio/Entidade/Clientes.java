package com.food.devtime.devtimefood.Dominio.Entidade;

import java.io.Serializable;

/**
 * Created by Lerron on 07/07/2016.
 */
public class Clientes implements Serializable
{
    public static String ID = "_id";
    public static String NOME = "NOME";
    public static String NROINSC = "NROINSC";
    public static String ENDERECO = "ENDERECO";
    public static String EMAIL = "EMAIL";
    public static String TEL = "TEL";

    private long id;
    private String Nome;
    private String NroInsc;
    private String Endereco;
    private String Email;
    private String Tel;

    @Override
    public String toString()
    {
        return getNome() + " - " + getEmail();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getNroInsc() {
        return NroInsc;
    }

    public void setNroInsc(String nroInsc) {
        NroInsc = nroInsc;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }
}
