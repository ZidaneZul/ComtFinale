package com.example.musicstream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public final String[] usernameList = {"username", "IamCool", "Wakanda4Life",
            "LoveU1000", "Naruto"};
    public final String[] passwordList = {"password", "qwerty", "passwert",
            "iloveyouback", "boruto12345"};
    EditText username;
    EditText password;
    Button login;
    boolean loginSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editTxtUsername);
        password = findViewById(R.id.editTxtPassword);
        login = (Button)findViewById(R.id.btnLogin);
    }
    public void login(View view)
    {
        loginSuccess = checkCredentials(username.getText().toString(), password.getText().toString());

        if(loginSuccess)
        {
            Toast.makeText(getBaseContext(), "WELCOME TO MY MUSIC APP! :)" ,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Your login was Unsuccessful. Please try again.", Toast.LENGTH_LONG).show();
        }

    }

    public boolean checkCredentials(String usernameInput, String passwordInput)
    {
        loginSuccess = false;
        boolean usernameExist = false;
        if((usernameInput.isEmpty() || usernameInput == null) &&
                (passwordInput.isEmpty() || passwordInput == null))
        {
            username.setError("Enter your Username!");
            password.setError("Enter your Password!");
            Toast.makeText(getBaseContext(), "Please enter your Username and Password to login." , Toast.LENGTH_LONG).show();
        }
        else if((usernameInput.isEmpty() || usernameInput == null))
        {
            username.setError("Enter your Username!");
            Toast.makeText(getBaseContext(), "Please enter your Username to login successfully." , Toast.LENGTH_LONG).show();
        }
        else if((passwordInput.isEmpty() || passwordInput == null))
        {
            password.setError("Enter your Password!");
            Toast.makeText(getBaseContext(), "Please enter your Password to login successfully." , Toast.LENGTH_LONG).show();
        }
        else
        {
            for (int index = 0; index < usernameList.length; index++) {
                if ((usernameList[index].equals(usernameInput)))
                {
                    usernameExist = true;
                    if (passwordList[index].equals(passwordInput))
                    {
                        loginSuccess = true;
                        break;
                    }
                    else {
                        password.setError("Enter a valid Password!");
                        Toast.makeText(getBaseContext(), "You have entered an incorrect password for " + usernameInput , Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
            if (!usernameExist) {
                username.setError("Enter a valid Username!");
                Toast.makeText(getBaseContext(), "The Username you have entered does not exist." , Toast.LENGTH_LONG).show();
            }
        }
        return loginSuccess;
    }
}