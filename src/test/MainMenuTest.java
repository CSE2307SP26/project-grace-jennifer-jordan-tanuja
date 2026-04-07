package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
    UserProfile user = new UserProfile("testUser", "testPass", "test@email.com", "01/01/1990", "000000");
    return new MainMenu(user);
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
}
