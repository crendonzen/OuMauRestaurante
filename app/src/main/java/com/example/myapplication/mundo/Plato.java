/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;


import android.graphics.Bitmap;

import java.io.Serializable;

/**
 *
 * @author Crendon
 */
public class Plato implements Serializable
{

    private int idPlato;
    private String categoria;
    private String nombre;
    private String descripcion;
    private double precio;
    private String  image;

    public Plato(int idPlato, String categoria, String nombre, String descripcion, double precio, String  image)
    {
        this.idPlato = idPlato;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.image = image;
    }


    public Plato(String categoria, String nombre, String descripcion, double precio, String  image)
    {
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.image = image;
    }

    public int getIdplato()
    {
        return idPlato;
    }

    public void setIdplato(int idplato)
    {
        this.idPlato = idplato;
    }

    public String getCategoria()
    {
        return categoria;
    }

    public void setCategoria(String categoria)
    {
        this.categoria = categoria;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public double getPrecio()
    {
        return precio;
    }

    public void setPrecio(double precio)
    {
        this.precio = precio;
    }

    public String  getImage()
    {
        return this.image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }


    public Pedido converAPedido()
    {
      return new  Pedido( idPlato,  categoria,  nombre,  descripcion,  precio,  image,0);
    }
}
