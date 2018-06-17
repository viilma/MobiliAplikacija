package com.byethost12.kitm.mobiliaplikacija.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.byethost12.kitm.mobiliaplikacija.Controller.Validation;
import com.byethost12.kitm.mobiliaplikacija.Model.Child;
import com.byethost12.kitm.mobiliaplikacija.Model.DatabaseSQLite;
import com.byethost12.kitm.mobiliaplikacija.R;

import static com.byethost12.kitm.mobiliaplikacija.View.ChildAdapter.ENTRY_ID;

public class EntryActivity extends AppCompatActivity {

    Button btnSubmit, btnUpdate, btnDelete;

    EditText etName, etSurname, etPersonId, etWeight, etHeight;
    RadioGroup rbGroup;
    RadioButton rbAnkstyvasis, rbIkimokyklinis, rbPriesmokyklinis;
    CheckBox cbBasketball, cbFootball, cbDances, cbKarate, cbCeramics;
    Spinner spinner;
    ArrayAdapter<String> adapter;

    Child pradinisChild;
    Child galutinisChild;

    DatabaseSQLite db;

    String items[] = {"Vyturėliai", "Čiauškučiai", "Cypliukai", "Žiniukai", "Zuikučiai", "Kodėlčiukai", "Drugeliai", "Pelėdžiukai", "Smalsučiai", "Pabiručiai", "Žvirbliukai"};

    boolean cancel = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseSQLite(EntryActivity.this);

        int entryID = -1;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(!extras.isEmpty()) {
                entryID = extras.getInt(ENTRY_ID);
            }
        } else { // jeigu yra naujas irasas, id = -1, jeigu egzistuojantis, bus teigiamas
            entryID = (Integer) savedInstanceState.getSerializable(ENTRY_ID);
        }

        if (entryID == -1) {
            setTitle(R.string.new_entry_label);
        } else {
            setTitle(R.string.entry_update_label);
        }

        pradinisChild = new Child();
        if (entryID == -1) { //naujas irasas
            pradinisChild.setId(-1);
            pradinisChild.setName("");
            pradinisChild.setSurname("");
            pradinisChild.setPersonId("");
            pradinisChild.setActivities("Krepšinis");
            pradinisChild.setEducation("Ankstyvasis");
            pradinisChild.setGroup("Vyturėliai");
            //pradinisChild.setHeight(0);
            //pradinisChild.setWeight(0);
        } else { // egzistuojantis irasas
           pradinisChild = db.getChild(entryID);
        }
        galutinisChild = new Child();

        btnSubmit = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);


        if (entryID == -1){ //naujas irasas - disable update button
            btnUpdate.setEnabled(false);
            btnSubmit.setEnabled(true);
            btnDelete.setEnabled(false);
        }else { // egzistuojantis irasas - disable submit
            btnUpdate.setEnabled(true);
            btnSubmit.setEnabled(false);
            btnDelete.setEnabled(true);
        }

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etPersonId = (EditText) findViewById(R.id.etPersonId);
        //etWeight = (EditText) findViewById(R.id.etWeight);
        //etHeight = (EditText) findViewById(R.id.etHeight);

        rbGroup = (RadioGroup) findViewById(R.id.rbGroup);
        rbAnkstyvasis = (RadioButton) findViewById(R.id.rbAnkstyvasis);
        rbIkimokyklinis = (RadioButton) findViewById(R.id.rbIkimokyklinis);
        rbPriesmokyklinis = (RadioButton) findViewById(R.id.rbPriesmokyklinis);

        cbBasketball = (CheckBox) findViewById(R.id.cbBasketball);
        cbFootball = (CheckBox) findViewById(R.id.cbFootball);
        cbDances = (CheckBox) findViewById(R.id.cbDances);
        cbKarate = (CheckBox) findViewById(R.id.cbKarate);
        cbCeramics = (CheckBox) findViewById(R.id.cbCeramics);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        fillFields(pradinisChild);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (!cancel) {

                    db.addChild(galutinisChild);

                    Intent goToSearchActivity = new Intent(EntryActivity.this, SearchActivity.class);
                    startActivity(goToSearchActivity);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (!cancel) {
                    db.updateChild(galutinisChild);
                    Intent goToSearchActivity = new Intent(EntryActivity.this, SearchActivity.class);
                    startActivity(goToSearchActivity);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteChild(pradinisChild.getId());

                Intent goToSearchActivity = new Intent(EntryActivity.this, SearchActivity.class);
                startActivity(goToSearchActivity);
            }
        });
    }

    private void getFields(){
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String personId = etPersonId.getText().toString();
        //double weight = Double.parseDouble(etWeight.getText().toString());
        //double height = Double.parseDouble(etHeight.getText().toString());

        if (!Validation.isValidNameSurname(name)){
            Toast.makeText(getApplicationContext(),
                    "Netinkamai įvestas vardas", Toast.LENGTH_LONG).show();
        } else if (!Validation.isValidNameSurname(surname)){
            Toast.makeText(getApplicationContext(),
                    "Netinkamai įvesta pavardė", Toast.LENGTH_LONG).show();
        } else if (!Validation.isValidAk(personId)){
            Toast.makeText(getApplicationContext(),
                    "Netinkamai įvestas asmens kodas", Toast.LENGTH_LONG).show();
        } else {

            cancel = false;
            String rb = "";
            String spinnerText = "";

            if(rbAnkstyvasis.isChecked()){
                rb = rbAnkstyvasis.getText().toString();
            }else if(rbIkimokyklinis.isChecked()){
                rb = rbIkimokyklinis.getText().toString();
            }else{
                rb = rbPriesmokyklinis.getText().toString();
            }

            String checkboxText = "";

            if(cbBasketball.isChecked()){
                checkboxText = checkboxText + "Krepšinis ";
            }

            if(cbFootball.isChecked()){
                checkboxText = checkboxText + "Futbolas ";
            }

            if(cbDances.isChecked()){
                checkboxText = checkboxText + "Šokiai ";
            }

            if(cbKarate.isChecked()){
                checkboxText = checkboxText + "Karate ";
            }

            if(cbCeramics.isChecked()){
                checkboxText = checkboxText + "Keramika ";
            }

            spinnerText = spinner.getSelectedItem().toString();

            galutinisChild.setId(pradinisChild.getId());
            galutinisChild.setName(name);
            galutinisChild.setSurname(surname);
            galutinisChild.setPersonId(personId);
            //galutinisChild.setHeight(height);
            //galutinisChild.setWeight(weight);
            galutinisChild.setActivities(checkboxText);
            galutinisChild.setEducation(rb);
            galutinisChild.setGroup(spinnerText);
        }
    }

    private void fillFields (Child child){
        etName.setText(child.getName());
        etSurname.setText(child.getSurname());
        etPersonId.setText(child.getPersonId());
        //etHeight.setText(String.valueOf(child.getHeight()));
        //etWeight.setText(String.valueOf(child.getWeight()));

        cbFootball.setChecked(child.getActivities().contains("Futbolas"));
        cbBasketball.setChecked(child.getActivities().contains("Krepšinis"));
        cbDances.setChecked(child.getActivities().contains("Šokiai"));
        cbKarate.setChecked(child.getActivities().contains("Karate"));
        cbCeramics.setChecked(child.getActivities().contains("Keramika"));

        rbIkimokyklinis.setChecked(child.getEducation().equals("Ikimokyklinis"));
        rbAnkstyvasis.setChecked(child.getEducation().equals("Ankstyvasis"));
        rbPriesmokyklinis.setChecked(child.getEducation().equals("Priešmokyklinis"));

        spinner.setSelection(adapter.getPosition(child.getGroup()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getFields();
                if (pradinisChild.equals(galutinisChild)) { //Nebuvo pakeistas
                    finish();
                } else {  //Buvo pakeistas
                    showDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EntryActivity.this);

        // set title
        alertDialogBuilder.setTitle("Įspėjimas");

        // set dialog message
        alertDialogBuilder
                .setMessage("Išsaugoti pakeitimus?")
                .setCancelable(false)
                .setPositiveButton("Taip",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Ne",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        EntryActivity.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}

