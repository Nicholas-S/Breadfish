package nl.breadfish.breadfish;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private int videoWidth = 640;
    private int videoHeight = 360;

    VideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        final TextView textView = findViewById(R.id.bufferView);
        videoview = findViewById(R.id.videoview);
        Uri uri = Uri.parse("https://breadfish.nl/src/breadfish.mp4");
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.requestFocus();
        videoview.resolveAdjustedSize(640, 360);
        textView.setEnabled(true);
        if(videoview.isPlaying())
        {
            textView.setEnabled(false);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        videoview.start();
        videoview.requestFocus();
        videoview.resolveAdjustedSize(640, 360);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
