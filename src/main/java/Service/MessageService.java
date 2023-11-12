package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    public MessageService(){
        messageDAO=new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO=messageDAO;
    }
    public Message addedMessage(Message message){
        if(!message.getMessage_text().isBlank() && message.getMessage_text().length()<255){
            if(messageDAO.isPostedByExist(message.getPosted_by())){
                return messageDAO.addMessage(message);
            }
        }
        return null;
    }
    public List<Message> getAllTheMessages(){
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }
     
    public Message deleteMessageById(int messageId){
        if(messageDAO.getMessageById(messageId) != null){
            return messageDAO.deletedBymessageId(messageId);
        }
        return null;
    }
    public Message updatedMessage(int messageId,Message message){
        //Message isExist = messageDAO.getMessageById(messageId);
        String messageText=message.getMessage_text();
        if(messageText!=null && !messageText.isEmpty() && messageText.length()<=255){
            Message existingMessage = messageDAO.getMessageById(messageId);

            if (existingMessage != null) {
                existingMessage.setMessage_text(messageText);

                if (messageDAO.updatedMessage(messageId, message)) {
                    return existingMessage;
                }
            }
        }
        return null;
    }
    public List<Message> getAllMessagesByAccountId(int postedBy){
        return messageDAO.getAllMessagesByAccountId(postedBy);
    }

    
}
