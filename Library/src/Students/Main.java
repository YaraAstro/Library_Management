package Students;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class Main {

	 private static ArrayList<Book> books = new ArrayList<>();
	 private static ArrayList<Patron> patrons = new ArrayList<>();
	 private static ArrayList<Borrows> borrowedBooks = new ArrayList<>();

	 public static void main(String[] args) {
	    String patronFilePath = "./Library/data/patronList.csv";
	    String bookFilePath = "./Library/data/booklist.csv";
	    String borrowsFilePath = "./Library/data/borrowedBooks.csv";
	    // Load patrons and books from CSV files
	    loadPatrons(patronFilePath);
	    loadBooks(bookFilePath);
	    loadBorrows(borrowsFilePath);
	
	    // Start the main menu
	    showMainMenu();
	}

    private static void loadPatrons(String filePath) {
        // Load the list of patrons from the CSV file
        patrons = (ArrayList<Patron>) Patron.loadPatronsFromCSV(filePath);

        if (patrons.isEmpty()) {
            System.out.println("No patrons found or failed to load patrons.");
        } else {
            System.out.println("Patrons loaded successfully.");
//            System.out.println(patrons);
        }
    }

    private static void loadBooks(String filePath) {
        // Load books from the booklist.csv file into the books ArrayList
        books = (ArrayList<Book>) Book.loadBooksFromCSV(filePath);

        if (books.isEmpty()) {
            System.out.println("No books found or failed to load books.");
        } else {
            System.out.println("Books loaded successfully.");
//            System.out.println(books);
        }
    }
    
    private static void loadBorrows(String filePath) {
        borrowedBooks = (ArrayList<Borrows>) Borrows.loadBorrowsFromCSV(filePath, patrons, books);
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrows found or failed to load borrows.");
        } else {
            System.out.println("Borrows loaded successfully.");
        }
    }
    
    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
        	// clear screen before show main menu
        	System.out.println("\n\n============== LIBRARY MANAGEMENT SYSTEM ==============");
        	System.out.println("|                      MAIN MENU                      |");
        	System.out.println("-------------------------------------------------------");
        	System.out.println("|                  1 : Patron Management              |");
        	System.out.println("|                  2 : Book Management                |");
        	System.out.println("|                  3 : Loans Management               |");
        	System.out.println("|                  4 : Exit Program                   |");
        	System.out.println("-------------------------------------------------------");
        	System.out.print("\nPlease enter your choice (1/2/3/4) : ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showPatronManagementMenu();
                    break;
                case 2:
                    showBookManagementMenu();
                    break;
                case 3:
                    showLoansManagementMenu();
                    break;
                case 4:
                    exitProgram();
                    break;
                default:
                    System.out.println("\n/!\\   Invalid choice, please try again!\n\n");
            }
        } while (choice != 4);
        
        scanner.close();
    }

    private static void showPatronManagementMenu() {
    	 Scanner scanner = new Scanner(System.in);
    	    int choice;

    	    do {
    	    	System.out.println("\n\n============== LIBRARY MANAGEMENT SYSTEM ==============");
            	System.out.println("|               PATRON MANAGEMENT MENU                |");
    	    	System.out.println("-------------------------------------------------------");
    	    	System.out.println("|                  1 : Search Patron                  |");
    	    	System.out.println("|                  2 : Add Patron                     |");
    	    	System.out.println("|                  3 : Exit sub-menu                  |");
    	    	System.out.println("-------------------------------------------------------");
    	    	System.out.print("\nEnter your choice (1/2/3) : ");

    	        choice = scanner.nextInt();
    	        scanner.nextLine(); // consume newline

    	        switch (choice) {
    	            case 1:
    	                searchPatron(scanner);
    	                break;
    	            case 2:
    	                addPatron(scanner);
    	                break;
    	            case 3:
    	                System.out.println("Exiting Patron Management Menu...");
    	                break;
    	            default:
    	                System.out.println("\n/!\\   Invalid choice. Please try again!\n\n");
    	        }
    	    } while (choice != 3);
    }
    
    private static void searchPatron(Scanner scanner) {
    	System.out.println("\n| SEARCH PATRON --------------");
        System.out.print("Enter Patron ID to search: ");
        String patronID = scanner.nextLine();

        // Find patron by ID
        for (Patron patron : patrons) {
            if (patron.getPatronID().equals(patronID)) {
                System.out.println("Patron Found: ");
                System.out.println(patron);

                // Display borrowed and overdue books for this patron
                System.out.println("\nBooks currently borrowed:");
                for (Borrows borrow : borrowedBooks) {
                    if (borrow.getPatron().getPatronID().equals(patronID)) {
                        Book book = borrow.getBook();
                        System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() + 
                                           ", ISBN: " + book.getISBN() + 
                                           ", Borrow Date: " + borrow.getBorrowDate() + 
                                           ", Due Date: " + borrow.getDueDate());

                        if (borrow.isOverdue()) {
                            System.out.println("** This book is overdue! **");
                        }
                    }
                }
                return;
            }
        }

        // If patron not found
        System.out.println("Patron with ID " + patronID + " not found.");
    }

    private static void addPatron(Scanner scanner) {
    	System.out.println("\n| ADD PATRON --------------");
        System.out.print("Enter Patron Name: ");
        String name = scanner.nextLine();

        // Generate a unique Patron ID in the format 10XX
        int nextId = patrons.size() + 1; // Get the next sequential number
        String newPatronID = String.format("10%02d", nextId); // Format to 10XX (e.g., 1001, 1002)

        // Create new patron and add to the in-memory list
        Patron newPatron = new Patron(name, newPatronID, 0);
        patrons.add(newPatron);

        // Save the new patron to the CSV file
        Patron.addPatronToCSV("./Library/data/patronList.csv", newPatron);

        System.out.println("New patron added successfully:");
        System.out.println(newPatron);
        
        // Optionally print the updated list (if desired)
        System.out.println("Updated Patron List:");
        for (Patron patron : patrons) {
            System.out.println(patron);
        }
    }
    
    private static void showBookManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
	    	System.out.println("\n\n============== LIBRARY MANAGEMENT SYSTEM ==============");
        	System.out.println("                BOOK MANAGEMENT MENU");
	    	System.out.println("-------------------------------------------------------");
	    	System.out.println("|                  1 : Check Book Availability        |");
	    	System.out.println("|                  2 : Add a Book                     |");
	    	System.out.println("|                  3 : Remove a Book                  |");
	    	System.out.println("|                  4 : View All Books                 |");
	    	System.out.println("|                  5 : Exit sub-menu                  |");
	    	System.out.println("-------------------------------------------------------");
	    	System.out.print("\nEnter your choice (1/2/3/4/5) : ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    checkBookAvailability(scanner);
                    break;
                case 2:
                    addBook(scanner);
                    break;
                case 3:
                    removeBook(scanner);
                    break;
                case 4:
                    viewAllBooks();
                    break;
                case 5:
                    System.out.println("Exiting Book Management Menu...");
                    break;
                default:
                    System.out.println("\n/!\\   Invalid choice. Please try again.\n\n");
            }
        } while (choice != 5);
    }
    
    // Method to check book availability by ISBN
    private static void checkBookAvailability(Scanner scanner) {
    	System.out.println("\n| CHECK BOOK AVAILABILITY --------------");
        System.out.print("Enter ISBN to check availability: ");
        String isbn = scanner.nextLine();

        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                System.out.println("Book Found: " + book);
                System.out.println("Available: " + (book.isAvailable() ? "Yes" : "No"));
                return;
            }
        }

        System.out.println("Book with ISBN " + isbn + " not found.");
    }
    
    // Method to add a new book
    private static void addBook(Scanner scanner) {
    	System.out.println("\n| ADD NEW BOOK --------------");
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();
        
        System.out.print("Enter Book ISBN: ");
        String ISBN = scanner.nextLine();
        
        System.out.print("Is the book available (t/f)? ");
        String input = scanner.next().trim().toLowerCase(); // Read input and normalize

        boolean availability;

        if (input.equals("t")) {
            availability = true;
        } else if (input.equals("f")) {
            availability = false;
        } else {
            System.out.println("/!\\  Invalid input. Please enter 't' for true or 'f' for false!\n"); 
            availability = true;
        }
        
        Book newBook = new Book(title, author, ISBN, availability);
        books.add(newBook);
        
        // Save the new book to the CSV file
        Book.addBookToCSV("./Library/data/booklist.csv", newBook);
        
        System.out.println("|*|  New book added successfully:");
        System.out.println(newBook);
    }
    
    // Method to remove a book by ISBN
    private static void removeBook(Scanner scanner) {
    	System.out.println("\n| REMOVE A BOOK --------------");
        System.out.print("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();

        // Search for the book in the in-memory list
        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                bookToRemove = book;
                break;
            }
        }

        // If the book is found, remove it from both the in-memory list and the CSV file
        if (bookToRemove != null) {
            books.remove(bookToRemove);
            System.out.println("|*|  Book removed from in-memory list.");
            
            // Remove the book from the CSV file as well
            Book.deleteBookByISBN("./Library/data/booklist.csv", isbn);
        } else {
            System.out.println("Book with ISBN " + isbn + " not found.");
        }
    }
    
    // Method to view all books
    private static void viewAllBooks() {
    	System.out.println("\n| VIEW ALL BOOKS --------------\n");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void showLoansManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
        	System.out.println("\n\n============== LIBRARY MANAGEMENT SYSTEM ==============");
        	System.out.println("                LOAN MANAGEMENT MENU");
	    	System.out.println("-------------------------------------------------------");
	    	System.out.println("|                  1 : Loan Book                      |");
	    	System.out.println("|                  2 : Return Book                    |");
	    	System.out.println("|                  3 : Books on Loan                  |");
	    	System.out.println("|                  4 : Overdue Books                  |");
	    	System.out.println("|                  5 : Exit sub-menu                  |");
	    	System.out.println("-------------------------------------------------------");
	    	System.out.print("\nEnter your choice (1/2/3/4/5) : ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    loanBook(scanner);
                    break;
                case 2:
                    returnBook(scanner);
                    break;
                case 3:
                    showBooksOnLoan();
                    break;
                case 4:
                    showOverdueBooks();
                    break;
                case 5:
                    System.out.println("Exiting Loans Management Menu...");
                    break;
                default:
                    System.out.println("\n/!\\   Invalid choice. Please try again.\n\n");
            }
        } while (choice != 5);
    }
    
    private static void loanBook(Scanner scanner) {
    	System.out.println("\n| LOAN A BOOK --------------");
        System.out.print("Enter Patron ID: ");
        String patronID = scanner.nextLine();

        // Search for Patron
        Patron patron = null;
        for (Patron p : patrons) {
            if (p.getPatronID().equals(patronID)) {
                patron = p;
                break;
            }
        }

        if (patron == null) {
            System.out.println("|!|  Patron not found!");
            return;
        }
        	
        // check for eligibility of patron
        if (!patron.isEligibleForLoan(borrowedBooks)) {
        	System.out.println("|!|  This Patron is't Eligible to borrow another book!");
            return;
        }

        // Check if patron has overdue books
        for (Borrows borrow : borrowedBooks) {
            if (borrow.getPatron().getPatronID().equals(patronID) && borrow.isOverdue()) {
                System.out.println("|!|  Patron has overdue books and cannot borrow until they are returned!");
                return;
            }
        }

        // Search for book by title and author
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();

        Book bookToLoan = null;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                if (book.isAvailable()) {
                    bookToLoan = book;
                    break;
                } else {
                    System.out.println("|!|  Book is currently unavailable!");
                    return;
                }
            }
        }

        if (bookToLoan == null) {
            System.out.println("Book not found.");
            return;
        }

        // Loan the book
        Date borrowDate = new Date();
        Borrows newBorrow = new Borrows(patron, bookToLoan, borrowDate);
        borrowedBooks.add(newBorrow);

        // Update patron's books borrowed count
        patron.incrementBooksBorrowed();
        Patron.updatePatronInCSV("./Library/data/patronList.csv", patron);

        // Update book availability
        bookToLoan.setAvailability(false);
        Book.updateBookAvailabilityInCSV("./Library/data/booklist.csv", bookToLoan);

        // Save the loan to the borrowedBooks.csv file
        Borrows.addBorrowToCSV("./Library/data/borrowedBooks.csv", newBorrow);

        System.out.println("Book loaned successfully. Due date: " + newBorrow.getDueDate());
    }

    private static void returnBook(Scanner scanner) {
        System.out.println("\n| RETURN THE BOOK --------------");
        System.out.print("Enter Patron ID: ");
        String patronID = scanner.nextLine();

        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();

        Borrows loanToReturn = null;

        // Find the matching loan record
        for (Borrows borrow : borrowedBooks) {
            if (borrow.getPatron().getPatronID().equals(patronID) &&
                borrow.getBook().getTitle().equalsIgnoreCase(title) &&
                borrow.getBook().getAuthor().equalsIgnoreCase(author)) {
                loanToReturn = borrow;
                break;
            }
        }

        if (loanToReturn != null) {
            // Remove the loan record from the list
            borrowedBooks.remove(loanToReturn);

            // Set the book as available again
            Book bookToReturn = loanToReturn.getBook();
            bookToReturn.setAvailability(true);

            // Update the book availability in the CSV file
            String bookFilePath = "./Library/data/booklist.csv";
            Book.updateBookAvailabilityInCSV(bookFilePath, bookToReturn);

            // Call the deleteBorrowRecord method to delete the record from the CSV file
            String borrowFilePath = "./Library/data/borrowedBooks.csv";
            String isbn = bookToReturn.getISBN();  // Get the ISBN from the book
            Borrows.deleteBorrowRecord(borrowFilePath, patronID, isbn);

            System.out.println("Book returned and record deleted successfully.");
        } else {
            System.out.println("No matching loan found.");
        }
    }


	private static void showBooksOnLoan() {
		System.out.println("\n| BOOKS ON LOAN --------------");
	    // Create a list to hold the details of books that are currently on loan
	    List<Borrows> booksOnLoan = new ArrayList<>();

	    // Iterate through the books list to find unavailable books
	    for (Book book : books) {
	        if (!book.isAvailable()) { // Check if the book is not available (on loan)
	            // Find the corresponding entry in the borrowedBooks list
	            for (Borrows borrow : borrowedBooks) {
	                if (borrow.getBook().getISBN().equals(book.getISBN())) {
	                    // If the book matches, add the borrow record to the booksOnLoan list
	                    booksOnLoan.add(borrow);
	                    break; // Exit the inner loop as we've found the borrow record
	                }
	            }
	        }
	    }

	    // If there are no books on loan, notify the user
	    if (booksOnLoan.isEmpty()) {
	        System.out.println("No books are currently on loan.");
	        return;
	    }

	    // Sort the list of books on loan by due date
	    booksOnLoan.sort((b1, b2) -> b1.getDueDate().compareTo(b2.getDueDate()));

	    // Set up date format for output
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    System.out.println("Books on Loan:");

	    // Display details of each book on loan
	    for (Borrows borrow : booksOnLoan) {
	        System.out.println("Title: " + borrow.getBook().getTitle() + 
	                           ", Author: " + borrow.getBook().getAuthor() +
	                           ", Patron: " + borrow.getPatron().getName() + 
	                           ", Due Date: " + sdf.format(borrow.getDueDate()));
	    }
	}

	private static void showOverdueBooks() {
	    System.out.println("\n| OVERDUE BOOKS --------------");
	    boolean hasOverdueBooks = false;

	    // Iterate through the borrowedBooks list
	    for (Borrows borrow : borrowedBooks) {
	        // If the book is overdue, display the record
	        if (borrow.isOverdue()) {
	            System.out.println(borrow.toString());  // Print the details of the overdue borrow
	            hasOverdueBooks = true;
	        }
	    }

	    // If no overdue books are found
	    if (!hasOverdueBooks) {
	        System.out.println("No overdue books found.");
	    }
	}

    private static void exitProgram() {
        // Logic to save patrons and books to their respective CSV files
        System.out.println("\nExiting program...");
        System.exit(0);
    }

}
