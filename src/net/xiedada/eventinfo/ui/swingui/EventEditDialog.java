package net.xiedada.eventinfo.ui.swingui;

import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.eventutilities.*;
import net.xiedada.eventinfo.eventutilities.Event;
import net.xiedada.eventinfo.eventutilities.Event.EventData;
import net.xiedada.eventinfo.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;

class EventEditDialog extends JDialog {
    private JTextField nameField;
    private JTextField descField;
    private JTextField locationField;
    private JSpinner dateSpinner;
    private JSpinner capacitySpinner;
    private char[] ticketTypes;
    private boolean isEdit;
    private net.xiedada.eventinfo.eventutilities.Event eventToEdit;
    private EventOrganizer organizer;

    public EventEditDialog(JFrame parent, net.xiedada.eventinfo.eventutilities.Event event, EventOrganizer organizer) {
        super(parent, event == null ? "Create Event" : "Edit Event", true);
        this.isEdit = (event != null);
        this.eventToEdit = event;
        this.organizer = organizer;
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        nameField = new JTextField();
        descField = new JTextField();
        locationField = new JTextField();
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        capacitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateSpinner);
        formPanel.add(new JLabel("Capacity:"));
        formPanel.add(capacitySpinner);
        add(formPanel, BorderLayout.CENTER);

        if (isEdit && event != null) {
            nameField.setText(event.getInfo().name);
            descField.setText(event.getInfo().description);
            locationField.setText(event.getInfo().location);
            dateSpinner.setValue(java.sql.Date.valueOf(event.getInfo().date));
            capacitySpinner.setValue(event.getInfo().capacity);
        }

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton okBtn = new JButton(isEdit ? "Save" : "Create");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        okBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            String location = locationField.getText().trim();
            java.util.Date date = (java.util.Date) dateSpinner.getValue();
            int capacity = (Integer) capacitySpinner.getValue();
            if (name.isEmpty() || desc.isEmpty() || location.isEmpty() || date == null) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                java.time.LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
                if (!isEdit) {
                    // 创建时弹出票种输入窗口
                    String ticketTypeStr = JOptionPane.showInputDialog(this, "Enter ticket types (e.g. A,B,C):");
                    if (ticketTypeStr == null || ticketTypeStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ticket types are required.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ticketTypes = ticketTypeStr.replaceAll("[^A-Z]", "").toCharArray();
                    if (ticketTypes.length == 0) {
                        JOptionPane.showMessageDialog(this, "At least one valid ticket type (A-Z) is required.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    organizer.createEvent(name, desc, location, localDate, capacity, ticketTypes);
                } else {
                    // 编辑时不弹出票种输入窗口
                    net.xiedada.eventinfo.eventutilities.Event.EventData newData = eventToEdit.new EventData(name, desc, location, localDate, capacity);
                    eventToEdit.updateEventData(newData);
                }
                JOptionPane.showMessageDialog(this, (isEdit ? "Event updated!" : "Event created!"), "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener(e -> dispose());
    }
}

