package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgregarPlatoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgregarPlatoFragment extends Fragment {


    EditText nombrePlato,precioPlato;
    Button agregar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgregarPlatoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgregarPlatoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgregarPlatoFragment newInstance(String param1, String param2) {
        AgregarPlatoFragment fragment = new AgregarPlatoFragment();
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
        View v= inflater.inflate(R.layout.fragment_agregar_plato, container, false);



        nombrePlato = (EditText) v.findViewById(R.id.textNombrePlato);
        precioPlato = (EditText) v.findViewById(R.id.textPrecioPlato);
        agregar =(Button) v.findViewById(R.id.botonAgregarPlato);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarPlato();
                limpiar();
                Navigation.findNavController(v).navigate(R.id.action_agregarPlatoFragment_to_platosFragment);
            }
        });

        return v;
    }

    public void agregarPlato()
    {
        final String nombre=nombrePlato.getText().toString();
        final double precio= Double.parseDouble(precioPlato.getText().toString());

        String URL="http://192.168.56.1//restaurante/platos/ingreso.php?nombre="+nombre+"&precio="+precio+"";
        RequestQueue servicio= Volley.newRequestQueue(getContext());
        StringRequest respuesta= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty())
                {

                }else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        servicio.add(respuesta);
    }

    public void limpiar(){
        nombrePlato.setText("");
        precioPlato.setText("");
    }

}