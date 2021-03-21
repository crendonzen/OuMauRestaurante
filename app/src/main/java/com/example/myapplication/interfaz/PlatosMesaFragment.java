package com.example.myapplication.interfaz;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.InputType;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPedidos;
import com.example.myapplication.adaptador.AdaptadorListaPlatos;
import com.example.myapplication.adaptador.PDFAdapter;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.adaptador.common;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Mesa;
import com.example.myapplication.mundo.Pedido;
import com.example.myapplication.mundo.Plato;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlatosMesaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlatosMesaFragment extends Fragment implements View.OnDragListener
{
    protected RequestQueue requestQueue;
    protected JsonRequest jsonRequest;
    private RecyclerView listaPedidos;
    private AdaptadorListaPedidos adaptadorListaPedidos;
    private ArrayList<Plato> platosMenu;
    private Factura pedidoFactura;
    private boolean isDropped = false;
    private MenuPlatosFragment.Listener mListener;
    private TextView numeroMesa;
    private ImageButton btnActualizarPedido;
    private ImageButton btnFactura;
    private ImageButton btnCocina;
    private Dialog mDialog;



    public PlatosMesaFragment()
    {

    }

    public static PlatosMesaFragment newInstance(String param1, String param2)
    {
        PlatosMesaFragment fragment = new PlatosMesaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_platos_mesa, container, false);
        this.pedidoFactura = new Factura();
        this.listaPedidos = v.findViewById(R.id.lista_pedidos);
        this.btnFactura = v.findViewById(R.id.imagenFactura);
        this.btnCocina = v.findViewById(R.id.imagenCocina);
        this.requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        this.adaptadorListaPedidos = new AdaptadorListaPedidos(getContext(), this.pedidoFactura.getPedidos ());
        this.listaPedidos.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.listaPedidos.setAdapter(this.adaptadorListaPedidos);
        this.btnActualizarPedido = v.findViewById(R.id.btnActualizarPedido);
/*
        this.btnActualizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });*/
        this.adaptadorListaPedidos.setOnclickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                Plato plato= pedidoFactura.getPedidos ().get (listaPedidos.getChildAdapterPosition (v));

                crearObservacion((Pedido) plato);

            }
        });

        numeroMesa = v.findViewById(R.id.numero_Mesa);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle)
            {
                Mesa mesa = (Mesa) bundle.getSerializable("mesa");
                numeroMesa.setText(mesa.getNumero());
                int idmesa = mesa.getIdmesa();
                Map<String, String> params = new HashMap<String, String>();
                Toast.makeText(getContext(), "" + idmesa, Toast.LENGTH_SHORT).show();
                params.put("buscarPlatoMesa", idmesa + "");
                JSONObject parameters = new JSONObject(params);
                String url = "http://openm.co/consultas/pedidos.php";

                jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            listaPedidos.setAdapter(adaptadorListaPedidos);
                            pedidoFactura.limpiarLista();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            JSONArray datos = response.getJSONArray("datos");
                            if (datos.length() > 0)
                            {
                                JSONObject pedido = datos.getJSONObject(0);
                                int mesas_idmesas = pedido.getInt("mesas_idmesas");
                                String mesas_numero = pedido.getString("mesas_numero");
                                String estado = pedido.getString("estado");
                                double factura_pagado = pedido.getDouble("factura_pagado");
                                double factura_IVA = pedido.getDouble("factura_IVA");
                                String factura_fecha = pedido.getString("factura_fecha");
                                int factura_idfacturas = pedido.getInt("factura_idfacturas");
                                int usuarios_idempleado = pedido.getInt("usuarios_idempleado");
                                String usuarios_identificacion = pedido.getString("usuarios_identificacion");
                                String usuarios_nombres = pedido.getString("usuarios_nombres");
                                String usuarios_apellidos = pedido.getString("usuarios_apellidos");
                                String usuarios_telefono = pedido.getString("usuarios_telefono");
                                String usuarios_cargo = pedido.getString("usuarios_cargo");

                                pedidoFactura.inicializarPedidos(mesas_idmesas,
                                        mesas_numero,
                                        estado,
                                        factura_pagado,
                                        factura_IVA,
                                        format.parse(factura_fecha),
                                        factura_idfacturas,
                                        usuarios_idempleado,
                                        usuarios_identificacion,
                                        usuarios_nombres,
                                        usuarios_apellidos,
                                        usuarios_telefono,
                                        usuarios_cargo);
                                String nombrePlato=datos.getJSONObject(0).getString ("platos_nombre");
                                for (int i = 0; i < datos.length() && !nombrePlato.equals ("null"); i++)
                                {
                                    JSONObject plato = datos.getJSONObject(i);
                                    int pedidos_cantidad = plato.getInt("pedidos_cantidad");
                                    String platos_imagen = plato.getString("platos_imagen");
                                    double platos_precio = plato.getDouble("platos_precio");
                                    String platos_descripcion = plato.getString("platos_descripcion");
                                    String platos_nombre = plato.getString("platos_nombre");
                                    String platos_categoria = plato.getString("platos_categoria");
                                    int platos_idplatos = plato.getInt("platos_idplatos");
                                    String pedidos_observacion = plato.getString("pedidos_observacion");

                                    Toast.makeText(getContext(), plato.getString("mesas_numero"), Toast.LENGTH_SHORT).show();

                                    Pedido pedidoDatos = new Pedido (
                                            platos_idplatos,
                                            platos_categoria,
                                            platos_nombre,
                                            platos_descripcion,
                                            platos_precio,
                                            platos_imagen,
                                            pedidos_cantidad
                                    );

                                    pedidoDatos.setObsevacion (pedidos_observacion);
                                    pedidoFactura.agregarPedido (pedidoDatos);
                                }
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                requestQueue.add(jsonRequest);
            }
        });

        this.listaPedidos.setOnDragListener(this);
        this.btnActualizarPedido.setOnDragListener(this);

        Dexter.withActivity(getActivity ()).withPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener ()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response)
            {
                btnFactura.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        crearPDFFactura (common.getRutaRaiz(getContext ())+"ticket.pdf");
                    }
                });
                btnCocina.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        crearPDFCocina (common.getRutaRaiz(getContext ())+"ticket.pdf");
                    }
                });
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response){}
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token){}
        }).check ();
        return v;

    }
    private void crearPDFFactura(String path)
    {
        if (new File (path).exists ())
        {
            new File (path).delete ();
        }
        try
        {
            if (this.pedidoFactura.hayPedidos())
            {
                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                SimpleDateFormat format=new SimpleDateFormat ("dd/MM/yyyy");
                Document document=new Document ();
                PdfWriter.getInstance (document, new FileOutputStream (path));
                document.open ();
                document.setPageSize (PageSize.NOTE);
                document.addCreationDate ();
                document.addAuthor ("Open");
                document.addAuthor ("user");
                BaseColor color=new BaseColor (0,153,204,255);
                float fontSize=20.0f;
                float valueFontSize=20.0f;
                double total=0;
                BaseFont fontName= BaseFont.createFont ("assets/fonts/Brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);
                Font titulo=new Font (fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
                addItem(document,"Orden pedido", Element.ALIGN_CENTER,titulo);

                Font numeroOrden=new Font (fontName,fontSize,Font.NORMAL,color);
                addItem(document,"Pedido no.", Element.ALIGN_LEFT,numeroOrden);

                Font numeroValorOrden=new Font (fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
                addItem(document,"#"+this.pedidoFactura.getFactura_idfacturas (), Element.ALIGN_LEFT,numeroValorOrden);
                agregarLinea(document);

                addItem(document,"Fecha de pedido", Element.ALIGN_LEFT,numeroOrden);
                addItem(document,format.format (pedidoFactura.getFactura_fecha ()), Element.ALIGN_LEFT,numeroValorOrden);
                agregarLinea(document);

                addItem(document,"Número de mesa", Element.ALIGN_LEFT,numeroOrden);
                addItem(document,pedidoFactura.getMesas_numero (), Element.ALIGN_LEFT,numeroValorOrden);
                agregarLinea(document);
                agregarEspacio (document);
                addItem (document,"Detalle de los platos",Element.ALIGN_LEFT,titulo);
                agregarLinea(document);

                for (Pedido pedido: pedidoFactura.getPedidos ())
                {
                    addItemleft (document,pedido.getNombre (),"",titulo,numeroValorOrden);
                    total+=pedido.getTotal();
                    addItemleft (document,pedido.getCantidad ()+"*"+nf.format(pedido.getPrecio ()),nf.format(pedido.getTotal())+"",titulo,numeroValorOrden);
                }

                agregarLinea(document);
                agregarEspacio (document);
                addItemleft (document,"Subtotal",nf.format(total)+"",titulo,numeroValorOrden);
                addItemleft (document,"IVA",nf.format(total*0.19)+"",titulo,numeroValorOrden);
                agregarLinea(document);
                addItemleft (document,"Total",nf.format((total*0.19)+total)+"",titulo,numeroValorOrden);
                document.close ();
                imprimiPDF();
            }else
            {
                Toast.makeText(getContext(), "No hay pedidos que imprimir", Toast.LENGTH_SHORT).show();
            }

        }catch (FileNotFoundException e)
        {
            e.printStackTrace ();
        } catch (DocumentException e)
        {
            e.printStackTrace ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    private void crearPDFCocina(String path)
    {
        if (new File (path).exists ())
        {
            new File (path).delete ();
        }
        try
        {
            if (this.pedidoFactura.hayPedidos())
            {
                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                SimpleDateFormat format=new SimpleDateFormat ("dd/MM/yyyy");
                Document document=new Document ();
                PdfWriter.getInstance (document, new FileOutputStream (path));
                document.open ();
                document.setPageSize (PageSize.NOTE);
                document.addCreationDate ();
                document.addAuthor ("Open");
                document.addAuthor ("user");
                BaseColor color=new BaseColor (0,153,204,255);
                float fontSize=20.0f;
                float valueFontSize=20.0f;
                double total=0;
                BaseFont fontName= BaseFont.createFont ("assets/fonts/Brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);
                Font titulo=new Font (fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
                addItem(document,"Orden pedido", Element.ALIGN_CENTER,titulo);

                Font numeroOrden=new Font (fontName,fontSize,Font.NORMAL,color);
                addItem(document,"Pedido no.", Element.ALIGN_LEFT,numeroOrden);

                Font numeroValorOrden=new Font (fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
                addItem(document,"#"+this.pedidoFactura.getFactura_idfacturas (), Element.ALIGN_LEFT,numeroValorOrden);
                agregarLinea(document);

                addItem(document,"Fecha de pedido", Element.ALIGN_LEFT,numeroOrden);
                addItem(document,format.format (pedidoFactura.getFactura_fecha ()), Element.ALIGN_LEFT,numeroValorOrden);
                agregarLinea(document);

                addItem(document,"Número de mesa", Element.ALIGN_LEFT,numeroOrden);
                addItem(document,pedidoFactura.getMesas_numero (), Element.ALIGN_LEFT,numeroValorOrden);
                agregarLinea(document);
                agregarEspacio (document);
                addItem (document,"Detalle de los platos",Element.ALIGN_LEFT,titulo);
                agregarLinea(document);

                for (Pedido pedido: pedidoFactura.getPedidos ())
                {
                    addItemleft (document,pedido.getNombre (),"",titulo,numeroValorOrden);
                    total+=pedido.getTotal();
                    addItemleft (document,pedido.getCantidad ()+"*"+nf.format(pedido.getPrecio ()),nf.format(pedido.getTotal())+"",titulo,numeroValorOrden);
                    addItemleft (document,pedido.getObsevacion (),"",titulo,numeroValorOrden);
                    agregarLinea(document);
                }


                agregarEspacio (document);
                agregarEspacio (document);
                addItemleft (document,"Total",nf.format(total)+"",titulo,numeroValorOrden);
                document.close ();
                imprimiPDF();
            }else
            {
                Toast.makeText(getContext(), "No hay pedidos que imprimir", Toast.LENGTH_SHORT).show();
            }


        }catch (FileNotFoundException e)
        {
            e.printStackTrace ();
        } catch (DocumentException e)
        {
            e.printStackTrace ();
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    private void imprimiPDF()
    {
        PrintManager printManager=(PrintManager)getContext ().getSystemService (Context.PRINT_SERVICE);
        try{

            PrintDocumentAdapter adapter=new PDFAdapter (getContext (),common.getRutaRaiz(getContext ())+"ticket.pdf",this.pedidoFactura,adaptadorListaPedidos,requestQueue);

            printManager.print ("Document",adapter, new PrintAttributes.Builder ().build ());

        }catch (Exception exception)
        {
            exception.printStackTrace ();
        }
    }

    private void addItemleft(Document document, String textLeft, String textRight, Font left, Font right) throws DocumentException
    {
        Chunk chunkLeft=new Chunk (textLeft,left);
        Chunk chunkRight=new Chunk (textRight ,right);
        Paragraph paragraph=new Paragraph (chunkLeft);
        paragraph.add (new Chunk ( new VerticalPositionMark ()));
        paragraph.add (chunkRight);
        document.add(paragraph);
    }

    private void agregarLinea(Document document) throws DocumentException
    {
        LineSeparator separator=new LineSeparator ();
        separator.setLineColor (new BaseColor (0,0,0,68));
        agregarEspacio(document);
        document.add (new Chunk (separator));
        agregarEspacio(document);
    }

    private void agregarEspacio(Document document) throws DocumentException
    {
        document.add (new Paragraph (""));
    }

    private void addItem(Document document, String oreden_detalle, int alignCenter, Font titulo) throws DocumentException {
        Chunk chunk=new Chunk (oreden_detalle,titulo);
        Paragraph paragraph=new Paragraph (chunk);
        paragraph.setAlignment (alignCenter);
        document.add(paragraph);
    }

    @Override
    public boolean onDrag(View v, DragEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
            //    v.setBackgroundColor(Color.LTGRAY);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(Color.YELLOW);
                break;
            case DragEvent.ACTION_DROP:
                if (pedidoFactura.getMesas_idmesas () !=0)
                {
                    int positionFuente = -1;
                    View viewSource = (View) event.getLocalState ();
                    RecyclerView RecyclerView = (RecyclerView) viewSource.getParent ();
                    positionFuente = (int) viewSource.getTag ();
                    if (RecyclerView.getAdapter () instanceof AdaptadorListaPlatos)
                    {
                        AdaptadorListaPlatos adaptadorListaPlatos = (AdaptadorListaPlatos) RecyclerView.getAdapter ();
                        Plato plato = adaptadorListaPlatos.getList ().get (positionFuente);
                        Pedido miPlato = pedidoFactura.buscarPedido (plato.getIdplato ());

                        if (miPlato instanceof Pedido)
                        {
                            crearObservacion(miPlato);
                            miPlato.setCantidad (miPlato.getCantidad () + 1);
                        } else
                        {
                            Pedido pedido=plato.converAPedido ();
                            pedido.setCantidad (1);
                            crearObservacion(pedido);
                            pedidoFactura.agregarPedido ( pedido);
                        }
                        v.setVisibility (View.VISIBLE);
                    } else if ((RecyclerView.getAdapter () instanceof AdaptadorListaPedidos))
                    {
                        final AdaptadorListaPedidos adaptadorListaPedidos = (AdaptadorListaPedidos) RecyclerView.getAdapter ();
                        final Pedido plato =  adaptadorListaPedidos.getList ().get (positionFuente);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext ());

                       // GridLayout gridLayout=new GridLayout (getContext ());
                        LayoutInflater inflater= getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_eliminar_plato,null);
                        builder.setView(view);
                        final AlertDialog dialog = builder.create();
                        dialog.show ();

                       final EditText input =view.findViewById(R.id.editTextCantPlatos);
                       TextView titulo = view.findViewById(R.id.textTitulo);
                       TextView titulo1 = view.findViewById(R.id.textTitulo1);
                       Button si = view.findViewById(R.id.buttonSi);
                       Button no = view.findViewById(R.id.buttonNO);

                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        titulo.setText ("¿Seguro que quieres retirar estos platos?");
                        titulo1.setText ("Ingrese la cantidad de platos de "+plato.getNombre ()+" a eliminar.");
                        input.setText (plato.getCantidad ()+"");

                        si.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                try
                                {
                                    int cantidad = Integer.parseInt(input.getText ().toString ());
                                    if (cantidad<=plato.getCantidad ())
                                    {
                                        HashMap<String, String> params = new HashMap<String, String> ();
                                        params.put ("eliminarUnPlatoPedido", "true");
                                        params.put ("cantidad", cantidad+"");
                                        params.put ("idplato", plato.getIdplato ()+"");
                                        params.put ("idfactura", pedidoFactura.getFactura_idfacturas () + "");
                                        JSONObject parameters = new JSONObject (params);
                                        String url = "http://openm.co/consultas/pedidos.php";

                                        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                                        {
                                            @Override
                                            public void onResponse(JSONObject response)
                                            {
                                                try
                                                {
                                                    pedidoFactura.limpiarLista ();
                                                    JSONArray datos = response.getJSONArray("datos");
                                                    if (datos.length() > 0)
                                                    {
                                                        for (int i = 0; i < datos.length(); i++)
                                                        {
                                                            JSONObject plato = datos.getJSONObject(i);
                                                            int pedidos_cantidad = plato.getInt("pedidos_cantidad");
                                                            String platos_imagen = plato.getString("platos_imagen");
                                                            double platos_precio = plato.getDouble("platos_precio");
                                                            String platos_descripcion = plato.getString("platos_descripcion");
                                                            String platos_nombre = plato.getString("platos_nombre");
                                                            String platos_categoria = plato.getString("platos_categoria");
                                                            int platos_idplatos = plato.getInt("platos_idplatos");
                                                            String pedidos_observacion = plato.getString("pedidos_observacion");

                                                            Toast.makeText(getContext(), plato.getString("mesas_numero"), Toast.LENGTH_SHORT).show();

                                                            Pedido platoDatos = new Pedido (
                                                                    platos_idplatos,
                                                                    platos_categoria,
                                                                    platos_nombre,
                                                                    platos_descripcion,
                                                                    platos_precio,
                                                                    platos_imagen,
                                                                    pedidos_cantidad
                                                            );
                                                            pedidoFactura.agregarPedido (platoDatos);
                                                            adaptadorListaPedidos.notifyDataSetChanged ();
                                                        }
                                                    }
                                                } catch (JSONException e)
                                                {
                                                    e.printStackTrace ();
                                                }
                                                Toast.makeText (getContext (), "Platos eliminados", Toast.LENGTH_SHORT).show ();
                                                dialog.cancel();
                                            }
                                        }, new Response.ErrorListener ()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error)
                                            {
                                                error.printStackTrace ();
                                            }
                                        });
                                        requestQueue.add (jsonRequest);
                                    }else
                                    {
                                        Toast.makeText (getContext (), "No se puede reducir esa cantidad de platos", Toast.LENGTH_SHORT).show ();
                                    }
                                }catch (NumberFormatException e)
                                {
                                    Toast.makeText (getContext (), "El número ingresado no es valido", Toast.LENGTH_SHORT).show ();e.printStackTrace ();
                                }
                            }
                        });
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });

                        dialog.show ();
                        v.setVisibility (View.VISIBLE);
                    }
                }else
                {
                    Toast.makeText (getContext (), "Por favor selecciona una mesa", Toast.LENGTH_SHORT).show ();
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(0);
                break;
            default:
                break;
        }

        if (!isDropped)
        {
            View vw = (View) event.getLocalState();
            vw.setVisibility(View.VISIBLE);
        }

        return true;
    }

    private void crearObservacion(final Pedido miPlato)
    {
        final String[] miObservacion = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext ());
        builder.setTitle("Title");

        final EditText input = new EditText(getContext ());
        input.setText (miPlato.getObsevacion ().isEmpty ()?"":miPlato.getObsevacion ());
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                miObservacion[0] = input.getText().toString();
                miPlato.setObsevacion ( miObservacion[0]);
                actuiizarPedido();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                actuiizarPedido();
            }
        });

        builder.show ();
    }

    private void actuiizarPedido()
    {
        String data = new Gson ().toJson (pedidoFactura.getPedidos ());

        HashMap<String, String> params = new HashMap<String, String> ();
        params.put ("modidificarListaPedido", data);
        params.put ("idfactura", pedidoFactura.getFactura_idfacturas () + "");
        JSONObject parameters = new JSONObject (params);

        String url = "http://openm.co/consultas/pedidos.php";

        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray datos = response.getJSONArray ("datos");
                    Toast.makeText (getContext (), datos.toString (), Toast.LENGTH_SHORT).show ();
                } catch (JSONException e) {
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



}



