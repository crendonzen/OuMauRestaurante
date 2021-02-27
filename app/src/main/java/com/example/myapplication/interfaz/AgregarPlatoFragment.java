package com.example.myapplication.interfaz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.myapplication.Abtract.InterfazFragamen;
import com.example.myapplication.R;
import com.example.myapplication.mundo.Mesa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AgregarPlatoFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private ImageButton imagenPlato;
    private EditText nombrePlato;
    private EditText precioPlato;
    private Spinner categoriaPalto;
    private EditText descripcionPlato;
    private Button buscarImage;
    private Button agreagrUnPlato;
    private Bitmap bitmap;
    private String path;
    private Uri miPath;
    private String selectedImagePath = null;
    private Activity actividad;
    private InterfazFragamen interfazFragamen;



    public AgregarPlatoFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_agregar_plato, container, false);
        this.imagenPlato =(ImageButton) view.findViewById(R.id.imgPlato);
        this.imagenPlato.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v)
            {

            }
        });

        if (miPath != null)
        {
            imagenPlato.setImageURI(miPath);
        }
        this.nombrePlato = (EditText) view.findViewById(R.id.txtnombrePlato);

        this.precioPlato = (EditText) view.findViewById(R.id.txtPrecionPlato);
        this.descripcionPlato = (EditText) view.findViewById(R.id.txtDescripcion);
        this.categoriaPalto = (Spinner) view.findViewById(R.id.spnCategoria);
        this.buscarImage =(Button) view.findViewById(R.id.btnBuscarImagen);
        this.agreagrUnPlato =(Button) view.findViewById(R.id.btnAgregar);

        this.buscarImage.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
                final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext ());
                alertOpciones.setTitle("Seleccione una Opción");
                alertOpciones.setItems(opciones, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if (opciones[i].equals("Tomar Foto"))
                        {
                            tomarFotografia();
                        }else  if (opciones[i].equals("Cargar Imagen"))
                        {
                            cargarImagen();
                        }else
                        {
                            dialogInterface.dismiss();
                        }
                    }
                });
                alertOpciones.show ();
            }
        });
        this.agreagrUnPlato.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                agregarPlato();
                limpiar();
                Navigation.findNavController(v).navigate(R.id.action_agregarPlatoFragment_to_platosFragment);
            }
        });
        if(validaPermisos())
        {
            buscarImage.setEnabled(true);
        }else{
           // buscarImage.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated (view, savedInstanceState);

    }

    public void cargarImagen()
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.createChooser(intent,"Seleccione la Aplicación");
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,COD_SELECCIONA);
    }

    public void agregarPlato()
    {
        String rutaImg="";
        String nombre=this.nombrePlato.getText().toString ();
        double precio= Double.parseDouble(precioPlato.getText().toString());
        Object select = this.categoriaPalto.getSelectedItem ();
        String descripcion = this.descripcionPlato.getText ().toString ();
        if (rutaImg.isEmpty ())
        {
            Toast.makeText(getContext(), "Seleccione una imagen valida",Toast.LENGTH_SHORT).show();
        }else if ((select instanceof  String))
        {
            Toast.makeText(getContext(), "Seleccione una categoria valida",Toast.LENGTH_SHORT).show();
        }else if (!nombre.isEmpty ())
        {
            Toast.makeText(getContext(), "Escriba un nombre de plato valida",Toast.LENGTH_SHORT).show();
        }else if (precio<0)
        {
            Toast.makeText(getContext(), "Escriba un precio de plato valida",Toast.LENGTH_SHORT).show();
        }else if (descripcion.isEmpty ())
        {
            Toast.makeText(getContext(), "Escriba una descripcon del plato valida",Toast.LENGTH_SHORT).show();
        }else
        {
            String URL="http://192.168.56.1//restaurante/platos/ingreso.php?nombre="+nombre+"&precio="+precio+"";
            RequestQueue servicio= Volley.newRequestQueue(getContext());
            Map<String,String> params= new HashMap<String, String> ();

            params.put("imagen",rutaImg);
            params.put("nombreImagen",rutaImg);
            params.put("categoria",select.toString());
            params.put("nombre",nombre);
            params.put("precio",precio+"");
            params.put("descripcion",descripcion);
            params.put("agregarPlato","true");

            JSONObject parameters = new JSONObject(params);

            JsonRequest jsonRequest=new JsonObjectRequest (Request.Method.POST, URL, parameters, new Response.Listener<JSONObject> ()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    if (response!=null)
                    {
                        Toast.makeText(getContext(), "Plato registrado con existo",Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                }
            }, new Response.ErrorListener ()
            {
                @Override
                public void onErrorResponse(VolleyError error){error.printStackTrace (); }
            });
            servicio.add(jsonRequest);
        }
    }



    private void tomarFotografia()
    {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false)
        {
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true)
        {
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }

        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N)
        {
            String authorities=getContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(getContext (),authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);

        ////
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == MainActivity.RESULT_OK)
        {
            switch (requestCode)
            {
                case COD_SELECCIONA:
                    String f=data.getData().getQuery ();
                    System.out.println (f);
                    miPath=data.getData();
                    imagenPlato.setImageURI(miPath);
                    Bitmap drawingCache = imagenPlato.getDrawingCache ();
                    drawingCache =redimensionarImagenMaximo(drawingCache,50,50);
                    imagenPlato.setImageBitmap (drawingCache);
                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(getContext (), new String[]{this.path}, null, new MediaScannerConnection.OnScanCompletedListener()
                    {
                        @Override
                        public void onScanCompleted(String path, Uri uri)
                        {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });
                    Bitmap bitmap= BitmapFactory.decodeFile(this.path);
                    imagenPlato.setImageBitmap(bitmap);
                    break;
            }
        }

    }
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth)
    {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        Matrix matrix = new Matrix ();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext ().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private boolean validaPermisos()
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            return true;
        }
        if((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)
                &&(getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED))
        {
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))
                || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)))
        {
            cargarDialogoRecomendacion();
        }else
        {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100)
        {
            if(grantResults.length==2
                    && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED)
            {
                buscarImage.setEnabled(true);
            }else
            {
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual()
    {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext ());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (opciones[i].equals("si"))
                {
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(getContext().getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Activity)
        {
            this.actividad = (Activity) context;
            this.interfazFragamen = (InterfazFragamen) this.actividad;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
    private void cargarDialogoRecomendacion()
    {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext ());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    public void limpiar()
    {
        nombrePlato.setText("");
        precioPlato.setText("");
    }

/*
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        // Save the image bitmap into outState
       Bitmap bitmap = ((BitmapDrawable)imagenPlato.getDrawable()).getBitmap();
      outState.putParcelable("bitmap", bitmap);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // Read the bitmap from the savedInstanceState and set it to the ImageView
        if (savedInstanceState != null)
        {
            Bitmap bitmap = (Bitmap) savedInstanceState.getParcelable("bitmap");
            imagenPlato.setImageBitmap(bitmap);
        }
    }*/
}