package com.taxi.rapide.taxi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.taxi.rapide.taxi.model.users;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    Button btnsingin, btnrgistor;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users1;
    RelativeLayout rootlayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users1 = database.getReference("Users");

        btnrgistor = (Button) findViewById(R.id.btnregistor);
        btnsingin = (Button) findViewById(R.id.btnsingin);
        rootlayout=(RelativeLayout)findViewById(R.id.rootlayout);

        btnrgistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "minnn", Toast.LENGTH_SHORT).show();
               // showRegisterDialog();
            }
        });
        btnsingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "ghgffhh", Toast.LENGTH_SHORT).show();
                showLoginDialog();

            }
        });


    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("SINGIN");
        dialog.setMessage("Please use email to sing up");

        LayoutInflater inflater=LayoutInflater.from(this);
        View singin=inflater.inflate(R.layout.singin,null);

        final MaterialEditText editEmail=singin.findViewById(R.id.Email);
        final MaterialEditText editPassword=singin.findViewById(R.id.editPassword);



        dialog.setView(singin);

        dialog.setPositiveButton("SINGIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

//                SpotsDialog alertDialog = new SpotsDialog(MainActivity.this);
//                alertDialog.show();



//                validation

                if (TextUtils.isEmpty(editEmail.getText().toString()))
                {
                    Snackbar.make(rootlayout, "please enter email", Snackbar.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(editPassword.getText().toString()))
                {
                    Snackbar.make(rootlayout, "please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (editPassword.getText().toString().length()<6)
                {
                    Snackbar.make(rootlayout, "password too short", Snackbar.LENGTH_SHORT).show();
                    return;


                }


                auth.signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
//                                Toast.makeText(MainActivity.this, "succes" , Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(MainActivity.this,Welcome.class));
                               finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootlayout,"echouer",Snackbar.LENGTH_SHORT).show();
                    }
                });





            }
        }); dialog.show();

    }

    private void showRegisterDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
//        dialog.setMessage("Please use email ");

        LayoutInflater inflater=LayoutInflater.from(this);
        View register=inflater.inflate(R.layout.register,null);

        final MaterialEditText editEmail=register.findViewById(R.id.Email);
        final MaterialEditText editPassword=register.findViewById(R.id.editPassword);
        final MaterialEditText editName=register.findViewById(R.id.editName);
        final MaterialEditText phone=register.findViewById(R.id.editphone);


        dialog.setView(register);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

//                validation
                if (TextUtils.isEmpty(editEmail.getText().toString()))
                {
                    Snackbar.make(rootlayout, "please enter email", Snackbar.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(editPassword.getText().toString()))
                {
                   Snackbar.make(rootlayout, "please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString()))
                {
                    Snackbar.make(rootlayout, "please enter phone", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (editPassword.getText().toString().length()<6)
                {
                    Snackbar.make(rootlayout, "password too short", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //save user
                                users us = new  users();
                                us.setEmail(editEmail.getText().toString());
                                us.setName(editName.getText().toString());
                                us.setPassword(editPassword.getText().toString());
                                us.setPhone(phone.getText().toString());

//                        use email to key
                                users1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(us).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//                                        Snackbar.make(rootlayout, "Register success fully", Snackbar.LENGTH_SHORT).show();
//                                        return;
                                        Toast.makeText(MainActivity.this, "succes" , Toast.LENGTH_SHORT).show();

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                                                Snackbar.make(rootlayout, "Failed"+e, Snackbar.LENGTH_SHORT).show();
//                                                return;
                                                Toast.makeText(MainActivity.this, "echouer" +e, Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootlayout, "Failed"+e, Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        });




            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }

}
