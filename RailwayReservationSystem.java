import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RailwayReservationSystem extends JFrame {

    private JTextField usernameField, ageField, genderField, mobileField, emailField, dojField, sourceField, destinationField, ticketPriceField, seatPreferenceField;
    private JButton addButton, updateButton, deleteButton;
    private Connection connection;

    public RailwayReservationSystem() {
        initializeUI();
        initializeDBConnection();
    }

    private void initializeUI() {
        setTitle("Railway Reservation System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(12, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Gender:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Mobile:"));
        mobileField = new JTextField();
        add(mobileField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Date of Journey:"));
        dojField = new JTextField();
        add(dojField);

        add(new JLabel("Source:"));
        sourceField = new JTextField();
        add(sourceField);

        add(new JLabel("Destination:"));
        destinationField = new JTextField();
        add(destinationField);

        add(new JLabel("Ticket Price:"));
        ticketPriceField = new JTextField();
        add(ticketPriceField);

        add(new JLabel("Seat Preference:"));
        seatPreferenceField = new JTextField();
        add(seatPreferenceField);

        addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonListener());
        addButton.setBackground(Color.GREEN);
        add(addButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(new UpdateButtonListener());
        updateButton.setBackground(Color.YELLOW);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonListener());
        deleteButton.setBackground(Color.RED);
        add(deleteButton);
    }

    private void initializeDBConnection() {
        String url = "jdbc:mysql://localhost:3307/railway";
        String username = "Abhinav1234";
        String password = "1234";
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("DB CONNECTED..");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String insertQuery = "INSERT INTO ticket_bookingtb (username, age, gender, mobile, email, doj, source, destination, ticketPrice, seatPreference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String checkEmailQuery = "SELECT COUNT(*) FROM ticket_bookingtb WHERE email=?";
            String retrieveQuery = "SELECT bookingId FROM ticket_bookingtb WHERE email=?";

            try (PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery)) {
                checkEmailStatement.setString(1, emailField.getText());
                ResultSet emailResultSet = checkEmailStatement.executeQuery();
                emailResultSet.next();
                int emailCount = emailResultSet.getInt(1);

                if (emailCount > 0) {
                    JOptionPane.showMessageDialog(null, "Email already exists. Please use a different email.");
                } else {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, usernameField.getText());
                        preparedStatement.setInt(2, Integer.parseInt(ageField.getText()));
                        preparedStatement.setString(3, genderField.getText());
                        preparedStatement.setString(4, mobileField.getText());
                        preparedStatement.setString(5, emailField.getText());
                        preparedStatement.setString(6, dojField.getText());
                        preparedStatement.setString(7, sourceField.getText());
                        preparedStatement.setString(8, destinationField.getText());
                        preparedStatement.setString(9, ticketPriceField.getText());
                        preparedStatement.setString(10, seatPreferenceField.getText());

                        int rowsInserted = preparedStatement.executeUpdate();

                        if (rowsInserted > 0) {
                            try (PreparedStatement retrieveStatement = connection.prepareStatement(retrieveQuery)) {
                                retrieveStatement.setString(1, emailField.getText());
                                ResultSet id = retrieveStatement.executeQuery();
                                if (id.next()) {
                                    int bookingId = id.getInt("bookingId");
                                    JOptionPane.showMessageDialog(null, "Booking info added successfully. Your BookingId is " + bookingId);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String updateQuery = "UPDATE ticket_bookingtb SET username=?, age=?, gender=?, mobile=?, email=?, doj=?, source=?, destination=?, ticketPrice=?, seatPreference=? WHERE bookingId=?";
            String bookingId = JOptionPane.showInputDialog("Enter Booking ID to update:");

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                preparedStatement.setString(1, usernameField.getText());
                preparedStatement.setInt(2, Integer.parseInt(ageField.getText()));
                preparedStatement.setString(3, genderField.getText());
                preparedStatement.setString(4, mobileField.getText());
                preparedStatement.setString(5, emailField.getText());
                preparedStatement.setString(6, dojField.getText());
                preparedStatement.setString(7, sourceField.getText());
                preparedStatement.setString(8, destinationField.getText());
                preparedStatement.setString(9, ticketPriceField.getText());
                preparedStatement.setString(10, seatPreferenceField.getText());
                preparedStatement.setInt(11, Integer.parseInt(bookingId));
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Booking info updated successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String deleteQuery = "DELETE FROM ticket_bookingtb WHERE bookingId=?";
            String bookingId = JOptionPane.showInputDialog("Enter Booking ID to delete:");
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, Integer.parseInt(bookingId));
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Booking info deleted successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RailwayReservationSystem frame = new RailwayReservationSystem();
            frame.getContentPane().setBackground(Color.CYAN);
            frame.setVisible(true);
        });
    }
}
