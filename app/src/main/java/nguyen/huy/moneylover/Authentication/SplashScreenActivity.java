package nguyen.huy.moneylover.Authentication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import nguyen.huy.moneylover.Tool.GetImage;
import nguyen.huy.moneylover.R;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView imgTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgTest = findViewById(R.id.imgTest);
        Bitmap bitmap;
        bitmap = GetImage.getBitmapFromString(this,getString(R.string.ts_awarded));
        imgTest.setImageBitmap(bitmap);
    }
}
