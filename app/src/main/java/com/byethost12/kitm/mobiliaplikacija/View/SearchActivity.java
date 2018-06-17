package com.byethost12.kitm.mobiliaplikacija.View;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.byethost12.kitm.mobiliaplikacija.Model.Child;
import com.byethost12.kitm.mobiliaplikacija.Model.DatabaseSQLite;
import com.byethost12.kitm.mobiliaplikacija.R;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.byethost12.kitm.mobiliaplikacija.View.ChildAdapter.ENTRY_ID;

public class SearchActivity extends AppCompatActivity {

    // Cloud kintamieji
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    // TODO: pasikeisti url ir cookies
    public static final String CLOUD_DB_URL = "http://java17.byethost4.com/mobile/database.php";
    public static final String COOKIES_CONTENT = "7a4d917e220fbf9a55cab3046bd1a3b7";

    SearchView searchView = null;

    RecyclerView rvChildren;
    ChildAdapter childAdapter;

    List<Child> childrenSQLite = Collections.emptyList();

    List<Child> childrenPaieskai = new ArrayList<Child>();

    DatabaseSQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.search_label);

        db = new DatabaseSQLite(SearchActivity.this);

        // Taupydami duomenų bazės resursus, darome 1 call'ą (getAllChildren) užkrovus paieškos langą,
        // vėliau (not implemented) vaikų ieškome iš užpildyto sąrašo
        childrenSQLite = db.getAllChildren();

        if (!childrenSQLite.isEmpty()) {
            setRecycleView(childrenSQLite);
        } else {
            Toast.makeText(this, "Duomenų bazėje įrašų nėra", Toast.LENGTH_SHORT).show();
        }

        Button btnPrideti = (Button) findViewById(R.id.btnAdd);
        btnPrideti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, EntryActivity.class);
                intent.putExtra(ENTRY_ID, -1);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //adds item to actionbar
        getMenuInflater().inflate(R.menu.search, menu);
        //get search item from actionbar and get search service
        MenuItem searchitem = menu.findItem(R.id.actionSearch);
        SearchManager searchManger = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchitem != null) {
            searchView = (SearchView) searchitem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManger.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setIconified(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
    }

    @Override
    //every time when you press search button an activity is recreated which in turn
    //calls this function
    protected void onNewIntent(Intent intent) {
        //get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            new AsyncFetch(query).execute();
        }
    }

    /*
        setup and hand over list childrenSQLite to recyclerview
        @params list of children from db
     */
    private void setRecycleView(List<Child> children) {
        rvChildren = (RecyclerView) findViewById(R.id.children_list);
        childAdapter = new ChildAdapter(SearchActivity.this, children);
        rvChildren.setAdapter(childAdapter);
        rvChildren.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
    }

    class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
        String searchQuery;
        HttpURLConnection conn;
        URL url = null;

        public AsyncFetch(String searchQuery) {
            this.searchQuery = searchQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Prašome palaukti");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
                // TODO: atkomentuoti, jeigu norite naudoti cloud
            /*try {
                // Enter URL address where your php file resides
                url = new URL(CLOUD_DB_URL);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // byethost naudoja antibot sistema, todel reikia kiekvienam rankutėmis suvesti cookie turinį,
                // kuris pas kiekviena bus skirtingas. kaip tai padaryti zemiau nuoroda
                // http://stackoverflow.com/questions/31912000/byethost-server-passing-html-values-checking-your-browser-with-json-string
                //7a4d917e220fbf9a55cab3046bd1a3b7
                conn.setRequestProperty("Cookie", "__test="+COOKIES_CONTENT+"; expires=2038 m. sausio 1 d., penktadienis 01:55:55; path=/");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                User vartotojas = new User(SearchActivity.this);
                Uri.Builder builder = new Uri.Builder().
                        appendQueryParameter("action", "search").
                        appendQueryParameter("searchQuery", searchQuery).
                        appendQueryParameter("username", vartotojas.getUsernameForLogin());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //writer.write(getPostDataString(data));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());
                } else {
                    return("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }*/

            // SQLite kodas
            // jeigu negaila db resursų, galima kiekvieną kartą call'inti pagal įvestus kriterijus paieškos
            //childrenSQLite = db.getChildByName(searchQuery);

            if (!childrenPaieskai.isEmpty()) {
                childrenPaieskai.clear();
            }

            // vartotojo paieska vykdoma sarase (ne db)
            for (int i = 0; i < childrenSQLite.size(); i++) {
                if (childrenSQLite.get(i).getName().contains(searchQuery)) {
                    childrenPaieskai.add(childrenSQLite.get(i));
                }
            }

            if (childrenPaieskai.isEmpty()) {
                return "no rows";
            } else {
                return "rows";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            progressDialog.dismiss();

            // TODO: atkomentuoti, jeigu norite naudoti cloud
            /*List<Child> data=new ArrayList<>();

            try {
                JSONArray jArray = new JSONArray(result);
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    //int id,String user, String name, double weight, double height, String cp,
                    // String abilities, String type
                      Child pokemonas = new Child(
                              json_data.getInt("id"),
                              json_data.getString("user"),
                              json_data.getString("name"),
                              json_data.getDouble("weight"),
                              json_data.getDouble("height"),
                              json_data.getString("cp"),
                              json_data.getString("abilities"),
                              json_data.getString("type")
                    );
                    data.add(pokemonas);
                }
                // Setup and Handover data to recyclerview
                setRecycleView(data);
            } catch (JSONException e) {
                // You to understand what actually error is and handle it appropriately
                Toast.makeText(SearchActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(SearchActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            }
        }*/
            setRecycleView(childrenPaieskai);
        }
    }
}

