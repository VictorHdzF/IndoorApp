package chaos.list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by danflovier on 25/09/2017.
 */

public class SQLiteAdapter {
/*
    private ListSQLHelper myhelper;
    private static final String TAG = "Testing: ";

    SQLiteAdapter(Context context) {
        myhelper = new ListSQLHelper(context);
    }

    long insertData(String uuid, String major, String minor, String x, String y) {

        SQLiteDatabase db = myhelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ListSQLHelper.EAN_ONE, ean);
        values.put(ListSQLHelper.LIFT_TRUCK, lift_truck);
        values.put(ListSQLHelper.STATUS, status);
        values.put(ListSQLHelper.SECTION, section);

        long id = db.insert(ListSQLHelper.TABLE_ONE, null , values);

        //db.close();

        return id;
    }

    public String getData(){
        SQLiteDatabase db = myhelper.getWritableDatabase();

        String[] columns = {ListSQLHelper.UUID, ListSQLHelper.MAJOR, ListSQLHelper.MINOR,
                ListSQLHelper.X, ListSQLHelper.Y};

        Cursor cursor = db.query(ListSQLHelper.TABLE_ONE,columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){

            int cid = cursor.getInt(cursor.getColumnIndex(ListSQLHelper.ID_ONE));

            String ean = cursor.getString(cursor.getColumnIndex(ListSQLHelper.EAN_ONE));
            String  lif_truck = cursor.getString(cursor.getColumnIndex(ListSQLHelper.LIFT_TRUCK));
            String  status = cursor.getString(cursor.getColumnIndex(ListSQLHelper.STATUS));
            String  section = cursor.getString(cursor.getColumnIndex(ListSQLHelper.SECTION));

            buffer.append(cid+ "\t\t\t" + ean + "\t\t\t\t" + lif_truck + "\t\t\t" + status + "\t\t\t" + section + "\n");
        }

        db.close();
        return buffer.toString();
    }

    ArrayList<String> getResults(String column) {
        ArrayList<String> ean = new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();

        try {
            String query = "SELECT * FROM " + ListSQLHelper.TABLE_ONE;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                ean.add(cursor.getString(cursor.getColumnIndex(column)));
            }
            cursor.close();
            db.close();

        }catch(Exception ex){
            Log.e(TAG,"ERROR "+ ex.toString());
        }
        return ean;
    }

    int getSize(){
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String query = "SELECT * FROM " + ListSQLHelper.TABLE_ONE;
        int size;

        Cursor cursor = db.rawQuery(query, null);

        size = cursor.getCount();

        cursor.close();
        db.close();

        return size;
    }


    /// TABLE TWO ///
    long insertProductsData(String ean, String productName) {

        SQLiteDatabase db = myhelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ListSQLHelper.EAN_TWO, ean);
        values.put(ListSQLHelper.PRODUCT_NAME, productName);

        long id = db.insert(ListSQLHelper.TABLE_TWO, null , values);

        db.close();

        return id;
    }

    /*
    ArrayList<String> getProductsResults(String column) {
        ArrayList<String> ean = new ArrayList<>();
        SQLiteDatabase db = myhelper.getWritableDatabase();

        try {
            String query = "SELECT * FROM " + ListSQLHelper.TABLE_TWO;
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                ean.add(cursor.getString(cursor.getColumnIndex(column)));
            }
            cursor.close();
            db.close();

        }catch(Exception ex){
            Log.e(TAG,"ERROR "+ ex.toString());
        }
        return ean;
    }*/
/*
    public String getProductsData(){
        SQLiteDatabase db = myhelper.getWritableDatabase();

        String[] columns = {ListSQLHelper.ID_TWO, ListSQLHelper.EAN_TWO, ListSQLHelper.PRODUCT_NAME};

        Cursor cursor = db.query(ListSQLHelper.TABLE_TWO,columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){

            int cid = cursor.getInt(cursor.getColumnIndex(ListSQLHelper.ID_TWO));

            String ean = cursor.getString(cursor.getColumnIndex(ListSQLHelper.EAN_TWO));
            String  product_name = cursor.getString(cursor.getColumnIndex(ListSQLHelper.PRODUCT_NAME));

            String short_product_name = product_name.substring(0,Math.min(product_name.length(), 12));
            buffer.append(cid+ "\t\t\t" + ean + "\t\t\t\t" + short_product_name + "\n");
        }

        db.close();
        return buffer.toString();
    }

    int getProductsDataSize(){
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String query = "SELECT * FROM " + ListSQLHelper.TABLE_TWO;
        int size;

        Cursor cursor = db.rawQuery(query, null);

        size = cursor.getCount();

        cursor.close();
        db.close();

        return size;
    }

    void deleteData(String table){
        SQLiteDatabase db = myhelper.getWritableDatabase();

        if (Objects.equals(table, "saved_products_db")) {
            // Delete data from table
            db.delete(ListSQLHelper.TABLE_ONE, null, null);
            // Reset ID value from table
            db.delete("sqlite_sequence", "name='" + ListSQLHelper.TABLE_TWO + "'", null);
        }

        if (Objects.equals(table, "products_db")){
            // Delete data from table
            db.delete(ListSQLHelper.TABLE_TWO, null, null);
            // Reset ID value from table
            db.delete("sqlite_sequence", "name='" + ListSQLHelper.TABLE_TWO + "'", null);
        }
        db.close();
    }

    String retrieveProductName(String ean){
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String value="";
        String query = "SELECT " +  ListSQLHelper.PRODUCT_NAME + " FROM " + ListSQLHelper.TABLE_TWO + " WHERE " + ListSQLHelper.EAN_TWO + "= " + ean;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            value = cursor.getString(0);
        }
        db.close();
        return value;
    }*/

}
