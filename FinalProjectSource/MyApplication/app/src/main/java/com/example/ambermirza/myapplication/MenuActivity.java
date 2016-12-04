package com.example.ambermirza.myapplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ambermirza on 12/3/16.
 */

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("Here's the menu");
        addContentView(text, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_myactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(),	item.getTitle() + " selected", Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.mapMenu:
                // do something
                break;
            case R.id.favMenu:
                // do something
                break;
            case R.id.listMenu:
                // do something
                break;
            case R.id.settingsMenu:
                // do something
                break;
        }
        return true;
    }

}