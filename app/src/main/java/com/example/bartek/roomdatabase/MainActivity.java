package com.example.bartek.roomdatabase;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 200;

    private Button bStart;
    private Button bStop;
    private TextView textView;

    private User user;
    private UserDao userDao;
    private List<User> users;

    private boolean permissionGranted;
    private Intent serviceIntent;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        bStart = (Button) findViewById(R.id.button2);
        bStop = (Button) findViewById(R.id.button3);

        permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        if (!permissionGranted)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        userDao = db.userDao();

        serviceIntent = new Intent(this, GpsService.class);




/*
        user = new User();
        user.setLastName("Ewa");
        user.setFirstName("Siedlecka");
        user.addLatitude(5456.43);
        user.addLatitude(4223.2);
        user.addLatitude(532.443);
        user.addLatitude(32.02);
        userDao.insert(user);
*/
        users = userDao.getAll();

        //Log.d(TAG, userDao.getUser().getFirstName() + " POBRANO?");

        //users.get(1).addLatitude(54.1);
        //userDao.update(users.get(1));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            stringBuilder.append(users.get(i).getId()).append(" ");
            stringBuilder.append(users.get(i).getStart_time()).append(" ");
            stringBuilder.append(users.get(i).getEnd_time()).append("\n");
            stringBuilder.append(users.get(i).getLatitude().toString()).append("\n");
            stringBuilder.append(users.get(i).getLongitude().toString()).append("\n\n");
        }
        Log.d(TAG, stringBuilder.toString());


        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permissionGranted) startService(serviceIntent);
                user = new User();
                user.setStart_time(Calendar.getInstance().getTimeInMillis());
                userDao.insert(user);

            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fusedLocationProviderClient.requestLocationUpdates(locationCallback);
                stopService(serviceIntent);
                users = userDao.getAll();

                //users.get(1).addLatitude(54.1);
                //userDao.update(users.get(1));
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < users.size(); i++) {
                    stringBuilder.append(users.get(i).getId()).append(" ");
                    stringBuilder.append(users.get(i).getStart_time()).append(" ");
                    stringBuilder.append(users.get(i).getEnd_time()).append("\n");
                    stringBuilder.append(users.get(i).getLatitude().toString()).append("\n");
                    stringBuilder.append(users.get(i).getLongitude().toString()).append("\n\n");
                }
                Log.d(TAG, stringBuilder.toString());

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
}
