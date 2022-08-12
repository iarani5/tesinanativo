package com.example.tesina_nativo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    int GALLERY_REQ_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lastname validation VALIADTE ONCHANGE
        /*lastName.addTextChangedListener(new TextValidator(lastName) {
            @Override public void validate(TextView textView, String text) {

                String firstNameVal = lastName.getText().toString();

                if(firstNameVal.matches("[ña-zA-Z]{3,}")) {
                    Log.i("val","es letra");
                } else {
                    lastName.setError("minimo 3 caracteres");
                }
            }
        });*/

        Spinner spinnerEstado=findViewById(R.id.spinner_estado);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.estado, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerEstado.setAdapter(adapter);

        Spinner spinnerEspecie=findViewById(R.id.spinner_especie);
        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this, R.array.especie, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerEspecie.setAdapter(adapter2);

        // PET AVATAR
        Button btnGallery = findViewById(R.id.btnGallery);
        ImageView img= (ImageView) findViewById(R.id.imgGallery);
        img.setImageResource(R.drawable.nuevo_proyecto);

        btnGallery.setOnClickListener(view -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });

        //***** SEND FORM *****//
        Gson gson = new Gson();

        Button sendFormBtn = (Button)findViewById(R.id.sendFormBtn);

        sendFormBtn.setOnClickListener(view -> {
            if(validateName()&&validateLastName()&&validateEmail()&&validatephoneNumber()&&validateComments()&&validateRace()){
                //save content
                //mensaje de creado con exito // Toast.makeText(getApplicationContext(), "Both Name and Password are required", Toast.LENGTH_LONG).show();
                Log.i("info","valido");
            }
            else{
                Log.i("info","invalido");
            }
            //Log.i("info","click");
            //Serialize to JSON. Then you can save string to file
         //   String json = gson.toJson(entity); // {"id":100,"name":"name"}

            // On application start you can deserialize your entity from file
         //   Entity read = gson.fromJson(json, Entity.class);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(resultCode, resultCode, data);
        ImageView imgGallery = findViewById(R.id.imgGallery);

        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                imgGallery.setImageURI(data.getData());
            }
        }

    }


    //********** VALIDATIONS **********//
    private boolean validateName(){
        EditText contactName = (EditText)findViewById(R.id.name);
        String firstNameVal = contactName.getText().toString();
        if(!firstNameVal.matches("[ña-zA-Z]{3,}")) {
            contactName.setError("minimo 3 caracteres");
            return false;
        }
        return true;
    }

    private boolean validateLastName(){
        EditText lastName = (EditText)findViewById(R.id.lastName);
        String firstNameVal = lastName.getText().toString();
        if(!firstNameVal.matches("[ña-zA-Z]{3,}")) {
            lastName.setError("minimo 3 caracteres");
            return false;
        }
        return true;
    }

    private boolean validateEmail(){
        EditText emailAdress = (EditText)findViewById(R.id.EmailAddress);
        String firstNameVal = emailAdress.getText().toString();
        if(!firstNameVal.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailAdress.setError("formato invalido");
            return false;
        }
        return true;
    }

    private boolean validatephoneNumber(){
        EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        String firstNameVal = phoneNumber.getText().toString();
        if(!firstNameVal.matches("[0-9]{8}")) {
            phoneNumber.setError("formato invalido");
            return false;
        }
        return true;
    }

    private boolean validateComments(){
        EditText comments = (EditText)findViewById(R.id.comments);
        String firstNameVal = comments.getText().toString();
        if(!firstNameVal.matches("^.{20,}$")) {
            comments.setError("minimo 20 caracteres");
            return false;
        }
        return true;
    }

    private boolean validateRace(){
        EditText race = (EditText)findViewById(R.id.race);
        String firstNameVal = race.getText().toString();
        if(!firstNameVal.matches("[ña-zA-Z]{3,}")) {
            race.setError("minimo 3 caracteres");
            return false;
        }
        return true;
    }
}


