package com.qrcode;
import com.google.zxing.WriterException;
import android.util.Log;

import com.qrcode.UserSchemas.UserSchema;
import com.qrcode.UserSchemas.Usertime;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;
import android.app.Activity;
import android.graphics.Color;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import android.widget.ImageView;
import com.google.zxing.EncodeHintType;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.util.Hashtable;

public final class function {
	
	public void reaction(boolean x,boolean y,Context context)
	{
		 if(x == true)
	     {
	         if(y)
	         {
	         	 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	             builder.setMessage("登录成功");
	             builder.setPositiveButton("确定", null);
	             AlertDialog dialog = builder.create();
	             dialog.show();
	         }
	         else
	         {
	             AlertDialog.Builder builder = new AlertDialog.Builder(context);
	             builder.setMessage("密码错误");
	             
	             builder.setPositiveButton("确定", null);
	             AlertDialog dialog = builder.create();
	             dialog.show();
	         }
	     }
	     else
	     {
	         // 用户不存在的情况
	         AlertDialog.Builder builder = new AlertDialog.Builder(context);
	         builder.setMessage("用户不存在");
	         
	         builder.setPositiveButton("确定", null);
	         AlertDialog dialog = builder.create();
	         dialog.show();
	     }
	}
	public void sing_reaction(boolean x,Context context)
	{
		 if(x)
         { //有重复阻止注册
             AlertDialog.Builder builder = new AlertDialog.Builder(context);
             builder.setMessage("用户已存在 无法注册");
             builder.setPositiveButton("确定", null);
             AlertDialog dialog = builder.create();
             dialog.show();
         }
	}
	public void scan_fail(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("扫描失败");
        builder.setPositiveButton("确定", null);
        AlertDialog dialog = builder.create();
        dialog.show();
	}

	public void LED()
	{
		try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	String execOpen = "ioctl -d /dev/ledtest 1 ";
        String execClose = "ioctl -d /dev/ledtest 0 ";
        int t;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; ++j) {
                t=j;
                if (i % 2 != 0)
                    t = 3 - j;
                // 打开LED灯
                try {	
                    Runtime.getRuntime().exec(execOpen + Integer.toString(t));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 暂停一会
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 关闭LED灯
                try {
                    Runtime.getRuntime().exec(execClose + Integer.toString(t));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 暂停一会
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}
	public void FMQ()
    {
    	 try {
             Runtime.getRuntime().exec("ioctl -d /dev/ledtest 0 4");
         } catch (Exception e) {
             e.printStackTrace();
         }
         try {
             Thread.sleep(2000);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	  try {
               Runtime.getRuntime().exec("ioctl -d /dev/ledtest 1 4");
          } catch (Exception e) {
              e.printStackTrace();
           }
         
    }
}
