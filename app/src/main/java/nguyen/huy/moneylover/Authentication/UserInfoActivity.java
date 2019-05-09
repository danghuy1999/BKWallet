package nguyen.huy.moneylover.Authentication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyen.huy.moneylover.R;

public class UserInfoActivity extends AppCompatActivity {
    CircleImageView avtUser;
    TextView txtUserName,txtBalance,txtEmail;
    Button btnChangePass;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Bitmap bitmap;
    ProgressDialog loadDialog;

    Dialog usernameDialog;
    EditText edtUsernameDialog;

    UserProfileChangeRequest profileUpdate;

    public static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        addControls();
        addEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE&& data!=null && data.getData()!=null)
        {
            Uri uri = data.getData();
            Bitmap imageBitmat = null;
            try {
                imageBitmat = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                avtUser.setImageBitmap(imageBitmat);
                uploadImageToStorage(imageBitmat);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void addControls() {
        avtUser = findViewById(R.id.avtUser);
        txtUserName = findViewById(R.id.txtUserName);
        txtBalance = findViewById(R.id.txtBalance);
        txtEmail = findViewById(R.id.txtEmail);
        btnChangePass = findViewById(R.id.btnChangePass);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loadDialog = new ProgressDialog(this);
        loadDialog.setCancelable(false);
        loadDialog.setMessage("Uploading ...");

        usernameDialog = new Dialog(this);
        usernameDialog.setContentView(R.layout.custom_userinfo_usernamedialog);
        customUsernameDialog();
        if (user != null)
        {
            String name = user.getDisplayName();
            String email = user.getEmail();

            if (name!=null) txtUserName.setText(name);
            if (email!=null) txtEmail.setText(email);
            getPhoto();
            getBalance(user.getUid());
        }

    }

    private void customUsernameDialog() {
//        usernameDialog.getWindow().setLayout(600,700);
        edtUsernameDialog = usernameDialog.findViewById(R.id.edtUsernameDialog);
        Button btnCancelDialog = usernameDialog.findViewById(R.id.btnCancelDialog);
        Button btnSaveDialog = usernameDialog.findViewById(R.id.btnSaveDialog);
        btnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameDialog.dismiss();
            }
        });
        btnSaveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edtUsernameDialog.getText().toString()).build();
                loadDialog.show();
                user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadDialog.dismiss();
                        usernameDialog.dismiss();
                        txtUserName.setText(edtUsernameDialog.getText().toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        loadDialog.dismiss();
                        usernameDialog.dismiss();

                    }
                });
            }
        });
    }

    private void addEvents() {
        avtUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
                return true;
            }
        });

        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtUsernameDialog.setText(txtUserName.getText().toString());
                usernameDialog.show();
            }
        });
    }

    private void getPhoto() {
        final long TEN_MEGABYTE = 10 * 1024 * 1024;

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "avatar.jpg");
        try {
            FileInputStream fis = new FileInputStream(mypath);
            Bitmap retrievedBitmap = BitmapFactory.decodeStream(fis);
            avtUser.setImageBitmap(retrievedBitmap);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            bitmap =BitmapFactory.decodeResource(getResources(),R.drawable.images);
            avtUser.setImageDrawable(getDrawable(R.drawable.images));
        }

        storageReference.child("avatar").getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                avtUser.setImageBitmap(bitmap);
                saveBitmapToInternalStorage(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getBalance(String uid) {
        Log.e("UID",uid);
        databaseReference.child(uid).child("Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long balance = dataSnapshot.getValue(Long.class);
                txtBalance.setText(balance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadImageToStorage(final Bitmap imageBitmat) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmat.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference.child("avatar").putBytes(data);
        loadDialog.show();
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Upload task",e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserInfoActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                saveBitmapToInternalStorage(imageBitmat);
                loadDialog.dismiss();
            }
        });
    }

    void saveBitmapToInternalStorage(Bitmap imageBitmap)
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, "avatar.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
    }
}
