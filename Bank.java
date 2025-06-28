package com.jc;

public class Bank {
		BankAccount[] accounts = new BankAccount[100];
		int count = 0;
		
		void createAccount(String accHolderName, double balance, int pin) {
			accounts[count++] = new BankAccount(accHolderName, balance, pin);
			System.out.println("Account created successfully");
			System.out.println("Account number: " + accounts[count - 1].getAccNo());
		}

			BankAccount findAccount(int accNo) {
				for (int i = 0; i < count; i++) {
					if (accounts[i].getAccNo() == accNo) {
						return accounts[i];
					}
				}
				System.out.println("Account not found");
				return null;
			}

			void deposit(int accNo, double amount) {
				BankAccount acc = findAccount(accNo);
				if (acc != null) {
					acc.deposit(amount);
				}
			}

			void withdraw(int accNo, double amount, int pin) {
				BankAccount acc = findAccount(accNo);
				if (acc != null) {
					acc.withdraw(amount, pin);
				}
			}

			void displayInfo(int accNo, int pin) {
				BankAccount acc = findAccount(accNo);
				if (acc != null) {
					acc.displayAccountInfo(pin);
				}
			}

			void updatePin(int accNo, int oldPin, int newPin) {
				BankAccount acc = findAccount(accNo);
				if (acc != null) {
					acc.updatePin(oldPin, newPin);
				}
			}

}
