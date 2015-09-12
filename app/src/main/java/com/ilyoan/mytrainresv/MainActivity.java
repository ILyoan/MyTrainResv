package com.ilyoan.mytrainresv;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ilyoan.mytrainresv.core.MyTrainResv;
import com.ilyoan.mytrainresv.core.Train;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private LoginFragment mLoginFragment = new LoginFragment();
    private SearchFragment mSearchFragment = new SearchFragment();
    private TrainListFragment mTrainListFragment = new TrainListFragment();

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fm.findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.container, mLoginFragment);
        ft.show(mLoginFragment);

        ft.add(R.id.container, mSearchFragment);
        ft.hide(mSearchFragment);

        ft.add(R.id.container, mTrainListFragment);
        ft.hide(mTrainListFragment);

        ft.commit();

        MyTrainResv.setActivity(this);
        MyTrainResv.getInstance().setNotification(this.myTrainNoti);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position > mNavigationDrawerFragment.VIEW_NAME.length) {
            throw new AssertionError();
        }
        mTitle = mNavigationDrawerFragment.VIEW_NAME[position];

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                ft.show(mLoginFragment);
                ft.hide(mSearchFragment);
                ft.hide(mTrainListFragment);
                break;
            case 1:
                ft.hide(mLoginFragment);
                ft.show(mSearchFragment);
                ft.hide(mTrainListFragment);
                break;
            case 2:
                ft.hide(mLoginFragment);
                ft.hide(mSearchFragment);
                ft.show(mTrainListFragment);
                break;
        }
        ft.commit();
    }

    public void onSectionAttached(int number) {
        if (number > mNavigationDrawerFragment.VIEW_NAME.length) {
            throw new AssertionError();
        }
        mTitle = mNavigationDrawerFragment.VIEW_NAME[number - 1];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void displayNoti(String title, String text) {
        displayNoti(title, text, false);
    }

    public void displayNoti(String title, String text, boolean vibrate) {
        int notificationId = 001;

        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_drawer)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentIntent(viewPendingIntent);

        if (vibrate) {
            notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000, 1000, 3000 });
        }

        // Get an instance of the NotificationManager service
        NotificationManager nm =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("MyTrainResv", "display notification: " + text);

        // Build the notification and issues it with notification manager.
        nm.notify(notificationId, notificationBuilder.build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void onFragmentInteraction(Uri uri) {
    }

    MyTrainResv.OnNotification myTrainNoti = new MyTrainResv.OnNotification() {
        @Override
        public void onTrainList(ArrayList<Train> trainList) {
            mTrainListFragment.setTrainList(trainList);
            onNavigationDrawerItemSelected(2);
        }
    };
}
