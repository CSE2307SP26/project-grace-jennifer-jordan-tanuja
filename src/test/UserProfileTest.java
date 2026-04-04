package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Arrays;

import org.junit.Test;

import main.UserProfile;

public class UserProfileTest {

    @Test
    public void testUserProfileConstructorSetsUsername() {
        UserProfile user = new UserProfile("alice", "password123");

        assertEquals("alice", user.getUsername());
    }

    @Test
    public void testUserProfileConstructorCreatesPrimaryAccount() {
        UserProfile user = new UserProfile("alice", "password123");

        assertEquals(new HashSet<>(Arrays.asList("primary")), user.getAccountNames());
    }

    @Test
    public void testCheckPasswordCorrectPassword() {
        UserProfile user = new UserProfile("alice", "password123");

        assertTrue(user.checkPassword("password123"));
    }

    @Test
    public void testCheckPasswordIncorrectPassword() {
        UserProfile user = new UserProfile("alice", "password123");

        assertFalse(user.checkPassword("wrongPassword"));
    }

    @Test
    public void testAccountsMapInitiallyContainsPrimaryOnly() {
        UserProfile user = new UserProfile("alice", "password123");

        assertTrue(user.getAccounts().containsKey("primary"));
        assertEquals(1, user.getAccounts().size());
    }
}