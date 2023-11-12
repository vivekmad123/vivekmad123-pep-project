package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public SocialMediaController() {
        this.accountService = new AccountService(); 
        this.messageService=new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountAddition);
        app.post("/login", this::postUserLoginStatus);
        app.post("/messages", this::postNewMessage);
        app.get("/messages",this::getMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deletedBymessageId);
        app.patch("/messages/{message_id}", this::updateMessageText);
        app.get("/accounts/{account_id}/messages",this::getAllMessagesById);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    private void postAccountAddition(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account;
        try {
            account = mapper.readValue(ctx.body(), Account.class);
            Account addedAccount = accountService.addAccount(account);

            if (addedAccount != null) {
                ctx.json(addedAccount);
            } else {
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            ctx.status(400).result("Invalid JSON format");
        }
    }
    private void postUserLoginStatus(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account;
        try{
            account=mapper.readValue(ctx.body(),Account.class);
            Account loginAccount=accountService.userLogin(account);
            if(loginAccount != null ){
                ctx.json(loginAccount);
            }
            else{
                ctx.status(401);
            }
        }
        catch(JsonProcessingException e){
            ctx.status(400);
        }
    }
    private void postNewMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message;
        try{
            message=mapper.readValue(ctx.body(),Message.class);
            Message newMessage=messageService.addedMessage(message);
            if(newMessage != null ){
                ctx.json(newMessage);
            }
            else{
                ctx.status(400);
            }
        }
        catch(JsonProcessingException e){
            ctx.status(400);
        }
        
    }
    private void getMessages(Context ctx) {
        List<Message> messages=messageService.getAllTheMessages();
        ctx.json(messages);
    }
    private void getMessageById(Context ctx) {
        //ObjectMapper mapper = new ObjectMapper();
        //Message message;
        int messageId;
        messageId=ctx.pathParamAsClass("message_id", Integer.class).get();
        Message messageById=messageService.getMessageById(messageId);
        if(messageById != null ){
            ctx.json(messageById);
        }
        else{
            ctx.status(200);
        }
    }
    
    private void deletedBymessageId(Context ctx) {
        int messageId=ctx.pathParamAsClass("message_id", Integer.class).get();
        Message deletedMessage=messageService.deleteMessageById(messageId);
        if(deletedMessage !=null){
            ctx.status(200);
            ctx.json(deletedMessage);

        }
        else{
            ctx.status(200);
        }
    }
    private void updateMessageText(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try{
            Message message=mapper.readValue(ctx.body(),Message.class);
            int messageId=ctx.pathParamAsClass("message_id", Integer.class).get();
            Message newMessage=messageService.updatedMessage(messageId,message);
            if(newMessage != null ){
                ctx.status(200);
                ctx.json(mapper.writeValueAsString(newMessage));
            }
            else{
                ctx.status(400);
            }
        }
        catch(JsonProcessingException e){
            ctx.status(400);
        }
    }
    private void getAllMessagesById(Context ctx) {
        int postedBy=ctx.pathParamAsClass("account_id", Integer.class).get();
        List<Message> AllMessages=messageService.getAllMessagesByAccountId(postedBy);
        ctx.json(AllMessages);
    }
    
}