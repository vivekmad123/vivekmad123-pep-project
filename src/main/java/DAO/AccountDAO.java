package DAO;

import Model.Account;

import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
    public Account addAccount(Account account){
        try(Connection con= ConnectionUtil.getConnection()){
            String sql="insert into Account(username,password) values(?,?);";
            PreparedStatement stmnt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, account.getUsername());
            stmnt.setString(2,account.getPassword());
            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmnt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int accountId = generatedKeys.getInt(1);
                        return new Account(accountId, account.getUsername(), account.getPassword());
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;       
    }
    public boolean isAccountExist(String Username){
        
        try(Connection con= ConnectionUtil.getConnection()){
           PreparedStatement st=con.prepareStatement("select * from Account where Account.username=?;");
           st.setString(1, Username);
           ResultSet rs1=st.executeQuery();
           if(rs1.next()){
            return true;
           }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Account userLogin(Account account){
        try(Connection con= ConnectionUtil.getConnection()){
            PreparedStatement stmnt2 = con.prepareStatement("select * from Account where username=? and password=?;");
            stmnt2.setString(1,account.getUsername());
            stmnt2.setString(2, account.getPassword());
            ResultSet rs2 = stmnt2.executeQuery();
            if(rs2.next()){
                int accountId=rs2.getInt("account_Id");
                String username=rs2.getString("username");
                String password=rs2.getString("password");
                return new Account(accountId, username, password);

            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
    

