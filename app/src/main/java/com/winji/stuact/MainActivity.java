package com.winji.stuact;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Snackbar snackbar;
    WebView webView;
    Animation animTranslate;
    private ProgressDialog progressx;
    String x = "x";
    LinearLayout parentLayout;
    TextView title, msg, or, orr;
    Button turnon, turnonn, refresh;

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, locationListener);


        parentLayout = findViewById(R.id.parentLayout);


        title = (TextView) findViewById(R.id.first);
        msg = (TextView) findViewById(R.id.second);
        or = (TextView) findViewById(R.id.or);
        orr = (TextView) findViewById(R.id.orr);
        turnon = (Button) findViewById(R.id.turn_on_wifi);
        turnonn = (Button) findViewById(R.id.turn_on_data);
        refresh = (Button) findViewById(R.id.refresh);
        //  ButtonAnimation();

        //Font
        Typeface typefacee = Typeface.createFromAsset(getAssets(), "appfont.otf");
        msg.setTypeface(typefacee);
        title.setTypeface(typefacee);
        or.setTypeface(typefacee);
        orr.setTypeface(typefacee);
        turnon.setTypeface(typefacee);
        turnonn.setTypeface(typefacee);
        refresh.setTypeface(typefacee);

        progressx = new ProgressDialog(this);
        progressx.setMessage("Please wait... ");
        progressx.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final RelativeLayout coordinatorLayout = (RelativeLayout) findViewById(R.id.a);
        snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);

        progressx.show();

        webView = (WebView) findViewById(R.id.activity_main_webview);


        showAndCheckInterNet();


        turnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        turnonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAndCheckInterNetWithToast();
            }
        });


    }


    //Button Animation
    private void ButtonAnimation() {
        //Button Animation
        animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        turnonn.startAnimation(myAnim);
        turnon.startAnimation(myAnim);
        refresh.startAnimation(myAnim);

    }


    private void showAndCheckInterNet() {
        if (!isNetworkAvailable()) {
            progressx.cancel();
            progressx.hide();
            parentLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        } else {
            parentLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
            webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new MyWebViewClient());
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setJavaScriptEnabled(true);
            //webPage.getSettings().setPluginState(PluginState.ON);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setDatabasePath("/data/data/" + "com.winji.stuact" + "/databases/");
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new ChromeClient());
            webView.loadUrl("http://www.stuact.xyz");
        }
    }

    private void showAndCheckInterNetWithToast() {
        if (!isNetworkAvailable()) {
            progressx.cancel();
            progressx.hide();
            parentLayout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            snackbar.show();
        } else {
            parentLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
            webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new MyWebViewClient());
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //webPage.getSettings().setPluginState(PluginState.ON);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setDatabasePath("/data/data/" + "com.winji.stuact" + "/databases/");
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new ChromeClient());
            webView.loadUrl("http://www.stuact.xyz");
        }
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

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!isNetworkAvailable()) {
                //  snackbar.show();

                buildDialog(MainActivity.this);
            } else {
                if (url.contains(".pdf")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try {
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                    }
                } else {
                    view.loadUrl(url);
                }

            }

            return true;

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (!isNetworkAvailable()) {
                //snackbar.show();
                buildDialog(MainActivity.this);

            }
        }

        public void onPageFinished(WebView view, String url) {
            progressx.cancel();
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebChromeClient {
        private MainActivity myActivity;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (!isNetworkAvailable()) {
                //   snackbar.show();
                buildDialog(MainActivity.this);
            } else {
                MainActivity.this.setValue(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        }


    }

    public void setValue(int progress) {
        this.progressx.setProgress(progress);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    public class ChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("TAG", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;

        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard

            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");

            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);


        }

        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {


                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }



}
