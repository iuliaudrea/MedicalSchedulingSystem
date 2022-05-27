package ro.unibuc.pao.services.database;


import java.sql.PreparedStatement;

public class AuditServices {
    public void write(ConnectionManager con, String action, String date) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO audit VALUES(NULL,?)");
            stmt.setString(1, action + "," + date);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
