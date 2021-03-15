package com.example.myapplication.interfaz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.adaptador.PDFAdapter;
import com.example.myapplication.adaptador.common;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseField;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

public class FacturaMesaFragment extends BottomSheetDialogFragment
{
    private Button btnImprimir;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_factura_mesa, container, false);
        this.btnImprimir=v.findViewById(R.id.btnImprimir);
        Dexter.withActivity(getActivity ()).withPermission (Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener ()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response)
            {
                btnImprimir.setOnClickListener (new View.OnClickListener ()
                {
                    @Override
                    public void onClick(View v)
                    {
                        crearPDF(common.getRutaRaiz(getContext ())+"ticket.pdf");
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

    private void crearPDF(String path)
    {
        if (new File (path).exists ())
        {
            new File (path).delete ();
        }
        try
        {
            Document document=new Document ();
            PdfWriter.getInstance (document, new FileOutputStream (path));
            document.open ();
            document.setPageSize (PageSize.A4);
            document.addCreationDate ();
            document.addAuthor ("Open");
            document.addAuthor ("user");
            BaseColor color=new BaseColor (0,153,204,255);
            float fontSize=20.0f;
            float valueFontSize=20.0f;

            BaseFont fontName= BaseFont.createFont ("assets/fonts/Brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);
            Font titulo=new Font (fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            addItem(document,"Oreden_detalle", Element.ALIGN_CENTER,titulo);

            Font numeroOrden=new Font (fontName,fontSize,Font.NORMAL,color);
            addItem(document,"Oreden_o.", Element.ALIGN_LEFT,numeroOrden);

            Font numeroValorOrden=new Font (fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
            addItem(document,"#545454", Element.ALIGN_LEFT,numeroValorOrden);
            agregarLinea(document);

            addItem(document,"Fecha de pedido", Element.ALIGN_LEFT,numeroOrden);
            addItem(document,"3/03/2019", Element.ALIGN_LEFT,numeroValorOrden);
            agregarLinea(document);

            addItem(document,"Nombre de la cuenta", Element.ALIGN_LEFT,numeroOrden);
            addItem(document,"User", Element.ALIGN_LEFT,numeroValorOrden);
            agregarLinea(document);
            agregarEspacio (document);
            addItem (document,"Detalle producto",Element.ALIGN_LEFT,titulo);
            agregarLinea(document);

            addItemleft (document,"pizza 25","(0.0%)",titulo,numeroValorOrden);
            addItemleft (document,"12*1000","12000",titulo,numeroValorOrden);
            agregarLinea(document);
            addItemleft (document,"pizza 25","(0.0%)",titulo,numeroValorOrden);
            addItemleft (document,"12*1000","12000",titulo,numeroValorOrden);
            agregarLinea(document);
            agregarEspacio (document);
            addItemleft (document,"Total","12000",titulo,numeroValorOrden);
            document.close ();
            imprimiPDF();

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
            PrintDocumentAdapter  adapter=new PDFAdapter (getContext (),common.getRutaRaiz(getContext ())+"ticket.pdf");
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


}