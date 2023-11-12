package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    private  AccountDAO accountDAO;
    public AccountService(){
        accountDAO= new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO=accountDAO;
    }
    
    public  Account addAccount(Account account){
        if(account.getUsername() != null && !account.getUsername().isBlank() && account.getPassword() != null && account.getPassword().length() >= 4){

            if(!accountDAO.isAccountExist(account.getUsername())){
                return accountDAO.addAccount(account);

            }
            else{
                System.out.println("Account already Exists");
            }
        }
        else{
           System.out.println("Account details didn't meet the requirements");
        }
        return null;
        
    }    
    public Account userLogin(Account account){
        accountDAO=new AccountDAO();
        return accountDAO.userLogin(account);
    }   
    
}
