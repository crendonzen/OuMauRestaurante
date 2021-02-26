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
public class Producto
{

    private int idProductos;
    private String nombre;
    private String codigo;

    public Producto(int idProductos, String nombre, String codigo)
    {
        this.idProductos = idProductos;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Producto(String nombre, String codigo)
    {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public int getIdproductos()
    {
        return idProductos;
    }

    public void setIdproductos(int idproductos)
    {
        this.idProductos = idproductos;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
