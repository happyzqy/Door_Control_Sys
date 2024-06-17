package com.qrcode;
import com.qrcode.function;
import com.qrcode.DBConnection;
import com.qrcode.UserSchemas.UserSchema;
import com.qrcode.UserSchemas.Usertime;
import com.qrcode.UserSchemas.Nfc;
import com.qrcode.QRLoginActivity;
import com.socket.ClientSocketThread;
import com.socket.Const;
import com.socket.clientSocketTools;
import com.socket.ClientSocketThread.MessageListener;
import com.google.zxing.WriterException;
import android.util.Log;
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
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.text.SimpleDateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.util.concurrent.TimeUnit;


public class CopyOfMainActivity extends Activity implements View.OnClickListener
{
	// UI控件
	private ImageView qrImgImageView;
	private TextView resutTextView;
	private EditText qrStrEditText;
	
	private EditText mEditText01;
	private EditText mEditText02;
    
	//初始选择按钮
    private Button nfc_btn = null;
    private Button code_btn = null;
    private Button ap_btn = null;
    private Button reg_btn = null;
    public int id_this;
    static DBConnection helper;

    private QRLoginActivity QRActivity;
    
    
    //nfc section
	private static int fd;
	//nfc button
	private Button button1=null;
	private Button button2=null;
	private Button button3=null;
	private TextView textView1=null;
	private String s;//检测到的nfc卡号
	private ClientSocketThread clientSocketThread=null;
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			s=(String) msg.obj;
			s=s.trim();
			textView1.setText(s);
			
		}
	};
	private byte[] data;
    


    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
      
        main_recall();
    }
    public void main_recall()
    {    
    	setContentView(R.layout.main);
    	//moto_opp(0);

    	 mEditText01 = (EditText) findViewById(R.id.editText1);
         mEditText02 = (EditText) findViewById(R.id.editText2);
         nfc_btn = (Button) findViewById(R.id.nfc);
         code_btn = (Button) findViewById(R.id.code);
         ap_btn = (Button) findViewById(R.id.login);
         reg_btn = (Button) findViewById(R.id.reg);
         bindListener();
         helper = new DBConnection(this);
    }
    
    private void bindListener() {
    	nfc_btn.setOnClickListener(this);
    	code_btn.setOnClickListener(this);
    	ap_btn.setOnClickListener(this);
    	reg_btn.setOnClickListener(this);
    }
    
    public void onClick(View view)//主页面布局
    {
    	int id=view.getId();	
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CopyOfMainActivity.this);
        final SQLiteDatabase db = helper.getWritableDatabase();
        builder1.setIcon(R.drawable.icon);
        switch(id)
        {
        	case R.id.login:
            	ContentValues values = new ContentValues();
                String name=mEditText01.getText().toString(),pass=mEditText02.getText().toString();
            	values.put(Usertime.USER_NAME, name);
                boolean ok=false,ok2=false;
                boolean []val=is_in(name,pass,db);//查询是否正确（密码账户）
                ok=val[0];ok2=val[1];
            	if(ok == true && ok2==true)//更新时间
                {
                    Date currentDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // 将Date对象格式化为字符串
                    String time = dateFormat.format(currentDate);
                    values.put(Usertime.time,time);
                    db.insert(Usertime.TABLE_NAME, null, values);
                }
                function fc=new function();
                fc.reaction(ok,ok2,CopyOfMainActivity.this);
                if(ok&&ok2)
                {
                   log_after(name);
                }
                db.close();
                break;
        	case R.id.code:
        		qrcode_login();
        		break;
        	case R.id.nfc:
        		nfc_login();
        		
        		break;
        	case R.id.reg:
        		ap_login();
        		break;
        }
    }
    
    public static boolean[] is_in(String name,String pass,SQLiteDatabase db) {//用来返回此时说法有这个名字和密码是否正确
        boolean[] val = new boolean[2];
        Cursor c = db.query(UserSchema.TABLE_NAME, new String[]
        {
            UserSchema.USER_NAME, UserSchema.PASSWORD
        }, null, null, null, null, null);
        int len = c.getCount();
        boolean ok = false; boolean ok2 = false;
        if(c.moveToFirst())
        {
            for(int i = 0; i < len; i++)
            {
                if(name.equals(c.getString(0)))
                {
                    if(pass.equals(c.getString(1))) ok2 = true;
                    ok = true;
                    break;
                }
                c.moveToNext();
            }
        }
        val[0]=ok; val[1]=ok2;
        c.close();
        return val;
    }
    public static boolean is_in_nfc(String name,String nfc_code,SQLiteDatabase db)
    {
    	 Cursor c = db.query(Nfc.TABLE_NAME, new String[]
    		        {
    		            Nfc.USER_NAME, Nfc.NFC
    		        }, null, null, null, null, null);
    		        int len = c.getCount();
    		        boolean ok = false;
    		        if(c.moveToFirst())
    		        {
    		            for(int i = 0; i < len; i++)
    		            {
    		                if(name.equals(c.getString(0)))
    		                {
    		                    if(nfc_code.equals(c.getString(1))) ok = true;
    		                    break;
    		                }
    		                c.moveToNext();
    		            }
    		        }
    		        c.close();
    		        return ok;
    }
    private void qrcode_login()
    {
    	 Intent openCameraIntent = new Intent(CopyOfMainActivity.this, CaptureActivity.class);
         startActivity(openCameraIntent); 
//         setContentView(R.layout.qrcode);
//    	 Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
//         scanBarCodeButton.setOnClickListener(new OnClickListener()
//         {
//        	 @Override
//             public void onClick(View v)
//             {
//                 Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
//                 startActivity(openCameraIntent); 
//             }
//         });         
    }
    public void nfc_login()
    {    
    	nfc_Activity();
        //Intent intent=new Intent(MainActivity.this,nfc_Activity);
        //startActivity(intent); 

        
    }
    private void ap_login()//注册界面
    {
        setContentView(R.layout.ap);
        Button ap_register_btn = (Button) findViewById(R.id.ap_register_btn);
        Button ap_return_btn = (Button) findViewById(R.id.ap_return_btn);
        final EditText mEditText01 = (EditText) findViewById(R.id.account);
        final EditText mEditText02 = (EditText) findViewById(R.id.password);
        final EditText mEditText03 = (EditText) findViewById(R.id.password2);
        //开始监听这几个按钮
        //登录
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);//用来展示二维码
        //注册  
        ap_register_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                ContentValues values = new ContentValues();//用来创建新的用户
                String name=mEditText01.getText().toString();
                String pass=mEditText02.getText().toString();
                String pass2=mEditText03.getText().toString();
                if(!pass.equals(pass2))
                {
                	 AlertDialog.Builder builder = new AlertDialog.Builder(CopyOfMainActivity.this);
    	             builder.setMessage("密码不相同");
    	             builder.setPositiveButton("确定", null);
    	             AlertDialog dialog = builder.create();
    	             dialog.show();
    	             return;
                }
                values.put(UserSchema.USER_NAME,name);
                values.put(UserSchema.PASSWORD, pass);
                String hashCode = Integer.toString(name.hashCode());
                values.put(UserSchema.hash,hashCode);
                SQLiteDatabase db = helper.getWritableDatabase();
                //判断是否重复用户名
                boolean []val=is_in(name,pass,db);
                boolean ok=val[0];
                function fc=new function();
                fc.sing_reaction(ok,CopyOfMainActivity.this);
                if(ok==false)//可以创建
                {   
                	db.insert(UserSchema.TABLE_NAME, null, values);
                    db.close();
                    try//将二维码展示
                    {
                        String contentString = hashCode;
                        if(!contentString.equals(""))
                        {
                            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
                            qrImgImageView.setImageBitmap(qrCodeBitmap);
                        }
                        else
                        {
                            Toast.makeText(CopyOfMainActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(WriterException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 返回
        ap_return_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	main_recall();
            }
        });
    }
    
    public void nfc_Activity()
    {


    	//注册NFC卡号
    	final String nfc1="0056B3F178";
    	final String nfc2="00C68DD778";
    	final String nfc3="00F6110679";
    	
    	boolean isOpen = false;
    	
    	//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nfc);
		
		button1=(Button)findViewById(R.id.btn_nfc_find);
		button2=(Button)findViewById(R.id.btn_nfc_return);
		//button2=(Button)findViewById(R.id.btn_nfc_light_open);
		//button3=(Button)findViewById(R.id.btn_nfc_light_close);
		textView1=(TextView)findViewById(R.id.txt_nfc_result);
		light(2);
		isOpen = false;
		
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				clientSocketThread=ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(), Const.SERVER_PORT);
				clientSocketThread.setListener(new MessageListener() {
					public void Message(byte[] message, int message_len) {
						// TODO Auto-generated method stub
						handler.sendMessage(handler.obtainMessage(100,"\n"+clientSocketTools.byte2hex(message, message_len)));
					}
				});
			}
		}).start();
		 //扫卡
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beep(0);
						
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
				beep(1);
				// TODO Auto-generated method stub
				try {
					clientSocketThread.getOutputStream().write(Const.nfc_open);
					for(int iii=1;iii<=10;iii++)
						for(int jjj=1;jjj<=1000;jjj++)
							for(int kkk=1;kkk<=1000;kkk++);
					clientSocketThread.getOutputStream().write(Const.nfc_read);
					clientSocketThread.getOutputStream().write(Const.matrix_open);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.print(s);
				Toast.makeText(CopyOfMainActivity.this, s, Toast.LENGTH_SHORT).show();
				//check_with_database(s);
				//moto_opp(1);
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				main_recall();
				//moto_opp(2);
			}
		});
    }
    
    
	private void check_with_database(String Nfc_code)
	{
        
        //获得扫描到的结果
	   SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.query(Nfc.TABLE_NAME, new String[]
                {
        	    	Nfc.USER_NAME, Nfc.NFC
                }, null, null, null, null, null);
        int len = c.getCount();
        String name="";
        boolean ok = false; boolean ok2 = false;
        if(c.moveToFirst()){
           for(int i = 0; i < len; i++){
              if(Nfc_code.equals(c.getString(1))){
                      ok = true;
                      name=c.getString(0);
                      break;
                   }
                 c.moveToNext();
          }
         }
    	main_recall();
        
        if(ok)
        {
        	log_after(name);
        }
        else
        {
        	 function fc=new function();
             //fc.reaction(false,false,MainActivity.this);
             db.close();
        }
	}
	
	private void moto_opp(int opp) {
		if(opp==0){
			try {
				clientSocketThread.getOutputStream().write(Const.moto_stop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (opp==1) {
			try {
				clientSocketThread.getOutputStream().write(Const.moto_left);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (opp==2) {
			try {
				clientSocketThread.getOutputStream().write(Const.moto_right);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return;
		}
	}
	
    private void light(int opp) {
		if(opp==0){
			try {
				clientSocketThread.getOutputStream().write(Const.blight_close);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (opp==1) {
			try {
				clientSocketThread.getOutputStream().write(Const.blight_green);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (opp==2) {
			try {
				clientSocketThread.getOutputStream().write(Const.blight_red);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return;
		}
	}
    private void beep(int sw) {
		
		if (sw == 0){
			try {
				Runtime.getRuntime().exec("ioctl -d /dev/ledtest 0 4");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (sw == 1) {
			try {
				Runtime.getRuntime().exec("ioctl -d /dev/ledtest 1 4");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
    }
    public void log_after(final String s)//将用户名字一起导入
    {
    	 setContentView(R.layout.find_time);
    	 Button tim_query = (Button) findViewById(R.id.tim_query);
         Button log_out = (Button) findViewById(R.id.tim_return);
         Button log_fmq= (Button) findViewById(R.id.log_fmq);
         Button log_led=(Button) findViewById(R.id.log_led);
         Button create_nfc=(Button) findViewById(R.id.nfc_reg);
         final TextView f1=(TextView) findViewById(R.id.textView1);
         Button open_d=(Button) findViewById(R.id.open_door);
         Button close_d=(Button) findViewById(R.id.close_door);
         
         open_d.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
        		 moto_opp(1);
//                 try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                 //moto_opp(0);
              }
         }); 
         close_d.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
        		 moto_opp(2);
//                 try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                // moto_opp(0);
              }
         }); 
         log_out.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
                 main_recall();
                 
              }
         }); 
         tim_query.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
        		      SQLiteDatabase db = helper.getWritableDatabase();
                      String name=s;
                      boolean ok=false,ok2=false;
                       //输出数据前五 
                        String []out=new String[5];
                        Cursor c = db.query(Usertime.TABLE_NAME, new String[]
                     	        {
                     	            Usertime.USER_NAME, Usertime.time
                     	        }, null, null, null, null, null);
                        int len = c.getCount();
                        int tmp=0;
                        if(c.moveToFirst())
             	       {
                     	for(int i = 0; i < len; i++)
                     	{
                     	 if(name.equals(c.getString(0)))
                     	{
                     	  tmp++;
                     	  if(tmp==6)
                     	  {
                     		  tmp=5;
                     		  for(int j=0;j<4;j++)
                     			  out[j]=out[j+1];
                     		  out[4]=c.getString(1);
                     	  }
                     	  else
                     	  {
                     		  out[tmp-1]=c.getString(1);
                     	  }
                     	}
                	          c.moveToNext();
                            }
                          }
                        //此时获得out保存前5个，接下来需要输出
                      if(tmp>=1)
                         f1.setText(out[0]);
                      function fc=new function();
                      fc.reaction(ok,ok2,CopyOfMainActivity.this);
                      db.close();
             }
         }); 
         log_fmq.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
        		 function fc=new function();
                 fc.FMQ();
             }
         }); 
         log_led.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
        		 function fc=new function();
                 fc.LED();
             }
         }); 
         create_nfc.setOnClickListener(new View.OnClickListener()
         {
        	 public void onClick(View v)
             {
        		 ContentValues values = new ContentValues();//用来创建新的用户
         		   //识别id
         		    //接下来绑定:
         		    //此时ID为String id
         		 String id="";
                 SQLiteDatabase db = helper.getWritableDatabase();
         		 boolean ok=is_in_nfc(s,id,db);
         		 if (ok==false){
         		 values.put(Nfc.USER_NAME,s);
                 values.put(Nfc.NFC,id);
                 db.insert(Nfc.TABLE_NAME, null, values);
                 }
         		 else
         		 {
         			AlertDialog.Builder builder = new AlertDialog.Builder(CopyOfMainActivity.this);
         	        builder.setMessage("已经存在NFC");
         	        builder.setPositiveButton("确定", null);        	        
         	        AlertDialog dialog = builder.create();
         	        dialog.show();
         	        
         		 }
         		 db.close();
                
                 return;     
             }
         });  
    }
 
    //扫描
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            //scanResult 就是获取的信息
            //这里判断是否为正确的信息：
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor c = db.query(UserSchema.TABLE_NAME, new String[]
            {
                UserSchema.USER_NAME, UserSchema.PASSWORD,UserSchema.hash
            }, null, null, null, null, null);
            int val = c.getCount();
            boolean ok = false;
            String  s2 = "";
            String name="";
            if(c.moveToFirst())
            {
                for(int i = 0; i < val; i++)
                {
                    s2 = c.getString(2);
                    if(s2.equals(scanResult))
                    {
                    	name=c.getString(0);
                        ok = true;
                        break;
                    }
                    c.moveToNext();
                }
            }
            function fc=new function();
            fc.reaction(ok,true,CopyOfMainActivity.this);
            if(ok)
            {
            	log_after(name);
            }
        }
        else
        {
        	 function fc=new function();
             fc.scan_fail(CopyOfMainActivity.this);
        }
    }
    
}