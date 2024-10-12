package student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class Borrows {

    private Patron patron;
    private Book book;
    private Date borrowDate;
    private Date dueDate;

    // Constructor
    public Borrows(Patron patron, Book book, Date borrowDate) {
        this.patron = patron;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = calculateDueDate(borrowDate);
    }

    // Getters
    public Patron getPatron() {
        return patron;
    }

    public Book getBook() {
        return book;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    // Check if the book is overdue
    public boolean isOverdue() {
        Date today = new Date();
        return today.after(dueDate);  // true if today is after the due date
    }

    // Calculate the due date (21 days after the borrow date)
    private Date calculateDueDate(Date borrowDate) {
        long dueTimeInMillis = borrowDate.getTime() + (21L * 24 * 60 * 60 * 1000); // 21 days in milliseconds
        return new Date(dueTimeInMillis);
    }

    // Method to load borrows from CSV
    public static List<Borrows> loadBorrowsFromCSV(String filePath, List<Patron> patrons, List<Book> books) {
        List<Borrows> borrows = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Check if the line contains exactly 4 fields (patronID, ISBN, borrowDate, dueDate)
                if (values.length == 4) {
                    String patronID = values[0].trim();
                    String isbn = values[1].trim();
                    Date borrowDate = sdf.parse(values[2].trim());
                    Date dueDate = sdf.parse(values[3].trim());

                    // Find the corresponding patron and book
                    Patron patron = findPatronByID(patronID, patrons);
                    Book book = findBookByISBN(isbn, books);

                    if (patron != null && book != null) {
                        Borrows borrow = new Borrows(patron, book, borrowDate);
                        borrow.calculateDueDate(dueDate);  // Set due date
                        borrows.add(borrow);
                    }
                }
            }
        } catch (IOException | java.text.ParseException e) {
            System.err.println("Error reading the borrow list file: " + e.getMessage());
        }

        return borrows;
    }
    
    // Helper method to find a patron by ID
    private static Patron findPatronByID(String patronID, List<Patron> patrons) {
        for (Patron patron : patrons) {
            if (patron.getPatronID().equals(patronID)) {
                return patron;
            }
        }
        return null; // Not found
    }
    
    // Helper method to find a book by ISBN
    private static Book findBookByISBN(String isbn, List<Book> books) {
        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null; // Not found
    }
    
    // Method to add borrow details to the CSV file without returnDate
    public static void addBorrowToCSV(String filePath, Borrows borrow) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            // Construct the CSV line without returnDate
            String line = borrow.getPatron().getPatronID() + "," + borrow.getBook().getISBN() + "," +
                          sdf.format(borrow.getBorrowDate()) + "," + sdf.format(borrow.getDueDate());
            
            bw.write(line);
            bw.newLine(); // Add a newline after the record
        } catch (IOException e) {
            System.err.println("Error writing to the borrowed books file: " + e.getMessage());
        }
    }
    
    // Method to delete a borrow record from CSV based on patronID and ISBN
    public static void deleteBorrowRecord(String filePath, String patronID, String isbn) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 4) {
                    String recordPatronID = values[0].trim();
                    String recordISBN = values[1].trim();

                    // If the record matches the patronID and ISBN, skip adding it to the updated list
                    if (recordPatronID.equals(patronID) && recordISBN.equals(isbn)) {
                        // This is the record to be deleted, so skip it
                        continue;
                    }

                    // If not a match, add the line to the updated list
                    updatedLines.add(line);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the borrowed books file: " + e.getMessage());
        }

        // Rewrite the CSV file with the updated list of borrow records
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the borrowed books file: " + e.getMessage());
        }
    }

    // To display borrowing details
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Patron: " + patron.getName() + ", Book: " + book.getTitle() + ", Borrow Date: " + sdf.format(borrowDate)
               + ", Due Date: " + sdf.format(dueDate);
    }


}
