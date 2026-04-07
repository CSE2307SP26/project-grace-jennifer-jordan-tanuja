package main;

import java.util.HashMap;
import java.util.Set;

public class UserProfile {

    private String username;
    private String password;
    private String email;
    private String dob;
    private HashMap<String, BankAccount> accounts;

    public UserProfile(String username, String password, String email, String dob, String primaryPin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
        this.accounts = new HashMap<>();
        this.accounts.put("primary", new BankAccount("checking", primaryPin));
    }

    public UserProfile(String username, String password, String primaryPin) { // fallback constructor for testing purposes
        this(username, password, "", "", primaryPin);
    }

    public String getUsername() {
        return this.username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return this.email;
    }

    public String getDob() {
        return this.dob;
    }

    // so we have a list of accounts associated with the user
    public HashMap<String, BankAccount> getAccounts() {
        return this.accounts;
    }

    public Set<String> getAccountNames() {
        return this.accounts.keySet();
    }
}