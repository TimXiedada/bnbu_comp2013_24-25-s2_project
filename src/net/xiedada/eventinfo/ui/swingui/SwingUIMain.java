package net.xiedada.eventinfo.ui.swingui;

import net.xiedada.eventinfo.eventutilities.*;
import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.DataLoaderDumper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.awt.*;
import javax.swing.*;

public class SwingUIMain {
    public static void main(String[] args) {
        // Load data from files
        DataLoaderDumper.loadUserData();
        DataLoaderDumper.loadEventData();
        DataLoaderDumper.loadTicketData();

        // Create the log in frame
        LoginForm loginForm = new LoginForm();

        // Add shutdown hook to save data on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DataLoaderDumper.dumpUserData();
            DataLoaderDumper.dumpEventData();
            DataLoaderDumper.dumpTicketData();
        }));
    }

    public static void launchMainPanel(User user) {
        SwingUtilities.invokeLater(() -> {
            MainPanel mainPanel = new MainPanel(user);
            mainPanel.setVisible(true);
        });
    }
}
