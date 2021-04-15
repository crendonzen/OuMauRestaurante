package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.MenuPlatosFragment;
import com.example.myapplication.mundo.Plato;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AdaptadorListaPlatosMenu extends  RecyclerView.Adapter<AdaptadorListaPlatosMenu.ViewHolder> implements View.OnClickListener
{

    private Plato plato;
    private ArrayList<Plato> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private  LayoutInflater  inflater;
    private View.OnClickListener listener;

    public AdaptadorListaPlatosMenu(Context contexto, ArrayList<Plato> lista)
    {
        this.list=lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_platos, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        try
        {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
            plato = this.list.get(position);
            String image = plato.getImage ();
            if (!image.isEmpty ())
            {
                Glide.get(contexto).clearMemory();
                Glide.with (contexto)
                        .load (image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .fitCenter()
                        .into (holder.imgPlatos);
            }
            holder.txtNombrePlato.setText(list.get(position).getNombre());
            holder.txtPrecioPlato.setText(String.valueOf(nf.format(list.get(position).getPrecio())));
            holder.txtCategoriaPlato.setText(list.get(position).getCategoria());
            holder.txtEstado.setText(list.get(position).getEstado ());

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        TextView txtNombrePlato,txtPrecioPlato,txtCategoriaPlato,txtEstado;
        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombrePlato=(TextView) itemView.findViewById(R.id.txtNombrePlato_menu);
            txtPrecioPlato=(TextView) itemView.findViewById(R.id.txtPrecio_menu);
            txtEstado=(TextView) itemView.findViewById(R.id.txtEstado);
            txtCategoriaPlato=(TextView) itemView.findViewById(R.id.categoria_menu);
            imgPlatos=(ImageView) itemView.findViewById(R.id.imgPlatos_menu);

        }
    }




}
