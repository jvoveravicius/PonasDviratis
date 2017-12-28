package creatlab.dviratis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ExplanationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);


    }

    public void goToMainActivity(View view) {

        Intent myIntent = new Intent(ExplanationActivity.this, MainActivity.class);
        ExplanationActivity.this.startActivity(myIntent);

    }
}
