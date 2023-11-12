package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public Message addMessage(Message message){
         try(Connection con= ConnectionUtil.getConnection()){
            String sql="insert into Message(posted_by, message_text, time_posted_epoch) values(?,?,?);";
            PreparedStatement pstmnt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmnt.setInt(1, message.getPosted_by());
            pstmnt.setString(2,message.getMessage_text());
            pstmnt.setLong(3,message.getTime_posted_epoch());
            int msgRowsAffected = pstmnt.executeUpdate();

            if (msgRowsAffected > 0) {
                try (ResultSet msggeneratedKeys = pstmnt.getGeneratedKeys()) {
                    if (msggeneratedKeys.next()) {
                        int messageId = msggeneratedKeys.getInt(1);
                        return new Message(messageId, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;       
    }
    public boolean isPostedByExist(int id){
        try(Connection con=ConnectionUtil.getConnection()){
            PreparedStatement pstmnt2=con.prepareStatement("select * from Message where posted_by=?;");
            pstmnt2.setInt(1,id);
            ResultSet rs=pstmnt2.executeQuery();
            if(rs.next()){
                return true;
            }
            
        }
        catch(SQLException e){
                System.out.println(e.getMessage());
        }
        return false;
    }
    public List<Message> getAllMessages(){
        List<Message> li = new ArrayList<>();
        try(Connection con=ConnectionUtil.getConnection()){
            Statement pstmnt3=con.createStatement();
            ResultSet rs2=pstmnt3.executeQuery("select * from Message;");
            while(rs2.next()){
                int messageId = rs2.getInt("message_id");
                int postedBy = rs2.getInt("posted_by");
                String messageText = rs2.getString("message_text");
                long timePostedEpoch = rs2.getLong("time_posted_epoch");
                li.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
            return li;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return li;
    }
    public Message getMessageById(int messageId){
        try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement pstmnt4=con.prepareStatement("select * from Message where message_id=?;");
            pstmnt4.setInt(1,messageId);
            ResultSet rs3=pstmnt4.executeQuery();
            if(rs3.next()){
                return new Message(rs3.getInt("message_id"),rs3.getInt("posted_by"),rs3.getString("message_text"),rs3.getLong("time_posted_epoch"));
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Message deletedBymessageId(int messageId){
        try(Connection con = ConnectionUtil.getConnection()){
            Message message = getMessageById(messageId);
            PreparedStatement pstmnt5=con.prepareStatement("delete from Message where message_id=?;");
            pstmnt5.setInt(1,messageId);
            int deletedRow=pstmnt5.executeUpdate();
            if(deletedRow==1){
                return message;
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        
    }
    public boolean updatedMessage(int messageId,Message message){
         try(Connection con = ConnectionUtil.getConnection()){
            PreparedStatement pstmnt6=con.prepareStatement("update Message set message_text=? where message_id=?;");
            pstmnt6.setString(1,message.getMessage_text());
            pstmnt6.setInt(2,messageId);
            int updatedRow= pstmnt6.executeUpdate();
            return updatedRow==1;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
   public List<Message> getAllMessagesByAccountId(int postedBy){
        List<Message> messagesList = new ArrayList<>();
        try(Connection con=ConnectionUtil.getConnection()){
            PreparedStatement pstmnt7=con.prepareStatement("select *  from Message where posted_by=?;");
            pstmnt7.setInt(1,postedBy);
            ResultSet rs=pstmnt7.executeQuery();
            while(rs.next()){
                int messageId = rs.getInt("message_id");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                messagesList.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
            return messagesList;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messagesList;
   }
    
}
