/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Crendon
 */
public class Mesa implements Serializable
{

    private int idMesa;
    private String numero;
    private String codigoQR;
    private String estado;
    public Mesa(int idmesa, String numero, String codigoQR,String estado)
    {
        this.idMesa = idmesa;
        this.numero = numero;
        this.codigoQR = codigoQR;
        this.estado = estado;
    }

    public Mesa(String numero, String codigoQR, String estado)
    {
        this.numero = numero;
        this.codigoQR = codigoQR;
        this.estado = estado;
    }

    public int getIdmesa()
    {
        return idMesa;
    }

    public void setIdmesa(int idmesa)
    {
        this.idMesa = idmesa;
    }

    public String getNumero()
    {
        return numero;
    }

    public void setNumero(String numero)
    {
        this.numero = numero;
    }

    public String getCodigoQR()
    {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR)
    {
        this.codigoQR = codigoQR;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
