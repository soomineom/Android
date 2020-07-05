package edu.example.contentprovider190603;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String CONTENT_URI = "content://edu.example.contentprovider190603/world";

    private static final int RQCODE_INSERT = 1;
    private static final int RQCODE_UPDATE = 2;
    private static final int RQCODE_DELETE = 3;

    private static final int MENU_INSERT	= Menu.FIRST ;
    private static final int MENU_UPDATE	= Menu.FIRST + 1 ;
    private static final int MENU_DELETE	= Menu.FIRST + 2;

    ListView lv;
    Cursor cursor;
    ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        cr = getContentResolver();
        cursor = cr.query(Uri.parse(CONTENT_URI), null, null, null, null);

        SimpleCursorAdapter ca = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor, new String[] {"country", "capital"},
                new int[] {android.R.id.text1, android.R.id.text2});

        lv = (ListView)findViewById(R.id.ListView01);
        lv.setAdapter(ca);

        registerForContextMenu(lv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_INSERT, Menu.NONE, "insert");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case MENU_INSERT:
                startActivityForResult(new Intent(this, InputActivity.class), RQCODE_INSERT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v == lv)
        {
            menu.add(Menu.NONE, MENU_UPDATE, Menu.NONE, "update");
            menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Intent i = new Intent(this, InputActivity.class);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        cursor.moveToPosition(position);
        int id = cursor.getInt(0);
        String country = cursor.getString(1);
        String capital = cursor.getString(2);

        i.putExtra("country", country);
        i.putExtra("capital", capital);
        i.putExtra("_id", id );

        switch(item.getItemId())
        {
            case MENU_UPDATE:
                startActivityForResult(i, RQCODE_UPDATE);
                break;
            case MENU_DELETE:
                //delete
                cr.delete(Uri.parse(CONTENT_URI + "/" +i.getStringExtra("country")), null, null);
                cursor.requery();
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int id = data.getIntExtra("_id", -1);
        String str1 = data.getStringExtra("country");
        String str2 = data.getStringExtra("capital");

        ContentValues row;

        if( RESULT_OK == resultCode)
        {
            switch(requestCode)
            {
                case RQCODE_INSERT:
                    row = new ContentValues();
                    row.put("country", str1);
                    row.put("capital", str2);
                    cr.insert(Uri.parse(CONTENT_URI),row);
                    break;
                case RQCODE_UPDATE:
                    row = new ContentValues();
                    row.put("country", str1);
                    row.put("capital", str2);
                    cr.update(Uri.parse(CONTENT_URI), row, "_id="+id, null);
                    break;
            }
            cursor.requery();
        }
    }
}
