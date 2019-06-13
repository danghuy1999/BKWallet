package nguyen.huy.moneylover.MainEvent.View;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import nguyen.huy.moneylover.MainEvent.Adapter.AdapterListEvents;
import nguyen.huy.moneylover.MainEvent.Model.Event;
import nguyen.huy.moneylover.R;

public class ListEventActivity extends AppCompatActivity implements ChildEventListener {

    ListView lvEvents;
    AdapterListEvents adapterListEvents;
    ArrayList<Event>arrListEvent;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Sự kiện").child("Đang áp dụng");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_list_event);

        addActionbar();
        addControls();
        addEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addActionbar() {
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Danh sách sự kiện");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }

    private void addControls() {
        lvEvents=findViewById(R.id.lvEvents);
        arrListEvent=new ArrayList<>();
        adapterListEvents=new AdapterListEvents(ListEventActivity.this,R.layout.bill_item_select_group,arrListEvent);
        lvEvents.setAdapter(adapterListEvents);

    }

    private void addEvents() {
        myRef.addChildEventListener(ListEventActivity.this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Event event=dataSnapshot.getValue(Event.class);
        arrListEvent.add(event);
        adapterListEvents.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
