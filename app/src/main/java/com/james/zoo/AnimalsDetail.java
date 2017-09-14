package com.james.zoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 101716 on 2017/9/13.
 */

public class AnimalsDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
    }
    public void backClick(View view){
        this.finish();
    }
}
