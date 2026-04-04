package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;

import main.StartupPage;
import main.UserProfile;

public class StartupPageTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testCreateAccountSuccessfully() {
        String input = "alice\npassword123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        StartupPage startup = new StartupPage();
        startup.createAccount();

        assertTrue(startup.getUsers().containsKey("alice"));
        assertTrue(startup.getUsers().get("alice").checkPassword("password123"));
    }

    @Test
    public void testCreateAccountDuplicateUsernameThenValidUsername() {
        String input = "alice\npassword123\nalice\nbob\nnewpass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StartupPage startup = new StartupPage();

        startup.createAccount(); // creates alice
        startup.createAccount(); // tries alice again, then uses bob

        assertTrue(output.toString().contains("That username already exists. Enter a different username: "));
        assertTrue(startup.getUsers().containsKey("alice"));
        assertTrue(startup.getUsers().containsKey("bob"));
        assertTrue(startup.getUsers().get("bob").checkPassword("newpass"));
    }

    @Test
    public void testLoginSuccessful() {
        String input = "alice\npassword123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StartupPage startup = new StartupPage();
        startup.getUsers().put("alice", new UserProfile("alice", "password123"));

        startup.login();

        assertTrue(output.toString().contains("Login successful."));
    }

    @Test
    public void testLoginFailsWrongPassword() {
        String input = "alice\nwrongpass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StartupPage startup = new StartupPage();
        startup.getUsers().put("alice", new UserProfile("alice", "password123"));

        startup.login();

        assertTrue(output.toString().contains("Invalid username or password."));
    }

    @Test
    public void testLoginFailsUsernameDoesNotExist() {
        String input = "fakeuser\npassword123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StartupPage startup = new StartupPage();
        startup.login();

        assertTrue(output.toString().contains("Invalid username or password."));
    }

    @Test
    public void testDisplayOptionsPrintsMenu() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StartupPage startup = new StartupPage();
        startup.displayOptions();

        String printed = output.toString();
        assertTrue(printed.contains("Welcome to the 237 Bank App!"));
        assertTrue(printed.contains("1. Create an account"));
        assertTrue(printed.contains("2. Login"));
        assertTrue(printed.contains("3. Exit"));
    }

    @Test
    public void testGetUserSelectionValidInput() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        StartupPage startup = new StartupPage();
        int selection = startup.getUserSelection();

        assertEquals(2, selection);
    }

    @Test
    public void testGetUserSelectionInvalidThenValidInput() {
        String input = "7\n0\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        StartupPage startup = new StartupPage();
        int selection = startup.getUserSelection();

        assertEquals(2, selection);
    }
}