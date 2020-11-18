package database;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import com.mysql.jdbc.Driver;

public class DatabaseConnectionHandler {
    //    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String ORACLE_URL = "jdbc:mysql://localhost:3306/304movie?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String ORACLE_USERNAME = "root";
    private static final String ORACLE_PASSWORD = "password";

    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean connectToOracle() {
        try {
            System.out.println("sssssssssssssssssssasdsad");
            if (connection != null)
                connection.close();

            connection = DriverManager.getConnection(ORACLE_URL, ORACLE_USERNAME, ORACLE_PASSWORD);
            connection.setAutoCommit(false);
            System.out.println("\nConnected to Oracle!");
            return true;

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    public TheatreModel[] showTheaters() {
        ArrayList<TheatreModel> models = new ArrayList<>();
        try {
            String sql = "SELECT * FROM THEATER";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getString(5));
                TheatreModel model = new TheatreModel(

                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));

                models.add(model);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new TheatreModel[models.size()]);
    }

    public void updateTheaters(int id, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE theater SET  = ? WHERE branch_id = ?");
            ps.setString(1, name);
            ps.setInt(2, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateCustomer(int id, String[] updateInfo) {
        try {
//            String[] column = new String[]{"name", "address", "email", "phoneNumber"};
//            String query = "UPDATE theater SET"
//            for(int i = 0; i < column.length; i++){
//
//            }
            String query = "Update customer set name = ?, address = ?, email = ?, phoneNumber= ? where customer_id= ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, updateInfo[0]);
            ps.setString(2, updateInfo[1]);
            ps.setString(3, updateInfo[2]);
            ps.setString(4, updateInfo[3]);
            ps.setInt(5, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public HallModel[] showHalls() {
        ArrayList<HallModel> models = new ArrayList<>();
        try {
            String sql = "SELECT * FROM HALLS";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                HallModel model = new HallModel(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4));

                models.add(model);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new HallModel[models.size()]);
    }

    public SeatModel[] showSeats() {
        ArrayList<SeatModel> models = new ArrayList<>();
        try {
            String sql = "SELECT * FROM SEATS ";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                SeatModel model = new SeatModel(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3));

                models.add(model);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new SeatModel[models.size()]);
    }


    public CustomerModel[] showCustomers() {
        ArrayList<CustomerModel> models = new ArrayList<>();
        try {
            String sql = "SELECT * FROM CUSTOMER";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                CustomerModel model = new CustomerModel(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
                models.add(model);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new CustomerModel[models.size()]);
    }

    public TicketModel[] showTickets() {
        ArrayList<TicketModel> models = new ArrayList<>();
        try {
            String sql = "SELECT * FROM TICKET";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                TicketModel model = new TicketModel(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
//                        rs.getString(6));
                models.add(model);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new TicketModel[models.size()]);
    }

    public OrderModel[] showOrders() {
        ArrayList<OrderModel> models = new ArrayList<>();
        try {
            String sql = "SELECT * FROM reservation";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                OrderModel model = new OrderModel(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6));
                models.add(model);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new OrderModel[models.size()]);
    }


    public int makePaymentCash(int amount) {
        int id = 0;
        try {

            PreparedStatement ps = connection.prepareStatement("Insert into payment(payment_amount) values(?)");
            ps.setInt(1, amount);
            int rowCount = ps.executeUpdate();
            System.out.println(rowCount);


            String sql = "select * from payment where payment_id = (select MAX(payment_id) from payment)";
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);


            if (res.next()) {
                id = res.getInt(1);
            }


            ps = connection.prepareStatement("Insert into cash(payment_id, payment_amount) values(?, ?)");
            ps.setInt(1, id);
            ps.setInt(2, amount);
            rowCount = ps.executeUpdate();
            connection.commit();
            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return id;
    }


    public int makePaymentCard(int amount, String cardnumber, String cvv) {
        int id = 0;
        try {

            PreparedStatement ps = connection.prepareStatement("Insert into payment(payment_amount) values(?)");
            ps.setInt(1, amount);
            int rowCount = ps.executeUpdate();
            System.out.println(rowCount);


            String sql = "select * from payment where payment_id = (select MAX(payment_id) from payment)";
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);


            if (res.next()) {
                id = res.getInt(1);
            }


            ps = connection.prepareStatement("Insert into card(payment_id, card_number, cvv) values(?, ?, ?)");
            ps.setInt(1, id);
            ps.setString(2, cardnumber);
            ps.setString(3, cvv);
            rowCount = ps.executeUpdate();
            connection.commit();
            ps.close();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return id;
    }



    public boolean createReservation ( String branch_name, String movie_name, String movie_language, String movie_format, String customer_name, int payment_id ) {
        try {


            String sql = "select movie_id from movie where  movie_name = '" + movie_name + "' and language = '" + movie_language + "' and format ='" + movie_format+"'";
            System.out.println(sql);

            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            int movie_id = 0;
            if (res.next()) {
                movie_id = res.getInt(1);
            }

            sql = "select customer_id from customer where name = '" + customer_name+"'";
            System.out.println(sql);
             statement = connection.createStatement();
             res = statement.executeQuery(sql);
            int customer_id = 0;
            if (res.next()) {
                customer_id= res.getInt(1);
            }

            Timestamp orderTime = new Timestamp(System.currentTimeMillis());
            PreparedStatement ps = connection.prepareStatement("insert into reservation(order_time, branch_name, movie_id, payment_id, customer_id)values(?,?,?,?,?)");
            ps.setTimestamp(1, orderTime);
            ps.setString(2, branch_name);
            ps.setInt(3, movie_id );
            ps.setInt(4, payment_id );
            ps.setInt(5, customer_id );
            //int rowCount = 0;
            int rowCount = ps.executeUpdate();
            connection.commit();
            ps.close();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return false;
    }

    //branch 营业额---report--show all branches
    public BranchRevenueModel[] branchRevenue (){
        ArrayList<BranchRevenueModel> models = new ArrayList<>();
        String sql;
        String branch;
        double amount;

        sql = "select th.branch_name, sum(p.payment_amount)\n" +
                "from theatre th, reservation r, payment p\n" +
                "where th.branch_name = r.branch_name and r.payment_id = p.payment_id\n" +
                "group by th.branch_name";
        try {
           // System.out.println(sql);
            Statement  statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);

            while ( res.next()) {
                branch = res.getString(1);
                System.out.println(branch);

                amount = res.getDouble(2);
                System.out.println(amount);
                BranchRevenueModel model = new BranchRevenueModel(branch, amount);
                models.add(model);
            }
            res.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new BranchRevenueModel[models.size()]);
    }
    //show the sold tickets for each branch, only show the branch sells more than 3 tickets
    public BranchTicketModel[] branchticket (){
        ArrayList<BranchTicketModel> models = new ArrayList<>();
        String sql;
        String branch;
        int ticket;

        sql = "select th.branch_name, COUNT(ticket_number)\n" +
                "from theatre th, ticket t, reservation r\n" +
                "where th.branch_name = r.branch_name and t.confirmation_number = r.confirmation_number\n" +
                "group by th.branch_name\n" +
                "having count(ticket_number) > 3";
        try {
            // System.out.println(sql);
            Statement  statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);

            while ( res.next()) {
                branch = res.getString(1);
                System.out.println(branch);

                ticket = res.getInt(2);
                System.out.println(ticket);
                BranchTicketModel model = new BranchTicketModel(branch, ticket);
                models.add(model);
            }
            res.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new BranchTicketModel[models.size()]);
    }

    //find average price for 2d and 3d movies, requiring each format has at least 2 movies
    public FormatPrice[] formatPrice (){
        ArrayList<FormatPrice> models = new ArrayList<>();
        String sql;
        String format;
        double price;

        sql = "select format, AVG(movie_price)\n" +
                "from movie m, movieprice mp\n" +
                "where m.movie_id = mp.movie_id\n" +
                "group by format\n" +
                "having 1 < (select count(*)\n" +
                "\t\tfrom movie m2\n" +
                "\t\twhere m.format = m2.format)";
        try {
            // System.out.println(sql);
            Statement  statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);

            while (res.next()) {
                format = res.getString(1);
                System.out.println(format);

                price = res.getInt(2);
                System.out.println(price);
                FormatPrice model = new FormatPrice(format,  price);
                models.add(model);
            }
            res.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new FormatPrice[models.size()]);
    }

    //find the customer information for who bought all movies
    public CustomerModel[] customerAllMovie (){
        ArrayList<CustomerModel> models = new ArrayList<>();
        String sql;


        sql = "select *\n" +
                "from customer c\n" +
                "where not exists\n" +
                "          (select m.movie_id\n" +
                "          from movie m\n" +
                "          where not exists\n" +
                "          (select r.customer_id\n" +
                "          from  reservation r\n" +
                "          where r.movie_id = m.movie_id and r.customer_id = c.customer_id))";
        try {
            // System.out.println(sql);
            Statement  statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);

            while (res.next()) {
                CustomerModel model = new CustomerModel(
                        res.getInt(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4),
                        res.getString(5));
                models.add(model);
            }
            res.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return models.toArray(new CustomerModel[models.size()]);
    }





    public boolean deleteMovie(String moviename) {
        try {

            PreparedStatement ps = connection.prepareStatement("delete from movie where movie_name = ?");
            ps.setString(1, moviename );
            int rowCount = ps.executeUpdate();
            connection.commit();
            ps.close();
            return true;

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return false;
    }


    public boolean addMovie(int movie_id, String movie_name, String language, String format, String movie_genre, String firm_rating, int active_date) {
        try {

            PreparedStatement ps = connection.prepareStatement("insert into movie(movie_id, movie_name, language, format, movie_genre, firm_rating, active_date)values(?,?,?,?,?,?,?)");
            ps.setInt(1, movie_id);
            ps.setString(2, movie_name);
            ps.setString(3, language);
            ps.setString(4, format);
            ps.setString(5, movie_genre );
            ps.setString(6, firm_rating);
            ps.setInt(7, active_date);
//            int rowCount = ps.executeUpdate();

            System.out.println("YAY");
            connection.commit();
            ps.close();
            return true;

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        System.out.println("NOPE");
        return false;


    }
}