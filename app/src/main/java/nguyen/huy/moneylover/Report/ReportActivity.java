package nguyen.huy.moneylover.Report;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class ReportActivity extends AppCompatActivity {
    TextView txtIncomeValue, txtOutcomeValue;
    ImageView imgOutcomeMax;
    TextView txtOutcomeMaxType, txtOutcomeMaxDesc, txtOutcomeMaxValue;
    TextView txtReportStatus;
    AnimatedPieView pieView;
    ListView lvOutcome;
    Intent intent;
    ArrayList<ArrayList<ThuChi>> arrayObject;
    Map<String, Long> pieValue;
    ArrayList<Report> pieReport;
    Bitmap bitmapGuiTien ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtIncomeValue = findViewById(R.id.txtIncomeValue);
        txtOutcomeValue = findViewById(R.id.txtOutcomeValue);
        imgOutcomeMax = findViewById(R.id.imgOutcomeMax);
        txtOutcomeMaxType = findViewById(R.id.txtOutcomeMaxType);
        txtOutcomeMaxDesc = findViewById(R.id.txtOutcomeMaxDesc);
        txtOutcomeMaxValue = findViewById(R.id.txtOutComeMaxValue);
        txtReportStatus = findViewById(R.id.txtReportStatus);
        lvOutcome = findViewById(R.id.lvOutcome);
        pieView = findViewById(R.id.pieView);
        pieReport = new ArrayList<>();

        bitmapGuiTien = BitmapFactory.decodeResource(getResources(), R.drawable.guitien);

        intent = getIntent();

        arrayObject = (ArrayList<ArrayList<ThuChi>>) intent.getSerializableExtra("ValueThisMonth");
        xuLyDuLieu();
        xuLyPieView();

    }

    private void addEvents() {

    }

    private void xuLyDuLieu() {
        pieValue = new HashMap<>();
        for (ArrayList<ThuChi> dayThuChi : arrayObject) {
            for (ThuChi thuChi : dayThuChi) {
                String nhom = thuChi.getNhom();
//                if (!nhom.equals("Rút tiền")) {
                    if (!pieValue.containsKey(nhom)) {
                        pieValue.put(nhom, Long.valueOf(thuChi.getSotien()));
                    } else {
                        long value = pieValue.get(nhom);
                        value += Long.valueOf(thuChi.getSotien());
                        pieValue.put(nhom,value);
                    }
//                }
            }
        }
        MaxThuChi maxThuChi = chuyenDoiDuLieu();
        if (maxThuChi!=null)
        {
            imgOutcomeMax.setImageBitmap(maxThuChi.getBitmap());
            txtOutcomeMaxType.setText(maxThuChi.getType());
            txtOutcomeMaxValue.setText(maxThuChi.getValue()+"");
            xuLyPieView();
        }
    }


    private void xuLyPieView() {

        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        for( Report report : pieReport)
        {
            config.addData(report);
        }

        config  .animOnTouch(true)
                .floatExpandAngle(10f)// Selected pie's angle of expansion
                .floatShadowRadius(10f)// Selected pie's shadow of expansion
                .floatUpDuration(500)
                .floatDownDuration(500)// Last selected pie's float down animation duration
//                .floatExpandSize(15)// Selected pie's size of expansion(only for pie-chart,not ring-chart)
                .strokeMode(false)// Whether to draw ring-chart(default:true)
                .strokeWidth(20)// Stroke width for ring-chart
                .duration(2500)// Animation drawing duration
                .startAngle(-90f)// Starting angle offset
                .selectListener(new OnPieSelectListener<IPieInfo>() {
                    @Override
                    public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
                        if (isFloatUp)
                        txtReportStatus.setText(pieInfo.getDesc()+" : "+  (long) pieInfo.getValue());
                        else
                        {
                            txtReportStatus.setText("Tổng lượng chi : 1200000");
                        }
                    }
                })// Click callback
                .drawText(true)// Whether to draw a text description
                .textSize(30)// Text description size
                .textMargin(8)// Margin between text and guide line
                .autoSize(true)// Auto fit chart radius
                .pieRadius(150)// Set chart radius
                .pieRadiusRatio(0.8f)// Chart's radius ratio for parent ViewGroup
                .guidePointRadius(2)// Chart's radius
                .guideLineWidth(4)// Text guide line stroke width
                .guideLineMarginStart(5)// Guide point margin from chart
                .textGravity(AnimatedPieViewConfig.ABOVE)
                .canTouch(true)// Whether to allow the pie click to enlarge
                .splitAngle(1.5f)// Clearance angle
                .focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV)// Alpha change mode for selected pie
//                .interpolator(new DecelerateInterpolator())// Set animation interpolator
                .focusAlpha(150); // Alpha for selected pie (depend on focusAlphaType)
        pieView.applyConfig(config);
        pieView.start();
    }

    private MaxThuChi chuyenDoiDuLieu() {
        if (!pieReport.isEmpty()) pieReport.clear();
        Iterator it = pieValue.entrySet().iterator();
        MaxThuChi maxThuChi=null;
        long maxValue = 0;
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            Report report = new Report();
            report.setPieValue((Long) pair.getValue());
            report.setPieDescription((String) pair.getKey());
            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            report.setPieColor(Color.rgb(r,g,b));
            report.setBitmap(bitmapGuiTien);
            if ((Long) pair.getValue()>maxValue)
            {
                maxThuChi = new MaxThuChi((String) pair.getKey(),(Long) pair.getValue(),Color.rgb(r,g,b),bitmapGuiTien);
            }
            pieReport.add(report);
            it.remove();
        }
        return maxThuChi;
    }


    class MaxThuChi {
        private String type;
        private long value;
        private int color;
        private Bitmap bitmap;
        public MaxThuChi() {

        }

        public MaxThuChi(String type, long value, int color,Bitmap bitmap) {
            this.type = type;
            this.value = value;
            this.color = color;
            this.bitmap = bitmap;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }
}
