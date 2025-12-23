import java.io.*;
import java.util.*;

public class LibrarySystem {
    public static final String USERS_FILE = "users.csv";
    public static final String BOOKS_FILE = "books.csv";
    public static final String TRANSACTIONS_FILE = "transactions.csv";
    
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        bootstrapFiles();
        while (true) {
            login();
        }
    }

    //---------- Bootstrap CSVs with headers and default admin
    public static void bootstrapFiles() {
        try {
            File uf = new File(USERS_FILE);
            if (!uf.exists() || uf.length() == 0) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
                    pw.println("userId,name,email,phone,type,password");
                    pw.println("admin,Administrator,admin@library.com,0000000000,Admin,admin123");
                    pw.println("stu1,Lavish,lavish@example.com,9000000001,Student,pass1");
                    pw.println("fac1,Prof. Rao,rao@example.com,9000000002,Faculty,pass2");
                }
            } else {
                // ensure admin exists
                boolean hasAdmin = false;
                try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
                    String line = br.readLine(); // skip header
                    while ((line = br.readLine()) != null) {
                        String[] d = line.split(",");
                        if (d.length >= 6 && d[0].equals("admin")) { hasAdmin = true; break; }
                    }
                }
                if (!hasAdmin) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                        bw.write("admin,Administrator,admin@library.com,0000000000,Admin,admin123");
                        bw.newLine();
                    }
                }
            }

            File bf = new File(BOOKS_FILE);
            if (!bf.exists() || bf.length() == 0) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKS_FILE))) {
                    pw.println("bookId,title,author,isbn,available");
                    pw.println("b1,Java Basics,James Gosling,1111,3");
                    pw.println("b2,Python Crash Course,Eric Matthes,2222,2");
                    pw.println("b3,Data Structures,Cormen et al.,3333,1");
                }
            }

            File tf = new File(TRANSACTIONS_FILE);
            if (!tf.exists() || tf.length() == 0) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
                    pw.println("txId,userId,bookId,bookTitle,borrowDate,dueDate,returnDate,status");
                }
            }
        } catch (IOException e) {
            System.out.println("Error bootstrapping files: " + e.getMessage());
        }
    }

    //--- Login and Menus
    public static void login() {
        System.out.print("\nUser ID: ");
        String uid = sc.nextLine().trim();
        System.out.print("Password: ");
        String pwd = sc.nextLine().trim();
        
        User u = User.authenticate(uid, pwd);
        
        if (u == null) {
            System.out.println("Invalid login!");
            return;
        }
        
        System.out.println("Login successful (" + u.getType() + ")");
        if (u.getType().equalsIgnoreCase("Admin")) {
            adminMenu();
        } else {
            userMenu(u);
        }
    }

    public static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1) User Management");
            System.out.println("2) Book Management");
            System.out.println("3) Transaction Management");
            System.out.println("4) View Statistics");
            System.out.println("5) Clear All Data");
            System.out.println("6) Exit Program");
            System.out.print("Choose: ");
            
            String raw = sc.nextLine();
            int ch = parseIntSafe(raw);
            
            switch (ch) {
                case 1 -> User.userManagement();
                case 2 -> Book.bookManagement();
                case 3 -> Transaction.transactionManagement();
                case 4 -> Transaction.viewStatistics();
                case 5 -> Transaction.clearAllData();
                case 6 -> { System.out.println("Goodbye!"); System.exit(0); }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public static void userMenu(User current) {
        while (true) {
            System.out.println("\n--- MEMBER MENU ---");
            System.out.println("1) Search Book by ID");
            System.out.println("2) Borrow Book");
            System.out.println("3) Return Book");
            System.out.println("4) Check My Overdue");
            System.out.println("5) Logout");
            System.out.println("6) Exit Program");
            System.out.print("Choose: ");
            
            String raw = sc.nextLine();
            int ch = parseIntSafe(raw);
            
            switch (ch) {
                case 1 -> Book.searchBookById();
                case 2 -> Transaction.borrowBook(current);
                case 3 -> Transaction.returnBook(current);
                case 4 -> Transaction.checkOverdue(current);
                case 5 -> { return; }
                case 6 -> { System.out.println("Goodbye!"); System.exit(0); }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return -1; }
    }
}
