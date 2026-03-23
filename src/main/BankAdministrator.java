package main;


public class BankAdministrator {
   
       
    public void collectFees(BankAccount account, double fee) {
        if(fee < 0) {
            throw new IllegalArgumentException();
        } else {
            account.adminWithdraw(fee);
        }
    }

}
