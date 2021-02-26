package com.example.myapplication.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.mundo.Plato;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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


    private static final String URL = "http://192.168.56.1/restaurante/platos/consultar_plato.php";

    ArrayList<Plato> platoslist;
    RecyclerView recyclerView;

    Activity actividad;
    LinearLayoutManager lm;


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


        agregarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_platosFragment_to_agregarPlatoFragment2);
            }
        });



        recyclerView = v.findViewById(R.id.listaPlatos);

        platoslist = new ArrayList<>();

      //  cargarPlatos();
     //   mostrarData();

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
}