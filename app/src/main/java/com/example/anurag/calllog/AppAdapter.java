package com.example.anurag.calllog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

//import com.bumptech.glide.Glide;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    List<String> data;
    String abhishekUrl ="https://s3.ap-south-1.amazonaws.com/call-users-images/abhishek.jpg";
    String anuragUrl = "https://s3.ap-south-1.amazonaws.com/call-users-images/anurag.jpg";
    String gudiyaUrl = "https://s3.ap-south-1.amazonaws.com/call-users-images/gudiya.jpg";
    private RecyclerViewClickListener mListener;

   public AppAdapter(List<String> data)
    {
        this.data = data;
        System.out.println(data.toString());

    }

    AppAdapter(List<String> data, RecyclerViewClickListener listener) {
        this.data = data;
       mListener = listener;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listr_item_layout,parent,false);
        return new AppViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
          String text = data.get(position);

          if(text.contains("Abhishek"))
          {
              Picasso.with(holder.imageView.getContext()).load(abhishekUrl)
                      .into(holder.imageView);
          }
        if(text.contains("Gudiya"))
        {
            Picasso.with(holder.imageView.getContext()).load(gudiyaUrl)
                    .into(holder.imageView);
        }
        if(text.contains("Anurag"))
        {
            Picasso.with(holder.imageView.getContext()).load(anuragUrl)
                    .into(holder.imageView);
        }

              holder.textView.setText(text);

//        Glide.with(holder.imageView.getContext()).load("http://192.168.1.5:8080/cover.jpg")
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class  AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       private RecyclerViewClickListener mListener;
       ImageView imageView;
       TextView textView;
        TextView textViewNoContent;
        public AppViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
            textView = itemView.findViewById(R.id.txtid);

        }

        public AppViewHolder(View v,RecyclerViewClickListener listener) {
            super(v);
            imageView = itemView.findViewById(R.id.imgView);

            textView = itemView.findViewById(R.id.txtid);
            mListener = listener;
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            mListener.onClick(view, getAdapterPosition(),textView.getText().toString());
//            mListener.onClick(view, getAdapterPosition(),"ok");
        }
    }

}
