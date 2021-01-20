import java.sql.*;
import java.util.Scanner;

public class LibraryProgram {
    //program attributes
    private static User[] user = new User[100]; //stores newly registered visitors and members
    private static int userCount = 0;
    private static Book[] book = new Book[500]; // store newly registered books.
    private static int bookCount = 0;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception
    {
        //database connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection=DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarysystem","root","password");

        Statement stmt = connection.createStatement();

        userMenu();
        System.out.print("Enter any corresponding option from above : ");
        String userOption = input.next();
        System.out.println("---------------------------");
        while (!userOption.equalsIgnoreCase("q"))
        {
            if (userOption.equalsIgnoreCase("l"))
            {
                loanBook(connection, stmt);
            }
            else if (userOption.equalsIgnoreCase("r"))
            {
                returnBook(connection, stmt);
            }
            else if (userOption.equalsIgnoreCase("p"))
            {
                reserveBook(connection, stmt);
            }
            else if (userOption.equalsIgnoreCase("i"))
            {
                inquiry(connection, stmt);
            }
            else if (userOption.equalsIgnoreCase("B"))
            {
                registerBook();
            }
            else if (userOption.equalsIgnoreCase("U"))
            {
                registerUser();
            }
            else if (userOption.equalsIgnoreCase("S"))
            {
                store(connection, stmt);
            }

            userMenu();
            System.out.print("Enter any corresponding option from above : ");
            userOption = input.next();
            System.out.println("-------------------------------");
            if(userOption.equalsIgnoreCase("Q")){
                connection.close();
            }
        }

    }
    public static void userMenu()
    {
        System.out.println("L - Loan a Book");
        System.out.println("R - Return a Book");
        System.out.println("P - Reserve");
        System.out.println("I - For any inquiries");
        System.out.println("B - For book registration");
        System.out.println("U - User registration");
        System.out.println("S - Store new registrations to database");
        System.out.println("Q - Quit");
    }
    public static void loanBook(Connection conn, Statement st)
    {
        //validating the user
        System.out.print("Enter the user's user number : ");
        String userNumber = input.next();


        try {
            ResultSet rsUser = st.executeQuery("select * from user where userNumber = '"+userNumber+"'");
            String userType = rsUser.getString(6);

            if(userType.equalsIgnoreCase("member")){
                //function for loan a book
                //availability of the book becomes false
                System.out.println("You can only select maximum of 5 books.");
                System.out.print("Enter the title of the book you want to load :");
                String bookTitle = input.next();
                System.out.print("Enter the publisher of the book :");
                String bookPublisher = input.next();

                //shows the available books on the book name and publisher
                //and user have to select and enter the book number

                ResultSet rsBook = st.executeQuery("select * from book where bookTitle = '"+bookTitle+"' AND publisher = '"+bookPublisher+"' ");

                System.out.println("Below are all the copies of the mentioned book. " +
                        "\n Please select a book which referenceOnly:false, availability:true & reserve:false");
                while(rsBook.next()){
                    System.out.println(rsBook.getString(1)+" "+rsBook.getString(2)+" "+rsBook.getInt(3)+
                            " referenceOnly:"+rsBook.getString(4)+" "+rsBook.getString(5)+" availability:"+rsBook.getBoolean(6)+
                            " reserve:"+rsBook.getString(7));

                }

                System.out.println();
                System.out.print("Enter the book number that you want to loan : ");
                String bookNumber = input.next();

                //important
                //check the book if its an reference only book or book is not available

                ResultSet rsBookFinal = st.executeQuery("select * from book where bookNumber = '"+bookNumber+"'");
                ResultSet rsUserFinal = st.executeQuery("select * from user where userNumber = '"+userNumber+"'");
                int numBooks = rsUserFinal.getInt(8);

                if(numBooks < 5){
                    if(rsBookFinal.getString(4).equalsIgnoreCase("false") ||
                            rsBookFinal.getString(6).equalsIgnoreCase("true") ||
                            rsBookFinal.getString(7).equalsIgnoreCase("false")){

                        numBooks ++;
                        System.out.println("You can burrow the book.");
                        st.executeUpdate("update user set bookNumber='"+bookNumber+"' where userNumber='"+userNumber+"'");
                        st.executeUpdate("update book set availability='false' where bookNumber='"+bookNumber+"'");
                        st.executeUpdate("update user set numBooks="+numBooks+" where userNumber='"+userNumber+"'");
                    }
                }



                //System.out.println("User has maximum of 2 weeks from now until he returns the book.");
            }else{
                System.out.println("Sorry! "+ userNumber + " is not registered as a member.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
    public static void returnBook(Connection conn, Statement st)
    {
        //function for return book
        //availability of the book becomes true
        System.out.print("Enter the book number : ");
        String bookNumber = input.next();
        System.out.print("Enter returned user's number : ");
        String userNumber = input.next();

        try{
            ResultSet rsUser = st.executeQuery("select * from user where userNumber = '"+userNumber+"'");
            int numBooks = rsUser.getInt(8);
            numBooks --;
            st.executeUpdate("update book set availability='true' where bookNumber='"+bookNumber+"'");
            st.executeUpdate("update user set numBooks = "+numBooks+" where userNumber = '"+userNumber+"'");
            System.out.println("Member with userNumber:"+userNumber+" has successfully return the book with the bookNumber:"+bookNumber);
            //ResultSet rsBook = st.executeQuery("select * from book where bookNumber = '" + bookNumber + "'");


        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static void reserveBook(Connection conn, Statement st)
    {
        //function to reserve books
        //reserve column of the specific book becomes true.

        System.out.print("Enter the title of the book : ");
        String bookTitle = input.next();
        System.out.print("Enter the publisher of the book : ");
        String bookPublisher = input.next();

        try {
            //shows the available books on the book name and publisher
            //and user have to select and enter the book number

            ResultSet rsBook = st.executeQuery("select * from book where bookTitle = '" + bookTitle + "' AND publisher = '" + bookPublisher + "' ");

            System.out.println("Below are all the copies of the mentioned book. " +
                    "\n Please select a book which referenceOnly:false, availability:true & reserve:false");
            while (rsBook.next()) {
                System.out.println(rsBook.getString(1) + " " + rsBook.getString(2) + " " + rsBook.getInt(3) +
                        " referenceOnly:" + rsBook.getString(4) + " " + rsBook.getString(5) + " availability:" + rsBook.getBoolean(6) +
                        " reserve:" + rsBook.getString(7));

            }

            System.out.println();
            System.out.print("Enter the book number you want to reserve :");
            String bookNumber = input.next();

            //important  (additional)
            //check if the book selected is not reserved..

            ResultSet rsBookFinal = st.executeQuery("select * from book where bookNumber = '"+bookNumber+"'");
            String reserve = rsBookFinal.getString(7);

            if(reserve.equalsIgnoreCase("false")){
                System.out.println("You successfully reserved the book with the book number :"+bookNumber);
                st.executeUpdate("update book set reserve='true' where bookNumber='"+bookNumber+"'");
            }else{
                System.out.println("Sorry the book is already reserved. Please try again another book which the reserve status is 'false'.");
            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        //check available books and reserved books. and set true to reserve if free
    }
    public static void inquiry(Connection conn, Statement st)
    {
        //function for inquiries
        System.out.print("Enter the book title : ");
        String bookTitle = input.next();
        System.out.print("Enter the book publisher : ");
        String bookPublisher = input.next();

        try {
            //shows the available books on the book name and publisher
            //and user have to select and enter the book number

            ResultSet rsBook = st.executeQuery("select * from book where bookTitle = '" + bookTitle + "' AND publisher = '" + bookPublisher + "' ");

            System.out.println("Below are all the copies of the mentioned book with its details. ");
            while (rsBook.next()) {
                System.out.println(rsBook.getString(1) + " " + rsBook.getString(2) + " " + rsBook.getInt(3) +
                        " referenceOnly:" + rsBook.getString(4) + " " + rsBook.getString(5) + " availability:" + rsBook.getBoolean(6) +
                        " reserve:" + rsBook.getString(7));

            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static void registerBook()
    {
        //function to register book.
        System.out.print("Enter book name : ");
        String bookName = input.next();
        System.out.print("Enter 'true' if reference only else 'false' if it is burrowable.");
        boolean referenceOnly = input.nextBoolean();
        System.out.print("Enter the publisher of the book. : ");
        String bookPublisher = input.next();
        System.out.print("Enter the number of copies : ");
        int numCopies = input.nextInt();
        while (numCopies > 10)
        {
            System.out.println("There can be only maximum of 10 copies per each book. " +
                    "\n Please enter the number of copies again.");
            System.out.print("Enter the number of copies : ");
            numCopies = input.nextInt();
        }

        System.out.print("Enter bookID : ");
        String bookID = input.next();

        String bookNumber = bookID + " 0000"; //original book number
        //for the original book registers
        book[bookCount] = new Book();
        book[bookCount].setBookTitle(bookName);
        book[bookCount].setBookNumber(bookNumber);
        book[bookCount].setNumCopies(numCopies);
        book[bookCount].setPublisher(bookPublisher);
        book[bookCount].setReferenceOnly(referenceOnly);

        if (book[bookCount] != null)
        {
            System.out.println(book[bookCount].getBookNumber() + " | " + book[bookCount].getBookTitle() + " is successfully registered.");
        }
        bookCount++;

        //automatically registers the copies of the original book according the number of copies given.
        for (int i = 1; i <= numCopies; i++)
        {
            bookNumber = bookID + " 000" + i;
            if(i == 10)
            {
                bookNumber = bookID + " 00" + i;
            }
            //checking every single copy of the original book burrowable or reference only.
            System.out.println("Book number :" + bookNumber + ". Is this copy of " + bookName + " reference only?" +
                    "\n Enter 'true' if yes, 'false' if not :");
            referenceOnly = input.nextBoolean();
            book[bookCount] = new Book();
            book[bookCount].setBookTitle(bookName);
            book[bookCount].setBookNumber(bookNumber);
            book[bookCount].setNumCopies(numCopies);
            book[bookCount].setPublisher(bookPublisher);
            book[bookCount].setReferenceOnly(referenceOnly);

            if (book[bookCount] != null)
            {
                System.out.println(book[bookCount].getBookNumber() + " | " + book[bookCount].getBookTitle() + " copy is successfully registered.");
            }
            bookCount++;
        }

        //bookCount++;
    }
    public static void registerUser()
    {
        //function to register new members.
        System.out.print("Enter user's name : ");
        String userName = input.next();
        System.out.print("Enter user's NIC : ");
        String userNIC = input.next();
        System.out.print("Enter user's number : ");
        String userNumber = input.next();
        System.out.print("Enter user's gender : ");
        String userGender = input.next();
        System.out.print("Enter user's address");
        String userAddress = input.next();
        System.out.print("Enter 'member' if he can burrow books else 'visitor' : ");
        String userType = input.next();

        user[userCount] = new User();
        user[userCount].setUserName(userName);
        user[userCount].setUserAddress(userAddress);
        user[userCount].setUserNIC(userNIC);
        user[userCount].setUserNumber(userNumber);
        user[userCount].setUserSex(userGender);
        user[userCount].setUserType(userType);

        userCount++;

    }
    public static void store(Connection conn, Statement st)
    {
        //function to store all the new books and users registered

        //entering the newly registered users to the database
        for(int i = 0; i < user.length; i++)
        {
            if(user[i] != null)
            {

                String userName = user[i].getUserName();
                String userNIC = user[i].getUserNIC();
                String userNumber = user[i].getUserNumber();
                String userGender = user[i].getUserSex();
                String userAddress = user[i].getUserAddress();
                String userType = user[i].getUserType();
                int numBooks = user[i].getNumBooks();


                try {
                    st.executeUpdate("INSERT INTO user " +
                            "(userNumber, userName, userSex, userAddress, userType, userNIC, numBooks) VALUES " +
                            "('"+userNumber+"','"+userName+"','"+userGender+"','"+userAddress+"','"+userType+"','"+userNIC+"',"+numBooks+")");
                    System.out.println(userNumber + " inserted successfully.");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }

        //entering the newly registered books to the database
        for(int i = 0; i < book.length; i++)
        {
            if(book[i] != null)
            {
                String bookTitle = book[i].getBookTitle();
                String bookNumber = book[i].getBookNumber();
                int numCopies = book[i].getNumCopies();
                String referenceOnly = Boolean.toString(book[i].getReferenceOnly());
                String publisher = book[i].getPublisher();
                String availabilityStatus = Boolean.toString(book[i].getAvailabilityStatus());
                String reserveStatus = Boolean.toString(book[i].getReserveStatus());

                try {
                    st.executeUpdate("INSERT INTO book " +
                            "(bookNumber, bookTitle, numCopies, referenceOnly, publisher, availability, reserve) VALUES " +
                            "('"+bookNumber+"','"+bookTitle+"',"+numCopies+",'"+referenceOnly+"','"+publisher+"','"+availabilityStatus+"','"+reserveStatus+"')");
                    System.out.println(bookNumber + " inserted successfully.");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        }
    }
}