package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.Test;

import main.MainMenu;
import main.UserProfile;

public class MainMenuTest {

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setIn(originalIn);
  }

  private MainMenu createMenu() {
    HashMap<String, UserProfile> allUsers = new HashMap<>();
    UserProfile user = new UserProfile("testUser", "testPass", "test@email.com", "01/01/1990", "000000");
    UserProfile user2 = new UserProfile("testUser2", "testPass", "test@email.com", "01/01/1990", "000000");
    allUsers.put("testUser", user);
    allUsers.put("testUser2", user2);

    return new MainMenu(user, allUsers);
  }

  @Test
  public void testAddingNewSavingsAccountValid() {
    String input = "John Doe\n01/01/1990\nnewAccount\nsavings\n000000\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();

    assertEquals(new HashSet<>(Arrays.asList("primary", "newAccount")), menu.getAllAccountNames());
  }

  @Test
  public void testAddingNewAccountInvalidNameThenValid() {
    String input = "John Doe\n01/01/1990\nprimary\nsecondTry\nchecking\n000000\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();

    String printed = output.toString();
    assertTrue(printed.contains("primary already exists. Enter a unique name for your new account: "));
    assertTrue(printed.contains("Successfully created new checking account with name: secondTry"));

    assertEquals(new HashSet<>(Arrays.asList("primary", "secondTry")), menu.getAllAccountNames());
  }

  @Test
  public void testAddingNewAccountInvalidTypeThenValid() {
    String input = "John Doe\n01/01/1990\nvacationFund\nbanana\nsavings\n000000\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();

    String printed = output.toString();
    assertTrue(printed.contains("Invalid account type. Please enter savings, checking, or credit: "));
    assertTrue(printed.contains("Successfully created new savings account with name: vacationFund"));

    assertEquals(new HashSet<>(Arrays.asList("primary", "vacationFund")), menu.getAllAccountNames());
  }

  // account type tests
  @Test
  public void testCreateSavingsAccountType() {
    String input = "John Doe\n01/01/1990\nvacationFund\nsavings\n000000\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();

    assertEquals("savings",
        menu.getAccounts().get("vacationFund").getAccountType());
  }

  @Test
  public void testInvalidAccountTypeThenValid() {
    String input = "John Doe\n01/01/1990\ntrip\nbanana\nsavings\n000000\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();

    assertEquals("savings",
        menu.getAccounts().get("trip").getAccountType());
  }

  @Test
  public void testAddingNewAccountFailsDobMismatch() {
    String input = "John Doe\n12/31/1999\nnewAccount\nsavings\n000000\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();

    assertTrue(output.toString().contains("Date of birth does not match our records. Account creation failed."));
    assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
  }

  @Test
  public void testCloseExistingAccountSuccessfully() {
    String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nsavings\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();
    menu.closeExistingAccount();

    assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
  }

  @Test
  public void testCloseNonexistentAccount() {
    String input = "fakeAccount\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.closeExistingAccount();

    assertTrue(output.toString().contains("That account does not exist."));
    assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
  }

  @Test
  public void testCannotClosePrimaryAccount() {
    String input = "primary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.closeExistingAccount();

    assertTrue(output.toString().contains("The primary account cannot be closed."));
    assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
  }

  @Test
  public void testCannotCloseAccountWithNonZeroBalance() {
    String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\nsavings\n50\nsavings\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();

    menu.createAdditionalAccount();
    menu.getAccounts().get("primary").deposit(50);
    menu.transferBetweenAccounts();
    menu.closeExistingAccount();

    assertTrue(output.toString().contains("The account balance must be 0 before closing the account."));
    assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
  }

  @Test
  public void testTransferMoneySuccessfully() {
    String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\nsavings\n40\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    MainMenu menu = createMenu();

    menu.createAdditionalAccount();
    menu.getAccounts().get("primary").deposit(100);
    menu.transferBetweenAccounts();

    assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
  }

  @Test
  public void testTransferFailsWhenSourceAccountDoesNotExist() {
    String input = "fake\nprimary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.transferBetweenAccounts();

    assertTrue(output.toString().contains("Source account does not exist."));
    assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
  }

  @Test
  public void testTransferFailsWhenDestinationAccountDoesNotExist() {
    String input = "primary\nfake\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.transferBetweenAccounts();

    assertTrue(output.toString().contains("Destination account does not exist."));
    assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
  }

  @Test
  public void testTransferFailsWhenInsufficientFunds() {
    String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\nsavings\n50\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();

    menu.createAdditionalAccount();
    menu.transferBetweenAccounts();

    assertTrue(output.toString().contains("Transfer failed."));
    assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
  }

  @Test
  public void testTransferFailsWhenAmountIsNegative() {
    String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\nsavings\n-20\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();

    menu.createAdditionalAccount();
    menu.getAccounts().get("primary").deposit(100);
    menu.transferBetweenAccounts();

    assertTrue(output.toString().contains("Transfer failed."));
    assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
  }

  @Test
  public void testTransferMoneyToAnotherUserSuccessfully() {
    String input = "\ntestUser2\nprimary\n50\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();

    menu.getAccounts().get("primary").deposit(100);
    menu.transferToAnotherUser();

    assertTrue(output.toString().contains("Transfer successful"));
  }

  @Test
  public void testCorrectPinOpensAccountMenu() {
    String input = "primary\n000000\n6\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    MainMenu menu = createMenu();
    menu.selectAccount();
    assertTrue(output.toString().contains("Currently in account primary"));
  }

  @Test
  public void testIncorrectPinPreventsAccess() {
    String input = "primary\n123456\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    MainMenu menu = createMenu();
    menu.selectAccount();
    assertTrue(output.toString().contains("Incorrect PIN. Access denied."));
  }

  // apply for loan tests
  @Test
  public void testApplyForLoanSuccessfully() {
    String input = "\nJohn Doe\n01/01/1990\n50000\n10000\n36\nprimary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Approved!"));
  }

  @Test
  public void testApplyForLoanFailsDobMismatch() {
    String input = "\nJohn Doe\n12/31/1999\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Date of birth does not match our records."));
    assertTrue(printed.contains("Loan application failed."));
  }

  @Test
  public void testApplyForLoanInvalidIncomeThenValid() {
    String input = "\nJohn Doe\n01/01/1990\nabc\n50000\n10000\n36\nprimary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Invalid input. Please enter a valid number for income."));
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Approved!"));
  }

  @Test
  public void testApplyForLoanInvalidLoanAmountThenValid() {
    String input = "\nJohn Doe\n01/01/1990\n50000\nabc\n10000\n36\nprimary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Invalid input. Please enter a valid number for loan amount."));
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Approved!"));
  }

  @Test
  public void testApplyForLoanInvalidDurationThenValid() {
    String input = "\nJohn Doe\n01/01/1990\n50000\n10000\nabc\n36\nprimary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Invalid input. Please enter a valid whole number for loan duration."));
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Approved!"));
  }

  @Test
  public void testApplyForLoanDeniedForLargeLoanComparedToIncome() {
    String input = "\nJohn Doe\n01/01/1990\n50000\n500000\n60\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Denied: Loan amount is too high compared to your income."));
  }

  @Test
  public void testApplyForLoanDeniedForHighMonthlyPayment() {
    String input = "\nJohn Doe\n01/01/1990\n100000\n500000\n60\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Denied: Monthly payment would be too high based on your income."));
  }

  @Test
  public void testLoanWasDepositedToSelectedAccount() {
    String input = "\nJohn Doe\n01/01/1990\n50000\n10000\n36\nprimary\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();

    menu.applyForLoan();

    String printed = output.toString();
    assertTrue(printed.contains("Loan application submitted successfully."));
    assertTrue(printed.contains("Loan Approved!"));
    assertTrue(printed.contains("Successfully deposited loan to primary"));
  }

  @Test
  public void testTransferFailsWhenSourceAccountFrozen() {
    String input = "John Doe\n01/01/1990\nsavings\nsavings\n000000\nprimary\nsavings\n40\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = createMenu();
    menu.createAdditionalAccount();
    menu.getAccounts().get("primary").deposit(100);
    menu.getAccounts().get("primary").freeze();
    menu.transferBetweenAccounts();

    assertTrue(output.toString().contains("Source account is frozen. Transfer failed."));
  }
}