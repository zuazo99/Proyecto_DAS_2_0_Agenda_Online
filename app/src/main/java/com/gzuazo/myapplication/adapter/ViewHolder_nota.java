package com.gzuazo.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gzuazo.myapplication.R;

public class ViewHolder_nota extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolder_nota.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*SE EJECUTA AL PRESIONAR EN EL ITEM*/
        void onItemLongClick(View view, int position); /*SE EJECUTA AL MANTENER PRESIONADO EN EL ITEM*/
    }

    public void setOnClickListener(ViewHolder_nota.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_nota(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });
    }

    public void setterDatos(Context context, String id_nota, String uid_usuario, String correo_usuario, String fechaHoraActual, String tituloNota,
                            String descripcion, String fechaNota, String estado){

        //Declarar las views
        TextView Id_Nota_Item, Uid_Usuario_Item, Correo_Usuario_Item,
                Fecha_Hora_registro_Item, Titulo_Nota_Item, Descripcion_Item, Fecha_Item, Estado_Item;

        ImageView Tarea_Finalizada_Item, Tarea_No_Finalizada_Item;

        // conexion con el item
        Id_Nota_Item = mView.findViewById(R.id.Id_Nota_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Correo_Usuario_Item = mView.findViewById(R.id.Correo_Usuario_Item);
        Fecha_Hora_registro_Item = mView.findViewById(R.id.Fecha_Hora_registro_Item);
        Titulo_Nota_Item = mView.findViewById(R.id.Titulo_Nota_Item);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);
        Estado_Item = mView.findViewById(R.id.Estado_Item);

        Tarea_Finalizada_Item = mView.findViewById(R.id.Tarea_Finalizada_Item);
        Tarea_No_Finalizada_Item = mView.findViewById(R.id.Tarea_No_Finalizada_Item);

        //settear los datos

        Id_Nota_Item.setText(id_nota);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_Usuario_Item.setText(correo_usuario);
        Fecha_Hora_registro_Item.setText(fechaHoraActual);
        Titulo_Nota_Item.setText(tituloNota);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fechaNota);
        Estado_Item.setText(estado);

        // Gestionamos color de estado

        if (estado.equals("Finalizado")){
            Tarea_Finalizada_Item.setVisibility(View.VISIBLE);
        }else {
            Tarea_No_Finalizada_Item.setVisibility(View.VISIBLE);
        }
    }
}
