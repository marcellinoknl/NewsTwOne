package com.example.newstwoone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsTwoneDetailActivity extends AppCompatActivity {

    String title, desc, content, imageUrl, url;
    private TextView titleTV, subDesc, contentTV;
    private ImageView newsIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_twone_detail);

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageUrl = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        titleTV = findViewById(R.id.tvNews);
        subDesc = findViewById(R.id.tvSubDesc);
        contentTV = findViewById(R.id.tvContent);
        newsIV = findViewById(R.id.ivNews);
        Button readNewsBtn = findViewById(R.id.btnRead);

        titleTV.setText(title);
        subDesc.setText(desc);
        contentTV.setText(content);
        Picasso.get().load(imageUrl).into(newsIV);
        readNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }
}