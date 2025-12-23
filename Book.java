import java.io.*;
import java.util.*;

public class Book {
    String id, title, author, isbn;
    int available;
    static Scanner sc = new Scanner(System.in);

    public Book(String id, String title, String author, String isbn, int available) {
        this.id = id; 
        this.title = title; 
        this.author = author; 
        this.isbn = isbn; 
        this.available = available;
    }

    public static void bookManagement() {
        while (true) {
            System.out.println("\n--- BOOK MANAGEMENT ---");
            System.out.println("1) Add New Book");
            System.out.println("2) View All Books");
            System.out.println("3) Search Book by ID");
            System.out.println("4) Search Book by Title");
            System.out.println("5) Search Book by Author");
            System.out.println("6) Search Book by ISBN");
            System.out.println("7) View Available Books");
            System.out.println("8) Update Book");
            System.out.println("9) Remove Book");
            System.out.println("10) Back");
            System.out.print("Choose: ");
            
            String raw = sc.nextLine();
            int ch = LibrarySystem.parseIntSafe(raw);
            
            switch (ch) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> searchBookById();
                case 4 -> searchBookByTitle();
                case 5 -> searchBookByAuthor();
                case 6 -> searchBookByISBN();
                case 7 -> viewAvailableBooks();
                case 8 -> updateBook();
                case 9 -> removeBook();
                case 10 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    // CSV helpers
    static List<Book> readAll() {
        List<Book> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.BOOKS_FILE))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 5) list.add(new Book(d[0], d[1], d[2], d[3], Integer.parseInt(d[4])));
            }
        } catch (IOException e) { System.out.println("Read books error: " + e.getMessage()); }
        return list;
    }

    static void writeAll(List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LibrarySystem.BOOKS_FILE))) {
            bw.write("bookId,title,author,isbn,available"); bw.newLine();
            for (Book b: books) {
                bw.write(String.join(",", b.id, b.title, b.author, b.isbn, String.valueOf(b.available)));
                bw.newLine();
            }
        } catch (IOException e) { System.out.println("Write books error: " + e.getMessage()); }
    }

    // Operations
    public static void addBook() {
        List<Book> books = readAll();
        System.out.print("Book ID: "); String id = sc.nextLine().trim();
        System.out.print("Title: "); String title = sc.nextLine().trim();
        System.out.print("Author: "); String author = sc.nextLine().trim();
        System.out.print("ISBN: "); String isbn = sc.nextLine().trim();
        System.out.print("Quantity: "); int q = LibrarySystem.parseIntSafe(sc.nextLine());
        
        books.add(new Book(id, title, author, isbn, q));
        writeAll(books);
        System.out.println("Book added successfully: " + title);
    }

    public static void viewBooks() {
        List<Book> books = readAll();
        System.out.println("bookId,title,author,isbn,available");
        for (Book b: books) System.out.println(b.id + "," + b.title + "," + b.author + "," + b.isbn + "," + b.available);
    }

    public static void searchBookById() {
        System.out.print("Enter Book ID: "); String id = sc.nextLine().trim();
        boolean found = false;
        for (Book b: readAll()) {
            if (b.id.equals(id)) { 
                System.out.println(b.id + "," + b.title + "," + b.author + "," + b.isbn + "," + b.available);
                found = true; 
            }
        }
        if (!found) System.out.println("Not found");
    }

    public static void searchBookByTitle() {
        System.out.print("Enter Title: "); String t = sc.nextLine().trim();
        boolean found = false;
        for (Book b: readAll()) {
            if (b.title.equalsIgnoreCase(t)) { 
                System.out.println(b.id + "," + b.title + "," + b.author + "," + b.isbn + "," + b.available);
                found = true; 
            }
        }
        if (!found) System.out.println("Not found");
    }

    public static void searchBookByAuthor() {
        System.out.print("Enter Author: "); String a = sc.nextLine().trim();
        boolean found = false;
        for (Book b: readAll()) {
            if (b.author.equalsIgnoreCase(a)) { 
                System.out.println(b.id + "," + b.title + "," + b.author + "," + b.isbn + "," + b.available);
                found = true; 
            }
        }
        if (!found) System.out.println("Not found");
    }

    public static void searchBookByISBN() {
        System.out.print("Enter ISBN: "); String i = sc.nextLine().trim();
        boolean found = false;
        for (Book b: readAll()) {
            if (b.isbn.equals(i)) { 
                System.out.println(b.id + "," + b.title + "," + b.author + "," + b.isbn + "," + b.available);
                found = true; 
            }
        }
        if (!found) System.out.println("Not found");
    }

    public static void viewAvailableBooks() {
        boolean any = false;
        for (Book b: readAll()) {
            if (b.available > 0) {
                System.out.println(b.id + "," + b.title + "," + b.author + "," + b.isbn + "," + b.available);
                any = true;
            }
        }
        if (!any) System.out.println("Not found");
    }

    public static void updateBook() {
        List<Book> books = readAll();
        System.out.print("Enter Book ID to update: "); String id = sc.nextLine().trim();
        boolean updated = false;
        for (Book b: books) {
            if (b.id.equals(id)) {
                System.out.print("New Title: "); b.title = sc.nextLine().trim();
                System.out.print("New Author: "); b.author = sc.nextLine().trim();
                System.out.print("New ISBN: "); b.isbn = sc.nextLine().trim();
                System.out.print("New Quantity: "); b.available = LibrarySystem.parseIntSafe(sc.nextLine());
                updated = true; break;
            }
        }
        writeAll(books);
        System.out.println(updated ? "Book updated successfully" : "Not found");
    }

    public static void removeBook() {
        List<Book> books = readAll();
        System.out.print("Enter Book ID to delete: "); String id = sc.nextLine().trim();
        int before = books.size();
        books.removeIf(b -> b.id.equals(id));
        writeAll(books);
        System.out.println(books.size() < before ? "Book deleted successfully" : "Not found");
    }

    // Stock helpers
    public static boolean decrementStock(String bookId) {
        List<Book> books = readAll(); 
        boolean ok = false;
        for (Book b: books) {
            if (b.id.equals(bookId)) {
                if (b.available > 0) { b.available--; ok = true; }
                break;
            }
        }
        if (ok) writeAll(books);
        return ok;
    }

    public static boolean incrementStock(String bookId) {
        List<Book> books = readAll(); 
        boolean ok = false;
        for (Book b: books) {
            if (b.id.equals(bookId)) { b.available++; ok = true; break; }
        }
        if (ok) writeAll(books);
        return ok;
    }

    public static String getTitleById(String id) {
        for (Book b: readAll()) if (b.id.equals(id)) return b.title;
        return "";
    }
}