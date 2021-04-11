package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.interfaz.MenuPlatosFragment;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Pedido;
import com.example.myapplication.mundo.Plato;

import java.util.ArrayList;
import java.util.Random;

import kotlin.reflect.KVisibility;

public class AdaptadorListaPedidos extends  RecyclerView.Adapter<AdaptadorListaPedidos.ViewHolder>  implements View.OnClickListener
{
    private Plato plato;
    private ArrayList<Pedido> list;
    private MenuPlatosFragment buscarPlato;
    private Context contexto;
    private static LayoutInflater  inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaPedidos(Context contexto, ArrayList<Pedido> lista)
    {
        this.list=lista;
        this.contexto = contexto;
        inflater = LayoutInflater.from(contexto);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_pedidos_platos, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        plato = list.get(position);

        holder.txtNombre.setText(list.get(position).getNombre ());
       // holder.txtPrecio.setText(list.get(position).getPrecio ()+"");
        holder.txtCantidadPlato.setText(list.get(position).getCantidad ()+"");
        String image = list.get (position).getImage ();
        if (!holder.imgPlatos.isShown ())
        {
            Glide.get(contexto).clearMemory();
            Glide.with (contexto)
                    .load (image )
                   // .diskCacheStrategy(DiskCacheStrategy.NONE)
                   // .skipMemoryCache(true)
                   // .fitCenter()
                    .into (holder.imgPlatos);
        }
        holder.item.setTag (position);
        holder.item.setOnLongClickListener (new View.OnLongClickListener ()
        {
            @Override
            public boolean onLongClick(View view)
            {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        if (!list.get(position).getObsevacion ().isEmpty ())
        {
            holder.imgInfo.setVisibility (View.VISIBLE);
        }else
        {
            holder.imgInfo.setVisibility (View.GONE);
        }

    }

    @Override
    public int getItemCount() {
            return list.size();
    }
    public void setFragment(MenuPlatosFragment buscarPlato)
    {
        this.buscarPlato=buscarPlato;
    }

    @Override
    public void onClick(View v)
    {
        if(listener!=null)
        {
            listener.onClick(v);
        }
    }
    public void setOnclickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout item;
        TextView txtNombre,txtPrecio,txtCategoria,txtObservacion,txtCantidadPlato;
        ImageView imgInfo;

        ImageView imgPlatos;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombre=(TextView) itemView.findViewById(R.id.txtNombrePlato);
            txtPrecio=(TextView) itemView.findViewById(R.id.txtPrecio);
            txtCantidadPlato=(TextView) itemView.findViewById(R.id.txtCantidadPlato);
            imgPlatos=(ImageView) itemView.findViewById(R.id.imgPlatos);
            item=(ConstraintLayout) itemView.findViewById (R.id.itemPlatoPedido);
            imgInfo=(ImageView) itemView.findViewById(R.id.imgInfo);
        }
    }



    public ArrayList<Pedido> getList()
    {
        return this.list;
    }


}
