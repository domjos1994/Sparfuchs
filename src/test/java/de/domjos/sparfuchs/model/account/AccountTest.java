package de.domjos.sparfuchs.model.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void init() {
        this.account = new Account();
        account.startAmount.setValue(0.0);
    }

    @Test
    public void testAccount() {
        Transaction transaction = new Transaction();
        transaction.amount.setValue(200.0);
        this.account.transactions.add(transaction);
        assertEquals(200.0, this.account.endAmount.get());

        transaction = new Transaction();
        transaction.amount.setValue(-50.0);
        this.account.transactions.add(transaction);
        assertEquals(150.0, this.account.endAmount.get());
    }
}
