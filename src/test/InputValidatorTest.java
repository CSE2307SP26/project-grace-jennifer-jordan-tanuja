package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import main.InputValidator;

public class InputValidatorTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testGetUserSelectionValidInput() {
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int result = InputValidator.getUserSelection(scanner, 5);
        assertEquals(3, result);
    }

    @Test
    public void testGetUserSelectionInvalidThenValidInput() {
        String input = "0\n6\ntext\n4\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        int result = InputValidator.getUserSelection(scanner, 5);
        assertEquals(4, result);

        String output = out.toString();
        assertTrue(output.contains("This input is invalid. Please select a number from 1-5"));
    }

    @Test
    public void testGetValidDoubleInvalidThenValid() {
        String input = "-5.5\n0\nnot-a-number\n25.5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        double result = InputValidator.getValidDouble(scanner, "Enter amount: ", "Amount");
        assertEquals(25.5, result, 0.001);

        String output = out.toString();
        assertTrue(output.contains("Amount must be greater than 0."));
        assertTrue(output.contains("Invalid input. Please enter a valid number for amount."));
    }

    @Test
    public void testGetValidIntInvalidThenValid() {
        String input = "-10\n0\nbad-input\n15\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        int result = InputValidator.getValidInt(scanner, "Enter duration: ", "Duration");
        assertEquals(15, result);

        String output = out.toString();
        assertTrue(output.contains("Duration must be greater than 0."));
        assertTrue(output.contains("Invalid input. Please enter a valid whole number for duration."));
    }

    @Test
    public void testGetValidPinValidInput() {
        String input = "123456\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        String result = InputValidator.getValidPin(scanner, "Enter PIN: ");
        assertEquals("123456", result);
    }

    @Test
    public void testGetValidPinInvalidThenValid() {
        String input = "12345\n1234567\nabcdef\n654321\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        String result = InputValidator.getValidPin(scanner, "Enter PIN: ");
        assertEquals("654321", result);
        assertTrue(out.toString().contains("Pin number must have 6 numerical digits."));
    }
}