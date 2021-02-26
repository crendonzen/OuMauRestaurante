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
public class Cliente
{

    private int idClientes;
    private String indentificacion;
    private String nombres;
    private String apellidos;

    public Cliente(int idclientes, String indentificacion, String nombres, String apellidos)
    {
        this.idClientes = idclientes;
        this.indentificacion = indentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Cliente(String indentificacion, String nombres, String apellidos)
    {
        this.indentificacion = indentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getIdclientes()
    {
        return idClientes;
    }

    public void setIdclientes(int idclientes)
    {
        this.idClientes = idclientes;
    }

    public String getIndentificacion()
    {
        return indentificacion;
    }

    public void setIndentificacion(String indentificacion)
    {
        this.indentificacion = indentificacion;
    }

    public String getNombres()
    {
        return nombres;
    }

    public void setNombres(String nombres)
    {
        this.nombres = nombres;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
