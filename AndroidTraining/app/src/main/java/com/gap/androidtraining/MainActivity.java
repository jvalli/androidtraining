package com.gap.androidtraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String INTENT_ACTION_MAIN = "android.intent.action.MAIN";

    private final Set<Fragment> fragments = new HashSet<>();
    private final Map<Fragment, Bundle> messages = new HashMap<>();

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @BindView(R.id.content_frame)
    FrameLayout mContentFrame;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        replace(new VenueSearch());
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(MainActivity.INTENT_ACTION_MAIN);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);

        messages.clear();
    }

    /**
     * Registers the fragment to receive the messages from other fragments.
     */
    public void subscribe(@NonNull Fragment fragment) {
        fragments.add(fragment);
        Log.d("subscribe", fragment.getClass().getSimpleName() + " subscribed, total count is " + fragments.size());
    }

    /**
     * Unregisters the fragment from receiving the messages from other fragments.
     */
    public void unsubscribe(@NonNull Fragment fragment) {
        fragments.remove(fragment);
        Log.d("unsubscribe", fragment.getClass().getSimpleName() + " unsubscribed, total count is " + fragments.size());
    }

    /**
     * Replaces the frame layout with given fragment.
     */
    public void replace(@NonNull Fragment fragment) {
        replace(R.id.content_frame, fragment);
    }

    /**
     * Replaces the frame layout with given ID with given fragment.
     */
    public void replace(@IdRes int id, @NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(id, fragment)
                .commitAllowingStateLoss();
    }

    /**
     * Adds fragment to stack.
     */
    public void add(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, fragment)
                .addToBackStack(fragment.getTag())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    /**
     * Pop back from to stack.
     */
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * Delivers the message bundle to receiver.
     */
    public void deliver(@NonNull Fragment receiver,
                        @NonNull Fragment sender,
                        @NonNull Bundle bundle) {
        if (fragments.contains(receiver) && !receiver.isDetached()) {
            Log.d("deliver", "Delivered to: " + receiver.getClass().getSimpleName());
            return;
        }
        messages.put(receiver, bundle);
    }

    /**
     * Received the stored message if any and removes from the message queue.
     */
    public Bundle receive(@NonNull Fragment receiver) {
        Bundle bundle = messages.get(receiver);
        messages.remove(receiver);
        return bundle;
    }

    @UiThread
    public void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
