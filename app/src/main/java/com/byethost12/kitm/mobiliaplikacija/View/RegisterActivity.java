package com.byethost12.kitm.mobiliaplikacija.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byethost12.kitm.mobiliaplikacija.Controller.Validation;
import com.byethost12.kitm.mobiliaplikacija.Model.DatabaseSQLite;
import com.byethost12.kitm.mobiliaplikacija.Model.User;
import com.byethost12.kitm.mobiliaplikacija.R;

import java.util.List;

/**
 * Created by mokytojas on 2018-02-07.
 */

public class RegisterActivity extends AppCompatActivity {
        EditText username;
        EditText password;
        EditText repeatpassword;
        EditText email;
        Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username            = (EditText)findViewById(R.id.etUsername);
        password            = (EditText)findViewById(R.id.etPassword);
        repeatpassword      = (EditText)findViewById(R.id.repeatpassword);
        email               = (EditText)findViewById(R.id.email);

        register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(!Validation.isValidCredentials(username.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Netinkamas prisijungimo vardas", Toast.LENGTH_LONG).show();
                }else if(!Validation.isValidCredentials(password.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Netinkamas slaptažodis", Toast.LENGTH_LONG).show();
                }else if(!password.getText().toString().equals(repeatpassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Nesutampa slaptažodžiai", Toast.LENGTH_LONG).show();
                }else if(!Validation.isValidEmail(email.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Netinkamai įvestas el.paštas", Toast.LENGTH_LONG).show();
                }else{// validated

                    DatabaseSQLite databaseSQLite = new DatabaseSQLite(getApplicationContext());

                    User userToSQLite = new User("1",
                            username.getText().toString(),
                            password.getText().toString(),
                            email.getText().toString());
                    databaseSQLite.addUser(userToSQLite);
                    List<User> useriai = databaseSQLite.getAllUsers();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}