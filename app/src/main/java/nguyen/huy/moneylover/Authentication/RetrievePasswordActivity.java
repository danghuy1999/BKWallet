package nguyen.huy.moneylover.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import nguyen.huy.moneylover.R;

public class RetrievePasswordActivity extends AppCompatActivity implements View.OnClickListener  {
    TextView txtSignUpRP;
    Button btnResetPassword;
    EditText edtEmailRP;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        addControls();
        addEvents();
    }
    private void addControls() {
        txtSignUpRP = findViewById(R.id.txtSignUpRP);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        edtEmailRP = findViewById(R.id.edtEmailRP);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {
        txtSignUpRP.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
    }

    private boolean checkEmailString(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void requestResetPassword() {
        String email = edtEmailRP.getText().toString();
        boolean checkEmail = checkEmailString(email);
        if (checkEmail)
        {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(RetrievePasswordActivity.this,getString(R.string.toast_PasswordResetSuccessfully),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        } else
        {
            Toast.makeText(RetrievePasswordActivity.this,getString(R.string.error_EmailType),Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        Intent SU_intent = new Intent(this,SignUpActivity.class);
        startActivity(SU_intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnResetPassword: requestResetPassword(); break;
            case R.id.txtSignUpRP : signUp(); break;
        }
    }
}
