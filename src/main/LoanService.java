package main;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LoanService {
    public static String evaluateLoanDecision(UserProfile user, double income, double loanAmount, int durationMonths,
            BankAccount primaryAccount) {

        int age = calculateAgeFromDob(user.getDob());

        if (age < 0) {
            return "Loan Denied: Invalid date of birth on file.";
        }

        if (age < 18) {
            return "Loan Denied: Applicant must be at least 18 years old.";
        }

        if (loanAmount < 500) {
            return "Loan Denied: Loan amount is too small.";
        }

        if (loanAmount > 1_000_000) {
            return "Loan Denied: Loan amount exceeds the maximum allowed.";
        }

        if (loanAmount > income * 5) {
            return "Loan Denied: Loan amount is too high compared to your income.";
        }

        double monthlyIncome = income / 12.0;
        double monthlyPayment = loanAmount / durationMonths;

        if (monthlyPayment > monthlyIncome * 0.5) {
            return "Loan Denied: Monthly payment would be too high based on your income.";
        }

        if (primaryAccount != null && primaryAccount.getBalance() < 0) {
            return "Loan Denied: Existing account balance indicates too much current debt.";
        }

        return "Loan Approved!";
    }

    private static int calculateAgeFromDob(String dob) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate today = LocalDate.now();
            return Period.between(birthDate, today).getYears();
        } catch (DateTimeParseException e) {
            return -1;
        }
    }
}
