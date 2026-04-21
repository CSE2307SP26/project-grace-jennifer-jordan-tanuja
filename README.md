# Bank Application

## Team Members:

* Grace Lee
* Jennifer Martinez Mejia
* Jordan Oliver
* Tanuja Gunapooti

## Project Description
This is a Java terminal app that simulates a bank. It has features for both customers and administrators. Customers can create a bank login, open or close accounts, check balances, view transaction history, and move money through deposits, withdrawals, or transfers. Administrators can collect fees and add interest to existing accounts.

## User stories

### Iteration 1
1. A bank customer should be able to deposit into an existing account. (Shook)
2. A bank customer should be able to withdraw from an account. (Jennifer)
3. A bank customer should be able to check their account balance. (Jennifer)
4. A bank customer should be able to view their transaction history for an account. (Grace)
5. A bank customer should be able to create an additional account with the bank. (Grace)
6. A bank customer should be able to close an existing account. (Tanuja)
7. A bank customer should be able to transfer money from one account to another. (Tanuja)
8. A bank adminstrator should be able to collect fees from existing accounts when necessary. (Jordan)
9. A bank adminstrator should be able to add an interest payment to an existing account when necessary. (Jordan)

### Iteration 2
11. A bank customer should be able to create an account with the bank using a username, password, email, and date of birth. (Tanuja and Jennifer)
12. A bank customer should be able to open a savings, checkings, or credit card account. (Tanuja)
13. A bank customer should be able to login into their account with their username and password. (Tanuja)
14. A bank customer should be prompted for their full name and date of birth before creating a new bank account, assuming that they are already part of the bank system. (Jennifer)
15. A bank customer should be able to view the all the names of their accounts and the account type. (Jennifer)
16. A bank customer should be able to set a PIN for their account when creating a bank account. (Grace)
17. A bank customer should be able to change their PIN for their account after date of birth verification. (Grace)
18. A bank customer should be required to write their PIN correctly before being able to access an account. (Jordan)
19. A bank administrator should be able to add an interest payment to all existing savings accounts at once. (Jordan)

### Iteration 3
20. A bank customer should be able to apply for a loan with their full name, date of birth, income, loan amount, and duration of loan. (Tanuja)
21. A bank customer should be able to see if their requested loan was approved or denied soon after submitting a loan request. (Tanuja)
22. A bank administrator should be able to log in with a valid employee ID and password. (Jennifer)
23. A bank administrator can see all the customers at the bank. (Jennifer)
24. A bank customer should be able to choose an account to deposit an approved loan amount. (Grace)
25. A bank customer should be able to transfer funds to another bank customer's primary account. (Grace)
26. A bank administrator should be able to unfreeze a bank account. (Jordan)
27. A bank administrator should be able to freeze any account, preventing withdrawals and transfers. (Jordan)

## What user stories do you intend to complete next iteration?
N/A. Iteration 3 is the last!

## Is there anything that you implemented but doesn't currently work?
From our testing, everything works, but we look forward to receiving feedback.

## What commands are needed to compile and run your code from the command line?

To access admin
- username: admin
- password: adminpass

To run program:
- ./runApp.sh

Which contains the commands
- cd src
- javac main/*.java && java main.StartupPage
