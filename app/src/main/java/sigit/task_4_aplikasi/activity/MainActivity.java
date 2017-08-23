package sigit.task_4_aplikasi.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.logging.Handler;

import sigit.task_4_aplikasi.model.Expenses;
import sigit.task_4_aplikasi.model.Income;
import sigit.task_4_aplikasi.R;
import sigit.task_4_aplikasi.sqlitedb.SqlLiteHelper;
import sigit.task_4_aplikasi.fragment.DashboardFragment;
import sigit.task_4_aplikasi.fragment.TransactionFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SqlLiteHelper sqliteHelper;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    NavigationView navigationView;
    private ArrayList<Expenses> mExpenseArrayList;
    private ArrayList<Income> mIncomeArrayList;
    private ProgressDialog pdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sqliteHelper = new SqlLiteHelper(this);
        sqliteHelper.CreateTabel();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        //` get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("dbadvance");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //display fragment by param id
        displaySelectedScreen(R.id.nav_menu1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void synch(){

        if (TextUtils.isEmpty(userId)){
            userId = mFirebaseDatabase.push().getKey();
        }
        //run progress dialog
        pdialog = new ProgressDialog(MainActivity.this);
        pdialog.setMessage("Processing");
        pdialog.setCancelable(false);
        pdialog.setIndeterminate(false);
        pdialog.show();
        //get Sql Lite DB
        mExpenseArrayList = sqliteHelper.getAllExpenses();
        mIncomeArrayList = sqliteHelper.getAllIncome();
//        create firebase by id
        mFirebaseDatabase.child(userId).child("expense").setValue(mExpenseArrayList);
        mFirebaseDatabase.child(userId).child("income").setValue(mIncomeArrayList);
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addChangeListener();
            }
        }, 2000);


    }
    private void addChangeListener(){
        //data change listener

        mFirebaseDatabase.child(userId).child("expense").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pdialog.hide();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Expenses expenses = dataSnapshot1.getValue(Expenses.class);

                    if (expenses==null){
                        showdialog();
                    }
//                    if sucsses storing data to firebase then showing a snackbar
                    Snackbar snackbar = Snackbar.make(navigationView, "Data Sucsses Synchroize to server", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    Log.e("MainAct","Exp data is changed!" +expenses.getDescription());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pdialog.hide();
                showdialog();
                Log.e("MainAct", "Filed to read exp", databaseError.toException());

            }
        });

        mFirebaseDatabase.child(userId).child("income").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pdialog.hide();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Income income = dataSnapshot1.getValue(Income.class);

                    if (income==null){
                        showdialog();
                    }
//                    if sucsses storing data to firebase then showing a sncakbar

                    Log.e("MainAct","Exp data is changed!"+income.getDescription()) ;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                filed tored value

                pdialog.hide();
                showdialog();
                Log.e("MainAct", "Filed to read exp", databaseError.toException());

            }
        });
    }
    private void showdialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //setting dialog
        alertDialog.setTitle("Filed To Synchronize");
        alertDialog.setMessage("Do you want to retry");
        alertDialog.setIcon(R.mipmap.ic_launcher);

        alertDialog.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                synch();
                dialogInterface.cancel();
            }
        });

        alertDialog.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        //Showing Alert Message
        alertDialog.show();
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new DashboardFragment();
                break;
            case R.id.nav_menu2:
                fragment = new TransactionFragment();
                break;
            case R.id.nav_menu3:
                synch();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            sqliteHelper.HapusTabel();
            sqliteHelper.CreateTabel();
            displaySelectedScreen(R.id.nav_menu1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
