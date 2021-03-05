package com.example.myapplication.interfaz;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPedidos;
import com.example.myapplication.adaptador.AdaptadorListaPlatos;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Mesa;
import com.example.myapplication.mundo.Plato;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuPlatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuPlatosFragment extends Fragment implements View.OnDragListener
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private SearchView buscarPlato;
    private RecyclerView listaPlatos;
    private RecyclerView listaPedidos;

    private AdaptadorListaPlatos adaptadorListaPlatos;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private ArrayList<Plato> platosMenu;
    private Factura pedidoFactura;
    private boolean isDropped = false;
    private Listener mListener;
    TextView numeroMesa;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private LinearLayout cardView;
    private static final String CARD_VIEW_TAG = "DRAG CARDVIEW";

    public MenuPlatosFragment()
    {

    }

    public static MenuPlatosFragment newInstance(String param1, String param2)
    {
        MenuPlatosFragment fragment = new MenuPlatosFragment ();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_menu_platos, container, false);
        this.pedidoFactura=new Factura ();
        this.buscarPlato = v.findViewById(R.id.searchBuscarPlato);
        this.listaPlatos = v.findViewById(R.id.listaPlatosMesas);
        this.listaPedidos = v.findViewById(R.id.listaPlatos);

        this.requestQueue =  VolleySingleton.getInstance(getContext ()).getRequestQueue();
        this.platosMenu = new ArrayList<Plato>();
        this.adaptadorListaPlatos = new AdaptadorListaPlatos (getContext (),this.platosMenu);
        this.adaptadorListaPedidos = new AdaptadorListaPedidos (getContext (),this.pedidoFactura.getPlatos ());

        this.listaPlatos.setLayoutManager(new GridLayoutManager(getContext(),5));
        this.listaPlatos.setAdapter(this.adaptadorListaPlatos);
        this.listaPedidos.setLayoutManager(new GridLayoutManager(getContext(),5));
        this.listaPedidos.setAdapter(this.adaptadorListaPedidos);

        this.buscarPlato.setOnQueryTextListener (new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String query) { return false;  }
            @Override
            public boolean onQueryTextChange(final String newText)
            {
                if(!newText.isEmpty())
                {
                    Map<String,String> params= new HashMap<String, String>();
                    params.put("buscarPlatos",newText);
                    JSONObject parameters = new JSONObject(params);
                    String url="http://openm.co/consultas/pedidos.php";
                    jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            platosMenu.clear();
                            try
                            {
                                listaPlatos.setAdapter(adaptadorListaPlatos);
                                JSONArray datos = response.getJSONArray ("datos");
                                for (int i = 0; i < datos.length(); i++)
                                {
                                    JSONObject plato = datos.getJSONObject(i);
                                    int idPlato=plato.getInt ("idplatos");
                                    String categoria=plato.getString("categoria");
                                    String nombre=plato.getString("nombre");
                                    String descripcion=plato.getString("descripcion");
                                    Double precio=plato.getDouble("precio");
                                    String image=plato.getString ("imagen");
                                    Plato m=new Plato( idPlato, categoria,  nombre, descripcion,precio,image);
                                    platosMenu.add (m);
                                }
                            } catch (JSONException e)
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
//                            Toast.makeText(getContext (), "El usuario no esta registrado o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(jsonRequest);
                }
                return false;
            }
        });


        numeroMesa= v.findViewById(R.id.numeroMesa);
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
                            for (int i = 0; i < datos.length (); i++)
                            {
                                JSONObject plato = datos.getJSONObject (i);
                                String platos_imagen = plato.getString ("platos_imagen");
                                double platos_precio = plato.getDouble ("platos_precio");
                                String platos_descripcion = plato.getString ("platos_descripcion");
                                String platos_nombre = plato.getString ("platos_nombre");
                                String platos_categoria = plato.getString ("platos_categoria");
                                int platos_idplatos = plato.getInt ("platos_idplatos");
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
        this.listaPlatos.setOnDragListener(this);
        this.listaPedidos.setOnDragListener(this);

        return v;
    }

    public interface Listener
    {
        void setEmptyList(boolean visibility);
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
                    positionFuente = (int) viewSource.getTag();
                    Plato customList = (Plato) adaptadorListaPlatos.getList ().get(positionFuente);

                    Toast.makeText(getContext (), customList.getNombre (), Toast.LENGTH_SHORT).show();
                    pedidoFactura.agregarPlato (customList);
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