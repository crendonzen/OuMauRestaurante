package com.example.myapplication.interfaz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPedidos;
import com.example.myapplication.adaptador.AdaptadorListaPlatos;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Plato;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private AdaptadorListaPlatos adaptadorListaPlatos;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private ArrayList<Plato> platosMenu;
    ArrayList<Plato> platoslist;
    private Factura pedidoFactura;
    private boolean isDropped = false;

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
        this.requestQueue =  VolleySingleton.getInstance(getContext ()).getRequestQueue();
        this.platosMenu = new ArrayList<Plato>();
        this.adaptadorListaPlatos = new AdaptadorListaPlatos (getContext (),this.platosMenu);
        this.adaptadorListaPedidos = new AdaptadorListaPedidos (getContext (),this.pedidoFactura.getPedidos ());

        this.listaPlatos.setLayoutManager(new GridLayoutManager(getContext(),6));
        this.listaPlatos.setAdapter(this.adaptadorListaPlatos);
        this.listaPlatos.setOnDragListener(this);

        this.buscarPlato.setOnQueryTextListener (new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String query) { return false;  }
            @Override
            public boolean onQueryTextChange(final String platoNombre)
            {
                cargarPlatos(platoNombre);
                return false;
            }
        });

        platoslist = new ArrayList<>();
        cargarPlatos("");

        return v;

    }
    public void cargarPlatos(String platoNombre)
    {
        Map<String,String> params= new HashMap<String, String>();
        params.put("buscarPlatos",platoNombre);
        JSONObject parameters = new JSONObject(params);
        String url="http://"+ Servidor.HOST +"/consultas/platos.php";
        jsonRequest=new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                platosMenu.clear();
                try
                {
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
                    adaptadorListaPlatos.notifyDataSetChanged ();
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
            }
        });
        requestQueue.add(jsonRequest);
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
              //  v.setBackgroundColor(Color.LTGRAY);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
           //    v.setBackgroundColor(Color.YELLOW);
                break;
            case DragEvent.ACTION_DROP:

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