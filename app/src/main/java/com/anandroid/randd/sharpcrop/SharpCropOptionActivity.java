package com.anandroid.randd.sharpcrop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SharpCropOptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharp_crop);
        setClick(R.id.login);
        setClick(R.id.register);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId())
        {
            case R.id.login :
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.register :
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
