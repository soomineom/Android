package edu.example.contentprovider190603;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {

    EditText et1;
    EditText et2;
    Button bt_ent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        bt_ent = (Button)findViewById(R.id.button);
        et1 = (EditText)findViewById(R.id.editText);
        et2 = (EditText)findViewById(R.id.editText2);

        Intent i = getIntent();
        String country = i.getStringExtra("country");
        String capital = i.getStringExtra("capital");

        et1.setText(country);
        et2.setText(capital);

        bt_ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                i.putExtra("country", et1.getText().toString());
                i.putExtra("capital", et2.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(KeyEvent.KEYCODE_BACK == keyCode)
        {
            setResult(RESULT_CANCELED, getIntent());
        }

        return super.onKeyDown(keyCode, event);
    }
}
