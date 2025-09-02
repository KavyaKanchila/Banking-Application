import java.sql.*;

public class Bank1 {
    
    // Database connection method
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load driver
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "yourpassword");
    }

    public class BankAccount {
        private int accNo;
        private String accHolderName;
        private double balance;
        private int pin;

        // Constructor sets fields from DB record
        public BankAccount(int accNo, String accHolderName, double balance, int pin) {
            this.accNo = accNo;
            this.accHolderName = accHolderName;
            this.balance = balance;
            this.pin = pin;
        }

        // Getters
        public int getAccNo() { return accNo; }
        public String getAccHolderName() { return accHolderName; }
        public double getBalance() { return balance; }
        public int getPin() { return pin; }

        // Deposit method updates DB balance
        public boolean deposit(double amount) {
            if (amount <= 0) return false;
            try (Connection con = getConnection()) {
                String sql = "UPDATE customer SET balance = balance + ? WHERE ac_no = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setInt(2, accNo);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    balance += amount;
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        // Withdraw with balance & pin check and update DB
        public boolean withdraw(double amount, int inputPin) {
            if (amount <= 0 || amount > balance || inputPin != pin) return false;
            try (Connection con = getConnection()) {
                String sql = "UPDATE customer SET balance = balance - ? WHERE ac_no = ? AND pass_code = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setInt(2, accNo);
                ps.setInt(3, pin);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    balance -= amount;
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        // Display account info if pin matches
        public void displayAccountInfo(int inputPin) {
            if (inputPin == pin) {
                System.out.println("Account Number: " + accNo);
                System.out.println("Account Holder: " + accHolderName);
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("Invalid PIN.");
            }
        }

        // Update pin if old pin matches, update DB
        public boolean updatePin(int oldPin, int newPin) {
            if (oldPin != pin) return false;
            try (Connection con = getConnection()) {
                String sql = "UPDATE customer SET pass_code = ? WHERE ac_no = ? AND pass_code = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, newPin);
                ps.setInt(2, accNo);
                ps.setInt(3, oldPin);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    pin = newPin;
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    // Create a new account - inserts record into DB
    public void createAccount(String accHolderName, double balance, int pin) {
        try (Connection con = getConnection()) {
            String sql = "INSERT INTO customer (cname, balance, pass_code) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, accHolderName);
            ps.setDouble(2, balance);
            ps.setInt(3, pin);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int accountNumber = rs.getInt(1);
                    System.out.println("Account created successfully. Account Number: " + accountNumber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Find account from DB and return BankAccount object or null
    public BankAccount findAccount(int accNo) {
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM customer WHERE ac_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("cname");
                double balance = rs.getDouble("balance");
                int pin = rs.getInt("pass_code");
                return new BankAccount(accNo, name, balance, pin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Account not found");
        return null;
    }

    // Deposit wrapper that finds account and deposits
    public void deposit(int accNo, double amount) {
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            if (acc.deposit(amount)) {
                System.out.println("Deposit successful.");
            } else {
                System.out.println("Deposit failed.");
            }
        }
    }

    // Withdraw wrapper that verifies pin and withdraws
    public void withdraw(int accNo, double amount, int pin) {
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            if (acc.withdraw(amount, pin)) {
                System.out.println("Withdrawal successful.");
            } else {
                System.out.println("Withdrawal failed. Check balance or PIN.");
            }
        }
    }

    // Display info wrapper verifying pin
    public void displayInfo(int accNo, int pin) {
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            acc.displayAccountInfo(pin);
        }
    }

    // Update PIN wrapper
    public void updatePin(int accNo, int oldPin, int newPin) {
        BankAccount acc = findAccount(accNo);
        if (acc != null) {
            if (acc.updatePin(oldPin, newPin)) {
                System.out.println("PIN updated successfully.");
            } else {
                System.out.println("PIN update failed. Old PIN incorrect.");
            }
        }
    }

    // Main method for quick testing
    public static void main(String[] args) {
        Bank1 bank = new Bank1();

    }
}

