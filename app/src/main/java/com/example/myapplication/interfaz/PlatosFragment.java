package com.example.myapplication.interfaz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPlatosMenu;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.mundo.Plato;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlatosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private SearchView buscarPlato;
    private RecyclerView listaPlatos;
    private Activity actividad;
    private InterfazFragamen interfazFragamen;

    private ArrayList<Plato> platosMenu;
    private AdaptadorListaPlatosMenu adaptadorListaPlatosMenu;

    ArrayList<Plato> platoslist;
    RecyclerView recyclerView;


    public PlatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlatosFragment newInstance(String param1, String param2) {
        PlatosFragment fragment = new PlatosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_platos, container, false);
        ImageButton agregarPlato = v.findViewById(R.id.botonAgregarPlato);
        this.listaPlatos = v.findViewById(R.id.listaPlatosCarta);
        this.buscarPlato = v.findViewById(R.id.buscar_plato);

        this.requestQueue =  VolleySingleton.getInstance(getContext ()).getRequestQueue();
        this.platosMenu = new ArrayList<Plato>();
        this.adaptadorListaPlatosMenu = new AdaptadorListaPlatosMenu (getContext (),this.platosMenu);
        listaPlatos.setAdapter(adaptadorListaPlatosMenu);
        this.listaPlatos.setLayoutManager(new GridLayoutManager(getContext(),2));


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

                                listaPlatos.setAdapter(adaptadorListaPlatosMenu);
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

                                adaptadorListaPlatosMenu = new AdaptadorListaPlatosMenu (getContext (),platosMenu);
                                listaPlatos.setAdapter(adaptadorListaPlatosMenu);
                                adaptadorListaPlatosMenu.setOnclickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {

                                        String numeroMesa = platosMenu.get(listaPlatos.getChildAdapterPosition(v)).getNombre();
                                        Toast.makeText(getContext (), "El usuario no esta registrado o contraseña incorrecta", Toast.LENGTH_SHORT).show();

                                        //enviar mediante la interface el objeto seleccionado al detalle
                                        //se envia el objeto completo
                                        //se utiliza la interface como puente para enviar el objeto seleccionado
                                        interfazFragamen.enviarPlato(platosMenu.get(listaPlatos.getChildAdapterPosition(v)));
                                        //luego en el mainactivity se hace la implementacion de la interface para implementar el metodo enviarpersona


                                    }
                                });

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


        agregarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_platosFragment_to_agregarPlatoFragment2);
            }
        });



        recyclerView = v.findViewById(R.id.listaPlatos);

        platoslist = new ArrayList<>();

      //  cargarPlatos();


        return v;
    }


  /*  public void cargarPlatos() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject plato = array.getJSONObject(i);

                        platoslist.add(new Platos(
                                plato.getString("nombre"),
                                plato.getDouble("precio")
                        ));

                        adaptadorPlato = new AdaptadorPlato(getContext(), platoslist);
                        recyclerView.setAdapter(adaptadorPlato);
                        adaptadorPlato.setOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nombre = platoslist.get(recyclerView.getChildAdapterPosition(v)).getNombrePlato();

                                Toast.makeText(getContext(), "Seleccionó: " + platoslist.get(recyclerView.getChildAdapterPosition(v)).getNombrePlato(), Toast.LENGTH_SHORT).show();
                                //enviar mediante la interface el objeto seleccionado al detalle
                                //se envia el objeto completo
                                //se utiliza la interface como puente para enviar el objeto seleccionado
                                interfaceComunicaFragments.enviarPlato(platoslist.get(recyclerView.getChildAdapterPosition(v)));
                                //luego en el mainactivity se hace la implementacion de la interface para implementar el metodo enviarpersona

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);



    }

    private void mostrarData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptadorPlato = new AdaptadorPlato(getContext(), platoslist);
        recyclerView.setAdapter(adaptadorPlato);

    }*/


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            //voy a decirle a mi actividad que sea igual a dicho contesto. castin correspondiente:
            this.actividad = (Activity) context;
            ////que la interface icomunicafragments sea igual ese contexto de la actividad:
            interfaceComunicaFragments = (iComunicaPlatosFragments) this.actividad;
            //esto es necesario para establecer la comunicacion entre la lista y el detalle
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }*/


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity)
        {
            this.actividad = (Activity) context;
            this.interfazFragamen = (InterfazFragamen) this.actividad;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}