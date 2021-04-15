/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;


import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Objects;

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
    private String  estado;
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
    public Plato(int idPlato, String categoria, String nombre, String descripcion, double precio, String  image, String  estado)
    {
        this.idPlato = idPlato;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.image = image;
        this.estado=estado;
    }
    public Plato( String categoria, String nombre, String descripcion, double precio, String  image, String  estado)
    {
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.image = image;
        this.estado=estado;
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

    public boolean equals(Plato otroPlato)
    {
        if (this == otroPlato) return true;
        if (otroPlato == null || getClass () != otroPlato.getClass ()) return false;

        return
            otroPlato.getPrecio ()==precio &&
            categoria.equals (otroPlato.getCategoria ()) &&
            nombre.equals (otroPlato.getNombre ()) &&
            descripcion.equals (otroPlato.getDescripcion ()) &&
            image.equals (otroPlato.getImage ()) &&
                    estado.equals (otroPlato.getEstado ());
    }
    public int getPosicionCategoria()
    {
        if (this.categoria.equals ("Hamburguesas"))
        {
            return 0;
        }else if (this.categoria.equals ("Alas"))
        {
            return 1;
        }else if (this.categoria.equals ("Pizza"))
        {
            return 2;
        }else if (this.categoria.equals ("Parrilla"))
        {
            return 3;
        }else if (this.categoria.equals ("Cajita feliz"))
        {
            return 4;
        }else if (this.categoria.equals ("Bebidas"))
        {
            return 5;
        }else
        {
            return 6;
        }
    }

    public int getPosicionEstado()
    {
        if (this.estado.equals ("Activo"))
        {
            return 0;
        }else {
            return 1;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash (idPlato, categoria, nombre, descripcion, precio, image);
    }

    public void setEstado(String estado)
    {
        this.estado=estado;
    }

    public String getEstado() {
        return estado;
    }
}
