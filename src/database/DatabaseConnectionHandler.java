package database;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnectionHandler extends JFrame {
    //    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String ORACLE_URL = "jdbc:mysql://localhost:3306/304movie?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String ORACLE_USERNAME = "root";
    private static final String ORACLE_PASSWORD = "einstein";

    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public Integer pmt;

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
//            updateInfo = new String[]{"hello", "444 dunbar street", "dfjkdj@gmail.com", "999-999-9999"};
            System.out.println(id);
            System.out.println(updateInfo[0]);
            System.out.println("HAY");
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

            String sql = "select name, address, email, phoneNumber" +
                    " from customer" +
                    " where customer_id = '" + id+"'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            String[][] tableContent = new String[50][7];
            int i = 0;
            while (rs.next()) {
                tableContent[i][0] = rs.getString(1);
                tableContent[i][1] = rs.getString(2);
                tableContent[i][2] = rs.getString(3);
                tableContent[i][3] = rs.getString(4);

                i++;
            }
            rs.close();
            statement.close();

            String[] names = {
                    "name",
                    "address",
                    "email",
                    "phoneNumber",

            };

            JTable table = new JTable(tableContent, names);
            JScrollPane scrollPane = new JScrollPane(table) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(1200, 600);
                }
            };
            JPanel contentPane = new JPanel();
            this.setContentPane(contentPane);

            // layout components using the GridBag layout manager
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            contentPane.setLayout(gb);
            contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            // place the pane
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(0, 0, 0, 0);
            gb.setConstraints(scrollPane, c);
            contentPane.add(scrollPane);

            // size the window to obtain a best fit for the components
            this.pack();

            // center the frame
            Dimension d = this.getToolkit().getScreenSize();
            Rectangle r = this.getBounds();
            this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

            // make the window visible
            this.setVisible(true);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }



    public ArrayList<MovieModel> view(String type, String input) {
        ArrayList<MovieModel> models = new ArrayList<>();

//        String sql = "select movie_id from movie where  movie_name = '" + movie_name + "' and language = '" + movie_language + "' and format ='" + movie_format+"'";
//        System.out.println(sql);
        try {
            String sql = null;

            if (type == "Movie Name") {
                sql = "SELECT * FROM movie where movie_name = '" + input +"'";
            } else if (type == "Language") {
                sql = "SELECT * from movie where language = '" + input +"'";
//                sql = "SELECT movie_name, format, movie_genre from movie" +
//                         " where language = 'English' ";
            }
            else if (type == "Format") {
                sql = "SELECT * from movie where format = '" + input +"'";
            }else if (type == "Genre") {
                sql = "SELECT * from movie where movie_genre ='" + input + "'";
            }
//          else if (type == "Language") {
//                sql = "SELECT movie_name, format, movie_genre from movie" +
//                        " where language = 'English' ";

            System.out.println(sql);
//            String sql = "SELECT type FROM MOVIE";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getString(5));
                MovieModel model = new MovieModel(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
//
                models.add(model);
//                System.out.println(models.size());
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
//        System.out.println(models.get(0).toString());
//        System.out.println(models.get(1).toString());
//        System.out.println(models.get(3).toString());

        return models;
    }


    public void project(String type, String c1, String c2, String c3, String input) throws SQLException {
        String sql = "SELECT "+c1+", "+c2+", "+c3+" FROM movie where movie_name = '" + input +"'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        String[][] tableContent = new String[50][7];
        int i = 0;
        while (rs.next()) {
            tableContent[i][0] = rs.getString(1);
            tableContent[i][1] = rs.getString(2);
            tableContent[i][2] = rs.getString(3);

            i++;
        }
        rs.close();
        statement.close();

        String[] names = {
                c1, c2, c3
        };

        JTable table = new JTable(tableContent, names);
        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1200, 600);
            }
        };
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // place the pane
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(scrollPane, c);
        contentPane.add(scrollPane);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

        // make the window visible
        this.setVisible(true);
//        ArrayList<MovieModel> models = new ArrayList<>();
//
////        String sql = "select movie_id from movie where  movie_name = '" + movie_name + "' and language = '" + movie_language + "' and format ='" + movie_format+"'";
////        System.out.println(sql);
//        try {
//            String sql = null;
//
////            String a = c1;
//
//
//
//            if (type == "Movie Name") {
//                sql = "SELECT "+c1+", "+c2+", "+c3+" FROM movie where movie_name = '" + input +"'";
//            } else if (type == "Language") {
//                sql = "SELECT c1, c2, c3 from movie where language = '" + input +"'";
//            }else if (type == "Format") {
//                sql = "SELECT c1, c2, c3 from movie where format = '" + input +"'";
//            }else if (type == "Genre") {
//                sql = "SELECT c1, c2, c3 from movie where movie_genre ='" + input +"'";
//            }
//            System.out.println(sql);
////            String sql = "SELECT type FROM MOVIE";
//            Statement statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery(sql);
//            while (rs.next()) {
////                System.out.println(rs.getString(1));
////                System.out.println(rs.getString(2));
////                System.out.println(rs.getString(3));
////                System.out.println(rs.getString(4));
////                System.out.println(rs.getString(5));
//                MovieModel model = new MovieModel(
////                        rs.getInt(1),
//                        rs.getString(1),
//                        rs.getString(2),
//                        rs.getString(3)
////                        rs.getString(5)
//                );
////
//                models.add(model);
////                System.out.println(models.size());
//            }
//            rs.close();
//            statement.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
////        System.out.println(models.get(0).toString());
////        System.out.println(models.get(1).toString());
////        System.out.println(models.get(3).toString());
//
//        return models;
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

        pmt = id;
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
        System.out.println(id);
        pmt = id;
        return pmt;
    }



    public String createReservation ( String branch_name, String movie_name, String movie_language, String movie_format, String customer_name, int payment_id , String seat_id, String hall_id, String movie_start_time) {
        int reservation_id = 0;
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
                System.out.println(customer_id);
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


            sql = "select * from reservation where confirmation_number = (select MAX(confirmation_number) from reservation)";
            statement = connection.createStatement();
            res = statement.executeQuery(sql);


            if (res.next()) {
                reservation_id = res.getInt(1);
                System.out.println("reservation id is " + reservation_id);
            }


            ps = connection.prepareStatement("insert into ticket( confirmation_number, seat_id, hall_id, movie_start_time)values(?,?,?,?)");
            ps.setInt(1, reservation_id);
            ps.setString(2, seat_id);
            ps.setString(3, hall_id );
            ps.setTimestamp(4, Timestamp.valueOf(movie_start_time) );
            //int rowCount = 0;
            rowCount = ps.executeUpdate();

            connection.commit();
            ps.close();




        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return "Reservation Successful, Here's your Resservation ID" + reservation_id;
    }

    //branch 营业额---report--show all branches
    public void branchRevenue (){
        ArrayList<BranchRevenueModel> models = new ArrayList<>();
        String sql;
        String branch;
        double amount;

        sql = "select th.branch_name, sum(p.payment_amount)\n" +
                "from theater th, reservation r, payment p\n" +
                "where th.branch_name = r.branch_name and r.payment_id = p.payment_id\n" +
                "group by th.branch_name";
        try {
           // System.out.println(sql);
            Statement  statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            String[][] tableContent = new String[50][2];
            int i = 0;
            while (res.next()) {
                tableContent[i][0] = res.getString(1);
                tableContent[i][1] = res.getString(2);

                i++;
            }
            res.close();
            statement.close();

            String[] names = {
                    "Theater branch name",
                    "Revenue"
            };

            JTable table = new JTable(tableContent, names);
            JScrollPane scrollPane = new JScrollPane(table) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(1200, 600);
                }
            };
            JPanel contentPane = new JPanel();
            this.setContentPane(contentPane);

            // layout components using the GridBag layout manager
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            contentPane.setLayout(gb);
            contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            // place the pane
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(0, 0, 0, 0);
            gb.setConstraints(scrollPane, c);
            contentPane.add(scrollPane);

            // size the window to obtain a best fit for the components
            this.pack();

            // center the frame
            Dimension d = this.getToolkit().getScreenSize();
            Rectangle r = this.getBounds();
            this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

            // make the window visible
            this.setVisible(true);
//            while ( res.next()) {
//                branch = res.getString(1);
//                System.out.println(branch);
//
//                amount = res.getDouble(2);
//                System.out.println(amount);
//                BranchRevenueModel model = new BranchRevenueModel(branch, amount);
//                models.add(model);
//            }
//            res.close();
//            statement.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
     //   return models.toArray(new BranchRevenueModel[models.size()]);
    }
    //show the sold tickets for each branch, only show the branch sells more than 1 tickets
    public void branchticket (){
        ArrayList<BranchTicketModel> models = new ArrayList<>();
        String sql;
        String branch;
        int ticket;

        sql = "select th.branch_name, COUNT(ticket_number)\n" +
                "from theater th, ticket t, reservation r\n" +
                "where th.branch_name = r.branch_name and t.confirmation_number = r.confirmation_number\n" +
                "group by th.branch_name\n" +
                "having count(ticket_number) > 1";
        try {
            // System.out.println(sql);
            Statement  statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            String[][] tableContent = new String[50][2];
//            while ( res.next()) {
//                branch = res.getString(1);
//                System.out.println(branch);
//
//                ticket = res.getInt(2);
//                System.out.println(ticket);
//                BranchTicketModel model = new BranchTicketModel(branch, ticket);
//                models.add(model);
//            }

            int i = 0;
            while (res.next()) {
                tableContent[i][0] = res.getString(1);
                tableContent[i][1] = res.getString(2);

                i++;
            }
            res.close();
            statement.close();

            String[] names = {
                    "Theater branch name",
                    "Ticket number"
            };

            JTable table = new JTable(tableContent, names);
            JScrollPane scrollPane = new JScrollPane(table) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(1200, 600);
                }
            };
            JPanel contentPane = new JPanel();
            this.setContentPane(contentPane);

            // layout components using the GridBag layout manager
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            contentPane.setLayout(gb);
            contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            // place the pane
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(0, 0, 0, 0);
            gb.setConstraints(scrollPane, c);
            contentPane.add(scrollPane);

            // size the window to obtain a best fit for the components
            this.pack();

            // center the frame
            Dimension d = this.getToolkit().getScreenSize();
            Rectangle r = this.getBounds();
            this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

            // make the window visible
            this.setVisible(true);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        //return models.toArray(new BranchTicketModel[models.size()]);
    }

    //find average price for 2d and 3d movies, requiring each format has at least 2 movies
    public void formatPrice (){
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
            String[][] tableContent = new String[50][2];
//            while (res.next()) {
//                format = res.getString(1);
//                System.out.println(format);
//
//                price = res.getInt(2);
//                System.out.println(price);
//                FormatPrice model = new FormatPrice(format,  price);
//                models.add(model);
//            }


            int i = 0;
            while (res.next()) {
                tableContent[i][0] = res.getString(1);
                tableContent[i][1] = res.getString(2);

                i++;
            }
            res.close();
            statement.close();

            String[] names = {
                    "MovieFormat",
                    "Avg_Price"
            };

            JTable table = new JTable(tableContent, names);
            JScrollPane scrollPane = new JScrollPane(table) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(1200, 600);
                }
            };
            JPanel contentPane = new JPanel();
            this.setContentPane(contentPane);

            // layout components using the GridBag layout manager
            GridBagLayout gb = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            contentPane.setLayout(gb);
            contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            // place the pane
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.insets = new Insets(0, 0, 0, 0);
            gb.setConstraints(scrollPane, c);
            contentPane.add(scrollPane);

            // size the window to obtain a best fit for the components
            this.pack();

            // center the frame
            Dimension d = this.getToolkit().getScreenSize();
            Rectangle r = this.getBounds();
            this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

            // make the window visible
            this.setVisible(true);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
       // return models.toArray(new FormatPrice[models.size()]);
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


   public void showMovie() throws SQLException {
       String sql = "SELECT movie.movie_name,movie.format,moviePrice.movie_price FROM movie, moviePrice where movie.movie_id = moviePrice.movie_id";
       Statement statement = connection.createStatement();
       ResultSet rs = statement.executeQuery(sql);
       String[][] tableContent = new String[50][7];
       int i = 0;
       while (rs.next()) {
           tableContent[i][0] = rs.getString(1);
           tableContent[i][1] = rs.getString(2);
           tableContent[i][2] = rs.getString(3);

           i++;
       }
       rs.close();
       statement.close();

       String[] names = {

               "MovieName",

               "Format",


               "Price"
       };

       JTable table = new JTable(tableContent, names);
       JScrollPane scrollPane = new JScrollPane(table) {
           @Override
           public Dimension getPreferredSize() {
               return new Dimension(1200, 600);
           }
       };
       JPanel contentPane = new JPanel();
       this.setContentPane(contentPane);

       // layout components using the GridBag layout manager
       GridBagLayout gb = new GridBagLayout();
       GridBagConstraints c = new GridBagConstraints();

       contentPane.setLayout(gb);
       contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

       // place the pane
       c.gridwidth = GridBagConstraints.REMAINDER;
       c.insets = new Insets(0, 0, 0, 0);
       gb.setConstraints(scrollPane, c);
       contentPane.add(scrollPane);

       // size the window to obtain a best fit for the components
       this.pack();

       // center the frame
       Dimension d = this.getToolkit().getScreenSize();
       Rectangle r = this.getBounds();
       this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

       // make the window visible
       this.setVisible(true);
   }


    public void showCash() throws SQLException {
        String sql = "SELECT * FROM cash";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        String[][] tableContent = new String[50][2];
        int i = 0;
        while (rs.next()) {
            tableContent[i][0] = rs.getString(1);
            tableContent[i][1] = rs.getString(2);

            i++;
        }
        rs.close();
        statement.close();

        String[] names = {
                "PaymentID",
                "PaymentAmount"
        };

        JTable table = new JTable(tableContent, names);
        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1200, 600);
            }
        };
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // place the pane
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(scrollPane, c);
        contentPane.add(scrollPane);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

        // make the window visible
        this.setVisible(true);
    }

    public void showCard() throws SQLException {
        String sql = "SELECT * FROM card";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        String[][] tableContent = new String[50][3];
        int i = 0;
        while (rs.next()) {
            tableContent[i][0] = rs.getString(1);
            tableContent[i][1] = rs.getString(2);
            tableContent[i][2] = rs.getString(3);


            i++;
        }
        rs.close();
        statement.close();

        String[] names = {
                "PaymentID",
                "Card Number",
                "CVV"

        };

        JTable table = new JTable(tableContent, names);
        JScrollPane scrollPane = new JScrollPane(table) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1200, 600);
            }
        };
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // place the pane
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(scrollPane, c);
        contentPane.add(scrollPane);

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

        // make the window visible
        this.setVisible(true);
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


    public boolean addMovie( String movie_name, String language, String format, String movie_genre, String firm_rating, double movie_price) {
        try {

            PreparedStatement ps = connection.prepareStatement("insert into movie( movie_name, language, format, movie_genre, firm_rating)values(?,?,?,?,?)");
            ps.setString(1, movie_name);
            ps.setString(2, language);
            ps.setString(3, format);
            ps.setString(4, movie_genre );
            ps.setString(5, firm_rating);


            int rowCount = ps.executeUpdate();

            ps = connection.prepareStatement("insert into moviePrice (movie_price)values(?)");
            ps.setDouble(1, movie_price);
            int rowCount2 = ps.executeUpdate();
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