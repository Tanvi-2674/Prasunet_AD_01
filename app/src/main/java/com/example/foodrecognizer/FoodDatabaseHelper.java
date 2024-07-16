package com.example.foodrecognizer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food.db";
    private static final int DATABASE_VERSION = 1;

    public FoodDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE food_items (id INTEGER PRIMARY KEY, name TEXT, calories INTEGER)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Apple', 52)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Banana', 89)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Orange', 43)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Strawberries', 49)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Grapes', 104)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Carrot', 25)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Potato', 161)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Tomato', 22)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Brown Rice', 216)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Pasta', 221)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Salmon', 206)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Almonds', 164)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Eggs', 72)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Butter', 102)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Ice Cream', 273)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('White Bread', 67)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Broccoli', 31)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Spinach', 7)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Whole Milk', 149)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Chocolate Chip Cookie', 149)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Potato Chips', 152)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Popcorn', 31)");
        db.execSQL("INSERT INTO food_items (name, calories) VALUES ('Candy Bar', 280)");
        // Add more food items as needed
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS food_items");
        onCreate(db);
    }

    public FoodItem getFoodItem(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items WHERE name = ?", new String[]{name});

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int caloriesIndex = cursor.getColumnIndex("calories");

            if (nameIndex != -1 && caloriesIndex != -1) {
                String foodName = cursor.getString(nameIndex);
                int calories = cursor.getInt(caloriesIndex);
                cursor.close();
                return new FoodItem(foodName, calories);
            }
        }

        return null;
    }
}
