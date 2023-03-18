package view;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private JLabel text1 = new JLabel("Number of clients:");
    private JLabel text2 = new JLabel("Number of queues:");
    private JLabel text3 = new JLabel("Simulation time:");
    private JLabel text4 = new JLabel("Minimum and maximum arrival time:");
    private JLabel text5 = new JLabel("Minimum and maximum service time:");
    private JLabel text6 = new JLabel("OUTPUT");

    private JTextField N          = new JTextField();
    private JTextField Q          = new JTextField();
    private JTextField simulation = new JTextField();
    private JTextField minService = new JTextField();
    private JTextField maxService = new JTextField();
    private JTextField minArrival = new JTextField();
    private JTextField maxArrival = new JTextField();

    private JButton startBtn = new JButton("Start simulation");
    private JButton clearBtn = new JButton("Clear");

    private JTextArea output = new JTextArea();

    public MainMenu() {

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(0, 3));

        content.add(text1);
        content.add(N);
        content.add(new JLabel(""));

        content.add(text2);
        content.add(Q);
        content.add(new JLabel(""));

        content.add(text3);
        content.add(simulation);
        content.add(new JLabel(""));

        content.add(text4);
        content.add(minArrival);
        content.add(maxArrival);

        content.add(text5);
        content.add(minService);
        content.add(maxService);

        content.add(clearBtn);
        content.add(new JLabel("OUTPUT"));
        content.add(startBtn);

        content.add(new JLabel(""));
        content.add(output);
        output.setEditable(false);

        this.setContentPane(content);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    //getters
    public int getN() {
        return Integer.parseInt(N.getText());
    }

    public int getQ() {
        return Integer.parseInt(Q.getText());
    }

    public int getSimulationTime() {
        return Integer.parseInt(simulation.getText());
    }

    public int getMinService() {
        return Integer.parseInt(minService.getText());
    }

    public int getMaxService() {
        return Integer.parseInt(maxService.getText());
    }

    public int getMinArrival() {
        return Integer.parseInt(minArrival.getText());
    }

    public int getMaxArrival() {
        return Integer.parseInt(maxArrival.getText());
    }

    public JButton getStartBtn() {
        return startBtn;
    }

    public JButton getClearBtn() {
        return clearBtn;
    }

    public JTextArea getOutput() {
        return output;
    }

    //setter
    public void setTextArea(String s) {
        this.output.setText(s);
    }
}
