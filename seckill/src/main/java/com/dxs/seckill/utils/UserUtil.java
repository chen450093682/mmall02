package com.dxs.seckill.utils;

import com.dxs.seckill.pojo.User;
import com.dxs.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: SecondKillMall
 * @description: 生成用户工具类
 * @author: aaa
 * @create: 2021-05-19 20:04
 **/
public class UserUtil {
    private static void creatUser(int count) throws Exception {
        List<User> users=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user=new User();
            user.setId(13000000000L+i);
            user.setNickname("user"+i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456",user.getSalt()));
            user.setLoginCount(1);
            user.setRegisterDate(new Date());
            users.add(user);
        }

        // //插入数据库
        // Connection connection=getConnection();
        // String sql="insert into t_user(login_count,nickname,register_date,salt,password,id) values(?,?,?,?,?,?)";
        // PreparedStatement statement = connection.prepareStatement(sql);
        // for (int i = 0; i < users.size(); i++) {
        //     User user=users.get(i);
        //     statement.setInt(1,user.getLoginCount());
        //     statement.setString(2,user.getNickname());
        //     statement.setTimestamp(3,new Timestamp(user.getRegisterDate().getTime()));
        //     statement.setString(4,user.getSalt());
        //     statement.setString(5,user.getPassword());
        //     statement.setLong(6,user.getId());
        //     statement.addBatch();
        // }
        // statement.executeBatch();
        // statement.clearParameters();
        // connection.close();

        //登录获取Ticket
        String urlString="http://localhost:8081/login/doLogin";
        File file = new File(System.getProperty("user.dir") + "\\seckill\\src\\main\\resources\\config.txt");
        if (file.exists()){
            file.delete();
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        file.createNewFile();
        randomAccessFile.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            //发送登录请求，并设置登录参数
            URL url = new URL(urlString);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            String params="mobile="+user.getId()+"&&password="+MD5Util.inputPassToFromPass("123456");
            outputStream.write(params.getBytes());
            outputStream.flush();

            //获取返回结果，并写入ByteArrayOutputStream
            InputStream inputStream = urlConnection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len=0;
            while ((len = inputStream.read(bytes))>=0) {
                byteArrayOutputStream.write(bytes,0,len);
            }
            inputStream.close();
            byteArrayOutputStream.close();

            //获取结果，通过objectMapper得到RespBean
            String response = new String(byteArrayOutputStream.toByteArray());
            System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();
            RespBean respBean = objectMapper.readValue(response, RespBean.class);
            String userTicket=(String)respBean.getObj();
            System.out.println("create userTicket by "+user.getId());

            //写回config.txt
            String row = user.getId() + "," + userTicket;
            // System.out.println(row);
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(row.getBytes());
            randomAccessFile.write("\r\n".getBytes());
            System.out.println("write to file :"+user.getId());
        }
        randomAccessFile.close();
        System.out.println("over");
    }

    private static Connection getConnection() throws Exception {
        String url="jdbc:mysql://152.136.144.184:3306/secondkillmall";
        String username="mysql";
        String password="123456";
        String driver="com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }

    public static void main(String[] args) throws Exception {
        // System.out.println(System.getProperty( "user.dir" ));
        creatUser(5000);
    }
}
