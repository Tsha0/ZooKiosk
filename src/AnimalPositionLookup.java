import java.sql.*;
import java.util.Scanner;

public class AnimalPositionLookup {
    public static void execute(Connection con, Scanner scanner) {
        String chosenFamily = null;
        String chosenType = null;
        String chosenBreed = null;

        try {
            String queryFamilies = "SELECT DISTINCT family FROM Animal";
            PreparedStatement psFamilies = con.prepareStatement(queryFamilies);
            ResultSet rsFamilies = psFamilies.executeQuery();
            System.out.println("Available Animal Families:");
            while (rsFamilies.next()) {
                String family = rsFamilies.getString("family");
                System.out.println(" - " + family);
            }
            rsFamilies.close();
            psFamilies.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving animal families.");
            e.printStackTrace();
            return;
        }

        System.out.print("Enter the animal family to lookup: ");
        chosenFamily = scanner.nextLine();

        try {
            String queryTypes = "SELECT DISTINCT atype FROM Animal WHERE family = ?";
            PreparedStatement psTypes = con.prepareStatement(queryTypes);
            psTypes.setString(1, chosenFamily);
            ResultSet rsTypes = psTypes.executeQuery();
            System.out.println("Available Types in family '" + chosenFamily + "':");
            boolean foundType = false;
            while (rsTypes.next()) {
                String type = rsTypes.getString("atype");
                System.out.println(" - " + type);
                foundType = true;
            }
            rsTypes.close();
            psTypes.close();
            if (!foundType) {
                System.out.println("No types found for family '" + chosenFamily + "'.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving types for family '" + chosenFamily + "'.");
            e.printStackTrace();
            return;
        }

        System.out.print("Enter the animal type to lookup: ");
        chosenType = scanner.nextLine();

        try {
            String queryBreeds = "SELECT DISTINCT breed FROM Animal WHERE family = ? AND atype = ?";
            PreparedStatement psBreeds = con.prepareStatement(queryBreeds);
            psBreeds.setString(1, chosenFamily);
            psBreeds.setString(2, chosenType);
            ResultSet rsBreeds = psBreeds.executeQuery();
            System.out.println("Available Breeds in family '" + chosenFamily + "', type '" + chosenType + "':");
            boolean foundBreed = false;
            while (rsBreeds.next()) {
                String breed = rsBreeds.getString("breed");
                System.out.println(" - " + breed);
                foundBreed = true;
            }
            rsBreeds.close();
            psBreeds.close();
            if (!foundBreed) {
                System.out.println("No breeds found for family '" + chosenFamily + "', type '" + chosenType + "'.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving breeds for family '" + chosenFamily + "', type '" + chosenType + "'.");
            e.printStackTrace();
            return;
        }

        System.out.print("Enter the breed to lookup: ");
        chosenBreed = scanner.nextLine();

        try {
            String query = "SELECT A.a_id, A.family, A.atype, A.breed, " +
                    "       A.s1_feeding_hours, A.e1_feeding_hours, " +
                    "       A.s2_feeding_hours, A.e2_feeding_hours, " +
                    "       A.s3_feeding_hours, A.e3_feeding_hours, " +
                    "       A.total_amount, " +
                    "       H.temperature, H.decoration, " +
                    "       R.name AS region_name, R.location " +
                    "FROM Animal A " +
                    "JOIN lives_in li ON A.a_id = li.a_id " +
                    "JOIN Habitat H ON li.at_id = H.at_id " +
                    "JOIN located_in l2 ON H.at_id = l2.at_id " +
                    "JOIN Region R ON l2.r_id = R.r_id " +
                    "WHERE A.family = ? AND A.atype = ? AND A.breed = ?";
            System.out.println(query);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, chosenFamily);
            ps.setString(2, chosenType);
            ps.setString(3, chosenBreed);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int a_id = rs.getInt("a_id");
                System.out.println("\nAnimal Information:");
                System.out.println("Animal ID: " + a_id);
                System.out.println("Family: " + rs.getString("family"));
                System.out.println("Type: " + rs.getString("atype"));
                System.out.println("Breed: " + rs.getString("breed"));
                System.out.println("Feeding Hours:");
                System.out.println("   Session 1: " + rs.getString("s1_feeding_hours") + " to " + rs.getString("e1_feeding_hours"));
                System.out.println("   Session 2: " + rs.getString("s2_feeding_hours") + " to " + rs.getString("e2_feeding_hours"));
                System.out.println("   Session 3: " + rs.getString("s3_feeding_hours") + " to " + rs.getString("e3_feeding_hours"));
                System.out.println("Total Amount: " + rs.getInt("total_amount"));
                System.out.println("\nHabitat Details:");
                System.out.println("   Temperature: " + rs.getString("temperature"));
                System.out.println("   Decoration: " + rs.getString("decoration"));
                System.out.println("\nRegion Details:");
                System.out.println("   Region Name: " + rs.getString("region_name"));
                System.out.println("   Location: " + rs.getString("location"));
            } else {
                System.out.println("No animal found for the criteria: Family='" + chosenFamily +
                        "', Type='" + chosenType + "', Breed='" + chosenBreed + "'.");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving animal information for the specified criteria.");
            e.printStackTrace();
        }
    }
}
