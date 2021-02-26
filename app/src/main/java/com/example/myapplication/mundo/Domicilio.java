/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;

/**
 *
 * @author Crendon
 */
public class Domicilio
{

    private int idDomicilios;
    private int idFacturas;
    private int idPlatos;
    private String direccion;
    private String referencia;
    private String celular;

    public Domicilio(int iddomicilios, int facturas_idfacturas, int platos_idplatos, String direccion, String referencia, String celular)
    {
        this.idDomicilios = iddomicilios;
        this.idFacturas = facturas_idfacturas;
        this.idPlatos = platos_idplatos;
        this.direccion = direccion;
        this.referencia = referencia;
        this.celular = celular;
    }

    public Domicilio(int facturas_idfacturas, int platos_idplatos, String direccion, String referencia, String celular)
    {
        this.idFacturas = facturas_idfacturas;
        this.idPlatos = platos_idplatos;
        this.direccion = direccion;
        this.referencia = referencia;
        this.celular = celular;
    }

    public int getIdDomicilios()
    {
        return idDomicilios;
    }

    public void setIdDomicilios(int idDomicilios)
    {
        this.idDomicilios = idDomicilios;
    }

    public int getIdFacturas()
    {
        return idFacturas;
    }

    public void setIdFacturas(int idFacturas)
    {
        this.idFacturas = idFacturas;
    }

    public int getIdPlatos()
    {
        return idPlatos;
    }

    public void setIdPlatos(int idPlatos)
    {
        this.idPlatos = idPlatos;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public void setDireccion(String direccion)
    {
        this.direccion = direccion;
    }

    public String getReferencia()
    {
        return referencia;
    }

    public void setReferencia(String referencia)
    {
        this.referencia = referencia;
    }

    public String getCelular()
    {
        return celular;
    }

    public void setCelular(String celular)
    {
        this.celular = celular;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
