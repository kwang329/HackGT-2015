package hackgt.directionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void openMapActivity(View view) {
        Intent switchToMap = new Intent(this, LocUpdater.class);
        startActivity(switchToMap);
    }

    public void openCameraActivity(View view) {
        Intent switchToCamera = new Intent(this, CameraActivity.class);
        startActivity(switchToCamera);
    }

    public void openInputActivity(View view) {
        Intent switchToInput = new Intent(this, LocInputActivity.class);
        startActivity(switchToInput);
    }
}
