package com.example.ambermirza.myapplication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ambermirza on 12/3/16.
 */

public class MainActivity extends Activity{
    static final int REQUEST_IMAGE_GALLERY = 1112;
    static final int REQUEST_IMAGE_CAPTURE = 1111;
    static final int PUZZLE_FROM_CAMERA = 1001;
    static final int PUZZLE_FROM_GALLERY = 1002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent startPuzzle = new Intent(MainActivity.this, PuzzleActivity.class);
                startPuzzle.putExtra("picture", byteArray);
                startPuzzle.putExtra("name", "Picture from Camera");

                startActivityForResult(startPuzzle, PUZZLE_FROM_CAMERA);
            } else if (requestCode == PUZZLE_FROM_CAMERA || resultCode == PUZZLE_FROM_GALLERY ) {
                Toast.makeText(this, "Choose an Option to Play Again!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_IMAGE_GALLERY && null != data) {


                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                    byte[] byteArray = stream.toByteArray();
                    Log.i("MAIN", "Start Puzzle");
                    Intent startPuzzle = new Intent(MainActivity.this, PuzzleActivity.class);
                    startPuzzle.putExtra("picture", byteArray);
                    Log.i("MAIN", "Start Added Byte Array");

                    startPuzzle.putExtra("name", "Picture from Gallery");

                    startActivityForResult(startPuzzle, PUZZLE_FROM_GALLERY);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*
                Log.i("MAIN", "Begin send data to puzzle");
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Log.i("MAIN", "Begin bitmap compression");
                Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Log.i("MAIN", "Start Puzzle");
                Intent startPuzzle = new Intent(MainActivity.this, PuzzleActivity.class);
                startPuzzle.putExtra("picture", byteArray);
                startPuzzle.putExtra("name", "Picture from Gallery");
                startActivityForResult(startPuzzle, PUZZLE_FROM_GALLERY);
                */
            }
        }
    }

    //function called when user hits menu item "MAP"
    public void map(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    //TODO finish
    //function called when user hits menu item "GALLERY"
    public void gallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);


    }

    //TODO finish
    //function called when user hits menu item "TAKE PHOTO"
    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Uh oh something went wrong with taking a photo",
                    Toast.LENGTH_SHORT).show();
        }
    }



}
