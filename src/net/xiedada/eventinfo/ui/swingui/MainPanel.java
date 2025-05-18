package net.xiedada.eventinfo.ui.swingui;

import java.awt.*;
import javax.swing.*;
import net.xiedada.eventinfo.eventutilities.*;
import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.exceptions.*;
import java.util.ArrayList;
import java.awt.event.*; // Importing event package with full path to avoid conflict
import net.xiedada.eventinfo.eventutilities.Event; // Explicitly importing Event class

public class MainPanel extends JFrame {

    public MainPanel(User user) {
        super("Main Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Top panel: Activity table
        JPanel activityPanel = new JPanel(new BorderLayout());
        JLabel activityLabel = new JLabel("Activity Information", JLabel.CENTER);
        activityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        activityPanel.add(activityLabel, BorderLayout.NORTH);

        String[] columnNames = { "Event ID", "Name", "Location", "Date", "Capacity", "Status" };
        Object[][] data = getActivityData(user);
        JTable activityTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(activityTable);
        activityPanel.add(scrollPane, BorderLayout.CENTER);

        add(activityPanel, BorderLayout.CENTER);

        // Bottom panel: User-specific actions
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());

        if (user instanceof Customer) {
            JButton bookButton = new JButton("Book Event");
            actionPanel.add(bookButton);

            bookButton.addActionListener(e -> {
                int selectedRow = activityTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to book.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String eventID = (String) activityTable.getValueAt(selectedRow, 0);
                try {
                    Event event = Event.getListOfAllEvents().stream()
                            .filter(ev -> ev.getID().toString().equals(eventID))
                            .findFirst()
                            .orElse(null);
                    if (event != null) {
                        String[] ticketOptions = new String[event.ticketTypes.length];
                        for (int i = 0; i < event.ticketTypes.length; i++) {
                            ticketOptions[i] = String.valueOf(event.ticketTypes[i]);
                        }
                        String selectedTicketType = (String) JOptionPane.showInputDialog(
                                this,
                                "Select ticket type:",
                                "Ticket Type",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                ticketOptions,
                                ticketOptions[0]
                        );
                        if (selectedTicketType != null) {
                            char ticketType = selectedTicketType.charAt(0);
                            ((Customer) user).bookTicket(event, ticketType);
                            JOptionPane.showMessageDialog(this, "Event booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Event not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton manageTicketsButton = new JButton("Manage Tickets");
            actionPanel.add(manageTicketsButton);

            manageTicketsButton.addActionListener(e -> {
                JDialog dialog = new JDialog(this, "Ticket Management", true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BorderLayout());
                dialog.add(new TicketManagementPanel((Customer) user), BorderLayout.CENTER);
                dialog.setVisible(true);
            });
        } else if (user instanceof EventOrganizer) {
            JButton createButton = new JButton("Create Event");
            JButton setAvailableButton = new JButton("Set Available");
            JButton submitButton = new JButton("Submit for Approval");
            JButton updateButton = new JButton("Update Event");

            actionPanel.add(createButton);
            actionPanel.add(setAvailableButton);
            actionPanel.add(submitButton);
            actionPanel.add(updateButton);

            createButton.addActionListener(e -> {
                EventEditDialog dialog = new EventEditDialog(this, null, (EventOrganizer) user);
                dialog.setVisible(true);
                // 刷新表格数据
                Object[][] newData = getActivityData(user);
                activityTable.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames));
            });

            setAvailableButton.addActionListener(e -> {
                int selectedRow = activityTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to set available.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String eventID = (String) activityTable.getValueAt(selectedRow, 0);
                try {
                    Event event = Event.getListOfAllEvents().stream()
                            .filter(ev -> ev.getID().toString().equals(eventID))
                            .findFirst()
                            .orElse(null);
                    if (event != null) {
                        event.setAvailable();
                        JOptionPane.showMessageDialog(this, "Event set to available!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Event not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            submitButton.addActionListener(e -> {
                int selectedRow = activityTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to submit for approval.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String eventID = (String) activityTable.getValueAt(selectedRow, 0);
                try {
                    Event event = Event.getListOfAllEvents().stream()
                            .filter(ev -> ev.getID().toString().equals(eventID))
                            .findFirst()
                            .orElse(null);
                    if (event != null) {
                        event.setApplying();
                        JOptionPane.showMessageDialog(this, "Event submitted for approval!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // 刷新表格数据
                        Object[][] newData = getActivityData(user);
                        activityTable.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames));
                    } else {
                        JOptionPane.showMessageDialog(this, "Event not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            updateButton.addActionListener(e -> {
                int selectedRow = activityTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to update.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String eventID = (String) activityTable.getValueAt(selectedRow, 0);
                Event event = Event.getListOfAllEvents().stream()
                        .filter(ev -> ev.getID().toString().equals(eventID))
                        .findFirst()
                        .orElse(null);
                if (event != null) {
                    // 只弹出编辑基本信息的对话框，不弹出票种输入窗口
                    EventEditDialog dialog = new EventEditDialog(this, event, (EventOrganizer) user);
                    dialog.setVisible(true);
                    // 刷新表格数据
                    Object[][] newData = getActivityData(user);
                    activityTable.setModel(new javax.swing.table.DefaultTableModel(newData, columnNames));
                } else {
                    JOptionPane.showMessageDialog(this, "Event not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (user instanceof Administrator) {
            JButton approveButton = new JButton("Approve Event");
            JButton disapproveButton = new JButton("Disapprove Event");
            JButton manageUsersButton = new JButton("Manage Users");

            actionPanel.add(approveButton);
            actionPanel.add(disapproveButton);
            actionPanel.add(manageUsersButton);

            approveButton.addActionListener(e -> {
                int selectedRow = activityTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to approve.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String eventID = (String) activityTable.getValueAt(selectedRow, 0);
                try {
                    Event event = Event.getListOfAllEvents().stream()
                            .filter(ev -> ev.getID().toString().equals(eventID))
                            .findFirst()
                            .orElse(null);
                    if (event != null && event.getStatus() == Event.EventStatus.APPLYING) {
                        ((Administrator) user).approveEvent(event);
                        JOptionPane.showMessageDialog(this, "Event approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Event not in APPLYING status or not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            disapproveButton.addActionListener(e -> {
                int selectedRow = activityTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an event to disapprove.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String eventID = (String) activityTable.getValueAt(selectedRow, 0);
                try {
                    Event event = Event.getListOfAllEvents().stream()
                            .filter(ev -> ev.getID().toString().equals(eventID))
                            .findFirst()
                            .orElse(null);
                    if (event != null && event.getStatus() == Event.EventStatus.APPLYING) {
                        ((Administrator) user).disapproveEvent(event);
                        JOptionPane.showMessageDialog(this, "Event disapproved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Event not in APPLYING status or not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            manageUsersButton.addActionListener(e -> {
                JDialog dialog = new JDialog(this, "User Management", true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BorderLayout());
                dialog.add(new UserManagementPanel(), BorderLayout.CENTER);
                dialog.setVisible(true);
            });
        }

        add(actionPanel, BorderLayout.SOUTH);
        setVisible(true);

        // 在窗口关闭时返回登录界面
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                user.logout();
                new LoginForm();
            }
        });
    }

    private Object[][] getActivityData(User user) {
        ArrayList<Event> events;
        if (user instanceof Customer) {
            events = Event.getListOfAllEvents();
            events.removeIf(event -> event.getStatus() != Event.EventStatus.APPROVED && event.getStatus() != Event.EventStatus.AVAILABLE);
        } else if (user instanceof EventOrganizer) {
            events = Event.getMyListOfEvents((EventOrganizer) user);
        } else {
            events = Event.getListOfAllEvents();
        }

        Object[][] data = new Object[events.size()][6];
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            data[i][0] = event.getID().toString();
            data[i][1] = event.getInfo().name;
            data[i][2] = event.getInfo().location;
            data[i][3] = event.getInfo().date.toString();
            data[i][4] = event.getInfo().capacity;
            data[i][5] = event.getStatus().toString();
        }
        return data;
    }
}

// 新增EventEditDialog类
