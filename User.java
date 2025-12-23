import java.io.*;
import java.util.*;

public class User {
    private String id, name, email, phone, type, password;
    static Scanner sc = new Scanner(System.in);

    public User(String id, String name, String email, String phone, String type, String password) {
        this.id = id; 
        this.name = name; 
        this.email = email; 
        this.phone = phone; 
        this.type = type; 
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }

    // Auth
    public static User authenticate(String id, String pwd) {
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 6 && d[0].equals(id) && d[5].equals(pwd)) {
                    return new User(d[0], d[1], d[2], d[3], d[4], d[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Auth error: " + e.getMessage());
        }
        return null;
    }

    // Menu
    public static void userManagement() {
        while (true) {
            System.out.println("\n--- USER MANAGEMENT ---");
            System.out.println("1) Add New User");
            System.out.println("2) View All Users");
            System.out.println("3) Search User by Name");
            System.out.println("4) Search User by ID");
            System.out.println("5) Update User");
            System.out.println("6) Delete User");
            System.out.println("7) Back");
            System.out.print("Choose: ");
            
            String raw = sc.nextLine();
            int ch = LibrarySystem.parseIntSafe(raw);
            
            switch (ch) {
                case 1 -> addUser();
                case 2 -> viewUsers();
                case 3 -> searchByName();
                case 4 -> searchById();
                case 5 -> updateUser();
                case 6 -> deleteUser();
                case 7 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    // CRUD
    public static void addUser() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LibrarySystem.USERS_FILE, true))) {
            System.out.print("Enter User ID: "); String id = sc.nextLine().trim();
            System.out.print("Enter Name: "); String name = sc.nextLine().trim();
            System.out.print("Enter Email: "); String email = sc.nextLine().trim();
            System.out.print("Enter Phone: "); String phone = sc.nextLine().trim();
            System.out.print("Enter Type (Student/Faculty/Admin): "); String type = sc.nextLine().trim();
            System.out.print("Enter Password: "); String pass = sc.nextLine().trim();
            
            bw.write(String.join(",", id, name, email, phone, type, pass)); 
            bw.newLine();
            System.out.println("User added successfully: " + name);
        } catch (IOException e) { System.out.println("Add user error: " + e.getMessage()); }
    }

    public static void viewUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line; 
            while ((line = br.readLine()) != null) System.out.println(line);
        } catch (IOException e) { System.out.println("View users error: " + e.getMessage()); }
    }

    public static void searchByName() {
        System.out.print("Enter name: "); String name = sc.nextLine().trim();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 2 && d[1].equalsIgnoreCase(name)) { 
                    System.out.println(line); 
                    found = true; 
                }
            }
        } catch (IOException e) { System.out.println("Search error: " + e.getMessage()); }
        if (!found) System.out.println("Not found");
    }

    public static void searchById() {
        System.out.print("Enter ID: "); String id = sc.nextLine().trim();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 1 && d[0].equals(id)) { 
                    System.out.println(line); 
                    found = true; 
                }
            }
        } catch (IOException e) { System.out.println("Search error: " + e.getMessage()); }
        if (!found) System.out.println("Not found");
    }

    public static void updateUser() {
        System.out.print("Enter User ID to update: "); String id = sc.nextLine().trim();
        List<String> out = new ArrayList<>(); 
        boolean updated = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line = br.readLine(); 
            out.add(line);
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 6 && d[0].equals(id)) {
                    System.out.print("New Name: "); d[1] = sc.nextLine().trim();
                    System.out.print("New Email: "); d[2] = sc.nextLine().trim();
                    System.out.print("New Phone: "); d[3] = sc.nextLine().trim();
                    System.out.print("New Type: "); d[4] = sc.nextLine().trim();
                    System.out.print("New Password: "); d[5] = sc.nextLine().trim();
                    line = String.join(",", d); 
                    updated = true;
                }
                out.add(line);
            }
        } catch (IOException e) { System.out.println("Update error: " + e.getMessage()); }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LibrarySystem.USERS_FILE))) {
            for (String s: out) { bw.write(s); bw.newLine(); }
        } catch (IOException e) { System.out.println("Write error: " + e.getMessage()); }
        
        System.out.println(updated ? "User updated successfully" : "Not found");
    }

    public static void deleteUser() {
        System.out.print("Enter User ID to delete: "); String id = sc.nextLine().trim();
        List<String> out = new ArrayList<>(); 
        boolean deleted = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(LibrarySystem.USERS_FILE))) {
            String line = br.readLine(); 
            out.add(line);
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", -1);
                if (d.length >= 1 && d[0].equals(id)) { deleted = true; continue; }
                out.add(line);
            }
        } catch (IOException e) { System.out.println("Delete error: " + e.getMessage()); }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LibrarySystem.USERS_FILE))) {
            for (String s: out) { bw.write(s); bw.newLine(); }
        } catch (IOException e) { System.out.println("Write error: " + e.getMessage()); }
        
        System.out.println(deleted ? "User deleted successfully" : "Not found");
    }
}
