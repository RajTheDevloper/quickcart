package com.quickcart.quickcart.playground;

abstract class Payment {
    protected double amount;

    public Payment(double amount){
        this.amount = amount;
    }

    public void printReceipt() {
        System.out.println("----receipt----");
        System.out.println("Amount Paid: $" + amount);
    }

    abstract void processPayment();
}

interface Refundable {
    void processRefund();
}


class CreditCardPayment extends Payment {
  private String cardNumber;

  public CreditCardPayment(double amount, String cardNumber){
    super(amount);
    this.cardNumber = cardNumber;
  }

  @Override
  void processPayment(){
    System.out.println("processing $" + amount + " securly via credit card: " + cardNumber);
  }
}

class UPIPayment extends Payment implements Refundable {
    private String upiId;

    public UPIPayment(double amount, String upiId) {
        super(amount);
        this.upiId = upiId;
    }

    @Override
    void processPayment() {
        System.out.println("Processing $" + amount + " instantly via UPI: " + upiId);
    }

    @Override
    public void processRefund() {
        System.out.println("Refunding $" + amount + " back to UPI ID: " + upiId);
    }
}

public class Main {
    public static void main(String[] args) {
      Payment cart1 = new CreditCardPayment(150.00, "4111-2222-3333-4444");
        Payment cart2 = new UPIPayment(45.50, "quickcart@ybl");

        // Storing them in a generic array
        Payment[] payments = {cart1, cart2};

        // Polymorphic loop: We call methods without needing to know 
        // whether it's a Credit Card or UPI payment beforehand!
        for (Payment p : payments) {
            p.printReceipt();   // Calls abstract class method
            p.processPayment(); // Automatically calls the correct subclass version (Dynamic Dispatch)
            
            // Checking if the payment also implements the Refundable contract
            if (p instanceof Refundable) {
                ((Refundable) p).processRefund();
            }
            System.out.println("====================");
        }
    }
}