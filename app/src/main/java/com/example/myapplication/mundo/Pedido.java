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
public class Pedido
{
    private int idPedidos;
    private int idPlatos;
    private int idMesas;
    private int idEmpleados;
    private int idFacturas;
    private String observacion;

    public Pedido(int idPedidos, int idPlatos, int idMesas, int idEmpleados, int idFacturas, String observacion)
    {
        this.idPedidos = idPedidos;
        this.idPlatos = idPlatos;
        this.idMesas = idMesas;
        this.idEmpleados = idEmpleados;
        this.idFacturas = idFacturas;
        this.observacion = observacion;
    }
    public Pedido( int idPlatos, int idMesas, int idEmpleados, int idFacturas, String observacion)
    {
        this.idPlatos = idPlatos;
        this.idMesas = idMesas;
        this.idEmpleados = idEmpleados;
        this.idFacturas = idFacturas;
        this.observacion = observacion;
    }

    public int getIdPedido()
    {
        return idPedidos;
    }

    public void setIdPedidos(int idPedidos)
    {
        this.idPedidos = idPedidos;
    }

    public int getIdPlatos()
    {
        return idPlatos;
    }

    public void setIdPlatos(int idPlatos)
    {
        this.idPlatos = idPlatos;
    }

    public int getIdMesa()
    {
        return idMesas;
    }

    public void setIdMesas(int idMesas)
    {
        this.idMesas = idMesas;
    }

    public int getIdEmpleado()
    {
        return idEmpleados;
    }

    public void setIdEmpleados(int idEmpleados)
    {
        this.idEmpleados = idEmpleados;
    }

    public int getIdFacturas()
    {
        return idFacturas;
    }

    public void setIdFacturas(int idFacturas)
    {
        this.idFacturas = idFacturas;
    }

    public String getObservacion()
    {
        return observacion;
    }

    public void setObservacion(String observacion)
    {
        this.observacion = observacion;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    

}
