package Students;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.util.ArrayList; 
import java.util.List; 

public class Patron { 

    private String name; 
    private String patronID; 
    private int booksBorrowed; 

    // Constructor 
    public Patron(String name, String patronID, int booksBorrowed) { 
        this.name = name; 
        this.patronID = patronID; 
        this.booksBorrowed = booksBorrowed; 
    } 

    // Getters 
    public String getName() { return name; } 
    public String getPatronID() { return patronID; } 
    public int getBooksBorrowed() { return booksBorrowed; } 

    // Setters 
    public void setBooksBorrowed(int booksBorrowed) { this.booksBorrowed = booksBorrowed; } 

    // Increment books borrowed 
    public void incrementBooksBorrowed() { this.booksBorrowed++; } 

    // Decrement books borrowed 
    public void decrementBooksBorrowed() { if (this.booksBorrowed > 0) { this.booksBorrowed--; } }
    
    public boolean isEligibleForLoan(List<Borrows> borrowedBooks) {
        for (Borrows borrow : borrowedBooks) {
            // Check if the patron ID matches any record in the borrowed books list
            if (borrow.getPatron().getPatronID().equals(this.patronID)) {
                return false;  // Patron is not eligible if they have already borrowed a book
            }
        }
        return true;  // Patron is eligible if they haven't borrowed any book
    }

    // Method to load patrons from CSV file 
    public static List<Patron> loadPatronsFromCSV(String filePath) { 
        List<Patron> patrons = new ArrayList<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { 
            String line; 
            while ((line = br.readLine()) != null) { 
                String[] values = line.split(","); 
                if (values.length == 3) { 
                    String name = values[0]; 
                    String patronID = values[1]; 
                    int booksBorrowed = Integer.parseInt(values[2]); 
                    Patron patron = new Patron(name, patronID, booksBorrowed); 
                    patrons.add(patron); 
                } 
            } 
        } catch (IOException e) { 
            System.err.println("Error reading the patron list file: " + e.getMessage()); 
        } 
        return patrons; 
    }

    // Method to update the patron's booksBorrowed count in the CSV file
    public static void updatePatronInCSV(String filePath, Patron updatedPatron) {
        List<Patron> patrons = loadPatronsFromCSV(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Patron patron : patrons) {
                if (patron.getPatronID().equals(updatedPatron.getPatronID())) {
                    // Update the booksBorrowed count for the matching patron
                    patron.setBooksBorrowed(updatedPatron.getBooksBorrowed());
                }
                String line = patron.getName() + "," + patron.getPatronID() + "," + patron.getBooksBorrowed();
                bw.write(line);
                bw.newLine(); // Write the updated or unchanged patron information
            }
        } catch (IOException e) {
            System.err.println("Error updating patron list file: " + e.getMessage());
        }
    }
    
    // Method to add a new patron to the CSV file
    public static void addPatronToCSV(String filePath, Patron patron) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = patron.getName() + "," + patron.getPatronID() + "," + patron.getBooksBorrowed();
            bw.write(line);
            bw.newLine(); // Add a newline after the record
        } catch (IOException e) {
            System.err.println("Error writing to the patron list file: " + e.getMessage());
        }
    }

    // To display patron information 
    @Override 
    public String toString() { 
        return "Patron ID: " + patronID + ", Name: " + name + ", Books Borrowed: " + booksBorrowed; 
    } 
}
