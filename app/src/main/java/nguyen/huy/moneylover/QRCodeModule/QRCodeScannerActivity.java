package nguyen.huy.moneylover.QRCodeModule;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import nguyen.huy.moneylover.R;

public class QRCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
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
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
