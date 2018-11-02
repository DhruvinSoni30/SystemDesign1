package com.example.kalpeshsoni.systemdesign1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;

public class Display extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<String> lisItems;
    ArrayAdapter adapter;
    TextView callDetails;

    ListView ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ll = findViewById(R.id.list);
        callDetails = findViewById(R.id.call);
        db = new DatabaseHelper(this);

        lisItems = new ArrayList<>();
        viewData();

        ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String text = ll.getItemAtPosition(1).toString();
                Toast.makeText(getApplicationContext(), "" + text, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + text));
                startActivity(intent);
            }
        });

        ll.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

             getcalldetails();
                return true;
            }
        });

    }

    private String getcalldetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        return sb.toString();

    }


    private void viewData(){

        Cursor cursor = db.getAllData();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(getApplicationContext(),"Nothing to show",Toast.LENGTH_LONG).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
               lisItems.add(cursor.getString(1));
               lisItems.add(cursor.getString(2));
            }

            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lisItems);
            ll.setAdapter(adapter);
        }
    }

}