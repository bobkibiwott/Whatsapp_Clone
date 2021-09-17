package com.codept.whatsappclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class PhoneAuth extends AppCompatActivity {
    CountryCodePicker ccp;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        EditText phoneNo=(EditText)findViewById(R.id.phoneAuthNumber);
        ccp=findViewById(R.id.ccp);
        Button done=findViewById(R.id.phoneAuthDone);
        ccp.registerCarrierNumberEditText(phoneNo);
        //
        builder = new AlertDialog.Builder(this);
        //

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
                    @Override
                    public void onValidityChanged(boolean isValidNumber) {
                        if (isValidNumber)
                        {
                            alertDialog(ccp.getFullNumberWithPlus());

                        }else
                        {
                            Toast.makeText(PhoneAuth.this, "Phone number is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    private void alertDialog(String number)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("We will be verifying the phone number:\n\n"+ number+"\n\nIs this OK,or would you like to edit the number?" );
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent=new Intent(PhoneAuth.this,OTPVerification.class);
                                intent.putExtra("number",number);
                                startActivity(intent);

                            }
                        });

        alertDialogBuilder.setNegativeButton("EDIT",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}