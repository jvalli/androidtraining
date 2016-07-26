package com.gap.androidtraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_search)
    EditText mEditTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_search)
    public void onClickSearch(View view) {
        String textToast = String.format("%s '%s'", getString(R.string.searching), mEditTextSearch.getText().toString());
        Toast.makeText(getApplicationContext(), textToast, Toast.LENGTH_SHORT).show();
    }
}
