package com.pa1.loginui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    //github https://github.com/pa1jesw/LoginUi.git

    //open settings->click Collaborators->type on search on username/email......
    //navigation drawer website for detail info   https://stablekernel.com/using-fragments-to-simply-android-nagigation-drawers/
//login using sqlite: http://computerjunkies.com.android-login-registration-screen-with-sqlite-database-example/
    Button buttong, buttons, buttonc;
    EditText user, pass;
    TextView tvforgot;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttong=findViewById(R.id.btnGoogle);
        buttonc=findViewById(R.id.btnnewAcc);
        buttons=findViewById(R.id.btnLogin);
        user=findViewById(R.id.et_email);
        pass=findViewById(R.id.et_pas);
        tvforgot=findViewById(R.id.tvForgot);
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In

        //pops up the available email sign in optinons of user
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Toast.makeText(this, "create Account", Toast.LENGTH_SHORT).show();
            buttonc.setEnabled(true);
        }
        else
        {
            buttonc.setEnabled(false);
            buttons.setEnabled(true);
            Toast.makeText(this, "please sign in", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginF(View view)
    {
        //google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 500);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 500) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //(TAG, "Google sign in failed", e);
                // ...
                Toast.makeText(this, "Exception occured", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(MainActivity.this, "Signinwithcredential:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i=new Intent(getApplicationContext(),
                                    Main2Activity.class);
                            startActivity(i);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Signinwithcredentials:failure", Toast.LENGTH_SHORT).show();
                     //       Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                       //     updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void signinF(View view) {
        final String email=user.getText().toString();
        String passw=pass.getText().toString();
        if(TextUtils.isEmpty(email)|| !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            user.setError("invalid email");
            user.requestFocus();
            return;
        }
        else if(passw.length()<8 )
        {
            pass.setError("Enter a valid password");
            pass.requestFocus();
            return;
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser currentuser=mAuth.getCurrentUser();
                        String useremail=currentuser.getEmail();
                        Toast.makeText(MainActivity.this, "Sign In successfull"+useremail, Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),
                                Main2Activity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Sign In failed with Exception\n"
                                +task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void createF(View view) {
        final String email=user.getText().toString();
        String passw=pass.getText().toString();
             if(TextUtils.isEmpty(email)|| !Patterns.EMAIL_ADDRESS.matcher(email).matches())
             {
                 user.setError("invalid email");
                 user.requestFocus();
                 return;
             }
             else if(passw.length()<8 )
             {
                 pass.setError("Enter a valid password");
                 pass.requestFocus();
                 return;
             }
             else
             {
                 mAuth.createUserWithEmailAndPassword(email,passw)
                         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "Created Account Successfully with email"+email, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Authentication failed with Exception\n"
                                            +task.getException(), Toast.LENGTH_SHORT).show();
                                }
                             }
                         });
             }
    }
}
