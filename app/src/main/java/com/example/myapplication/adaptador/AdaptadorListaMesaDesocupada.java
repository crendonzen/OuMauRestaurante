package com.example.myapplication.adaptador;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interfaz.PedidosMesaFragment;
import com.example.myapplication.mundo.Mesa;

import java.util.ArrayList;
import java.util.Map;

public class AdaptadorListaMesaDesocupada  extends  RecyclerView.Adapter<AdaptadorListaMesaDesocupada.ViewHolder> implements View.OnClickListener{

    private Mesa proyecto;
    private ArrayList<Mesa> list;
    private PedidosMesaFragment buscarMesa;
    private Context contexto;
    private static LayoutInflater inflater = null;
    private View.OnClickListener listener;
    @Override
    public void onClick(View v) {
        if(listener!=null)
        {
            listener.onClick(v);
        }
    }
     public AdaptadorListaMesaDesocupada(Context conexto, ArrayList<Mesa> lista){
         this.list=lista;
         this.contexto = conexto;
         inflater = (LayoutInflater ) conexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_lista_mesas_desocupadas, null);
        view.setOnClickListener(this);
        return new AdaptadorListaMesaDesocupada.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        proyecto = this.list.get(position);
        holder.txtNumeroMesaDesocupada.setText(String.valueOf(list.get(position).getNumero ()));
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
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setFragment(PedidosMesaFragment buscarMesa)
    {
        this.buscarMesa=buscarMesa;
    }

    public ArrayList<Mesa> getList()
    {
        return this.list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item;
        TextView txtNumeroMesaDesocupada;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumeroMesaDesocupada=(TextView)itemView.findViewById(R.id.txtNombreMesaLibre);
            item=(ConstraintLayout) itemView.findViewById (R.id.itemMesaDesocupada);
        }
    }
    public void metodocualquiera()
    {
        //Este commit

    }
}
