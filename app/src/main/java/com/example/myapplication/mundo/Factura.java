/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.mundo;

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
    private String pedidos_observacion;
    private int pedidos_cantidad;
    private int pedidos_idpedidos;
    private int usuarios_idempleado;
    private String usuarios_identificacion;
    private String usuarios_nombres;
    private String usuarios_apellidos;
    private String usuarios_telefono;
    private String usuarios_cargo;
    private String platos_imagen;
    private double platos_precio;
    private String platos_descripcion;
    private String platos_nombre;
    private String platos_categoria;
    private int platos_idplatos;

    public Factura(int mesas_idmesas, String mesas_numero, String estado, double factura_pagado, double factura_IVA, Date factura_fecha, int factura_idfacturas, String pedidos_observacion, int pedidos_cantidad, int pedidos_idpedidos, int usuarios_idempleado, String usuarios_identificacion, String usuarios_nombres, String usuarios_apellidos, String usuarios_telefono, String usuarios_cargo, String platos_imagen, double platos_precio, String platos_descripcion, String platos_nombre, String platos_categoria, int platos_idplatos)
    {
        this.mesas_idmesas = mesas_idmesas;
        this.mesas_numero = mesas_numero;
        this.estado = estado;
        this.factura_pagado = factura_pagado;
        this.factura_IVA = factura_IVA;
        this.factura_fecha = factura_fecha;
        this.factura_idfacturas = factura_idfacturas;
        this.pedidos_observacion = pedidos_observacion;
        this.pedidos_cantidad = pedidos_cantidad;
        this.pedidos_idpedidos = pedidos_idpedidos;
        this.usuarios_idempleado = usuarios_idempleado;
        this.usuarios_identificacion = usuarios_identificacion;
        this.usuarios_nombres = usuarios_nombres;
        this.usuarios_apellidos = usuarios_apellidos;
        this.usuarios_telefono = usuarios_telefono;
        this.usuarios_cargo = usuarios_cargo;
        this.platos_imagen = platos_imagen;
        this.platos_precio = platos_precio;
        this.platos_descripcion = platos_descripcion;
        this.platos_nombre = platos_nombre;
        this.platos_categoria = platos_categoria;
        this.platos_idplatos = platos_idplatos;
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

    public String getPedidos_observacion()
    {
        return pedidos_observacion;
    }

    public void setPedidos_observacion(String pedidos_observacion)
    {
        this.pedidos_observacion = pedidos_observacion;
    }

    public int getPedidos_cantidad()
    {
        return pedidos_cantidad;
    }

    public void setPedidos_cantidad(int pedidos_cantidad)
    {
        this.pedidos_cantidad = pedidos_cantidad;
    }

    public int getPedidos_idpedidos()
    {
        return pedidos_idpedidos;
    }

    public void setPedidos_idpedidos(int pedidos_idpedidos)
    {
        this.pedidos_idpedidos = pedidos_idpedidos;
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

    public String getPlatos_imagen()
    {
        return platos_imagen;
    }

    public void setPlatos_imagen(String platos_imagen)
    {
        this.platos_imagen = platos_imagen;
    }

    public double getPlatos_precio()
    {
        return platos_precio;
    }

    public void setPlatos_precio(double platos_precio)
    {
        this.platos_precio = platos_precio;
    }

    public String getPlatos_descripcion()
    {
        return platos_descripcion;
    }

    public void setPlatos_descripcion(String platos_descripcion)
    {
        this.platos_descripcion = platos_descripcion;
    }

    public String getPlatos_nombre()
    {
        return platos_nombre;
    }

    public void setPlatos_nombre(String platos_nombre)
    {
        this.platos_nombre = platos_nombre;
    }

    public String getPlatos_categoria()
    {
        return platos_categoria;
    }

    public void setPlatos_categoria(String platos_categoria)
    {
        this.platos_categoria = platos_categoria;
    }

    public int getPlatos_idplatos()
    {
        return platos_idplatos;
    }

    public void setPlatos_idplatos(int platos_idplatos)
    {
        this.platos_idplatos = platos_idplatos;
    }





    
    

}
