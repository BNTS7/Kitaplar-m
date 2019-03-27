package com.example.kitaplarm;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


   // static  Bitmap chosenImage;
    static ArrayList<Bitmap> bookImage;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menüyü kullanmamız için gerekli
        MenuInflater menuInflater = getMenuInflater();
        //hangi menuyu çıkarıcaz onu yapıyoruz
        menuInflater.inflate(R.menu.add_book,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //menu secildiğinde olacaklar yazılıyor
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_book){
            Intent intent  =  new Intent(getApplicationContext(),Main2Activity.class);
            intent.putExtra("info", "new");
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.listview);
        final ArrayList<String> bookName = new ArrayList<>();
        //final ArrayList<Bitmap> bookImage = new ArrayList<>();
         bookImage = new ArrayList<>();


        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,bookName);
        listView.setAdapter(arrayAdapter);

        try {
            Main2Activity.database = this.openOrCreateDatabase("Books",MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS books (name VARCHAR , image BLOB)");

            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM books",null);

            int nameIx = cursor.getColumnIndex("name");
            int imageIn = cursor.getColumnIndex("image");

            cursor.moveToFirst();

            while (cursor != null){
                bookName.add(cursor.getString(nameIx));

                byte[]byteArray = cursor.getBlob(imageIn);
                Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                bookImage.add(image);

                cursor.moveToNext();

                //değişikliği adaptere haber veriyeor.adapterde listeyi güncelliyor
                arrayAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("info","old");
                intent.putExtra("name", bookName.get(position));
               // chosenImage = bookImage.get(position);
                //ikinci yöntem adapetri static yapıp positionu aktarcaz

                intent.putExtra("position",position);
                startActivity(intent);



            }
        });

    }


}
