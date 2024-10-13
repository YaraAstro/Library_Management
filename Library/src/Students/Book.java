package Students;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Book {  

    private String title;  
    private String author;  
    private String ISBN;  
    private boolean availability;  

    // Constructor  
    public Book(String title, String author, String ISBN, boolean availability) {  
        this.title = title;  
        this.author = author;  
        this.ISBN = ISBN;  
        this.availability = availability;  
    }  

    // Getters  
    public String getTitle() { return title; }  
    public String getAuthor() { return author; }  
    public String getISBN() { return ISBN; }  
    public boolean isAvailable() { return availability; }  

    // Setters  
    public void setAvailability(boolean availability) {  
        this.availability = availability;  
    }

    // Method to load books from CSV file 
    public static List<Book> loadBooksFromCSV(String filePath) { 
        List<Book> books = new ArrayList<>(); 

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { 
            String line; 
            while ((line = br.readLine()) != null) { 
                String[] values = line.split(","); 
                if (values.length == 4) { 
                    String title = values[0]; 
                    String author = values[1]; 
                    String ISBN = values[2]; 
                    boolean availability = Boolean.parseBoolean(values[3]); 
                    Book book = new Book(title, author, ISBN, availability); 
                    books.add(book); 
                } 
            } 
        } catch (IOException e) { 
            System.err.println("Error reading the book list file: " + e.getMessage()); 
        } 
        return books; 
    } 
    
    // Method to add a new book to the CSV file
    public static void addBookToCSV(String filePath, Book book) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = book.getTitle() + "," + book.getAuthor() + "," + book.getISBN() + "," + book.isAvailable();
            bw.write(line);
            bw.newLine(); // Add a newline after the record
        } catch (IOException e) {
            System.err.println("Error writing to the book list file: " + e.getMessage());
        }
    } 
    
    // Method to delete a book from the CSV file based on ISBN
    public static void deleteBookByISBN(String filePath, String ISBN) {
        List<Book> books = loadBooksFromCSV(filePath);
        boolean bookFound = false;

        // Remove the book with the matching ISBN
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getISBN().equals(ISBN)) {
                books.remove(i);
                bookFound = true;
                break;
            }
        }

        if (bookFound) {
            // Overwrite the CSV file with the updated list of books
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (Book book : books) {
                    String line = book.getTitle() + "," + book.getAuthor() + "," + book.getISBN() + "," + book.isAvailable();
                    bw.write(line);
                    bw.newLine(); // Add a newline after each record
                }
                System.out.println("Book with ISBN " + ISBN + " was successfully deleted.");
            } catch (IOException e) {
                System.err.println("Error writing to the book list file: " + e.getMessage());
            }
        } else {
            System.out.println("Book with ISBN " + ISBN + " was not found.");
        }
    }

    // Method to update book's availability in the CSV file
    public static void updateBookAvailabilityInCSV(String filePath, Book updatedBook) {
        List<Book> books = loadBooksFromCSV(filePath);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                if (book.getISBN().equals(updatedBook.getISBN())) {
                    // Update the availability of the matching book
                    book.setAvailability(updatedBook.isAvailable());
                }
                String line = book.getTitle() + "," + book.getAuthor() + "," + book.getISBN() + "," + book.isAvailable();
                bw.write(line);
                bw.newLine(); // Write the updated or unchanged book information
            }
        } catch (IOException e) {
            System.err.println("Error updating book list file: " + e.getMessage());
        }
    }
    
    // To display book information  
    @Override  
    public String toString() {  
        return "Title: " + title + ", Author: " + author + ", ISBN: " + ISBN + ", Available: " + availability;  
    } 
}
