package nguyen.huy.moneylover.QRCodeModule;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import nguyen.huy.moneylover.R;

public class QRCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    private final static int REQUEST_CAMERA = 1;
    boolean cameraReady;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraReady = false;
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
            return;
        } else
        {
            cameraReady = true;
        }
    }

    @Override
    public void handleResult(Result result) {
        Intent resultIntent = new Intent();
        try {
            JSONObject jsonObject = new JSONObject(result.getText());
            resultIntent.putExtra("Success",true);
            resultIntent.putExtra("Value",result.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            resultIntent.putExtra("Success",false);
        } finally {
            setResult(Activity.RESULT_OK,resultIntent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraReady) scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraReady)
        {
            scannerView.startCamera();
            scannerView.setResultHandler(this);
        }

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            cameraReady = true;
        }
        else {
            finish();
        }
    }
}
