package com.playatanu.test;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.net.URLEncoder;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    private static final int pic_id = 123;
    TextRecognizer textRecognizer;
    EditText Result;
    TextToSpeech TTP;
    Button camera_open_id;
    ImageView click_image_id;
    View click;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        camera_open_id = (Button)findViewById(R.id.camera_button);
        click_image_id = (ImageView)findViewById(R.id.click_image);
        click = findViewById(R.id.click);

        Result = findViewById(R.id.Result);



        TTP = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    TTP.setLanguage(Locale.UK);
                }
            }
        });

        TTP.speak("Hi! Search Anythings",TextToSpeech.QUEUE_FLUSH,null);






        click.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {


                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);

                startActivityForResult(camera_intent, pic_id);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");



            click_image_id.setImageBitmap(photo);

            Frame frame = new Frame.Builder().setBitmap(photo).build();
            textRecognizer = new TextRecognizer.Builder(this).build();

            SparseArray<TextBlock> item = textRecognizer.detect(frame);

            StringBuilder stringBuilder = new StringBuilder();

            for (int i=0; i<item.size(); i++){

                stringBuilder.append(item.valueAt(i).getValue());

            }

            Result.setText(stringBuilder.toString());


            TTP.speak(Result.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);



            camera_open_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String q = Result.getText().toString();
                    Uri uri = Uri.parse("http://www.google.com/images?q="+q);
                    Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
                    TTP.speak("Serching On Google "+q,TextToSpeech.QUEUE_FLUSH,null);
                    startActivity(gSearchIntent);

                }
            });

        }


    }
}

