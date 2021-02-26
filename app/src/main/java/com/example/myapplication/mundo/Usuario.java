/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;

import android.os.Parcelable;

import java.io.Serializable;

/**
 *
 * @author Crendon
 */
public class Usuario
{
    private int idEmpleados;
    private String identificacion;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String contrasena;
    private String rol;
    private String cargo;
    private String nick;

    public Usuario(int idEmpleados, String identificacion, String nombres, String apellidos, String telefono, String contrasena, String rol, String cargo, String nick) {
        this.idEmpleados = idEmpleados;
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
        this.cargo = cargo;
        this.nick = nick;
    }

    public Usuario( String identificacion, String nombres, String apellidos, String telefono, String contrasena, String rol, String cargo, String nick) {

        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.rol = rol;
        this.cargo = cargo;
        this.nick = nick;
    }

    public int getIdEmpleados() {
        return idEmpleados;
    }

    public void setIdEmpleados(int idEmpleados) {
        this.idEmpleados = idEmpleados;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


    public int getIdempleados()
    {
        return idEmpleados;
    }

    public void setIdempleados(int idempleados)
    {
        this.idEmpleados = idempleados;
    }

    public String getIdentificacion()
    {
        return identificacion;
    }

    public void setIdentificacion(String identificacion)
    {
        this.identificacion = identificacion;
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

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getContrasena()
    {
        return contrasena;
    }

    public void setContrasena(String contrasena)
    {
        this.contrasena = contrasena;
    }

    public String getRol()
    {
        return rol;
    }

    public void setRol(String rol)
    {
        this.rol = rol;
    }

    @Override
    public String toString()
    {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
