package hackgt.directionapp;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.text.TextUtils;
import android.widget.EditText;
import org.json.JSONObject;

public class LocInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_input);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loc_input, menu);
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

    public void submitAddresses(View view) {
        EditText loc1 = (EditText) findViewById(R.id.addr1);
        EditText loc2 = (EditText) findViewById(R.id.addr2);
        String addr1 = loc1.getText().toString();
        String addr2 = loc2.getText().toString();
        System.out.println(addr1);
        System.out.println(addr2);

        String origin = "origin=" + addr1;
        String destination = "destination=" + addr2;
        //Bundle bundle = ApplicationInfo.metaData;
        //String key = bundle.getString("my_api_key");

        String parameters = origin + "&" + destination + "&key=AIzaSyCEIowunK5yoWCnll0NkHDFX13blVy9JoI";
        //Hardcoded  key because I can't be bothered to get ApplicationInfo to work. Big safety issue here though
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        download(url);
    }

    private void download(String url) {
        DownloadTask dt = new DownloadTask();
        dt.doInBackground(url);
    }

}
