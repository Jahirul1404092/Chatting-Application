package com.amsa.plover;
//////////////////////////////////////////////////////////////eta delete kore deua jay
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Contact> mData;
    Dialog mydialog;

    public RecyclerViewAdapter(Context mContext, List<Contact> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.item_contact,viewGroup,false);
        final MyViewHolder myViewHolder=new MyViewHolder(view);

        // Dialog ini
        mydialog=new Dialog(mContext);
        mydialog.setContentView(R.layout.dialog_contact);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        myViewHolder.contact_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView dialog_name_tv=(TextView) mydialog.findViewById(R.id.dialog_name_Id);
                TextView dialog_phone_tv=(TextView) mydialog.findViewById(R.id.dialog_phone_Id);
                ImageView dialog_contact_img=(ImageView)mydialog.findViewById(R.id.dialog_image_Id);

                dialog_name_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getName());
                dialog_phone_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getPhone());
                dialog_contact_img.setImageResource(mData.get(myViewHolder.getAdapterPosition()).getPhoto());

                Toast.makeText(mContext,"Test Click"+String.valueOf(myViewHolder.getAdapterPosition()),Toast.LENGTH_LONG).show();
                mydialog.show();
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tv_name.setText(mData.get(i).getName());
        myViewHolder.tv_phone.setText(mData.get(i).getPhone());
        myViewHolder.img.setImageResource(mData.get(i).getPhoto());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout contact_item;
        private TextView tv_name;
        private TextView tv_phone;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_item=(LinearLayout)itemView.findViewById(R.id.contact_item_Id);
            tv_name=(TextView)itemView.findViewById(R.id.name_contactId);
            tv_phone=(TextView)itemView.findViewById(R.id.phone_contactId);
            img=(ImageView)itemView.findViewById(R.id.img_contact);

        }
    }
}
