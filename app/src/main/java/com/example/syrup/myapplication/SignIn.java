package com.example.syrup.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.syrup.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    //properties
    private final String NAME = "Name";
    private final String SURNAME = "Surname";
    private final String EMAIL = "Email";
    private final String GROUPCODE = "GroupCode";
    private final String TAG = "userInfo";
    private Button buttonRegister;
    private ImageView profileImage;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private DocumentReference mDocRef;

    private StorageReference storageReference;
    private StorageReference myRef;
    private StorageReference imgRef;

    private Uri imgUri;
    private Uri downloadUri;

    //constants
    private final String FB_STORAGE_REF = "users/";
    private final String FB_PROFILE = "profile/";
    private final String FB_RECORDINGS = "recordings/";
    private final String FB_CONTACT_LIST = "contactList/";
    private final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        buttonRegister    = findViewById(R.id.register);

        editTextName      = findViewById(R.id.name);
        editTextSurname   = findViewById(R.id.surname);
        editTextEmail     = findViewById(R.id.email);
        editTextPassword  = findViewById(R.id.password);
        editTextPassword2 = findViewById(R.id.password2);
        profileImage = findViewById(R.id.profileImage);

        storage = FirebaseStorage.getInstance();

        buttonRegister.setOnClickListener(this);


        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mDocRef = FirebaseFirestore.getInstance().document("users/userInfo");

        storageReference = storage.getReference();
    }

    private void registerUser() {
        final String name      = editTextName.getText().toString().trim();
        final String surname   = editTextSurname.getText().toString().trim();
        final String email     = editTextEmail.getText().toString().trim();
        final String password  = editTextPassword.getText().toString().trim();
        final String password2 = editTextPassword2.getText().toString().trim();
        final String groupCode = "";
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(surname))
        {
            Toast.makeText(this, "enter surname", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(password2))
        {
            Toast.makeText(this, "enter password again", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if ( !password.equals(password2) )
        {
            Toast.makeText(this, "passwords does not match", Toast.LENGTH_SHORT).show();
            return;
        }

        //if validations are ok ->

        progressDialog.setMessage("registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful() )
                        {
                            progressDialog.dismiss();
                            //successful
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            Map<String, Object> dataToSave = new HashMap<String, Object>();
                            dataToSave.put(NAME, name);
                            dataToSave.put(SURNAME, surname);
                            dataToSave.put(EMAIL, email);
                            dataToSave.put(GROUPCODE, groupCode);

                            //setting firebase storage
                            String path = "firememes/" + UUID.randomUUID();
                            //creating base reference
                            StorageReference groupNum = storage.getReference();
                            StorageReference imagesRef = groupNum.child("images");

                            //creating profile folder in Firebase
                            byte[] b = new byte[20];
                            new Random().nextBytes(b);
                            myRef = storageReference.child(FB_STORAGE_REF + name + surname + "/" + FB_PROFILE).child("template.txt");
                            myRef.putBytes(b);

                            //creating recordings folder in Firebase
                            myRef = storageReference.child(FB_STORAGE_REF + name + surname + "/" + FB_RECORDINGS).child("template.txt");
                            myRef.putBytes(b);

                            //creating contact list folder in Firebase
                            myRef = storageReference.child(FB_STORAGE_REF + name + surname + "/" + FB_CONTACT_LIST).child("template.txt");
                            myRef.putBytes(b);

                            firebaseFirestore.collection("users").document( currentUser.getUid())
                                .set(dataToSave)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully saved!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                            //start activity
                            Toast.makeText(SignIn.this,
                                    "resigtered successfully", Toast.LENGTH_SHORT).show();
                            Intent goMainPage = new Intent(SignIn.this, MainActivity.class);
                            startActivity(goMainPage);
                        }
                        else
                            progressDialog.dismiss();
                            Toast.makeText(SignIn.this,
                                    "could not register", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if ( view == buttonRegister )
        {
            registerUser();
        }
    }

    //ClickListener for adding photo
    public void btnBrowseClick(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO
        //fix issue with adding photo to Firebase
        //add this feature to the register button instead of working when the image is selected
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            imgUri = data.getData();
            imgRef = storageReference.child(FB_STORAGE_REF +  FB_PROFILE + "images").child("profilePicture" + getImageExtension(imgUri));
            imgRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.get().load(downloadUri).fit().centerCrop().into(profileImage);

                    Toast.makeText(SignIn.this, "Upload done", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignIn.this, "Unable to upload", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public String getImageExtension( Uri uri )
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
