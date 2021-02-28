package com.example.myapplication.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaz.PedidosMesaFragment;
import com.example.myapplication.mundo.Mesa;

import java.util.ArrayList;

public class AdaptadorListaMesa extends  RecyclerView.Adapter<AdaptadorListaMesa.ViewHolder> implements View.OnClickListener
{
    private Mesa proyecto;
    private ArrayList<Mesa> list;
    private PedidosMesaFragment buscarMesa;
    private Context contexto;
    private static LayoutInflater  inflater = null;
    private View.OnClickListener listener;

    public AdaptadorListaMesa(Context conexto, ArrayList<Mesa> lista)
    {
        this.list=lista;
        this.contexto = conexto;
        inflater = (LayoutInflater ) conexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.fragment_lista_pedidos_mesa, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        proyecto = this.list.get(position);

        holder.txtNumeroMesa.setText(String.valueOf(list.get(position).getNumero()));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setFragment(PedidosMesaFragment buscarMesa)
    {
        this.buscarMesa=buscarMesa;
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
    public class ViewHolder  extends RecyclerView.ViewHolder
    {
        TextView txtNumeroMesa;

        public ViewHolder (@NonNull View itemView)
        {
            super(itemView);
            txtNumeroMesa=(TextView) itemView.findViewById(R.id.txtNombrePlato);
        }
    }


}
