package com.winji.stuact;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }

    private void StartAnimations() {

        ImageView iv = (ImageView) findViewById(R.id.logo);
        Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(1800);
        iv.clearAnimation();
        iv.setAnimation(fadeInAnimation);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.fade_in_intent, R.anim.fade_out_intent);
            }
        }, 2200);


    }

    public void buildDialog(Context c) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflate the custom layout/view
        final View customView = inflater.inflate(R.layout.alertdialog_internet_connection, null);
        TextView title = (TextView) customView.findViewById(R.id.first);
        TextView msg = (TextView) customView.findViewById(R.id.second);
        TextView or = (TextView) customView.findViewById(R.id.or);
        Button turnon = (Button) customView.findViewById(R.id.turn_on_wifi);
        Button turnonn = (Button) customView.findViewById(R.id.turn_on_data);
        ImageView closeImg = (ImageView) customView.findViewById(R.id.closeImg);


        //Font
        Typeface typefacee = Typeface.createFromAsset(getAssets(), "appfont.otf");
        msg.setTypeface(typefacee);
        title.setTypeface(typefacee);
        or.setTypeface(typefacee);
        turnon.setTypeface(typefacee);
        turnonn.setTypeface(typefacee);
        builder.setView(customView);


        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        ColorDrawable back = new ColorDrawable(getResources().getColor(android.R.color.transparent));
        InsetDrawable inset = new InsetDrawable(back, 120);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.show();

        turnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        turnonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
