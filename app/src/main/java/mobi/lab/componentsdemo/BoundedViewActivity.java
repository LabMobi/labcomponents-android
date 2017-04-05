package mobi.lab.componentsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BoundedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounded_view);
        setTitle(R.string.title_bounded_view);
    }
}
