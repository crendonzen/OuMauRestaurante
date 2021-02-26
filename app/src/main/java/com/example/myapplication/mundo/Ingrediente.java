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
public class Ingrediente
{

    private int idIngredientes;
    private int idPlatos;
    private int idProductos;
    private int cantidad;

    public Ingrediente(int idPlatos, int idProductos, int cantidad)
    {
        this.idPlatos = idPlatos;
        this.idProductos = idProductos;
        this.cantidad = cantidad;
    }

    public Ingrediente(int idIngredientes, int idPlatos, int idProductos, int cantidad)
    {
        this.idIngredientes = idIngredientes;
        this.idPlatos = idPlatos;
        this.idProductos = idProductos;
        this.cantidad = cantidad;
    }

    public int getIdIngredientes()
    {
        return idIngredientes;
    }

    public void setIdIngredientes(int idIngredientes)
    {
        this.idIngredientes = idIngredientes;
    }

    public int getIdPlatos()
    {
        return idPlatos;
    }

    public void setIdPlatos(int idPlatos)
    {
        this.idPlatos = idPlatos;
    }

    public int getIdProductos()
    {
        return idProductos;
    }

    public void setIdProductos(int idProductos)
    {
        this.idProductos = idProductos;
    }

    public int getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(int cantidad)
    {
        this.cantidad = cantidad;
    }

}
