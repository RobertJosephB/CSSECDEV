package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class SQLite {
    
    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";
    
    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0,\n"
            + " salt VARBINARY,\n"
            + " answer1 TEXT,\n"
            + " answer2 TEXT,\n"
            + " answer3 TEXT\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES(?,?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)){
        
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setInt(3, stock);
            pstmt.setString(4, timestamp);
        
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addLogs(String event, String username, String desc, String timestamp) {
    String sql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
    
    try (Connection conn = DriverManager.getConnection(driverURL);
        PreparedStatement pstmt = conn.prepareStatement(sql)){
        
        pstmt.setString(1, event);
        pstmt.setString(2, username);
        pstmt.setString(3, desc);
        pstmt.setString(4, timestamp);
        
        pstmt.executeUpdate();
    } catch (Exception ex) {
        System.out.print(ex);
    }
}
    
    public void addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES(?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
           PreparedStatement pstmt = conn.prepareStatement(sql)){
        
            pstmt.setString(1, name);
            pstmt.setInt(2, stock);
            pstmt.setDouble(3, price);
            

            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addUser(String username, String password,String secQuest,String secQuest1,String secQuest2, byte[] salt) {
        String sql = "INSERT INTO users(username,password,answer1,answer2,answer3,salt) VALUES(?,?,?,?,?,?)";
        
        String hashedPass = getHash(password,salt);
        String hashedSec1 = getHash(secQuest,salt);
        String hashedSec2 = getHash(secQuest1,salt);
        String hashedSec3 = getHash(secQuest2,salt);
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPass);
            pstmt.setBytes(6, salt);
            pstmt.setString(3, hashedSec1);
            pstmt.setString(4, hashedSec2);
            pstmt.setString(5, hashedSec3);
            pstmt.executeUpdate();
            
//      PREPARED STATEMENT EXAMPLE
//      String sql = "INSERT INTO users(username,password) VALUES(?,?)";
//      PreparedStatement pstmt = conn.prepareStatement(sql)) {
//      pstmt.setString(1, username);
//      pstmt.setString(2, password);
//      pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    
    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        
        try {
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return histories;
    }
    public ArrayList<History> getUserHistory(String username){
        String sql = "SELECT id, username, name, stock, timestamp FROM history where username = ?";
        ArrayList<History> histories = new ArrayList<History>();
        
        try {
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return histories;
    }
    
    
    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                                   rs.getString("event"),
                                   rs.getString("username"),
                                   rs.getString("desc"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logs;
    }
    
    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return products;
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, role, locked FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("password"),
                                   rs.getInt("role"),
                                   rs.getInt("locked")));
            }
        } catch (Exception ex) {}
        return users;
    }
    
    public void addUser(String username, String password, int role) {
        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + password + "','" + role + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void removeUser(String username) {
    String sql = "DELETE FROM users WHERE username=?";

    try (Connection conn = DriverManager.getConnection(driverURL);
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, username);
        stmt.executeUpdate();
        System.out.println("User " + username + " has been deleted.");
    } catch (Exception ex) {
        System.out.print(ex);
    }
}

    public Product getProduct(String name){
        String sql = "SELECT name, stock, price FROM product WHERE name=?;";
        Product product = null;
        try{
             Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             pstmt.setString(1, name);

             ResultSet rs = pstmt.executeQuery();
             while (rs.next()) {
                 product = new Product(rs.getString("name"),
            rs.getInt("stock"),
            rs.getFloat("price"));
             }


         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return product;
    }
    public Product getProduct(int id){
        String sql = "SELECT name, stock, price FROM product WHERE id=?;";
        Product product = null;
        try{
             Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, id);

             ResultSet rs = pstmt.executeQuery();
             while (rs.next()) {
                 product = new Product(rs.getString("name"),
            rs.getInt("stock"),
            rs.getFloat("price"));
             }


         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return product;
    }
    public int getStock(String name){
        String sql = "SELECT stock, price FROM product WHERE name=?;";
        int stock = 0;
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                stock = rs.getInt("stock");
            }
           
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stock;
    }
    public void buyProduct(int stock,String name){
        String sql = "UPDATE product set stock =? where name =?";

        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, stock);
            pstmt.setString(2, name);
            
            pstmt.executeUpdate();
           
            
        } catch (Exception ex) {
            ex.printStackTrace();
            
        }
       
    }
    public boolean checkUser(String username, String password, String passwordConfirm){
        String sql = "SELECT id, username, password, role, locked FROM users WHERE username=?";
        ArrayList<User> users = new ArrayList<User>();
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt("locked")));
            }
            if(users.isEmpty() && passwordConfirm.equals(password))
                return true;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean userExists(String username){
        String sql = "SELECT id, username, password, role, locked FROM users WHERE username=?";
        ArrayList<User> users = new ArrayList<User>();
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt("locked")));
            }
            if(users.isEmpty())
                return false;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public String getHash(String password, byte[] salt) {
         String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(salt);

            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            
            
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    
    public byte[] newSalt() {
    Random RANDOM = new SecureRandom();   
        
    byte[] salt = new byte[16];
    RANDOM.nextBytes(salt);
    return salt;
  }
    
    public byte[] getSalt(String username) {
        String sql = "SELECT id, username, password, role, locked, salt FROM users WHERE username=?";
        User user = new User("","");
        byte[] noSalt = new byte[1];
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt("locked"),
                        rs.getBytes("salt"));
            }
            if(user.getSalt()==null)
                return noSalt;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        return user.getSalt();
    }
 
    
    
    public boolean authenticateUser(String username, String password){
        String sql = "SELECT id, username, password, role, locked FROM users WHERE username=? and password=?";
        ArrayList<User> users = new ArrayList<User>();
        
        String hashedPass = getHash(password, getSalt(username));
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPass);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt("locked")));
            }
            if(users.isEmpty())
                return false;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
        
    }
    public boolean lockout(String username){
        String sql = "UPDATE users SET locked=1 WHERE username=?";
        ArrayList<User> users = new ArrayList<User>();
        
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
        
            pstmt.executeUpdate();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean unlock(String username){	
        String sql = "UPDATE users SET locked=0 WHERE username=?";	
        ArrayList<User> users = new ArrayList<User>();	
        	
            	
        try{	
            Connection conn = DriverManager.getConnection(driverURL);	
            PreparedStatement pstmt = conn.prepareStatement(sql);	
            pstmt.setString(1, username);	
        	
            pstmt.executeUpdate();	
            	
        } catch (Exception ex) {	
            ex.printStackTrace();	
            return false;	
        }	
        return true;	
    }
    public int getLocked(String username){
        String sql = "Select locked FROM users WHERE username=?";
        ArrayList<User> users = new ArrayList<User>();
        int temp =-1;
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
        
             ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                        
                        temp =rs.getInt("locked");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return temp;
        }
        return temp;
    }
    public int getRole(String username, String password){
        String sql = "Select role FROM users WHERE username=? and password=?";
        int temp =-1;
        
        String hashedPass = getHash(password, getSalt(username));
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPass);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                        System.out.println(temp);
                        temp =rs.getInt("role");
            }
      
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return temp;
        
    }
    
    public boolean forgotPassword(String username, String password){
        String sql = "UPDATE users SET password=?,salt=? WHERE username=?";
        ArrayList<User> users = new ArrayList<User>();
        
        String hashedPass = getHash(password, getSalt(username));
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hashedPass);
            pstmt.setBytes(2, getSalt(username));
            pstmt.setString(3, username);
            pstmt.executeUpdate();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
        
    }
    
    public List<String> getSecurityQuestionAnswers(String username){
        String sql = "SELECT username,password, answer1, answer2, answer3 FROM users WHERE username=?";
        User user = new User("","");
        List<String> answers = new ArrayList<String>();
        
            
        try{
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                user = new User(rs.getString("username"),
                        rs.getString("password"));
                user.setAnswer1(rs.getString("answer1"));
                user.setAnswer2(rs.getString("answer2"));
                user.setAnswer3(rs.getString("answer3"));
            }
            if(user==null)
                return null;
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        answers.add(user.getAnswer1());
        answers.add(user.getAnswer2());
        answers.add(user.getAnswer3());
        return answers;
        
    }
    public void addProduct(int id,String name,int stock,float price){
      
        String sql = "INSERT INTO product(id, name, stock,price) VALUES (?, ?, ?,?)";
        try {
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.setString(2,name);
            pstmt.setInt(3, stock);
            pstmt.setFloat(4,price);
            pstmt.executeUpdate();

        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void editProduct(int id,String name,int stock,float price){
      
        String sql = "UPDATE product SET name = ?,stock=?, price = ? WHERE id=?";
        try {
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(4,id);
            pstmt.setString(1,name);
            pstmt.setInt(2, stock);
            pstmt.setFloat(3,price);
            pstmt.executeUpdate();
            
        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void removeProduct(String name){
      
        String sql = "DELETE from product WHERE name=?";
        try {
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.execute();

        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
     
   	
    	
    public boolean editUserRole(String name, int role) {	
        String sql = "UPDATE users SET role = ? WHERE username = ?";	
        	
        	
        try{	
            Connection conn = DriverManager.getConnection(driverURL);	
            	
            PreparedStatement pstmt = conn.prepareStatement(sql);	
            	
            pstmt.setInt(1, role);	
            pstmt.setString(2, name);	
        	
            pstmt.executeUpdate();	
            	
        } catch (Exception ex) {	
            ex.printStackTrace();	
            return false;	
        }	
        return true;	
    }
    public void removeProduct(int id){
      
        String sql = "DELETE from product WHERE id=?";
        try {
            Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.execute();

        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}