# Library Management System (LMS)

[cite_start]A console-based Java application designed to automate and manage library operations efficiently[cite: 6]. [cite_start]This system uses role-based access control to differentiate between Admin and Member functionalities[cite: 11, 13].

## 🚀 Features

### Admin Features
* [cite_start]**User Management**: Add, view, search, update, and delete users[cite: 28].
* [cite_start]**Book Management**: Add, view, search (by ID, Title, Author, or ISBN), update, and remove books[cite: 29].
* [cite_start]**Transaction Management**: Issue books, return books, and track overdue items[cite: 30].
* [cite_start]**System Statistics**: Display total counts for available, issued, and overdue books[cite: 31].
* [cite_start]**Data Control**: Reset the system by clearing all stored CSV records[cite: 32].

### Member Features (Student/Faculty)
* [cite_start]**Book Search**: Locate books quickly by their unique ID[cite: 35].
* [cite_start]**Borrowing**: Borrow available books with automated stock management[cite: 36, 650].
* [cite_start]**Returns**: Return books to update the library inventory[cite: 37, 678].
* [cite_start]**Overdue Tracking**: Check for personal overdue items[cite: 472].

## 📁 Project Structure

[cite_start]The project consists of 4 Java files and 3 CSV files for persistent data storage[cite: 60]:

| File Type | Names |
| :--- | :--- |
| **Java Files** | [cite_start]`LibrarySystem.java`, `Book.java`, `Transaction.java`, `User.java` [cite: 60] |
| **CSV Files** | [cite_start]`books.csv`, `transactions.csv`, `users.csv` [cite: 60] |
| **Batch File** | [cite_start]`run.bat` (For easy compilation and execution) [cite: 60] |

## 🛠️ How to Run

1.  [cite_start]**Preparation**: Ensure all `.java` and `.csv` files are in the same folder[cite: 15].
2.  [cite_start]**Execution**: Run `run.bat` to compile the source code and start the system[cite: 60].
3.  **Authentication**:
    * **Admin Login**: User ID: `admin` | [cite_start]Password: `admin123`[cite: 368].
    * **Sample Student**: User ID: `stu1` | [cite_start]Password: `pass1`[cite: 369].

## ⚙️ Customization

* [cite_start]**Loan Duration**: The system defaults to a **14-day** borrowing period[cite: 655]. [cite_start]To change this, update the `plusDays()` value in `Transaction.java`[cite: 655].
* [cite_start]**Persistent Storage**: Data is saved in CSV files, ensuring records are maintained between sessions without a SQL database[cite: 8, 54].

## 🤝 Contributing
[cite_start]This project is built using basic OOP concepts and is open for improvements, such as adding a GUI or SQL database integration[cite: 54, 59]. Feel free to fork and submit a pull request!
