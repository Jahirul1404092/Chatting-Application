package com.amsa.plover;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context mContext;
    List<Profile> mData;
    Dialog mydialog;

    public ContactAdapter(Context mContext, List<Profile> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.item_contact,viewGroup,false);
        final MyViewHolder myViewHolder=new MyViewHolder(view);

        // Dialog ini
        mydialog=new Dialog(mContext);
        mydialog.setContentView(R.layout.dialog_contact);
        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        myViewHolder.contact_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView dialog_name_tv=(TextView) mydialog.findViewById(R.id.dialog_name_Id);
                TextView dialog_phone_tv=(TextView) mydialog.findViewById(R.id.dialog_phone_Id);
                ImageView dialog_contact_img=(ImageView)mydialog.findViewById(R.id.dialog_image_Id);

                dialog_name_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getName());
                dialog_phone_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getPhone());
//                dialog_contact_img.setImageResource(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl());
                Picasso.with(mContext).load(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()).placeholder(R.drawable.male).fit().centerCrop().into(dialog_contact_img);

//                Toast.makeText(mContext,mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl(),Toast.LENGTH_LONG).show();
                mydialog.show();
                return false;
            }
        });
        myViewHolder.contact_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Messaging.class);
                intent.putExtra("id", mData.get(myViewHolder.getAdapterPosition()).getId());
                intent.putExtra("name", mData.get(myViewHolder.getAdapterPosition()).getName());
                intent.putExtra("mobile", mData.get(myViewHolder.getAdapterPosition()).getPhone());
                intent.putExtra("gender", mData.get(myViewHolder.getAdapterPosition()).getGender());
                intent.putExtra("password", mData.get(myViewHolder.getAdapterPosition()).getPassword());
                intent.putExtra("imageUri", mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl());
                mContext.startActivity(intent);
//                System.out.println("mmmmmmmmmmmmmmmmmmmmm     "+mData.get(myViewHolder.getAdapterPosition()).getId()+" "+mData.get(myViewHolder.getAdapterPosition()).getName()+"  "+mData.get(myViewHolder.getAdapterPosition()).getPassword()+"  "+mData.get(myViewHolder.getAdapterPosition()).getGender()+"  "+mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()+"  "+mData.get(myViewHolder.getAdapterPosition()).getPhone()+"  "+mData.get(myViewHolder.getAdapterPosition()).getImageName());
                Toast.makeText(mContext,"Press Reload button",Toast.LENGTH_LONG).show();
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.tv_name.setText(mData.get(i).getName());
            myViewHolder.tv_phone.setText(mData.get(i).getPhone());
//        myViewHolder.img.setImageResource(mData.get(i).getPhoto());
            Picasso.with(mContext).load(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()).placeholder(R.drawable.male).fit().centerCrop().into(myViewHolder.img);

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









//package com.example.chatapp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class ChatAdapter extends ArrayAdapter<Profile> {
//
//    private Activity context;
//    private List<Profile> profileList;
//
//    public ChatAdapter(Activity context, List<Profile> profileList) {
//        super(context, R.layout.samplefor_chat_fragment_row, profileList);
//        this.context = context;
//        this.profileList = profileList;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater layoutInflater=context.getLayoutInflater();
//        View view=layoutInflater.inflate(R.layout.samplefor_chat_fragment_row,null,true);
//
//        Profile profile= profileList.get(position);
//
//        TextView cpt=(TextView)view.findViewById(R.id.chatProfileNameTextViewId);
//        TextView lastMsg=(TextView)view.findViewById(R.id.chatProfileNameTextViewId);
//        ImageView chatProfileImageView=(ImageView)view.findViewById(R.id.chatProfileImageViewId);
//
//        cpt.setText(profile.getName());
//        lastMsg.setText("Profile.getLastMsg()");
//        Picasso.with(view.getContext()).load(profile.getImageDownloadUrl()).placeholder(R.mipmap.ic_action_person).fit().centerCrop().into(chatProfileImageView);
//        return view;
//    }
//}
