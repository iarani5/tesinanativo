package com.example.tesina_nativo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    int GALLERY_REQ_CODE = 1000;
    animal Oneanimal = new animal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Button sendFormBtn = (Button)findViewById(R.id.sendFormBtn);

        sendFormBtn.setOnClickListener(view -> {
            if(validateName()&&validateLastName()&&validateEmail()&&validatephoneNumber()&&validateComments()&&validateRace()){
                //mensaje de creado con exito // Toast.makeText(getApplicationContext(), "Both Name and Password are required", Toast.LENGTH_LONG).show();

                Spinner animalSpecies = (Spinner)findViewById(R.id.spinner_especie);
                Oneanimal.setAnimalSpecies(animalSpecies.getSelectedItem().toString());

                Spinner animalState = (Spinner)findViewById(R.id.spinner_estado);
                Oneanimal.setAnimalState(animalState.getSelectedItem().toString());

                Switch animalCastrated = (Switch)findViewById(R.id.switch1);
                Oneanimal.setAnimalCastrated(animalCastrated.isChecked());

                ImageView photo= (ImageView) findViewById(R.id.imgGallery);
                /*Drawable drawable = photo.getDrawable();

                drawable.setBounds(20, 30, drawable.getIntrinsicWidth()+20, drawable.getIntrinsicHeight()+30);
                drawable.draw(canvas);*/

                //String encodedImage = Base64.encodeToString(, Base64.DEFAULT);


                // Serialization
                Gson gson = new Gson();
                String json = gson.toJson(Oneanimal);
                try {
                    save(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //save(,json);
               /* private String saveToInternalStorage(Bitmap bitmapImage){
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    // Create imageDir
                    File mypath=new File(directory,"profile.jpg");

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return directory.getAbsolutePath();
                }*/

            }
            else{
                Log.i("info","invalido");
            }

            // On application start you can deserialize your entity from file
            //   Entity read = gson.fromJson(json, Entity.class);
        });
    }


    //**** save JSON ****//

    public void save(String jsonString) throws IOException {
        //Get your FilePath and use it to create your File
        String yourFilePath = this.getFilesDir() + "/" + "filename.txt";
        File yourFile = new File(yourFilePath);

        try{
            //Create your FileOutputStream, yourFile is part of the constructor
            FileOutputStream fileOutputStream = new FileOutputStream(yourFile);
            //Convert your JSON String to Bytes and write() it
            fileOutputStream.write(jsonString.getBytes());
            //Finally flush and close your FileOutputStream
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //*** read JSON ***//
    public void read() throws IOException {
        Gson gson = new Gson();
        String text = null;
        //Make sure to use a try-catch statement to catch any errors
            try {
                //Make your FilePath and File
                String yourFilePath = this.getFilesDir() + "/" + "filename.txt";
                File yourFile = new File(yourFilePath);
                //Make an InputStream with your File in the constructor
                InputStream inputStream = new FileInputStream(yourFile);
                StringBuilder stringBuilder = new StringBuilder();
                //Check to see if your inputStream is null
                //If it isn't use the inputStream to make a InputStreamReader
                //Use that to make a BufferedReader
                //Also create an empty String
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    //Use a while loop to append the lines from the Buffered reader
                    while ((receiveString = bufferedReader.readLine()) != null){
                        stringBuilder.append(receiveString);
                    }
                    //Close your InputStream and save stringBuilder as a String
                    inputStream.close();
                    text = stringBuilder.toString();
                    Log.i("rtaaa", gson.fromJson(stringBuilder.toString()));
                }
            } catch (FileNotFoundException e) {
                //Log your error with Log.e
            } catch (IOException e) {
                //Log your error with Log.e
            }
            //Use Gson to recreate your Object from the text String
            //Object yourObject = gson.fromJson(text);
        }


    //************ file ***********//
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
        Oneanimal.setHumanName(contactName.getText().toString());
        if(!Oneanimal.getHumanName().matches("[ña-zA-Z ]{3,}")) {
            contactName.setError("minimo 3 caracteres");
            return false;
        }
        return true;
    }

    private boolean validateLastName(){
        EditText lastName = (EditText)findViewById(R.id.lastName);
        Oneanimal.setHumanLastName(lastName.getText().toString());
        if(!Oneanimal.getHumanLastName().matches("[ña-zA-Z ]{3,}")) {
            lastName.setError("minimo 3 caracteres");
            return false;
        }
        return true;
    }

    private boolean validateEmail(){
        EditText emailAdress = (EditText)findViewById(R.id.EmailAddress);
        Oneanimal.setHumanMail(emailAdress.getText().toString());
        if(!Oneanimal.getHumanMail().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailAdress.setError("formato invalido");
            return false;
        }
        return true;
    }

    private boolean validatephoneNumber(){
        EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        Oneanimal.setHumanPhone(phoneNumber.getText().toString());
        if(!Oneanimal.getHumanPhone().matches("[0-9]{8}")) {
            phoneNumber.setError("formato invalido");
            return false;
        }
        return true;
    }

    private boolean validateComments(){
        EditText comments = (EditText)findViewById(R.id.comments);
        Oneanimal.setComments(comments.getText().toString());
        if(!Oneanimal.getComments().matches("^.{20,}$")) {
            comments.setError("minimo 20 caracteres");
            return false;
        }
        return true;
    }

    private boolean validateRace(){
        EditText race = (EditText)findViewById(R.id.race);
        Oneanimal.setAnimalRace(race.getText().toString());
        if(!Oneanimal.getAnimalRace().matches("[ña-zA-Z ]{3,}")) {
            race.setError("minimo 3 caracteres");
            return false;
        }
        return true;
    }
}

// TO READ THE FILE

/*

private void loadImageFromStorage(String path)
{

    try {
        File f=new File(path, "profile.jpg");
        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imgPicker);
        img.setImageBitmap(b);
    }
    catch (FileNotFoundException e)
    {
        e.printStackTrace();
    }

}


 */