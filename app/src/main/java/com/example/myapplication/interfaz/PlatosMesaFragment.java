package com.example.myapplication.interfaz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
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
public class PlatosMesaFragment extends Fragment {


    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;


    private RecyclerView listaPedidos;

    private AdaptadorListaPlatos adaptadorListaPlatos;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private ArrayList<Plato> platosMenu;
    private Factura pedidoFactura;
    private boolean isDropped = false;
    private MenuPlatosFragment.Listener mListener;
    TextView numeroMesa;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlatosMesaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlatosMesaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlatosMesaFragment newInstance(String param1, String param2) {
        PlatosMesaFragment fragment = new PlatosMesaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported

                Mesa result = (Mesa) bundle.getSerializable("mesa");
                // Do something with the result...
                Toast.makeText(getContext (),result.getNumero(), Toast.LENGTH_SHORT).show();
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_platos_mesa, container, false);
        this.pedidoFactura=new Factura ();
        this.listaPedidos = v.findViewById(R.id.lista_pedidos);
        this.requestQueue =  VolleySingleton.getInstance(getContext ()).getRequestQueue();
        this.adaptadorListaPedidos = new AdaptadorListaPedidos (getContext (),this.pedidoFactura.getPlatos());
        this.listaPedidos.setLayoutManager(new GridLayoutManager(getContext(),5));
        this.listaPedidos.setAdapter(this.adaptadorListaPedidos);




        numeroMesa= v.findViewById(R.id.numero_Mesa);
        Bundle objetoPlato = getArguments();
        Mesa mesa = null;
        if(objetoPlato !=null)
        {
            mesa = (Mesa) objetoPlato.getSerializable("mesa");
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
                        SimpleDateFormat format=new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                        JSONArray datos = response.getJSONArray ("datos");
                        if (datos.length ()>-1)
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
                            int pedidos_cantidad = pedido.getInt ("pedidos_cantidad");
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
                                    pedidos_cantidad,
                                    pedidos_idpedidos,
                                    usuarios_idempleado,
                                    usuarios_identificacion,
                                    usuarios_nombres,
                                    usuarios_apellidos,
                                    usuarios_telefono,
                                    usuarios_cargo);
                            pedidoFactura.limpiarLista ();
                            for (int i = 0; i < datos.length (); i++) {
                                JSONObject plato = datos.getJSONObject (i);
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
                                        platos_imagen

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
      //  this.listaPlatos.setOnDragListener(this);

        //this.listaPedidos.setOnDragListener(this);

        return v;
    }

    public void recibirMesa(Mesa mesa)
    {

        Toast.makeText(getContext (),"Mesa ha sido ocupada", Toast.LENGTH_SHORT).show();
        Bundle objetoPlato = getArguments();

        if(objetoPlato !=null) {
            numeroMesa.setText(mesa.getNumero());
            int idmesa = mesa.getIdmesa();
        }
    }

}