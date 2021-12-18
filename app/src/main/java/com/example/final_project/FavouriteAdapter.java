package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FavouriteAdapter extends ArrayAdapter<NasaModel> {

    private Context context;
    private List<NasaModel> data;

    public FavouriteAdapter(Context context, List<NasaModel> data) {
        super(context, R.layout.news_item, data);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View rowView = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent,false);

        NasaModel model = data.get(position);

        ImageView image = rowView.findViewById(R.id.image);
        TextView title = rowView.findViewById(R.id.title);
        TextView date = rowView.findViewById(R.id.date);
        TextView explanation = rowView.findViewById(R.id.explanation);
        LinearLayout root = rowView.findViewById(R.id.root);

        Typeface typeface = ResourcesCompat.getFont(context,R.font.montserrat_bold);

        title.setText(model.getTitle());
        explanation.setText(model.getExplanation());
        date.setText(Utils.formatDate(model.getDate()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);
        requestOptions.error(R.drawable.image_placeholder);
        if (model.getUrl() != null){
            Glide.with(context).load(model.getUrl()).apply(requestOptions).into(image);
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Details.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data",model);
                context.startActivity(intent);
            }
        });

        title.setTypeface(typeface);

        return rowView;
    }

}
