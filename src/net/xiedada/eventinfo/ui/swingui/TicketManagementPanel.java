package net.xiedada.eventinfo.ui.swingui;

import java.awt.*;
import javax.swing.*;
import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.eventutilities.*;
import java.util.UUID;

public class TicketManagementPanel extends JPanel {

    public TicketManagementPanel(Customer customer) {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Ticket Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Ticket list area
        JTextArea ticketListArea = new JTextArea();
        ticketListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(ticketListArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton refreshButton = new JButton("Refresh");
        JButton returnButton = new JButton("Return Ticket");
        JButton changeButton = new JButton("Change Ticket");
        buttonPanel.add(refreshButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(changeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Refresh button action
        refreshButton.addActionListener(e -> {
            StringBuilder ticketList = new StringBuilder();
            for (Ticket ticket : customer.getTickets()) {
                ticketList.append(ticket.toString()).append("\n");
            }
            ticketListArea.setText(ticketList.toString());
        });

        // Return button action
        returnButton.addActionListener(e -> {
            String[] ticketOptions = customer.getTickets().stream()
                    .map(ticket -> ticket.getTicketID().toString())
                    .toArray(String[]::new);
            String selectedTicketID = (String) JOptionPane.showInputDialog(
                    this,
                    "Select the Ticket to return:",
                    "Return Ticket",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ticketOptions,
                    ticketOptions[0]
            );
            if (selectedTicketID != null) {
                try {
                    Ticket ticketToReturn = Ticket.getListOfAllTickets().get(UUID.fromString(selectedTicketID));
                    customer.ReturnTicket(ticketToReturn);
                    JOptionPane.showMessageDialog(this, "Ticket returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshButton.doClick(); // Refresh the ticket list
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Change button action
        changeButton.addActionListener(e -> {
            String[] ticketOptions = customer.getTickets().stream()
                    .map(ticket -> ticket.getTicketID().toString())
                    .toArray(String[]::new);
            String selectedTicketID = (String) JOptionPane.showInputDialog(
                    this,
                    "Select the Ticket to change:",
                    "Change Ticket",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ticketOptions,
                    ticketOptions[0]
            );
            if (selectedTicketID != null) {
                try {
                    Ticket ticketToChange = Ticket.getListOfAllTickets().get(UUID.fromString(selectedTicketID));
                    if (ticketToChange != null && ticketToChange.getOwner().userID == (customer.userID)) {
                        String[] ticketTypeOptions = new String[ticketToChange.getEvent().ticketTypes.length];
                        for (int i = 0; i < ticketToChange.getEvent().ticketTypes.length; i++) {
                            ticketTypeOptions[i] = String.valueOf(ticketToChange.getEvent().ticketTypes[i]);
                        }
                        String selectedTicketType = (String) JOptionPane.showInputDialog(
                                this,
                                "Select new ticket type:",
                                "Change Ticket Type",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                ticketTypeOptions,
                                ticketTypeOptions[0]
                        );
                        if (selectedTicketType != null) {
                            char newType = selectedTicketType.charAt(0);
                            customer.ChangeTicket(ticketToChange, newType);
                            JOptionPane.showMessageDialog(this, "Ticket changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshButton.doClick(); // Refresh the ticket list
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Ticket not found or does not belong to you.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Throwable ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Initial refresh
        refreshButton.doClick();
    }
}
