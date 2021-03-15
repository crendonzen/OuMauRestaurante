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
public class Pedido extends Plato
{

    private String obsevacion="";
    private int cantidad;

    public Pedido(int idPlato, String categoria, String nombre, String descripcion, double precio, String image, int cantidad)
    {
        super (idPlato, categoria, nombre, descripcion, precio, image);
        this.cantidad=cantidad;

    }

    public int getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(int cantidad)
    {
        this.cantidad = cantidad;
    }
    public String getObsevacion()
    {
        return obsevacion;
    }

    public void setObsevacion(String obsevacion)
    {
        this.obsevacion = obsevacion;
    }

    public double getTotal()
    {
        return this.cantidad*this.getPrecio ();
    }
}
