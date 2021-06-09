package cuc.edu.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "cn.edu.cuc.mishello.MESSAGE";
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load Saved to editText
        sharedPreferences = getSharedPreferences(getString(R.string.pref_filename), Context.MODE_PRIVATE);
        String saved_message = sharedPreferences.getString(getString(R.string.pref_usr_input_msg), "");
        EditText editText = (EditText) findViewById(R.id.edit_message);
        editText.setText(saved_message);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Prepare SharedPreferences
        sharedPreferences = getSharedPreferences(getString(R.string.pref_filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        // Save Data
        editor.putString(getString(R.string.pref_usr_input_msg), message);
        editor.commit();

        startActivity(intent);
    }

}