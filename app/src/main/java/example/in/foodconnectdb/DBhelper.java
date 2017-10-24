package example.in.foodconnectdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 01-08-2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "food_detail";

    private static final String FOOD_TABLE = "food";

    private static final String COL_ID = "id";

    private static final String COL_NAME = "name";

    private static final String COL_RATE = "rate";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtable = " CREATE TABLE " + FOOD_TABLE +"("+COL_ID + " INTEGER , " +COL_NAME + " TEXT, " +COL_RATE+ " TEXT " + ")";
        db.execSQL(createtable);
    }

    // it is used to create a new or add the data//
    public void add(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID, model.getId());
        values.put(COL_NAME, model.getFoodname());
        values.put(COL_RATE, model.getRate());
        db.insert(FOOD_TABLE,null,values);
        db.close();
    }

    public List<Model> getList() {

        List<Model> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FOOD_TABLE, null);
        if (cursor.moveToFirst()) {
            do {

                Model model = new Model();
                model.setFoodname(cursor.getString(1));
                model.setRate(Double.valueOf(cursor.getString(2)));
                model.setId(Integer.valueOf(cursor.getString(0)));

                list.add(model);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void tabledelete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL( " DROP TABLE IF EXISTS " + FOOD_TABLE);
        onCreate(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL( " DROP TABLE IF EXISTS " + FOOD_TABLE);
        onCreate(database);
    }

    public void update(Model model, String whereClause) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, model.getFoodname());
        values.put(COL_RATE, model.getRate());
        db.update(FOOD_TABLE,values,COL_ID + "= ?" ,new String[] { whereClause});

    }

    public void delete(String whereclause) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FOOD_TABLE, COL_RATE  + " = ?", new String[] { whereclause});
        db.close();
    }



}
