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

public class TransferMoneyTests {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testTransferMoneySuccessfully() {
        String input = "savings\n100\nprimary\nsavings\n40\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MainMenu menu = new MainMenu();

        menu.createAdditionalAccount();
        menu.performDeposit();
        menu.transferBetweenAccounts(); // transfers 40 from primary to savings

        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenSourceAccountDoesNotExist() {
        String input = "fake\nprimary\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = new MainMenu();
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

        MainMenu menu = new MainMenu();
        menu.transferBetweenAccounts();

        assertTrue(output.toString().contains("Destination account does not exist."));
        assertEquals(new HashSet<>(Arrays.asList("primary")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenInsufficientFunds() {
        String input = "savings\nprimary\nsavings\n50\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = new MainMenu();

        menu.createAdditionalAccount();
        menu.transferBetweenAccounts(); // tries to transfer 50 from primary with balance 0

        assertTrue(output.toString().contains(
                "Transfer failed. Make sure the amount is valid and the source account has enough funds."));
        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }

    @Test
    public void testTransferFailsWhenAmountIsNegative() {
        String input = "savings\n100\nprimary\nsavings\n-20\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        MainMenu menu = new MainMenu();

        menu.createAdditionalAccount(); 
        menu.performDeposit(); 
        menu.transferBetweenAccounts(); // tries negative transfer

        assertTrue(output.toString().contains(
                "Transfer failed. Make sure the amount is valid and the source account has enough funds."));
        assertEquals(new HashSet<>(Arrays.asList("primary", "savings")), menu.getAllAccountNames());
    }
}