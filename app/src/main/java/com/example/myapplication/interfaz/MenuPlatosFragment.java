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
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuPlatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuPlatosFragment extends Fragment implements View.OnDragListener, View.OnLongClickListener
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private SearchView buscarPlato;
    private RecyclerView listaPlatos;
    private RecyclerView listaPedidos;

    private AdaptadorListaPlatos adaptadorListaPlatos;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private ArrayList<Plato> platos;
    private ArrayList<Factura> pedidos;
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
        this.buscarPlato = v.findViewById(R.id.searchBuscarPlato);
        this.listaPlatos = v.findViewById(R.id.listaPlatosMesas);
        this.listaPedidos = v.findViewById(R.id.listaPlatos);


        this.requestQueue = Volley.newRequestQueue(getContext());
        this.platos = new ArrayList<Plato>();
        this.pedidos = new ArrayList<Factura> ();

        this.adaptadorListaPlatos = new AdaptadorListaPlatos (getContext (),this.platos);
        this.adaptadorListaPedidos = new AdaptadorListaPedidos (getContext (),this.pedidos);

        this.listaPlatos.setLayoutManager(new GridLayoutManager(getContext(),4));
        this.listaPlatos.setAdapter(this.adaptadorListaPlatos);

        this.listaPedidos.setLayoutManager(new GridLayoutManager(getContext(),4));
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
                    String url="https://openm.co/consultas/pedidos.php";
                    jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            platos.clear();
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
                                    platos.add (m);
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
            String url="https://openm.co/consultas/pedidos.php";
            jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    pedidos.clear();
                    try
                    {
                        listaPedidos.setAdapter(adaptadorListaPedidos);
                        JSONArray datos = response.getJSONArray ("datos");
                        for (int i = 0; i < datos.length(); i++)
                        {
                            SimpleDateFormat format=new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                            JSONObject plato = datos.getJSONObject(i);
                            int mesas_idmesas=plato.getInt("mesas_idmesas");
                            String mesas_numero=plato.getString("mesas_numero");
                            String estado=plato.getString("estado");
                            double factura_pagado=plato.getDouble ("factura_pagado");
                            double factura_IVA=plato.getDouble ("factura_IVA");
                            String factura_fecha=plato.getString ("factura_fecha");
                            int factura_idfacturas=plato.getInt ("factura_idfacturas");
                            String pedidos_observacion=plato.getString("pedidos_observacion");
                            int pedidos_cantidad=plato.getInt("pedidos_cantidad");
                            int pedidos_idpedidos=plato.getInt ("pedidos_idpedidos");
                            int usuarios_idempleado=plato.getInt ("usuarios_idempleado");
                            String usuarios_identificacion=plato.getString("usuarios_identificacion");
                            String usuarios_nombres=plato.getString("usuarios_nombres");
                            String usuarios_apellidos=plato.getString("usuarios_apellidos");
                            String usuarios_telefono=plato.getString("usuarios_telefono");
                            String usuarios_cargo=plato.getString("usuarios_cargo");
                            String platos_imagen=plato.getString("platos_imagen");
                            double platos_precio=plato.getDouble("platos_precio");
                            String platos_descripcion=plato.getString("platos_descripcion");
                            String platos_nombre=plato.getString("platos_nombre");
                            String platos_categoria=plato.getString("platos_categoria");
                            int platos_idplatos=plato.getInt ("platos_idplatos");
                            Toast.makeText(getContext (), plato.getString("mesas_numero"), Toast.LENGTH_SHORT).show();
                            Factura factura=new Factura ( mesas_idmesas,
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
                                    usuarios_cargo,
                                    platos_imagen,
                                    platos_precio,
                                    platos_descripcion,
                                    platos_nombre,
                                    platos_categoria,
                                    platos_idplatos);

                            pedidos.add (factura);

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
                    Toast.makeText(getContext (), "El usuario no esta registrado o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonRequest);
        }
/*
        cardView =  v1.findViewById(R.id.linearLayout5);
        cardView.setTag(CARD_VIEW_TAG);
        cardView.setOnLongClickListener(this);*/

        v.findViewById(R.id.listaPlatos).setOnDragListener(this);
        v.findViewById(R.id.listaPlatosMesas).setOnDragListener(this);

        return v;
    }


    @Override
    public boolean onDrag(View view, DragEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case DragEvent.ACTION_DRAG_STARTED:

                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                {
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                view.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                view.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                view.getBackground().clearColorFilter();
                view.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData = item.getText().toString();
                Toast.makeText(getContext (), "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                view.getBackground().clearColorFilter();
                view.invalidate();
                View v = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);//remove the dragged view
                CardView container = (CardView) view;
                container.addView(v);
                v.setVisibility(View.VISIBLE);
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                view.getBackground().clearColorFilter();
                view.invalidate();
                if (event.getResult())
                    Toast.makeText(getContext (), "The drop was handled.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext() , "The drop didn't work.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view)
    {
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        view.setVisibility(View.INVISIBLE);
        return true;
    }
}