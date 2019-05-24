package nguyen.huy.moneylover.MainBill;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nguyen.huy.moneylover.R;

public class ActivityDialogRepeat extends Activity {

    public static final int INTENT_ACTIVITY_RESULT0 = 200;
    public static final int INTENT_ACTIVITY_RESULT1 = 201;
    public static final int INTENT_ACTIVITY_RESULT2 = 202;
    public static final int INTENT_ACTIVITY_RESULT3 = 203;


    private TextView txtFirstDate,txtLastDate,txtDate,txtNumberDate,txtCustom;
    private TextView txtCancel,txtFinish,txtNum,txtDayOfWeek,txtAlarmTime;
    private Spinner spRepeat;

    Calendar cal,calStart,calFinish;

    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2=new SimpleDateFormat("dd");
    SimpleDateFormat sdf3=new SimpleDateFormat("E");
    SimpleDateFormat sdf4=new SimpleDateFormat("E, dd/MM/yyyy");
    SimpleDateFormat sdf5=new SimpleDateFormat("HH:mm");


    String listMode[]={
            "Lặp hàng ngày",
            "Lặp hàng tuần",
            "Lặp hàng tháng",
            "Lặp hàng năm"
    };
    final String listDate[]={
            "ngày",
            "tuần",
            "tháng",
            "năm"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_dialog_repeat);

        addControls();
        addEvents();
    }

    private void addControls() {

        spRepeat=findViewById(R.id.spRepeat);
        txtDate=findViewById(R.id.txtDate);
        txtCustom=findViewById(R.id.txtCustom);
        txtFirstDate=findViewById(R.id.txtFirstDate);
        txtLastDate=findViewById(R.id.txtLastDate);
        txtNumberDate=findViewById(R.id.txtNumberDate);
        txtNum=findViewById(R.id.txtNum);
        txtDayOfWeek=findViewById(R.id.txtDayOfWeek);
        txtCancel=findViewById(R.id.txtCancel);
        txtFinish=findViewById(R.id.txtFinish);
        txtAlarmTime=findViewById(R.id.txtAlarmTime);

        cal=Calendar.getInstance();
        calStart=Calendar.getInstance();
        calFinish=Calendar.getInstance();
        Date date =new Date();
        txtNum.setText(sdf2.format(date)+")");
        txtDayOfWeek.setText(sdf3.format(date)+")");

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(
                ActivityDialogRepeat.this,
                R.layout.bill_spinner_item,
                listMode);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spRepeat.setAdapter(adapter);
    }

    private void addEvents() {
        spRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtDate.setText(listDate[position]);
                if(spRepeat.getSelectedItemPosition()==0){
                    txtCustom.setText("");
                    txtNum.setHeight(0);
                    txtDayOfWeek.setHeight(0);
                    txtFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=getIntent();
                            Log.e("RUN","OK");
                            Log.e("txt",txtNumberDate.getText().toString());
                            calStart.add(Calendar.DAY_OF_MONTH, Integer.parseInt(txtNumberDate.getText().toString()));
                            Log.e("There is",sdf4.format(calStart.getTime()));
                            intent.putExtra("DAY",sdf4.format(calStart.getTime()));
                            setResult(INTENT_ACTIVITY_RESULT0,intent);
                            finish();
                        }
                    });
                }
                else if(spRepeat.getSelectedItemPosition()==1){
                    txtDayOfWeek.setHeight(36);
                    txtDayOfWeek.setWidth(80);
                    txtNum.setHeight(0);
                    txtNum.setWidth(0);
                    txtCustom.setText("vào cùng một ngày hàng tuần (");
                    txtFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=getIntent();
                            calStart.add(Calendar.DAY_OF_MONTH,
                                    7*Integer.parseInt(txtNumberDate.getText().toString()));
                            intent.putExtra("WEEK",sdf4.format(calStart.getTime()));
                            setResult(INTENT_ACTIVITY_RESULT1,intent);
                            finish();
                        }
                    });
                }
                else if(spRepeat.getSelectedItemPosition()==2){
                    txtNum.setHeight(36);
                    txtNum.setWidth(45);
                    txtDayOfWeek.setHeight(0);
                    txtDayOfWeek.setWidth(0);
                    txtCustom.setText("vào cùng một ngày hàng tháng (");
                    txtFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=getIntent();
                            calStart.add(Calendar.MONTH,Integer.parseInt(txtNumberDate.getText().toString()));
                            intent.putExtra("MONTH",sdf4.format(calStart.getTime()));
                            setResult(INTENT_ACTIVITY_RESULT2,intent);
                            finish();
                        }
                    });
                }
                else if(spRepeat.getSelectedItemPosition()==3){
                    txtCustom.setText("");
                    txtNum.setHeight(0);
                    txtDayOfWeek.setHeight(0);
                    txtFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=getIntent();
                            calStart.add(Calendar.YEAR,Integer.parseInt(txtNumberDate.getText().toString()));
                            intent.putExtra("YEAR",sdf4.format(calStart.getTime()));
                            setResult(INTENT_ACTIVITY_RESULT3,intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtFirstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPrecessOpenFirstDate();
            }
        });


        txtLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessOpenLastDate();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessTimeAlarm();
            }
        });
    }

    private void toProcessTimeAlarm() {
        TimePickerDialog.OnTimeSetListener callback2=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cal.set(cal.HOUR_OF_DAY,hourOfDay);
                cal.set(cal.MINUTE,minute);
                txtAlarmTime.setText(sdf5.format(cal.getTime()));
            }
        };

        TimePickerDialog timePickerDialog=new TimePickerDialog(ActivityDialogRepeat.this,
                callback2,
                cal.get(cal.HOUR_OF_DAY),
                cal.get(cal.MINUTE),
                true);

        timePickerDialog.show();
    }


    private void toPrecessOpenFirstDate() {
        DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(cal.YEAR,year);
                cal.set(cal.MONTH,month);
                cal.set(cal.DAY_OF_MONTH,dayOfMonth);
                calStart=cal;
                txtFirstDate.setText(sdf1.format(cal.getTime()));
                txtNum.setText(sdf2.format(cal.getTime())+")");
                txtDayOfWeek.setText(sdf3.format(cal.getTime())+")");
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityDialogRepeat.this,
                callback1,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void toProcessOpenLastDate() {
        DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(cal.YEAR,year);
                cal.set(cal.MONTH,month);
                cal.set(cal.DAY_OF_MONTH,dayOfMonth);
                txtLastDate.setText(sdf1.format(cal.getTime()).toString());
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityDialogRepeat.this,
                callback1,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
