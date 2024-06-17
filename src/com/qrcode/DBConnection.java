package com.qrcode;
import com.qrcode.UserSchemas.UserSchema;
import android.content.Context;
import com.qrcode.UserSchemas.Usertime;
import com.qrcode.UserSchemas.Nfc;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "recode";
    private static final int DATABASE_VERSION = 1;
    
    public DBConnection(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db){
        // 创建两个数据库
    	
        String sql = "CREATE TABLE " + "Users" + " (" + "_id" + " INTEGER primary key autoincrement, " + "user_name" + " text not null, " + "password" + " text not null," + "hash"+" text not null "+");";
        db.execSQL(sql);
        String sql2 = "CREATE TABLE " + "Users_login" + " (" + "_id" + " INTEGER primary key autoincrement, " + "user_name" + " text not null, " + "time" + " text not null "+");";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE " + "nfc" + " (" + "_id" + " INTEGER primary key autoincrement, " + "NFC_code" + " text not null, " + "user_name" + " text not null "+");";
        db.execSQL(sql3);
       
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    	
//    	if(oldVersion<2){
//        db.execSQL("DROP TABLE IF EXISTS"+UserSchema.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS"+Usertime.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS"+UserSchema.TABLE_NAME);
//        
//        onCreate(db);
//        }
    }
}
