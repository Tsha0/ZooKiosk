import java.sql.*;
import java.util.Scanner;

public class SponsorConservationProgram {
    public static void execute(Connection con, Scanner scanner) {
        try {
            String listQuery = "SELECT cp_id, name FROM Conservation_Program";
            PreparedStatement psList = con.prepareStatement(listQuery);
            ResultSet rsList = psList.executeQuery();
            System.out.println("Available Conservation Programs:");
            while (rsList.next()) {
                int cp_id = rsList.getInt("cp_id");
                String cpName = rsList.getString("name");
                System.out.println("Program ID: " + cp_id + " | Name: " + cpName);
            }
            rsList.close();
            psList.close();

            System.out.print("Enter the ID of the conservation program you wish to sponsor: ");
            int cpId;
            try {
                cpId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid program ID format.");
                return;
            }

            System.out.print("Enter your sponsor company name: ");
            String sponsorName = scanner.nextLine();

            System.out.print("Enter the amount of funding you wish to provide: ");
            double fundingAmount;
            try {
                fundingAmount = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid funding amount format.");
                return;
            }

            String checkSponsor = "SELECT * FROM Sponsor WHERE name = ?";
            PreparedStatement psCheck = con.prepareStatement(checkSponsor);
            psCheck.setString(1, sponsorName);
            ResultSet rsCheck = psCheck.executeQuery();
            boolean sponsorExists = rsCheck.next();
            rsCheck.close();
            psCheck.close();

            if (!sponsorExists) {
                String insertSponsor = "INSERT INTO Sponsor (name, amount_of_funding) VALUES (?, ?)";
                PreparedStatement psInsertSponsor = con.prepareStatement(insertSponsor);
                psInsertSponsor.setString(1, sponsorName);
                psInsertSponsor.setDouble(2, fundingAmount);
                int sponsorRows = psInsertSponsor.executeUpdate();
                psInsertSponsor.close();
                if (sponsorRows > 0) {
                    System.out.println("Sponsor added successfully.");
                } else {
                    System.out.println("Error adding sponsor.");
                    return;
                }
            } else {
                System.out.println("Sponsor already exists. Proceeding to connect with the conservation program.");
            }

            String insertFunds = "INSERT INTO funds (cp_id, name) VALUES (?, ?)";
            PreparedStatement psFunds = con.prepareStatement(insertFunds);
            psFunds.setInt(1, cpId);
            psFunds.setString(2, sponsorName);
            int fundsRows = psFunds.executeUpdate();
            psFunds.close();
            if (fundsRows > 0) {
                System.out.println("Sponsorship connected successfully.");
            } else {
                System.out.println("Error connecting sponsorship.");
            }

        } catch (SQLException e) {
            System.out.println("Error sponsoring conservation program.");
            e.printStackTrace();
        }
    }
}
