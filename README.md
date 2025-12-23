# Library Management System (LMS)

A console-based Java application designed to automate and manage library operations efficiently.This system uses role-based access control to differentiate between Admin and Member functionalities.

## 🚀 Features

### Admin Features
* **User Management**: Add, view, search, update, and delete users.
* **Book Management**: Add, view, search (by ID, Title, Author, or ISBN), update, and remove books.
* **Transaction Management**: Issue books, return books, and track overdue items.
* **System Statistics**: Display total counts for available, issued, and overdue books.
* **Data Control**: Reset the system by clearing all stored CSV records.

### Member Features (Student/Faculty)
* **Book Search**: Locate books quickly by their unique ID.
* **Borrowing**: Borrow available books with automated stock management.
* **Returns**: Return books to update the library inventory.
* **Overdue Tracking**: Check for personal overdue items.

## 📁 Project Structure

The project consists of 4 Java files and 3 CSV files for persistent data storage:

| File Type | Names |
| :--- | :--- |
| **Java Files** | `LibrarySystem.java`, `Book.java`, `Transaction.java`, `User.java`  |
| **CSV Files** | `books.csv`, `transactions.csv`, `users.csv`  |
| **Batch File** | `run.bat` (For easy compilation and execution)  |

## 🛠️ How to Run

1.  **Preparation**: Ensure all `.java` and `.csv` files are in the same folder.
2.  **Execution**: Run `run.bat` to compile the source code and start the system.
3.  **Authentication**:
    * **Admin Login**: User ID: `admin` | Password: `admin123`.
    * **Sample Student**: User ID: `stu1` | Password: `pass1`.

## ⚙️ Customization

* **Loan Duration**: The system defaults to a **14-day** borrowing period. To change this, update the `plusDays()` value in `Transaction.java`.
* **Persistent Storage**: Data is saved in CSV files, ensuring records are maintained between sessions without a SQL database.

## 🤝 Contributing
This project is built using basic OOP concepts and is open for improvements, such as adding a GUI or SQL database integration. Feel free to fork and submit a pull request!
