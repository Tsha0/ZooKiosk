import java.sql.*;
import java.util.Scanner;

public class AttractionsInRegion {
    public static void execute(Connection con, Scanner scanner) {
        try {
            String listRegionsQuery = "SELECT r_id, name FROM Region";
            PreparedStatement psList = con.prepareStatement(listRegionsQuery);
            ResultSet rsList = psList.executeQuery();
            System.out.println("Available Regions:");
            while (rsList.next()) {
                int r_id = rsList.getInt("r_id");
                String name = rsList.getString("name");
                System.out.println("Region ID: " + r_id + ", Name: " + name);
            }
            rsList.close();
            psList.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving region list.");
            e.printStackTrace();
            return;
        }

        System.out.print("Enter the region name to lookup attractions: ");
        String regionName = scanner.nextLine();

        try {
            String queryShows = "SELECT A.at_id, A.name, S.sdate, S.start_time, S.end_time, S.num_of_ticks, S.description " +
                    "FROM Attraction A " +
                    "JOIN Show S ON A.at_id = S.at_id " +
                    "JOIN located_in L ON A.at_id = L.at_id " +
                    "JOIN Region R ON L.r_id = R.r_id " +
                    "WHERE R.name = ?";
            PreparedStatement psShows = con.prepareStatement(queryShows);
            psShows.setString(1, regionName);
            ResultSet rsShows = psShows.executeQuery();
            System.out.println("\nShows in region " + regionName + ":");
            boolean found = false;
            while (rsShows.next()) {
                int at_id = rsShows.getInt("at_id");
                String name = rsShows.getString("name");
                Date sdate = rsShows.getDate("sdate");
                Time startTime = rsShows.getTime("start_time");
                Time endTime = rsShows.getTime("end_time");
                int numTicks = rsShows.getInt("num_of_ticks");
                String description = rsShows.getString("description");
                System.out.println("Attraction ID: " + at_id + ", Name: " + name +
                        ", Date: " + sdate + ", Start: " + startTime +
                        ", End: " + endTime + ", Tickets: " + numTicks +
                        ", Description: " + description);
                found = true;
            }
            if (!found) {
                System.out.println("No shows found in this region.");
            }
            rsShows.close();
            psShows.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving shows for region " + regionName);
            e.printStackTrace();
        }

        try {
            String queryHabitats = "SELECT A.at_id, A.name, H.temperature, H.decoration " +
                    "FROM Attraction A " +
                    "JOIN Habitat H ON A.at_id = H.at_id " +
                    "JOIN located_in L ON A.at_id = L.at_id " +
                    "JOIN Region R ON L.r_id = R.r_id " +
                    "WHERE R.name = ?";
            PreparedStatement psHabitats = con.prepareStatement(queryHabitats);
            psHabitats.setString(1, regionName);
            ResultSet rsHabitats = psHabitats.executeQuery();
            System.out.println("\nHabitats in region " + regionName + ":");
            boolean found = false;
            while (rsHabitats.next()) {
                int at_id = rsHabitats.getInt("at_id");
                String name = rsHabitats.getString("name");
                String temperature = rsHabitats.getString("temperature");
                String decoration = rsHabitats.getString("decoration");
                System.out.println("Attraction ID: " + at_id + ", Name: " + name +
                        ", Temperature: " + temperature + ", Decoration: " + decoration);
                found = true;
            }
            if (!found) {
                System.out.println("No habitats found in this region.");
            }
            rsHabitats.close();
            psHabitats.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving habitats for region " + regionName);
            e.printStackTrace();
        }

        try {
            String queryFacilities = "SELECT A.at_id, A.name, F.opening_hours, F.closing_hours " +
                    "FROM Attraction A " +
                    "JOIN Facility F ON A.at_id = F.at_id " +
                    "JOIN located_in L ON A.at_id = L.at_id " +
                    "JOIN Region R ON L.r_id = R.r_id " +
                    "WHERE R.name = ?";
            PreparedStatement psFacilities = con.prepareStatement(queryFacilities);
            psFacilities.setString(1, regionName);
            ResultSet rsFacilities = psFacilities.executeQuery();
            System.out.println("\nFacilities in region " + regionName + ":");
            boolean found = false;
            while (rsFacilities.next()) {
                int at_id = rsFacilities.getInt("at_id");
                String name = rsFacilities.getString("name");
                String opening = rsFacilities.getString("opening_hours");
                String closing = rsFacilities.getString("closing_hours");
                System.out.println("Attraction ID: " + at_id + ", Name: " + name +
                        ", Opens: " + opening + ", Closes: " + closing);
                found = true;
            }
            if (!found) {
                System.out.println("No facilities found in this region.");
            }
            rsFacilities.close();
            psFacilities.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving facilities for region " + regionName);
            e.printStackTrace();
        }
    }
}
