package test;

import org.junit.After;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;

import main.MainMenu;

public class MainMenuTest {
  private final PrintStream originalOut = System.out;

  @After
  public void restoreStreams() { // https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
    System.setOut(originalOut);
  }

  @Test
  public void testAddingNewAccountValid() {

    String input = "newAccount\n"; // https://stackoverflow.com/questions/31635698/junit-testing-for-user-input-using-scanner
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    MainMenu menu = new MainMenu();
    menu.createAdditionalAccount();

    assertEquals(new HashSet<>(Arrays.asList("primary", "newAccount")), menu.getAllAccountNames());
  }

  @Test
  public void testAddingNewAccountInvalidThenValid() {
    String input = "primary\nsecondTry";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    MainMenu menu = new MainMenu();
    menu.createAdditionalAccount();

    String printed = output.toString();
    assertTrue(printed.contains("primary already exists. Enter a unique name for your new account: "));
    assertTrue(printed.contains("Successfully created new account with name: secondTry"));

    assertEquals(new HashSet<>(Arrays.asList("primary", "secondTry")), menu.getAllAccountNames());
  }
}