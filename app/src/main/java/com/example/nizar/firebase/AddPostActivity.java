package com.example.nizar.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URL;

public class AddPostActivity extends AppCompatActivity {

    private ImageButton mselectedImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private TextView onImgClick;
    private Button mShareBtn;
    private  Uri imgUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private static final int GALLERY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mselectedImage= findViewById(R.id.img);
        mPostTitle = findViewById(R.id.post_title);
        mPostDesc = findViewById(R.id.post_desc);
        onImgClick = findViewById(R.id.onImgClick);
        mShareBtn = findViewById(R.id.share_btn);

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        mselectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharingPost();
            }
        });

    }

    private void sharingPost() {
        mProgress.setMessage("Uploading UR Post...");
        mProgress.show();
        String title_val = mPostTitle.getText().toString();
        String desc_val = mPostDesc.getText().toString();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && imgUri!= null){
            final StorageReference filePath = mStorage.child("Blog_Image").child(imgUri.getLastPathSegment());
           UploadTask uploadTask = filePath.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Uri down = filePath.getDownloadUrl();
               }
           });
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if(!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return filePath.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if(task.isSuccessful()){
//                        Uri downloadUri = task.getResult();
//                       String upload_img_url = downloadUri.toString();
//                    }else {
//                        Toast.makeText(getApplicationContext(),"Image uploading failed",Toast.LENGTH_LONG).show();
//                    }
//                }
//            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
           imgUri = data.getData();
           mselectedImage.setImageURI(imgUri);
           mselectedImage.setScaleType(ImageView.ScaleType.FIT_XY);
           onImgClick.setVisibility(View.INVISIBLE);


       }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
