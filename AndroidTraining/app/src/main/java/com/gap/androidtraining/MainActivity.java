package com.gap.androidtraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private EditText mEditTextSearch;
    private Button mButtonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextSearch = (EditText)findViewById(R.id.edit_text_search);
        mButtonSearch = (Button)findViewById(R.id.button_search);
        assert mButtonSearch != null;
        mButtonSearch.setOnClickListener(this);

    }

    public void onClick(View v) {

        String textToast = String.format("%s '%s'", getString(R.string.searching), mEditTextSearch.getText().toString());
        Toast.makeText(getApplicationContext(), textToast, Toast.LENGTH_SHORT).show();
    }
}
