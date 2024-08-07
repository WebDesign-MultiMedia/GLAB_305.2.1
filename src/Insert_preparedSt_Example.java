import java.sql.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Insert_preparedSt_Example {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        String dburl = "jdbc:mysql://localhost:3306/classicmodels";
        String user = "root";
        String password = "password";
        try{
             con = DriverManager.getConnection(dburl, user, password);
            System.out.println("Connection established successfully!");

            /*-------lets insert one record using a prepared statement ----*/
            int newEmployeeNumber = getNextEmployeeNumber(con);

            String sqlQuery = "INSERT INTO EMPLOYEES (officeCode,firstName,lastName,email,extension,reportsTo,VacationHours, employeeNumber, jobTitle) VALUES (?,?,?,?,?,?,?,?,?)";
            prepStmt = con.prepareStatement(sqlQuery);
            prepStmt.setInt(1, 6); //officeCode
            prepStmt.setString(2, "Jamil"); //firstName
            prepStmt.setString(3, "Fink"); //lastName
            prepStmt.setString(4, "JJ@gmail.com"); //email
            prepStmt.setString(5, "2759"); //extension
            prepStmt.setInt(6, 1143); //reportsTo
            prepStmt.setInt(7, 9); //VacationHours
            prepStmt.setInt(8, newEmployeeNumber);
            prepStmt.setString(9, "Manager"); //jobTitle

            int affectedRows = prepStmt.executeUpdate();
            System.out.println(affectedRows + " row(s) affected !!");

            prepStmt = con.prepareStatement ("select * from employees where employeeNumber = ? ");
            prepStmt.setInt(1, newEmployeeNumber);


            //execute select query
            rs = prepStmt.executeQuery();

            // Display function to show the ResultSet
            while(rs.next()){
                System.out.println("Employee Details:");
                System.out.println(rs.getString("firstName"));
                System.out.println(rs.getString("lastname"));
                System.out.println(rs.getString("email"));
                System.out.println(rs.getString("extension"));
                System.out.println(rs.getString("reportsTo"));
                System.out.println(rs.getString("vacationHours"));
                System.out.println(rs.getString("jobTitle"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (prepStmt != null) prepStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
}
    private static int getNextEmployeeNumber(Connection con) throws SQLException {
        int nextEmployeeNumber = 0;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT MAX(employeeNumber) AS maxEmployeeNumber FROM employees");

            if (rs.next()) {
                nextEmployeeNumber = rs.getInt("maxEmployeeNumber") + 1;
            } else {
                // If no records in the table, start with 1 (or your desired initial value)
                nextEmployeeNumber = 1;
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return nextEmployeeNumber;
    }
}