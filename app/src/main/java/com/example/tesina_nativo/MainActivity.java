package com.example.tesina_nativo;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    animal Oneanimal = new animal();
    ImageView img;
    String root;
    Context context;
    Uri uri;
    String dataLoaded;

    //generate key to store user info in db
    Date date = new Date();
    Timestamp timestamp2 = new Timestamp(date.getTime());

    String name = "user_pet" + timestamp2;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();



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

        //******** IMAGE ******//

        //permissions to user required
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        // PET AVATAR
        Button btnGallery = findViewById(R.id.btnGallery);
        img = (ImageView) findViewById(R.id.imgGallery);
        img.setImageResource(R.drawable.nuevo_proyecto);

        btnGallery.setOnClickListener(view -> {
            context=this;
            selectImage(context);
        });

        //***** SEND FORM *****//

        Button sendFormBtn = (Button)findViewById(R.id.sendFormBtn);

        sendFormBtn.setOnClickListener(view -> {
            if(validateName()&&validateLastName()&&validateEmail()&&validatephoneNumber()&&validateComments()&&validateRace()){

                Spinner animalSpecies = (Spinner)findViewById(R.id.spinner_especie);
                Oneanimal.setAnimalSpecies(animalSpecies.getSelectedItem().toString());

                Spinner animalState = (Spinner)findViewById(R.id.spinner_estado);
                Oneanimal.setAnimalState(animalState.getSelectedItem().toString());

                Switch animalCastrated = (Switch)findViewById(R.id.switch1);
                Oneanimal.setAnimalCastrated(animalCastrated.isChecked());

                saveData(Oneanimal);
            }
            else{
                Log.i("info","invalido");
            }

            // On application start you can deserialize your entity from file
            // Entity read = gson.fromJson(json, Entity.class);
        });
    }

    public void saveData(animal user_animal){

        Map<String, Object> user = new HashMap<>();
        user.put("situacion", user_animal.getAnimalState());
        user.put("especie", user_animal.getAnimalSpecies());
        user.put("castrado", user_animal.isAnimalCastrated());
        user.put("raza", user_animal.getAnimalRace());
        user.put("comentarios", user_animal.getComments());
        user.put("nombre", user_animal.getHumanName());
        user.put("apellido", user_animal.getHumanLastName());
        user.put("email", user_animal.getHumanMail());
        user.put("celular", user_animal.getHumanPhone());
        user.put("foto", uri);

        // Add a new document with a generated ID
        db.collection(name)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        modal();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void getData(){
        db.collection(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //document.getData()
                                //ENVIAR DATOS DE LA BBD DESDE ESTA CLASE AL LAYOUT DE MODAL
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //******* MODAL *******//

    public void modal() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Publicación creada con éxito");
        // alert.setMessage("Message");

        alert.setPositiveButton("Ver", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //get data form db
                getData();

                setContentView(R.layout.modal);

            }
        });

        alert.setNegativeButton("Volver",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();

    }

    //************ file ***********//
    private void selectImage(Context context) {
        final CharSequence[] options = { "Tomar Foto", "Elegir de la galerìa","Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Elegir foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tomar Foto")) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());

                    uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    startActivityForResult(takePicture, 0);


                } else if (options[item].equals("Elegir de la galerìa")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                    Oneanimal.setAnimalPhoto(uri);


                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case 0:
                    img.setImageURI(uri);
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                img.setImageURI(data.getData());
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    //** save file **//

    private File SaveImage() throws IOException{
        String nameFile = "foto_";
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(nameFile,".jpg", directory);
        root = file.getAbsolutePath();
        return file;
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