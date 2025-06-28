package com.jc;

public class TestBank {

			public static void main(String[] args) {
				Bank b = new Bank();

				// Create accounts
				b.createAccount("ABC", 5000, 1234);
				b.createAccount("XYZ", 15000, 2345);

				// Try storing the account numbers
				int acc1 = b.accounts[0].getAccNo();
				int acc2 = b.accounts[1].getAccNo();

				// Deposit
				b.deposit(acc1, 2000);

				// Withdraw
				b.withdraw(acc1, 1000, 1234);    // Valid
				b.withdraw(acc1, 500, 1111);     // Invalid PIN

				// Display Info
				b.displayInfo(acc1, 1234);       // Valid
				b.displayInfo(acc2, 1111);       // Invalid PIN

				// Update PIN
				b.updatePin(acc1, 1234, 5678);   // Success
				b.displayInfo(acc1, 5678);       // Use new PIN
			}
	}

