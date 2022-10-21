package com.example.androidassignments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {
    String snackText = "You selected the crab";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, R.string.snackbar_message, Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();
        switch (id) {
            case R.id.crab:
                Log.d("Toolbar", "Crab Selected");
                Snackbar.make(findViewById(R.id.fab), snackText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.fish:
                Log.d("Toolbar", "Fish Selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.back);
                // Add the buttons
                builder.setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.octopus:
                Log.d("Toolbar", "Octopus Selected");
                AlertDialog.Builder op_3_builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogLayout = inflater.inflate(R.layout.edit_snackbar, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                op_3_builder.setView(dialogLayout)
                        .setPositiveButton(R.string.okButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                snackText = ((EditText)dialogLayout.findViewById(R.id.newMessage)).getText().toString();

                            }
                        })
                        .setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                op_3_builder.create().show();
                break;
            case R.id.about:
                CharSequence text = "Version 1.0 Adam Gerrish";
                Toast toast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
                toast.show();
                break;
        }

        return true;
    }
}