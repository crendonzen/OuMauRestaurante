package com.example.myapplication.interfaz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.myapplication.R;


public class HomeFragment extends Fragment {
  PlatosFragment platosFragment;
    PedidosFragment pedidosFragment;

    private String rolUsuario;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);


        recuperarPreferencias();

        ImageButton agregarPedido = root.findViewById(R.id.botonAgregar);

        agregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment pedidosFragment = new PedidosFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, pedidosFragment);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();
            }
        });

        ImageButton pedidos = root.findViewById(R.id.botonPedidos);

        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Navigation.findNavController(v).navigate(R.id.nav_slideshow);
                Toast.makeText(getContext(), "Modulo en desarrollo", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton platos = root.findViewById(R.id.botonPlatos);

        platos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rolUsuario.equals("Admin"))
                {
                    Fragment platosFragment = new PlatosFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, platosFragment);
                    transaction.addToBackStack(null);

                    // Commit a la transacción
                    transaction.commit();
                }
                else
                {
                    Toast.makeText(getContext(), "No tiene permisos para acceder a Menu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }







    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void recuperarPreferencias()
    {
        SharedPreferences preferences= getContext ().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        boolean sesion=preferences.getBoolean("sesion",false);
        if(sesion)
        {
            this.rolUsuario=preferences.getString("rol", "No hay nada");
        }
    }

}