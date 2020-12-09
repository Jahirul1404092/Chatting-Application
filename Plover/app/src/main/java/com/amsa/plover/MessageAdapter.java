package com.amsa.plover;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    Context mContext;
    List<Message> mData;
    Dialog mydialog;

    public MessageAdapter(Context mContext, List<Message> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.item_message,viewGroup,false);
        final MyViewHolder myViewHolder=new MyViewHolder(view);


        // Dialog ini
//        mydialog=new Dialog(mContext);
//        mydialog.setContentView(R.layout.dialog_contact);
//        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//
//        myViewHolder.contact_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView dialog_name_tv=(TextView) mydialog.findViewById(R.id.dialog_name_Id);
//                TextView dialog_phone_tv=(TextView) mydialog.findViewById(R.id.dialog_phone_Id);
//                ImageView dialog_contact_img=(ImageView)mydialog.findViewById(R.id.dialog_image_Id);
//
//                dialog_name_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getName());
//                dialog_phone_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getPhone());
////                dialog_contact_img.setImageResource(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl());
//                Picasso.with(mContext).load(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()).placeholder(R.mipmap.ic_action_person).fit().centerCrop().into(dialog_contact_img);
//
//                Toast.makeText(mContext,mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl(),Toast.LENGTH_LONG).show();
//                mydialog.show();
//            }
//        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String Id=new Home().getProfileId();
        final Uri RImageUrl=Uri.parse(new Home().getImageDownloadUrl());
        final String fid=new Messaging().getFid();
        final Uri fImageUrl=new Messaging().getFimageUri();
        if(mData.get(i).getMsgId().equals(Id)){
            myViewHolder.message_right_item.setVisibility(View.VISIBLE);
            myViewHolder.message_left_item.setVisibility(View.GONE);
            if(mData.get(i).getMsgType().equals("img")){
                Uri imguri=Uri.parse(mData.get(myViewHolder.getAdapterPosition()).getMsg());
                Picasso.with(mContext).load(imguri).placeholder(R.drawable.male).fit().centerCrop().into(myViewHolder.right_img);
            }
            else{
                myViewHolder.tv_right_msg.setText(mData.get(i).getMsg());
            }

            Picasso.with(mContext).load(RImageUrl).placeholder(R.drawable.male).fit().centerCrop().into(myViewHolder.right_profileimg);
        }
        else{
            myViewHolder.message_left_item.setVisibility(View.VISIBLE);
            myViewHolder.message_right_item.setVisibility(View.GONE);
            if(mData.get(i).getMsgType().equals("img")){
                Uri imguri=Uri.parse(mData.get(myViewHolder.getAdapterPosition()).getMsg());
                Picasso.with(mContext).load(imguri).placeholder(R.drawable.male).fit().centerCrop().into(myViewHolder.left_img);
            }
            else{
                myViewHolder.tv_left_msg.setText(mData.get(i).getMsg());
            }

            Picasso.with(mContext).load(RImageUrl).placeholder(R.drawable.male).fit().centerCrop().into(myViewHolder.left_profileimg);
        }


//        myViewHolder.tv_msg.setText(mData.get(i).getMsg());
//        myViewHolder.tv_phone.setText(mData.get(i).getPhone());
//        myViewHolder.profileimg.setImageResource(R.mipmap.ic_action_person);
//        if(mData.get(i).getMsgId().equals(Id)){
//            myViewHolder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//        }
//        else {
//            myViewHolder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//        }
//

//        Picasso.with(mContext).load(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()).placeholder(R.mipmap.ic_action_person).fit().centerCrop().into(myViewHolder.img);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout message_item;
        private LinearLayout message_left_item;
        private LinearLayout message_right_item;
        private TextView tv_left_msg;
        private TextView tv_right_msg;
        private ImageView left_profileimg;
        private ImageView right_profileimg;
        private ImageView left_img;
        private ImageView right_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message_item=(LinearLayout)itemView.findViewById(R.id.message_item_Id);
            message_left_item=(LinearLayout)itemView.findViewById(R.id.message_left_item_Id);
            message_right_item=(LinearLayout)itemView.findViewById(R.id.message_right_item_Id);
            tv_left_msg=(TextView)itemView.findViewById(R.id.message_left_textView_Id);
            left_profileimg=(ImageView)itemView.findViewById(R.id.profile_left_img_message);
            left_img=(ImageView)itemView.findViewById(R.id.message_left_imageView_Id);
            tv_right_msg=(TextView)itemView.findViewById(R.id.message_right_textView_Id);
            right_profileimg=(ImageView)itemView.findViewById(R.id.profile_right_img_message);
            right_img=(ImageView)itemView.findViewById(R.id.message_right_imageView_Id);

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
