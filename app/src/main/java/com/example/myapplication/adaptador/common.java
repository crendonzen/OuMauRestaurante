package com.example.myapplication.adaptador;

import android.content.Context;

import com.example.myapplication.R;

import java.io.File;

public class common
{
    public static String getRutaRaiz(Context context)
    {
        File directorio=new File (android.os.Environment.getExternalStorageDirectory ()
                +File.separator
                +context.getResources ().getString (R.string.app_name)
                +File.separator);
        if (!directorio.exists ())
        {
            directorio.mkdir ();
        }
        return directorio.getPath ()+File.separator;
    }
}
