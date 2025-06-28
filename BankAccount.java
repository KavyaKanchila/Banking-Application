package com.jc;

public class BankAccount {

		private int accNo;
		private String accHolderName;
		private double balance;
		private static int accNoGen = 10001;
		private int pin;

		public BankAccount(String accHolderName, double balance, int pin) {
			this.accHolderName = accHolderName;
			this.balance = balance;
			this.pin = pin;
			this.accNo = accNoGen++;
		}

		public int getAccNo() {
			return accNo;
		}

		public String getAccHolderName() {
			return accHolderName;
		}

		public double getBalance(int upin) {
			if (pin == upin) {
				return balance;
			} 
			else {
				System.out.println("Invalid PIN");
				return -1;
			}
		}

		public void deposit(double amt) {
			if (amt > 0) {
				balance += amt;
				System.out.println("Deposit success of amount: " + amt);
			} else {
				System.out.println("Invalid deposit amount");
			}
		}

		public void withdraw(double amt, int upin) {
			if (pin == upin) {
				if (amt <= balance && amt > 0) {
					balance -= amt;
					System.out.println("Withdraw success of amount: " + amt);
				} else {
					System.out.println("Invalid amount / Insufficient funds");
				}
			} else {
				System.out.println("Invalid PIN");
			}
		}

		public void displayAccountInfo(int upin) {
			if (pin == upin) {
				System.out.println("Account Number: " + accNo);
				System.out.println("Account Holder: " + accHolderName);
				System.out.println("Balance: " + balance);
			} else {
				System.out.println("Invalid PIN. Access denied.");
			}
		}

		public void updatePin(int oldPin, int newPin) {
			if (this.pin == oldPin) {

				this.pin = newPin;
				System.out.println("PIN updated successfully");

			} else {
				System.out.println("Incorrect old PIN");
			}
		}
	}

