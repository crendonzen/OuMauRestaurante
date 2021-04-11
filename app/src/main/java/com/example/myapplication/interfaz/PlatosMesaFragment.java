package com.example.myapplication.interfaz;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adaptador.AdaptadorListaPedidos;
import com.example.myapplication.adaptador.AdaptadorListaPlatos;
import com.example.myapplication.adaptador.PDFAdapter;
import com.example.myapplication.adaptador.Servidor;
import com.example.myapplication.adaptador.VolleySingleton;
import com.example.myapplication.adaptador.common;
import com.example.myapplication.mundo.Factura;
import com.example.myapplication.mundo.Mesa;
import com.example.myapplication.mundo.Pedido;
import com.example.myapplication.mundo.Plato;
import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
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

import java.io.ByteArrayOutputStream;
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
    private TextView lblTotal;
    private ImageButton btnActualizarPedido;
    private ImageButton btnFactura;
    private ImageButton btnCocina;
    private Dialog mDialog;
    private double total;
    private EditText info;
    private int idMesa;



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
        info=v.findViewById(R.id.info);
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
                Pedido miPedido= pedidoFactura.getPedidos ().get (listaPedidos.getChildAdapterPosition (v));
              //  Pedido miPedido=pedidoFactura.buscarPedido(listaPedidos.getChildAdapterPosition(v));
                Toast.makeText(getContext(),listaPedidos.getChildAdapterPosition(v)+"" , Toast.LENGTH_SHORT).show();
                crearObservacion(miPedido);

            }
        });

        numeroMesa = v.findViewById(R.id.numero_Mesa);
        this.lblTotal = v.findViewById(R.id.lblPrecioTotal);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle)
            {
                Mesa mesa = (Mesa) bundle.getSerializable("mesa");
                if (mesa instanceof Mesa )
                {
                    numeroMesa.setText(mesa.getNumero());
                    int idmesa = mesa.getIdmesa();
                    Map<String, String> params = new HashMap<String, String>();
                    //Toast.makeText(getContext(), "" + idmesa, Toast.LENGTH_SHORT).show();
                    params.put("buscarPlatoMesa", idmesa + "");
                    JSONObject parameters = new JSONObject(params);
                    String url =Servidor.HOST +"/consultas/pedidos.php";

                    jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                Boolean respuesta = response.getBoolean ("respuesta");

                                if (respuesta.booleanValue ())
                                {
                                    pedidoFactura.limpiarLista();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    JSONArray datos = response.getJSONArray("datos");
                                    if (datos.length() > 0)
                                    {
                                        total=0;
                                        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
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
                                        for (int i = 0; i < datos.length(); i++)
                                        {
                                            JSONObject plato = datos.getJSONObject(i);

                                           int pedidos_cantidad = 0;
                                           double platos_precio=0;
                                            int platos_idplatos=0;
                                            try
                                            {
                                                pedidos_cantidad = plato.getInt("pedidos_cantidad");
                                                platos_precio = plato.getDouble("platos_precio");
                                                platos_idplatos = plato.getInt("platos_idplatos");
                                                String platos_imagen = plato.getString("platos_imagen");
                                                String platos_descripcion = plato.getString("platos_descripcion");
                                                String platos_nombre = plato.getString("platos_nombre");
                                                String platos_categoria = plato.getString("platos_categoria");
                                                String pedidos_observacion = plato.getString("pedidos_observacion");
                                                total+=platos_precio*pedidos_cantidad;
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

                                            }catch (Exception e)
                                            {
                                                pedidos_cantidad = 0;
                                                platos_precio=0;
                                                platos_idplatos=0;
                                            }
                                        }
                                        lblTotal.setText (nf.format (total));
                                    }
                                    adaptadorListaPedidos.notifyDataSetChanged ();
                                }else
                                {
                                    String error = response.getString ("error");
                                    Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
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

                   idMesa=mesa.getIdmesa();
                }else if(!(mesa instanceof Mesa))
                {
                    numeroMesa.setText("Seleccione una mesa");
                    pedidoFactura.limpiarLista ();
                    pedidoFactura.setMesas_idmesas (0);
                    adaptadorListaPedidos.notifyDataSetChanged ();
                    lblTotal.setText ("$ 0");
                }
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
                final ProgressDialog loading = ProgressDialog.show(getContext (),"Creando factura...","Espere por favor...",false,false);

                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                float fontSize=26.0f;
                float valueFontSize=26.0f;
                BaseFont fontName= BaseFont.createFont ("assets/fonts/Brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);

                Font titulo=new Font (fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
                Font numeroValorOrden=new Font (fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
                SimpleDateFormat format=new SimpleDateFormat ("dd/MM/yyyy");
                Document document=new Document ();
                try {
                    PdfWriter.getInstance (document, new FileOutputStream (path));
                }catch (Exception e){

                }

                document.open ();
                document.setPageSize (PageSize.NOTE);
                document.addCreationDate ();
                document.addAuthor ("Open");
                document.addAuthor ("user");
                BaseColor color=new BaseColor (Color.BLACK);

                double total=0;
                addItemImage (document, Element.ALIGN_CENTER, R.mipmap.restaurante);


                addItem(document,"NIT. 1085.266.866-3 No. Resp. IVA", Element.ALIGN_CENTER,numeroValorOrden);

                addItem(document,"NIT. 1.085.266.866-3 No. Resp. IVA", Element.ALIGN_CENTER,numeroValorOrden);

                addItem(document,"Calle 18a #3-05 B/Lorenzo", Element.ALIGN_CENTER,numeroValorOrden);
                addItemleftImage( document,   Element.ALIGN_CENTER, "305 484 8526",  numeroValorOrden,  R.mipmap.redesw);

                addItem(document,"Oumaorestaurante@gmail.com", Element.ALIGN_CENTER,numeroValorOrden);
                addItemleftImage( document,   Element.ALIGN_CENTER, "@Oumao.oficial",  numeroValorOrden,  R.mipmap.redes);


                titulo = new Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);


                agregarEspacio (document);
                agregarEspacio (document);


                Font numeroOrden=new Font (fontName,fontSize,Font.NORMAL,color);
                addItem(document,"FACTURA", Element.ALIGN_CENTER,numeroOrden);


                addItem(document,""+this.pedidoFactura.getFactura_idfacturas (), Element.ALIGN_CENTER,numeroValorOrden);
                agregarLinea(document);

                addItemleft (document,"Fecha de pedido", "Numero de mesa",numeroValorOrden,numeroValorOrden);
                addItemleft (document,format.format (pedidoFactura.getFactura_fecha ()), pedidoFactura.getMesas_numero (),numeroValorOrden,numeroValorOrden);
                agregarLinea(document);
                agregarEspacio (document);
                addItem (document,"Ordenes del pedido",Element.ALIGN_CENTER,titulo);
                agregarLinea(document);

                for (Pedido pedido: pedidoFactura.getPedidos ())
                {
                    addItemleft (document,pedido.getNombre (),"",titulo,numeroValorOrden);
                    total+=pedido.getTotal();
                    addItemleft (document,nf.format(pedido.getPrecio ())+"*"+pedido.getCantidad (),nf.format(pedido.getTotal())+"",titulo,numeroValorOrden);
                }

                agregarLinea(document);
                agregarEspacio (document);
              //  addItemleft (document,"Subtotal",nf.format(total)+"",titulo,numeroValorOrden);
              //  agregarLinea(document);
                addItemleft (document,"Total",nf.format(total)+"",titulo,numeroValorOrden);
                document.close ();
                imprimiPDF("caja");
                loading.dismiss ();
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
                final ProgressDialog loading = ProgressDialog.show(getContext (),"Creando factura...","Espere por favor...",false,false);

                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                float fontSize=26.0f;
                float valueFontSize=26.0f;
                BaseFont fontName= BaseFont.createFont ("assets/fonts/Brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);

                Font titulo=new Font (fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
                Font numeroValorOrden=new Font (fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
                SimpleDateFormat format=new SimpleDateFormat ("dd/MM/yyyy");
                Document document=new Document ();
                PdfWriter.getInstance (document, new FileOutputStream (path));
                document.open ();
                document.setPageSize (PageSize.A4);
                document.addCreationDate ();
                document.addAuthor ("Open");
                document.addAuthor ("user");
                BaseColor color=new BaseColor (Color.BLACK);

                double total=0;
                addItemImage (document, Element.ALIGN_CENTER, R.mipmap.restaurante);


                addItem(document,"NIT. 1085.266.866-3 No. Resp. IVA", Element.ALIGN_CENTER,numeroValorOrden);

                addItem(document,"NIT.1.085.266.866-3 No. Resp. IVA", Element.ALIGN_CENTER,numeroValorOrden);

                addItem(document,"Calle 18a #3-05 B/Lorenzo", Element.ALIGN_CENTER,numeroValorOrden);
                addItemleftImage( document,   Element.ALIGN_CENTER, "305 484 8526",  numeroValorOrden,  R.mipmap.redesw);

                addItem(document,"Oumaorestaurante@gmail.com", Element.ALIGN_CENTER,numeroValorOrden);
                addItemleftImage( document,   Element.ALIGN_CENTER, "@Oumao.oficial",  numeroValorOrden,  R.mipmap.redes);

                agregarEspacio (document);
                agregarEspacio (document);

                Font numeroOrden=new Font (fontName,fontSize,Font.NORMAL,color);
                addItem(document,"FACTURA", Element.ALIGN_CENTER,numeroOrden);


                addItem(document,""+this.pedidoFactura.getFactura_idfacturas (), Element.ALIGN_CENTER,numeroValorOrden);
                agregarLinea(document);

                addItemleft (document,"Fecha de pedido", "Numero de mesa",numeroValorOrden,numeroValorOrden);
                addItemleft (document,format.format (pedidoFactura.getFactura_fecha ()), pedidoFactura.getMesas_numero (),numeroValorOrden,numeroValorOrden);
                agregarLinea(document);
                agregarEspacio (document);
                addItem (document,"Ordenes del pedido",Element.ALIGN_CENTER,titulo);
                agregarLinea(document);
                for (Pedido pedido: pedidoFactura.getPedidos ())
                {
                    addItemleft (document,pedido.getNombre (),"",titulo,numeroValorOrden);
                    total+=pedido.getTotal();
                    addItemleft (document,nf.format(pedido.getPrecio ())+"*"+pedido.getCantidad (),nf.format(pedido.getTotal())+"",titulo,numeroValorOrden);
                    if (!pedido.getObsevacion ().isEmpty ())
                        addItemleft (document,pedido.getObsevacion (),"",titulo,numeroValorOrden);
                    agregarLinea(document);
                }


                agregarEspacio (document);
                agregarEspacio (document);
                addItemleft (document,"Total",nf.format(total)+"",titulo,numeroValorOrden);
                document.close ();
                imprimiPDF("");
                loading.dismiss ();
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

    private void imprimiPDF(String destino)
    {
        PrintManager printManager=(PrintManager)getContext ().getSystemService (Context.PRINT_SERVICE);
        try{

            PrintDocumentAdapter adapter=new PDFAdapter (getContext (),common.getRutaRaiz(getContext ())+"ticket.pdf",this.pedidoFactura,destino,requestQueue);

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


    private void addItemleftImage(Document document,  int alignCenter, String textRight,  Font right, int idImage) throws DocumentException, IOException {
        agregarEspacio (document);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), idImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image logo = Image.getInstance(stream.toByteArray());

        logo.scaleAbsolute (80,40);

        Paragraph paragraph=new Paragraph ();
        logo.setAlignment(Image.ALIGN_CENTER);
        Chunk chunk=new Chunk (logo,120,-10);
        paragraph.add (chunk);
        paragraph.setIndentationRight (140);
        paragraph.add (new Chunk ( new VerticalPositionMark ()));
        Chunk chunkRight=new Chunk (textRight ,right);
        paragraph.add (chunkRight);
        paragraph.setAlignment (alignCenter);

        document.add(paragraph);
    }


    public Image obtenerImage(int idImage) throws IOException, BadElementException {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), idImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image logo = Image.getInstance(stream.toByteArray());
        return  logo;
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
        document.add (new Paragraph ("."));
    }

    private void addItem(Document document, String orden_detalle, int alignCenter, Font titulo) throws DocumentException {
        Chunk chunk=new Chunk (orden_detalle,titulo);
        Paragraph paragraph=new Paragraph (chunk);
        paragraph.setAlignment (alignCenter);
        document.add(paragraph);
    }
    private void addItemImage(Document document,  int alignCenter, int idImage) throws DocumentException, IOException
    {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), idImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image logo = Image.getInstance(stream.toByteArray());
        logo.scaleAbsolute (155,120);
        logo.setAlignment(Image.ALIGN_CENTER);
        Paragraph paragraph=new Paragraph ();
        paragraph.add (logo);
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
               // v.setBackgroundColor(Color.YELLOW);
                break;
            case DragEvent.ACTION_DROP:
                if (pedidoFactura.getMesas_idmesas () !=0)
                {

                    int positionFuente = -1;
                    View viewSource = (View) event.getLocalState ();
                    RecyclerView RecyclerView = (RecyclerView) viewSource.getParent ();
                    positionFuente = (int) viewSource.getTag ();
                    if ((RecyclerView.getAdapter () instanceof AdaptadorListaPlatos)&&v.getId ()== R.id.lista_pedidos)
                    {
                        Glide.get(getContext ()).clearMemory();
                        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                        AdaptadorListaPlatos adaptadorListaPlatos = (AdaptadorListaPlatos) RecyclerView.getAdapter ();
                        Plato plato = adaptadorListaPlatos.getList ().get (positionFuente);
                        Pedido miPlato = pedidoFactura.buscarPedido (plato.getIdplato ());

                        if (miPlato instanceof Pedido)
                        {
                            total+=miPlato.getTotal ();
                            miPlato.setCantidad (miPlato.getCantidad () + 1);
                        } else
                        {
                            Pedido pedido=plato.converAPedido ();
                            pedido.setCantidad (1);
                            pedidoFactura.agregarPedido ( pedido);
                            total+=pedido.getTotal ();
                        }
                        lblTotal.setText (nf.format (total));
                        actuiizarPedido();
                        v.setVisibility (View.VISIBLE);

                      //  Toast.makeText (getContext (), v.getId ()+"", Toast.LENGTH_SHORT).show ();
                    } else if ((RecyclerView.getAdapter () instanceof AdaptadorListaPedidos)&&v.getId ()== R.id.btnActualizarPedido)
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
                                        String url = Servidor.HOST +"/consultas/pedidos.php";

                                        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
                                        {
                                            @Override
                                            public void onResponse(JSONObject response)
                                            {
                                                try
                                                {
                                                    Boolean respuesta = response.getBoolean ("respuesta");

                                                    if (respuesta.booleanValue ())
                                                    {
                                                        pedidoFactura.limpiarLista ();
                                                        JSONArray datos = response.getJSONArray("datos");
                                                        if (datos.length() > 0)
                                                        {
                                                            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
                                                            total=0;
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


                                                              //  Toast.makeText(getContext(), plato.getString("mesas_numero"), Toast.LENGTH_SHORT).show();

                                                                Pedido platoDatos = new Pedido (
                                                                        platos_idplatos,
                                                                        platos_categoria,
                                                                        platos_nombre,
                                                                        platos_descripcion,
                                                                        platos_precio,
                                                                        platos_imagen,
                                                                        pedidos_cantidad
                                                                );
                                                                total+=platoDatos.getTotal ();
                                                                pedidoFactura.agregarPedido (platoDatos);
                                                                adaptadorListaPedidos.notifyDataSetChanged ();
                                                            }
                                                            lblTotal.setText (nf.format (total));
                                                        }
                                                        Toast.makeText (getContext (), "Platos eliminados", Toast.LENGTH_SHORT).show ();
                                                    } else
                                                    {
                                                        String error = response.getString ("error");
                                                        Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e)
                                                {
                                                    e.printStackTrace ();
                                                }
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

    private void crearObservacion(final Pedido miPedido)
    {
        final String[] miObservacion = {""};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext ());

        LayoutInflater inflater= getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_observacion_plato,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show ();


        final EditText input =view.findViewById(R.id.editTextObsPlatos);
        Button agregarObservacion=view.findViewById(R.id.btnAgrObs);
        Button cancelar=view.findViewById(R.id.btnCancelar);
        input.setText (miPedido.getObsevacion ().isEmpty ()?"":miPedido.getObsevacion ());

        //input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        agregarObservacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                miObservacion[0] = input.getText().toString();
                crearObservacionPedido(miPedido.getIdplato(), miObservacion[0]);
                dialog.cancel();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });


        dialog.show ();
    }

    private void crearObservacionPedido( int idPedido, String miObservacion)
    {
        HashMap<String, String> params = new HashMap<String, String> ();
        params.put ("crearObservacion", "true");
        params.put ("idfactura", pedidoFactura.getFactura_idfacturas () + "");
        params.put ("idPedido", idPedido + "");
        params.put ("miObservacion", miObservacion);
        JSONObject parameters = new JSONObject (params);

        String url = Servidor.HOST +"/consultas/pedidos.php";

        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response) {
                Boolean respuesta = null;
                try
                {
                    respuesta = response.getBoolean ("respuesta");
                    if (respuesta.booleanValue ())
                    {
                        Toast.makeText (getContext (), "Observación creada exitosamente", Toast.LENGTH_SHORT).show ();
                    }else
                    {
                        String error = response.getString ("error");
                        Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                    }
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
    }



    private void actuiizarPedido()
    {
        String data = new Gson ().toJson (pedidoFactura.getPedidos ());

        HashMap<String, String> params = new HashMap<String, String> ();
        params.put ("modidificarListaPedido", data);
        params.put ("idfactura", pedidoFactura.getFactura_idfacturas () + "");
        JSONObject parameters = new JSONObject (params);

        String url = Servidor.HOST +"/consultas/pedidos.php";

        jsonRequest = new JsonObjectRequest (Request.Method.POST, url, parameters, new Response.Listener<JSONObject> ()
        {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Boolean respuesta = response.getBoolean ("respuesta");

                    if (respuesta.booleanValue ())
                    {
                        JSONArray datos = response.getJSONArray ("datos");
                    }else
                    {
                        String error = response.getString ("error");
                        Toast.makeText(getContext(), respuesta.booleanValue ()+" Error: "+error,Toast.LENGTH_SHORT).show();
                    }
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



