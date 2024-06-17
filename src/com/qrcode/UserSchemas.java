package com.qrcode;

public class UserSchemas {
	public interface UserSchema//用户信息
    {
        String TABLE_NAME = "Users"; //Table Name
        String ID = "_id"; //ID
        String hash="hash";
        String USER_NAME = "user_name";
        String PASSWORD = "password";
    }
    public interface Usertime//用户每次登录信息
    {
    	String TABLE_NAME = "Users_login"; //Table Name
        String ID = "_id"; //ID
        String USER_NAME = "user_name";
        String time="time";
        
    }
    public interface Nfc
    {
    	 String TABLE_NAME = "nfc"; //Table Name
         String ID = "_id"; //ID
         String NFC="NFC_code";
         String USER_NAME = "user_name";
    }
}
