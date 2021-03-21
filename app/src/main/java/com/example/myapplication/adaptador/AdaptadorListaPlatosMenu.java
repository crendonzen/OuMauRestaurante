package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.MenuPlatosFragment;
import com.example.myapplication.mundo.Plato;

import java.util.ArrayList;

public class AdaptadorListaPlatosMenu extends  RecyclerView.Adapter<AdaptadorListaPlatosMenu.ViewHolder> implements View.OnClickListener
{

    private Plato plato;
    private ArrayList<Plato> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaPlatosMenu(Context conexto, ArrayList<Plato> lista)
    {
        this.list=lista;
        inflater = (LayoutInflater ) conexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.fragment_lista_platos, null);
        view.setOnClickListener(this);
        return new AdaptadorListaPlatosMenu.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        plato = this.list.get(position);
        holder.txtNombrePlato.setText(list.get(position).getNombre());
        holder.txtPrecioPlato.setText(String.valueOf(list.get(position).getPrecio()));

        Glide.with(inflater.getContext ())
                .load(list.get(position).getImage())
                .into(holder.imgPlatos);



    }
    public ArrayList<Plato> getList()
    {
        return this.list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtNombrePlato,txtPrecioPlato,txtCategoriaPlato,txtDescripcionPlato;
        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombrePlato=(TextView) itemView.findViewById(R.id.txtNombrePlato_menu);
            txtPrecioPlato=(TextView) itemView.findViewById(R.id.txtPrecio_menu);

            txtCategoriaPlato=(TextView) itemView.findViewById(R.id.categoria_menu);
            imgPlatos=(ImageView) itemView.findViewById(R.id.imgPlatos_menu);

        }
    }



}
