package nguyen.huy.moneylover.Report.Model;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.PieOption;

public class Report implements IPieInfo {
    private long pieValue;
    private int pieColor;
    private String pieDescription;
    private Bitmap bitmap;
    public Report() {
    }

    public void setPieColor(int pieColor) {
        this.pieColor = pieColor;
    }

    public void setPieDescription(String pieDescription) {
        this.pieDescription = pieDescription;
    }

    public long getPieValue() {
        return pieValue;
    }

    public void setPieValue(long pieValue) {
        this.pieValue = pieValue;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public double getValue() {
        return pieValue;
    }

    @Override
    public int getColor() {
        return pieColor;
    }

    @Override
    public String getDesc() {
        return pieDescription;
    }

    @Nullable
    @Override
    public PieOption getPieOpeion() {
        PieOption option = new PieOption();
        option.setLabelIcon(bitmap);
        option.setLabelPadding(5);
        return option;
    }
}
