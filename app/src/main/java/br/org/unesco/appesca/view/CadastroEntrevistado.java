package br.org.unesco.appesca.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import br.org.unesco.appesca.R;

/**
 * Created by juniorbraga on 06/01/16.
 */
public class CadastroEntrevistado extends Activity{

    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    byte CAMERA_REQUEST ;
    private ImageView btnIniciar;
    private ImageView imgFoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questao_identificacao);

        btnIniciar = (ImageView) findViewById(R.id.entrevistadoImg);
        imgFoto = (ImageView) findViewById(R.id.entrevistadoImg);


    }

    public void onClickCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imgFoto.setImageBitmap(bitmap);

        }
    }

}

