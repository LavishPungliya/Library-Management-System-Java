import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Transaction {
    String txId, userId, bookId, bookTitle, borrowDate, dueDate, returnDate, status;
    static Scanner sc = new Scanner(System.in);

    public Transaction(String txId, String userId, String bookId, String bookTitle, String borrowDate, String dueDate, String returnDate, String status) {
        this.txId = txId; 
        this.userId = userId; 
        this.bookId = bookId; 
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate; 
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    //---- Admin Menu
    public static void transactionManagement() {
        while (true) {
            System.out.println("\n--- TRANSACTIONS ---");
            System.out.println("1) View All Transactions");
            System.out.println("2) View All Overdues");
            System.out.println("3) Delete Transaction");
            System.out.println("4) Back");
            System.out.print("Choose: ");
            
            String raw = sc.nextLine();
            int ch = LibrarySystem.parseIntSafe(raw);
            
            switch (ch) {
                case 1 -> viewTransactions();
                case 2 -> viewOverdues();
                case 3 -> deleteTransaction();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    //---- CSV helpers ----
    static List<Transaction> readAll() {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.TRANSACTIONS_FILE))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 8) {
                    list.add(new Transaction(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7]));
                }
            }
        } catch (IOException e) { System.out.println("Read tx error: " + e.getMessage()); }
        return list;
    }

    static void writeAll(List<Transaction> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LibrarySystem.TRANSACTIONS_FILE))) {
            bw.write("txId,userId,bookId,bookTitle,borrowDate,dueDate,returnDate,status");
            bw.newLine();
            for (Transaction t: list) {
                bw.write(String.join(",", t.txId, t.userId, t.bookId, t.bookTitle, t.borrowDate, t.dueDate, t.returnDate, t.status));
                bw.newLine();
            }
        } catch (IOException e) { System.out.println("Write tx error: " + e.getMessage()); }
    }

    //---- Admin ops
    public static void viewTransactions() {
        System.out.println("txId,userId,bookId,bookTitle,borrowDate,dueDate,returnDate,status");
        for (Transaction t: readAll()) {
            System.out.println(String.join(",", t.txId, t.userId, t.bookId, t.bookTitle, t.borrowDate, t.dueDate, t.returnDate, t.status));
        }
    }

    public static void viewOverdues() {
        List<Transaction> list = readAll();
        boolean any = false;
        LocalDate today = LocalDate.now();
        for (Transaction t: list) {
            if (t.returnDate.isEmpty()) {
                try {
                    LocalDate due = LocalDate.parse(t.dueDate);
                    if (due.isBefore(today)) {
                        String userName = getUserName(t.userId);
                        System.out.println(userName + "-" + t.bookTitle + " (Due: " + t.dueDate + ")");
                        any = true;
                    }
                } catch (Exception ignored) {}
            }
        }
        if (!any) System.out.println("Not found");
    }

    public static void deleteTransaction() {
        System.out.print("Enter txId to delete: "); String txid = sc.nextLine().trim();
        List<Transaction> list = readAll();
        int before = list.size();
        list.removeIf(t -> t.txId.equals(txid));
        writeAll(list);
        System.out.println(list.size() < before ? "Transaction deleted successfully" : "Not found");
    }

    //---- Member ops
    public static void borrowBook(User user) {
        System.out.print("Enter Book ID to borrow: "); String bookId = sc.nextLine().trim();
        
        // check availability
        List<Book> all = Book.readAll();
        Book target = null;
        for (Book b: all) if (b.id.equals(bookId)) { target = b; break; }
        
        if (target == null) { System.out.println("Not found"); return; }
        if (target.available <= 0) { System.out.println("Book is unavailable at this moment"); return; }
        
        // decrement
        boolean ok = Book.decrementStock(bookId);
        if (!ok) { System.out.println("Book is unavailable at this moment"); return; }
        
        // add tx
        String txId = UUID.randomUUID().toString();
        LocalDate today = LocalDate.now();
        LocalDate due = today.plusDays(14);
        
        List<Transaction> list = readAll();
        list.add(new Transaction(txId, user.getId(), bookId, target.title, today.toString(), due.toString(), "", "BORROWED"));
        writeAll(list);
        
        System.out.println("Book borrowed successfully. Due: " + due);
    }

    public static void returnBook(User user) {
        System.out.print("Enter Book ID to return: "); String bookId = sc.nextLine().trim();
        List<Transaction> list = readAll();
        Transaction open = null;
        
        for (Transaction t: list) {
            if (t.userId.equals(user.getId()) && t.bookId.equals(bookId) && t.returnDate.isEmpty()) {
                open = t; break;
            }
        }
        
        if (open == null) { System.out.println("Not found"); return; }
        
        // mark return
        open.returnDate = LocalDate.now().toString();
        open.status = "RETURNED";
        writeAll(list);
        
        // increment stock
        Book.incrementStock(bookId);
        System.out.println("Book returned successfully.");
    }

    public static void checkOverdue(User user) {
        List<Transaction> list = readAll();
        boolean any = false;
        LocalDate today = LocalDate.now();
        
        for (Transaction t: list) {
            if (t.userId.equals(user.getId()) && t.returnDate.isEmpty()) {
                try {
                    LocalDate due = LocalDate.parse(t.dueDate);
                    if (due.isBefore(today)) {
                        System.out.println("Overdue: " + t.bookTitle + " (Due: " + t.dueDate + ")");
                        any = true;
                    }
                } catch (Exception ignored) {}
            }
        }
        if (!any) System.out.println("No overdues.");
    }

    // ---- Stats & Clear ----
    public static void viewStatistics() {
        int totalAvailable = 0;
        for (Book b: Book.readAll()) totalAvailable += b.available;
        
        int issued = 0, returned = 0, overdues = 0;
        LocalDate today = LocalDate.now();
        
        for (Transaction t: readAll()) {
            if (t.status.equalsIgnoreCase("BORROWED") && t.returnDate.isEmpty()) {
                issued++;
                try { 
                    if (LocalDate.parse(t.dueDate).isBefore(today)) overdues++; 
                } catch (Exception ignored) {}
            } else if (t.status.equalsIgnoreCase("RETURNED")) {
                returned++;
            }
        }
        
        System.out.println("Total Books (available count): " + totalAvailable);
        System.out.println("Books Issued (currently out): " + issued);
        System.out.println("Books Returned (all-time): " + returned);
        System.out.println("Overdues (currently late): " + overdues);
    }

    public static void clearAllData() {
        try {
            // rewrite headers and default admin
            try (PrintWriter pw = new PrintWriter(new FileWriter(LibrarySystem.USERS_FILE))) {
                pw.println("userId,name,email,phone,type,password");
                pw.println("admin,Administrator,admin@library.com,0000000000,Admin,admin123");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(LibrarySystem.BOOKS_FILE))) {
                pw.println("bookId,title,author,isbn,available");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(LibrarySystem.TRANSACTIONS_FILE))) {
                pw.println("txId,userId,bookId,bookTitle,borrowDate,dueDate,returnDate,status");
            }
            System.out.println("All data cleared.");
        } catch (IOException e) { System.out.println("Clear error: " + e.getMessage()); }
    }

    //---- Helpers ----
    static String getUserName(String userId) {
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 2 && d[0].equals(userId)) return d[1];
            }
        } catch (IOException ignored) {}
        return userId;
    }
}
