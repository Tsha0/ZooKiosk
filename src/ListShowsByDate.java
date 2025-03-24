import java.sql.*;
import java.util.Scanner;

public class ListShowsByDate {
    public static void execute(Connection con, Scanner scanner) {
        System.out.print("Enter show date (YYYY-MM-DD): ");
        String showDate = scanner.nextLine();
        try {
            String query = "SELECT A.at_id, A.name, S.sdate, S.start_time, S.end_time, S.num_of_ticks, S.description " +
                    "FROM Attraction A " +
                    "JOIN Show S ON A.at_id = S.at_id " +
                    "WHERE S.sdate = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, showDate);
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            System.out.println("Shows on " + showDate + ":");
            while (rs.next()) {
                int at_id = rs.getInt("at_id");
                String name = rs.getString("name");
                Date sdate = rs.getDate("sdate");
                Time startTime = rs.getTime("start_time");
                Time endTime = rs.getTime("end_time");
                int numTicks = rs.getInt("num_of_ticks");
                String description = rs.getString("description");
                System.out.println("Attraction ID: " + at_id + ", \nName: " + name +
                        ", \nDate: " + sdate + ", \nStart: " + startTime +
                        ", \nEnd: " + endTime + ", \nTickets Available: " + numTicks +
                        ", \nDescription: " + description);
                found = true;
            }
            if (!found) {
                System.out.println("No shows found on " + showDate);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving shows for date " + showDate);
            e.printStackTrace();
        }
    }
}
