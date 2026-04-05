package main;

import java.util.HashMap;
import java.util.Set;

public class UserProfile {

    private String username;
    private String password;
    private HashMap<String, BankAccount> accounts;

    public UserProfile(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new HashMap<>();
        this.accounts.put("primary", new BankAccount("checking"));
    }

    public String getUsername() {
        return this.username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // so we have a list of accounts associated with the user
    public HashMap<String, BankAccount> getAccounts() {
        return this.accounts;
    }

    public Set<String> getAccountNames() {
        return this.accounts.keySet();
    }
}