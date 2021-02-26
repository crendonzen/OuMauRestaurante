package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallePlatoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallePlatoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String URL = "http://192.168.56.1/restaurante/platos/consultar_plato.php";
    TextView nombrePlato,precioPlato;

    public DetallePlatoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetallePlatoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetallePlatoFragment newInstance(String param1, String param2) {
        DetallePlatoFragment fragment = new DetallePlatoFragment();
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

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detalle_plato, container, false);


        nombrePlato= view.findViewById(R.id.editTextNombrePlato);
        precioPlato= view.findViewById(R.id.editTextPrecioPlato);

        Bundle objetoPlato = getArguments();
        Platos platos = null;
        //validacion para verificar si existen argumentos para mostrar
        if(objetoPlato !=null){

            platos = (Platos) objetoPlato.getSerializable("objetoPlato");

            nombrePlato.setText(platos.getNombrePlato());
            precioPlato.setText(String.valueOf(platos.getPrecioPlato()));

        }

       return view;
    }*/
}