package com.example.myapplication.interfaz;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPedidos;
import com.example.myapplication.adaptador.AdaptadorListaPlatos;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Mesa;
import com.example.myapplication.mundo.Plato;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlatosMesaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlatosMesaFragment extends Fragment implements View.OnDragListener
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private RecyclerView listaPedidos;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private ArrayList<Plato> platosMenu;
    private Factura pedidoFactura;
    private boolean isDropped = false;
    private MenuPlatosFragment.Listener mListener;
    private TextView numeroMesa;
    private ImageButton btnActualizarPedido;

    public PlatosMesaFragment()
    {

    }

    public static PlatosMesaFragment newInstance(String param1, String param2)
    {
        PlatosMesaFragment fragment = new PlatosMesaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_platos_mesa, container, false);
        this.pedidoFactura=new Factura ();
        this.listaPedidos = v.findViewById(R.id.lista_pedidos);
        this.requestQueue =  VolleySingleton.getInstance(getContext ()).getRequestQueue();
        this.adaptadorListaPedidos = new AdaptadorListaPedidos (getContext (),this.pedidoFactura.getPlatos());
        this.listaPedidos.setLayoutManager(new GridLayoutManager(getContext(),5));
        this.listaPedidos.setAdapter(this.adaptadorListaPedidos);
        this.btnActualizarPedido=v.findViewById(R.id.btnActualizarPedido);

        this.btnActualizarPedido.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String data = new Gson ().toJson(pedidoFactura.getPlatos ());

                HashMap<String, String> params= new HashMap<String, String>();
                params.put("modidificarListaPedido",data);
                params.put("idfactura",pedidoFactura.getFactura_idfacturas ()+"");
                JSONObject parameters = new JSONObject(params);

                String url="http://openm.co/consultas/pedidos.php";

                jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {

                            JSONArray datos = response.getJSONArray ("datos");
                            Toast.makeText (getContext (), datos.toString (), Toast.LENGTH_SHORT).show ();
                        }catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                    }
                }, new Response.ErrorListener ()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace ();
                    }
                });
                requestQueue.add(jsonRequest);
            }
        });

        numeroMesa= v.findViewById(R.id.numero_Mesa);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle)
            {
                Mesa mesa = (Mesa) bundle.getSerializable("mesa");
                numeroMesa.setText(mesa.getNumero());
                int idmesa = mesa.getIdmesa ();
                Map<String,String> params= new HashMap<String, String>();
                Toast.makeText(getContext (), ""+idmesa, Toast.LENGTH_SHORT).show();
                params.put("buscarPlatoMesa",idmesa+"");
                JSONObject parameters = new JSONObject(params);
                String url="http://openm.co/consultas/pedidos.php";

                jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            listaPedidos.setAdapter(adaptadorListaPedidos);
                            pedidoFactura.limpiarLista ();
                            SimpleDateFormat format=new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                            JSONArray datos = response.getJSONArray ("datos");
                            if (datos.length ()>0)
                            {
                                JSONObject pedido = datos.getJSONObject (0);
                                int mesas_idmesas = pedido.getInt ("mesas_idmesas");
                                String mesas_numero = pedido.getString ("mesas_numero");
                                String estado = pedido.getString ("estado");
                                double factura_pagado = pedido.getDouble ("factura_pagado");
                                double factura_IVA = pedido.getDouble ("factura_IVA");
                                String factura_fecha = pedido.getString ("factura_fecha");
                                int factura_idfacturas = pedido.getInt ("factura_idfacturas");
                                String pedidos_observacion = pedido.getString ("pedidos_observacion");
                                int pedidos_idpedidos = pedido.getInt ("pedidos_idpedidos");
                                int usuarios_idempleado = pedido.getInt ("usuarios_idempleado");
                                String usuarios_identificacion = pedido.getString ("usuarios_identificacion");
                                String usuarios_nombres = pedido.getString ("usuarios_nombres");
                                String usuarios_apellidos = pedido.getString ("usuarios_apellidos");
                                String usuarios_telefono = pedido.getString ("usuarios_telefono");
                                String usuarios_cargo = pedido.getString ("usuarios_cargo");

                                pedidoFactura.inicializarPedidos (mesas_idmesas,
                                        mesas_numero,
                                        estado,
                                        factura_pagado,
                                        factura_IVA,
                                        format.parse (factura_fecha),
                                        factura_idfacturas,
                                        pedidos_observacion,
                                        pedidos_idpedidos,
                                        usuarios_idempleado,
                                        usuarios_identificacion,
                                        usuarios_nombres,
                                        usuarios_apellidos,
                                        usuarios_telefono,
                                        usuarios_cargo);

                                for (int i = 0; i < datos.length (); i++)
                                {
                                    JSONObject plato = datos.getJSONObject (i);
                                    int pedidos_cantidad = plato.getInt ("pedidos_cantidad");
                                    String platos_imagen = plato.getString ("platos_imagen");
                                    double platos_precio = plato.getDouble ("platos_precio");
                                    String platos_descripcion = plato.getString ("platos_descripcion");
                                    String platos_nombre = plato.getString ("platos_nombre");
                                    String platos_categoria = plato.getString ("platos_categoria");
                                    int platos_idplatos = plato.getInt ("platos_idplatos");
                                    Toast.makeText (getContext (), plato.getString ("mesas_numero"), Toast.LENGTH_SHORT).show ();

                                    Plato platoDatos = new Plato (
                                        platos_idplatos,
                                        platos_categoria,
                                        platos_nombre,
                                        platos_descripcion,
                                        platos_precio,
                                        platos_imagen,
                                        pedidos_cantidad
                                    );
                                    pedidoFactura.agregarPlato (platoDatos);
                                }
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace ();
                        }
                    }
                }, new Response.ErrorListener ()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace ();
                    }
                });
                requestQueue.add(jsonRequest);
            }
        });

        this.listaPedidos.setOnDragListener(this);

        return v;
    }

    @Override
    public boolean onDrag(View v, DragEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(Color.LTGRAY);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(Color.YELLOW);
                break;
            case DragEvent.ACTION_DROP:
                if (pedidoFactura.getMesas_idmesas () !=0)
                {
                    int positionFuente = -1;
                    View viewSource = (View) event.getLocalState();
                    RecyclerView RecyclerView = (RecyclerView) viewSource.getParent();
                    AdaptadorListaPlatos adaptadorListaPlatos = (AdaptadorListaPlatos) RecyclerView.getAdapter();
                    positionFuente = (int) viewSource.getTag();
                    Plato plato = (Plato) adaptadorListaPlatos.getList ().get(positionFuente);
                    Plato miPlato = pedidoFactura.buscarPlato (plato.getIdplato ());
                    if (miPlato instanceof  Plato)
                    {
                        miPlato.setCantidad (miPlato.getCantidad ()+1);
                    }else
                    {
                        plato.setCantidad (1);
                        pedidoFactura.agregarPlato (plato);
                    }

                    listaPedidos.setAdapter(adaptadorListaPedidos);
                    v.setVisibility(View.VISIBLE);
                }else
                {
                    Toast.makeText(getContext (), "Por favor selecciona una mesa", Toast.LENGTH_SHORT).show();
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(0);
                break;
            default:
                break;
        }

        if (!isDropped)
        {
            View vw = (View) event.getLocalState();
            vw.setVisibility(View.VISIBLE);
        }

        return true;
    }

}