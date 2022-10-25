package com.example.logbook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  EditText imageURL;
  Button addBtn;
  ImageView imageView;
  Button pre;
  Button next;
  int index = 0;
  ArrayList<ImageEntity> arrayList;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    imageURL = findViewById(R.id.imageURL);
    addBtn = findViewById(R.id.addBtn);
    imageView = findViewById(R.id.imageView);
    pre = findViewById(R.id.pre);
    next = findViewById(R.id.next);
    loadData(0);
    // sử dụng button add
    addBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String image = imageURL.getText().toString();
        if (TextUtils.isEmpty(image)) {
          imageURL.setError("Phai them url moi duoc add");
          return;
        }
        if (!image.contains("https") || !image.contains("http")) {
          imageURL.setError("Khong dung link anh");
          return;
        }
        saveImg(image);
        imageURL.setText("");
        Toast.makeText(MainActivity.this, "Them anh thanh cong", Toast.LENGTH_SHORT).show();
      }
    });
    pre.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(index == 0){
          Toast.makeText(MainActivity.this, "day la phan tu dau tien khong the pre", Toast.LENGTH_SHORT).show();
        }else {
          for (int j = 0; j < arrayList.size(); j++) {
            if (index > 0 ) {
              index = index -1;
              Picasso
                .with(MainActivity.this)
                .load(arrayList.get(index).imgUrl)
                .placeholder(R.drawable.loading_animation)
                .into(imageView);
              Toast.makeText(MainActivity.this, "prev", Toast.LENGTH_SHORT).show();
            }
          }
        }
      }
    });
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(index == arrayList.size() -1){
          Toast.makeText(MainActivity.this, "day la phan tu cuoi cung khong the next", Toast.LENGTH_SHORT).show();
        }else {
          for (int j = 0; j < arrayList.size(); j++) {
            if (index < arrayList.size()-1) {
              index = index + 1;
              Toast.makeText(MainActivity.this, "next", Toast.LENGTH_SHORT).show();
              Picasso.with(MainActivity.this)
                .load(arrayList.get(index).imgUrl)
                .placeholder(R.drawable.loading_animation)
                .into(imageView);
            }
          }
        }
      }
    });
  }



  private void saveImg(String imageURL) {
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("imgURL", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    //convert theo json
    Gson gson = new Gson();
    // add ele in arr

    arrayList.add(new ImageEntity(imageURL));

    String json = gson.toJson(arrayList);
    editor.putString("imaURL",json);
    editor.apply();
//  tvSize.setText(" hello");
    Toast.makeText(MainActivity.this, "add thanh cong", Toast.LENGTH_SHORT).show();
    loadData(arrayList.size()-1);
    index = arrayList.size()-1;
  }


  private void loadData(int index) {

    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("imgURL", MODE_PRIVATE);

    Gson gson = new Gson();

    String json = sharedPreferences.getString("imaURL", null);
    Type type = new TypeToken<ArrayList<ImageEntity>>(){

    }.getType();

    arrayList = gson.fromJson(json, type);

    if(arrayList == null){
      arrayList = new ArrayList<>();
      Toast.makeText(MainActivity.this, "show k co phan tu nao", Toast.LENGTH_SHORT).show();
      // show k co phan tu nao
    }else{
      for(int i = 0; i < arrayList.size(); i++){
        if(index != i){
//          imageView.setImageURI();
          Picasso
            .with(getApplicationContext())
            .load(arrayList.get(index).imgUrl)
            .placeholder(R.drawable.loading_animation)
            .into(imageView);
//          pre(0);
//          next(0);
          // show thang dau tien
        }else{
          Picasso
            .with(getApplicationContext())
            .load(arrayList.get(arrayList.size()-1).imgUrl)
            .placeholder(R.drawable.loading_animation)
            .into(imageView);
//          pre(0);
//          next(0);
          //show thang i
        }
      }
    }
  }


}