package ui;


import model.HallModel;


import javax.swing.*;
import java.awt.*;

public class ShowHallUI extends JFrame {

    private HallModel[] models;

    public ShowHallUI(HallModel[] models) {
        super("Halls");
        this.models = models;
    }

    public void showFrame() {
        String[][] tableContent = new String[models.length][4];
        for (int i = 0; i < models.length; i++) {
            tableContent[i][0] = models[i].getHallid().strip();
            tableContent[i][1] = models[i].getBranch().strip();

            tableContent[i][2] = String.valueOf(models[i].getSeatAmount()).strip();
            tableContent[i][3] = models[i].getHallType().strip();

        }
        String[] names = {
                "Hall",
                "TheatreName",
                "seatAmount",
                "HallType"

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

}