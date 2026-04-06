package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import main.BankAccount;
import main.UserProfile;

public class UserProfileTest {
    private UserProfile user;

    @Before
    public void setup() {
        this.user = new UserProfile("alice", "password123", "000000");
    }

    @Test
    public void testUserProfileConstructorSetsUsername() {
        assertEquals("alice", user.getUsername());
    }

    @Test
    public void testUserProfileConstructorCreatesPrimaryAccount() {
        assertEquals(new HashSet<>(Arrays.asList("primary")), user.getAccountNames());
    }

    @Test
    public void testCheckPasswordCorrectPassword() {
        assertTrue(user.checkPassword("password123"));
    }

    @Test
    public void testCheckPasswordIncorrectPassword() {
        assertFalse(user.checkPassword("wrongPassword"));
    }

    @Test
    public void testAccountsMapInitiallyContainsPrimaryOnly() {
        assertTrue(user.getAccounts().containsKey("primary"));
        assertEquals(1, user.getAccounts().size());
    }
}