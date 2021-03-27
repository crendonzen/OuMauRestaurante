package com.example.myapplication.adaptador;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Plato;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PDFAdapter extends PrintDocumentAdapter
{
    private final Context context;
    private final String destino;
    private String path;
    private int repeticion=0;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private Factura pedidoFactura;
    private RequestQueue requestQueue;
    private JsonRequest jsonRequest;

    public PDFAdapter(Context context, String s, Factura factura,String destino, AdaptadorListaPedidos listaPedidos,RequestQueue requestQueue)
    {

        this.context=context;
        this.path=s;
        this.pedidoFactura=factura;
        this.adaptadorListaPedidos=listaPedidos;
        this.requestQueue=requestQueue;
        this.destino=destino;

    }
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        if (cancellationSignal.isCanceled ())
        {
            callback.onLayoutCancelled ();
        }else
        {

            PrintDocumentInfo.Builder builder=new PrintDocumentInfo.Builder ("file name");
            builder.setContentType (PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount (PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build ();
            callback.onLayoutFinished (builder.build (),!newAttributes.equals (oldAttributes));

            if (this.repeticion==1&& this.destino.equals ("caja"))
            {
                String data = new Gson ().toJson (pedidoFactura.getPedidos ());

                HashMap<String, String> params = new HashMap<String, String> ();
                params.put ("pagarPedido", data);
                params.put ("idfactura", pedidoFactura.getFactura_idfacturas () + "");
                params.put ("idmesa", pedidoFactura.getMesas_idmesas ()+ "");
                JSONObject parameters = new JSONObject (params);

                String url = "http://openm.co/consultas/pedidos.php";

                jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {

                            JSONArray datos = response.getJSONArray ("datos");
                            pedidoFactura.limpiarLista ();
                            adaptadorListaPedidos.notifyDataSetChanged ();
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                    }
                }, new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace ();
                    }
                });
                requestQueue.add (jsonRequest);
                adaptadorListaPedidos.notifyDataSetChanged ();
            }
            repeticion=repeticion+1;
        }

    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        InputStream in=null;
        OutputStream out = null;
        try
        {
            File file=new File (path);
            in=new FileInputStream (file);
            out=new FileOutputStream (destination.getFileDescriptor ());
            byte[] buff=new byte[15633];
            int size;
            while ((size=in.read (buff))>=0&&!cancellationSignal.isCanceled ())
            {
                out.write (buff,0,size);
            }
            if (cancellationSignal.isCanceled ())
            {
                callback.onWriteCancelled ();
            }else
            {
                callback.onWriteFinished (new PageRange[]{PageRange.ALL_PAGES});
            }
        }catch (FileNotFoundException e)
        {
            e.printStackTrace ();
        }catch (IOException e){
            e.printStackTrace ();
        }finally {
            try {
                in.close ();
                out.close ();
            }catch (IOException e){
                e.printStackTrace ();
            }
        }
    }
}
