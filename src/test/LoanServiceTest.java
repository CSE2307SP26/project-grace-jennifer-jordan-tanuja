package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.BankAccount;
import main.LoanService;
import main.UserProfile;

public class LoanServiceTest {

    private UserProfile createAdultUser() {
        return new UserProfile("testUser", "testPass", "test@email.com", "01/01/1990", "000000");
    }

    private UserProfile createMinorUser() {
        return new UserProfile("minorUser", "testPass", "minor@email.com", "01/01/2012", "000000");
    }

    @Test
    public void testLoanApproved() {
        UserProfile user = createAdultUser();

        String result = LoanService.evaluateLoanDecision(user, 60000, 10000, 36);

        assertEquals("Loan Approved!", result);
    }

    @Test
    public void testLoanDeniedApplicantUnder18() {
        UserProfile user = createMinorUser();

        String result = LoanService.evaluateLoanDecision(user, 60000, 10000, 36);

        assertEquals("Loan Denied: Applicant must be at least 18 years old.", result);
    }

    @Test
    public void testLoanDeniedAmountTooSmall() {
        UserProfile user = createAdultUser();

        String result = LoanService.evaluateLoanDecision(user, 60000, 200, 12);

        assertEquals("Loan Denied: Loan amount is too small.", result);
    }

    @Test
    public void testLoanDeniedAmountTooLarge() {
        UserProfile user = createAdultUser();

        String result = LoanService.evaluateLoanDecision(user, 60000, 1_500_000, 60);

        assertEquals("Loan Denied: Loan amount exceeds the maximum allowed.", result);
    }

    @Test
    public void testLoanDeniedTooHighComparedToIncome() {
        UserProfile user = createAdultUser();

        String result = LoanService.evaluateLoanDecision(user, 50000, 500000, 60);

        assertEquals("Loan Denied: Loan amount is too high compared to your income.", result);
    }

    @Test
    public void testLoanDeniedMonthlyPaymentTooHigh() {
        UserProfile user = createAdultUser();

        String result = LoanService.evaluateLoanDecision(user, 60000, 20000, 6);

        assertEquals("Loan Denied: Monthly payment would be too high based on your income.", result);
    }

    @Test
    public void testLoanDeniedNegativePrimaryBalance() {
        UserProfile user = createAdultUser();
        BankAccount primary = user.getAccounts().get("primary");
        primary.adminWithdraw(100);

        String result = LoanService.evaluateLoanDecision(user, 60000, 10000, 36);

        assertEquals("Loan Denied: Existing account balance indicates too much current debt.", result);
    }

    @Test
    public void testLoanDeniedInvalidDobOnFile() {
        UserProfile user = new UserProfile("badDobUser", "testPass", "bad@email.com", "not-a-date", "000000");

        String result = LoanService.evaluateLoanDecision(user, 60000, 10000, 36);

        assertEquals("Loan Denied: Invalid date of birth on file.", result);
    }
}