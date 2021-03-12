/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class Factura
{
    private int mesas_idmesas;
    private String mesas_numero;
    private String  estado;
    private double factura_pagado;
    private double factura_IVA;
    private Date factura_fecha;
    private int factura_idfacturas;
    private int usuarios_idempleado;
    private String usuarios_identificacion;
    private String usuarios_nombres;
    private String usuarios_apellidos;
    private String usuarios_telefono;
    private String usuarios_cargo;
    private ArrayList<Pedido> pedidos;

    public Factura()
    {
        this.pedidos=new ArrayList<> ();
    }

    public void inicializarPedidos(
        int mesas_idmesas,
        String mesas_numero,
        String estado,
        double factura_pagado,
        double factura_IVA,
        Date factura_fecha,
        int factura_idfacturas,
        int usuarios_idempleado,
        String usuarios_identificacion,
        String usuarios_nombres,
        String usuarios_apellidos,
        String usuarios_telefono,
        String usuarios_cargo
    )
    {
        this.mesas_idmesas = mesas_idmesas;
        this.mesas_numero = mesas_numero;
        this.estado = estado;
        this.factura_pagado = factura_pagado;
        this.factura_IVA = factura_IVA;
        this.factura_fecha = factura_fecha;
        this.factura_idfacturas = factura_idfacturas;
        this.usuarios_idempleado = usuarios_idempleado;
        this.usuarios_identificacion = usuarios_identificacion;
        this.usuarios_nombres = usuarios_nombres;
        this.usuarios_apellidos = usuarios_apellidos;
        this.usuarios_telefono = usuarios_telefono;
        this.usuarios_cargo = usuarios_cargo;
    }
    public void agregarPedido(Pedido pedido)
    {
        this.pedidos.add (pedido);
    }

    public int getMesas_idmesas()
    {
        return mesas_idmesas;
    }

    public void setMesas_idmesas(int mesas_idmesas)
    {
        this.mesas_idmesas = mesas_idmesas;
    }

    public String getMesas_numero()
    {
        return mesas_numero;
    }

    public void setMesas_numero(String mesas_numero)
    {
        this.mesas_numero = mesas_numero;
    }

    public String getEstado()
    {
        return estado;
    }

    public void setEstado(String estado)
    {
        this.estado = estado;
    }

    public double getFactura_pagado()
    {
        return factura_pagado;
    }

    public void setFactura_pagado(double factura_pagado)
    {
        this.factura_pagado = factura_pagado;
    }

    public double getFactura_IVA()
    {
        return factura_IVA;
    }

    public void setFactura_IVA(double factura_IVA)
    {
        this.factura_IVA = factura_IVA;
    }

    public Date getFactura_fecha()
    {
        return factura_fecha;
    }

    public void setFactura_fecha(Date factura_fecha)
    {
        this.factura_fecha = factura_fecha;
    }

    public int getFactura_idfacturas()
    {
        return factura_idfacturas;
    }

    public void setFactura_idfacturas(int factura_idfacturas)
    {
        this.factura_idfacturas = factura_idfacturas;
    }
    public int getUsuarios_idempleado()
    {
        return usuarios_idempleado;
    }

    public void setUsuarios_idempleado(int usuarios_idempleado)
    {
        this.usuarios_idempleado = usuarios_idempleado;
    }

    public String getUsuarios_identificacion()
    {
        return usuarios_identificacion;
    }

    public void setUsuarios_identificacion(String usuarios_identificacion)
    {
        this.usuarios_identificacion = usuarios_identificacion;
    }

    public String getUsuarios_nombres()
    {
        return usuarios_nombres;
    }

    public void setUsuarios_nombres(String usuarios_nombres)
    {
        this.usuarios_nombres = usuarios_nombres;
    }

    public String getUsuarios_apellidos()
    {
        return usuarios_apellidos;
    }

    public void setUsuarios_apellidos(String usuarios_apellidos)
    {
        this.usuarios_apellidos = usuarios_apellidos;
    }

    public String getUsuarios_telefono()
    {
        return usuarios_telefono;
    }

    public void setUsuarios_telefono(String usuarios_telefono)
    {
        this.usuarios_telefono = usuarios_telefono;
    }

    public String getUsuarios_cargo()
    {
        return usuarios_cargo;
    }

    public void setUsuarios_cargo(String usuarios_cargo)
    {
        this.usuarios_cargo = usuarios_cargo;
    }

    public ArrayList<Pedido> getPlatos()
    {
        return  this.pedidos;
    }

    public void limpiarLista()
    {
        this.pedidos.clear ();
    }

    public Pedido buscarPedido(int idplato)
    {
        Pedido miPedido=null;
        for (Pedido plato: this.pedidos)
        {
            if (plato.getIdplato ()== idplato)
            {
                miPedido=plato;
                break;
            }
        }
        return miPedido;
    }
}
