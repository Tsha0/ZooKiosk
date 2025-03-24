import java.sql.* ;
import java.util.Scanner;
class zooKiosk
{
    public static void main ( String [ ] args ) throws SQLException
    {
        // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
        String tableName = "";
        int sqlCode=0;      // Variable to hold SQLCODE
        String sqlState="00000";  // Variable to hold SQLSTATE

        if ( args.length > 0 )
            tableName += args [ 0 ] ;
        else
            tableName += "exampletbl";

        // Register the driver.  You must register the driver before you can use it.
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2025-comp421.cs.mcgill.ca:50000/comp421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "cs421g115";
        String your_password = "Batterynoway";
        //AS AN ALTERNATIVE, you can just set your password in the shell environment in the Unix (as shown below) and read it from there.
        //$  export SOCSPASSWD=yoursocspasswd
        if(your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null)
        {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        if(your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null)
        {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        Connection con = null;
        Statement statement = null;
        Scanner scanner = new Scanner(System.in);

        try {
            con = DriverManager.getConnection(url, your_userid, your_password);
            statement = con.createStatement();
            System.out.println("Connected to DB2 database.");
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
            System.exit(1);
        }

        int option = 0;
        while (option != 5) {
            System.out.println("\nZoo Kiosk Main Menu");
            System.out.println("1. Look Up All Attractions in Region");
            System.out.println("2. Look Up Position for a Specific Animal");
            System.out.println("3. List All Show information for a Specific Date");
            System.out.println("4. Sponsor a Conservation Program");
            System.out.println("5. Exist Kiosk");
            System.out.print("Enter your option: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number.");
                continue;
            }

            switch(option) {
                case 1:
                    AttractionsInRegion.execute(con, scanner);
                    break;
                case 2:
                    AnimalPositionLookup.execute(con, scanner);
                    break;
                case 3:
                    ListShowsByDate.execute(con, scanner);
                    break;
                case 4:
                    SponsorConservationProgram.execute(con, scanner);
                    break;
                case 5:
                    System.out.println("Exiting kiosk. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // Close resources.
        try {
            if (statement != null)
                statement.close();
            if (con != null)
                con.close();
            scanner.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println("Error closing resources.");
            e.printStackTrace();
        }
    }

}
