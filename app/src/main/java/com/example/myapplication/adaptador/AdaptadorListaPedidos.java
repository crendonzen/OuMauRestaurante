package com.example.myapplication.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.MenuPlatosFragment;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Plato;

import java.util.ArrayList;

public class AdaptadorListaPedidos extends  RecyclerView.Adapter<AdaptadorListaPedidos.ViewHolder>
{

    private Plato plato;
    private ArrayList<Plato> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;


    public AdaptadorListaPedidos(Context conexto, ArrayList<Plato> lista)
    {
        this.list=lista;
        inflater = (LayoutInflater ) conexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.fragment_lista_pedidos_platos, null);
        return new AdaptadorListaPedidos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        plato = this.list.get(position);
        holder.txtNombre.setText(list.get(position).getNombre ());
        holder.txtPrecio.setText(list.get(position).getPrecio ()+"");
        Glide.with(inflater.getContext ())
                .load(list.get(position).getImage ())
                .into(holder.imgPlatos);

    }

    @Override
    public int getItemCount() {
            return list.size();
    }
    public void setFragment(MenuPlatosFragment buscarPlato)
    {
        this.buscarPlato=buscarPlato;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNombre,txtPrecio,txtCategoria,txtObservacion;
        Spinner spnCantidad;
        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombre=(TextView) itemView.findViewById(R.id.txtNombrePlato);
            txtPrecio=(TextView) itemView.findViewById(R.id.txtPrecio);
            spnCantidad=(Spinner) itemView.findViewById(R.id.spnCantidad);
            imgPlatos=(ImageView) itemView.findViewById(R.id.imgPlatos);
        }
    }



    public ArrayList<Plato> getList()
    {
        return this.list;
    }


}
