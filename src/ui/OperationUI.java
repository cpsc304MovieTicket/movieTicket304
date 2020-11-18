package ui;

import database.DatabaseConnectionHandler;
import delegates.DatabaseDelegate;
import delegates.OperationDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OperationUI extends JFrame{
    private OperationDelegate delegate;

    private DatabaseConnectionHandler dbHandler;

    public OperationUI() {
        super("Theater Operation");
        dbHandler = new DatabaseConnectionHandler();
        dbHandler.connectToOracle();
    }

    public void showFrame(OperationDelegate delegate) {
        this.delegate = delegate;

        JButton addMovie = new JButton("Add Movie");
        JButton deleteMovie = new JButton("Delete Movie");
//        JButton seatButton = new JButton("seat");
//        JButton orderButton = new JButton("order");
//        JButton ticketButton = new JButton("ticket");



        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        // theatre button

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 0);
        gb.setConstraints(addMovie, c);
        contentPane.add(addMovie);
        addMovie.addActionListener(e -> this.delegate.addMovie(1,"2013","Englsih","2D","Disaster", "G",60));


        // place the hall button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 0);
        gb.setConstraints(deleteMovie, c);
        contentPane.add(deleteMovie);
        deleteMovie.addActionListener(e -> this.delegate.deleteMovie("Alone"));

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

        // make the window visible
        this.setVisible(true);

    }
}