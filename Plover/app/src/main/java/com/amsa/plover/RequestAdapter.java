package com.amsa.plover;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    Context mContext;
    List<Profile> mData;
    Dialog mydialog;

    public RequestAdapter(Context mContext, List<Profile> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view;
        view= LayoutInflater.from(mContext).inflate(R.layout.item_request,viewGroup,false);
        final MyViewHolder myViewHolder=new MyViewHolder(view);

        // Dialog ini
//        mydialog=new Dialog(mContext);
//        mydialog.setContentView(R.layout.dialog_contact);
//        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        myViewHolder.contact_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                TextView dialog_name_tv=(TextView) mydialog.findViewById(R.id.dialog_name_Id);
//                TextView dialog_phone_tv=(TextView) mydialog.findViewById(R.id.dialog_phone_Id);
//                ImageView dialog_contact_img=(ImageView)mydialog.findViewById(R.id.dialog_image_Id);
//
//                dialog_name_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getName());
//                dialog_phone_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getPhone());
////                dialog_contact_img.setImageResource(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl());
//                Picasso.with(mContext).load(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()).placeholder(R.mipmap.ic_action_person).fit().centerCrop().into(dialog_contact_img);
//
////                Toast.makeText(mContext,mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl(),Toast.LENGTH_LONG).show();
////                Toast.makeText(mContext,new Home().getProfileId(),Toast.LENGTH_LONG).show();
//                mydialog.show();

                final String profileId=new Home().getProfileId();
                final String name=new Home().getName();
                final String mobile=new Home().getMobile();
                final String imageName=new Home().getImageName();
                final String gender=new Home().getGender();
                final String imageDownloadUrl=new Home().getImageDownloadUrl();

                final Dialog mydialog;
                mydialog=new Dialog(mContext);
                mydialog.setContentView(R.layout.dialogforsearch);
                TextView search_dialog_name_tv=(TextView) mydialog.findViewById(R.id.search_dialog_name_Id);
                TextView search_dialog_phone_tv=(TextView) mydialog.findViewById(R.id.search_dialog_phone_Id);
                ImageView search_dialog_contact_img=(ImageView)mydialog.findViewById(R.id.search_dialog_image_Id);
                final Button search_dialog_req_send_btn=(Button) mydialog.findViewById(R.id.search_dialog_req_send_btn_Id);
                final Button search_dialog_req_cancel_btn=(Button) mydialog.findViewById(R.id.search_dialog_req_cancel_btn_Id);
                final Button search_dialog_req_accept_btn=(Button) mydialog.findViewById(R.id.search_dialog_req_accept_btn_Id);
//                search_dialog_req_accept_btn.setEnabled(true);
//                search_dialog_req_cancel_btn.setEnabled(true);
                search_dialog_req_send_btn.setEnabled(false);
                search_dialog_req_send_btn.setVisibility(View.GONE);

                search_dialog_name_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getName());
                search_dialog_phone_tv.setText(mData.get(myViewHolder.getAdapterPosition()).getPhone());
                Picasso.with(mydialog.getContext()).load(mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl()).placeholder(R.drawable.male).fit().centerCrop().into(search_dialog_contact_img);
                try {
                    mydialog.show();
                }
                catch (Exception e){

                }
                search_dialog_req_cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //////////request cancel jodi onno keu pathay
                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestCome").child(mData.get(myViewHolder.getAdapterPosition()).getId());
                        databaseReference3.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {/////nijer reqCome theke delete kore
                                if(task1.isSuccessful()){

                                }
                            }
                        });
                        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Profile").child(mData.get(myViewHolder.getAdapterPosition()).getId()).child("RequestSent").child(profileId);
                        databaseReference4.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {////onner reqSent theke delete kora
                                if(task1.isSuccessful()){
                                    Toast.makeText(mydialog.getContext(),"request canceled",Toast.LENGTH_LONG).show();
                                    mydialog.dismiss();
                                }
                            }
                        });

                    }
                });

                search_dialog_req_accept_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ////accept request
                        String key=profileId+"+";
                        key += mData.get(myViewHolder.getAdapterPosition()).getId();

                        final String msgId=String.valueOf(Calendar.getInstance().getTimeInMillis());
                        HashMap<String,String> hashMap3=new HashMap<>();
                        hashMap3.put("Msg","Welcome");
                        hashMap3.put("MsgId",profileId);
                        hashMap3.put("MsgType","text");
                        DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference("Friendship").child(key).child(msgId);
                        databaseReference5.setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {/////friend directory te fnf directory baniye
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {
                                if(task1.isSuccessful()){

                                    String key=mData.get(myViewHolder.getAdapterPosition()).getId()+"+";
                                    key += profileId;
                                    HashMap<String,String> hashMap10=new HashMap<>();
                                    hashMap10.put("Msg","Welcome");
                                    hashMap10.put("MsgId",mData.get(myViewHolder.getAdapterPosition()).getId());
                                    hashMap10.put("MsgType","text");
                                    DatabaseReference databaseReference10 = FirebaseDatabase.getInstance().getReference("Friendship").child(key).child(msgId);
                                    databaseReference10.setValue(hashMap10).addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()){

                                            }
                                        }
                                    });

                                    HashMap<String,String> hashMap8=new HashMap<>();
                                    hashMap8.put("Id",profileId);
                                    hashMap8.put("Name",name);
                                    hashMap8.put("Gender",gender);
                                    hashMap8.put("Phone",mobile);
                                    hashMap8.put("Password","notUsed");
                                    hashMap8.put("ImageName",imageName);
                                    hashMap8.put("ImageDownloadUrl",imageDownloadUrl);
                                    DatabaseReference databaseReference8 = FirebaseDatabase.getInstance().getReference("Profile").child(mData.get(myViewHolder.getAdapterPosition()).getId()).child("Friends").child(profileId);
                                    databaseReference8.setValue(hashMap8).addOnCompleteListener(new OnCompleteListener<Void>() {//tar friend lis e nijeke include kora
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {////tar friends directory te amake add kora
                                            if(task1.isSuccessful()){

                                            }
                                        }
                                    });
                                    HashMap<String,String> hashMap9=new HashMap<>();
                                    hashMap9.put("Id",mData.get(myViewHolder.getAdapterPosition()).getId());
                                    hashMap9.put("Name",mData.get(myViewHolder.getAdapterPosition()).getName());
                                    hashMap9.put("Gender",mData.get(myViewHolder.getAdapterPosition()).getGender());
                                    hashMap9.put("Phone",mData.get(myViewHolder.getAdapterPosition()).getPhone());
                                    hashMap9.put("Password","notUsed");
                                    hashMap9.put("ImageName",mData.get(myViewHolder.getAdapterPosition()).getImageName());
                                    hashMap9.put("ImageDownloadUrl",mData.get(myViewHolder.getAdapterPosition()).getImageDownloadUrl());
                                    DatabaseReference databaseReference9 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("Friends").child(mData.get(myViewHolder.getAdapterPosition()).getId());
                                    databaseReference9.setValue(hashMap9).addOnCompleteListener(new OnCompleteListener<Void>() {//nijer friend lis e take include kora
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {////amar friends directory te take add kora
                                            if(task1.isSuccessful()){
//                                                Toast.makeText(mydialog.getContext(),"request Sent",Toast.LENGTH_LONG).show();
                                                mydialog.dismiss();
                                            }
                                        }
                                    });

                                    DatabaseReference databaseReference6 = FirebaseDatabase.getInstance().getReference("Profile").child(profileId).child("RequestCome").child(mData.get(myViewHolder.getAdapterPosition()).getId());
                                    databaseReference6.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {/////nijer reqCome theke delete kore
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()){

                                            }
                                        }
                                    });
                                    DatabaseReference databaseReference7 = FirebaseDatabase.getInstance().getReference("Profile").child(mData.get(myViewHolder.getAdapterPosition()).getId()).child("RequestSent").child(profileId);
                                    databaseReference7.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {////onner reqSent theke delete kora
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()){
                                                Toast.makeText(mydialog.getContext(),"request accepted",Toast.LENGTH_LONG).show();
                                                mydialog.dismiss();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                });
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
            contact_item=(LinearLayout)itemView.findViewById(R.id.request_item_Id);
            tv_name=(TextView)itemView.findViewById(R.id.name_request_Id);
            tv_phone=(TextView)itemView.findViewById(R.id.phone_request_Id);
            img=(ImageView)itemView.findViewById(R.id.img_request);

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
