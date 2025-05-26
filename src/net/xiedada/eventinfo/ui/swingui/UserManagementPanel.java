/* SPDX-License-Identifier: Apache-2.0 */
/*
    Copyright (c) 2024 Xie Youtian. 

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package net.xiedada.eventinfo.ui.swingui;
import java.awt.*;
import javax.swing.*;
import net.xiedada.eventinfo.userutilities.*;
import net.xiedada.eventinfo.exceptions.*;

public class UserManagementPanel extends JPanel {

    public UserManagementPanel() {
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("User Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // User list area
        JTextArea userListArea = new JTextArea();
        userListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userListArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton refreshButton = new JButton("Refresh");
        JButton suspendButton = new JButton("Suspend/Restore User");
        buttonPanel.add(refreshButton);
        buttonPanel.add(suspendButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Refresh button action
        refreshButton.addActionListener(e -> {
            StringBuilder userList = new StringBuilder();
            for (User user : User.getListofAllUsers()) {
                userList.append(user.toString()).append("\n");
            }
            userListArea.setText(userList.toString());
        });

        // Suspend button action
        suspendButton.addActionListener(e -> {
            String userInput = JOptionPane.showInputDialog(this, "Enter the User ID or username to suspend/restore:", "Suspend/Restore User", JOptionPane.PLAIN_MESSAGE);
            if (userInput != null && !userInput.isEmpty()) {
                try {
                    User userToSuspend = null;
                    try {
                        int userID = Integer.parseInt(userInput);
                        userToSuspend = User.findUserWithUID(userID);
                    } catch (NumberFormatException ex) {
                        userToSuspend = User.findUserWithUserName(userInput);
                    }
                    if (userToSuspend != null) {
                        Administrator admin = (Administrator) User.getListofAllUsers().stream()
                                .filter(u -> u instanceof Administrator && u.isSignedIn())
                                .findFirst()
                                .orElse(null);
                        if (admin != null) {
                            admin.suspendUser(userToSuspend);
                            JOptionPane.showMessageDialog(this, "User suspended/restored successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            // 自动刷新
                            StringBuilder userList = new StringBuilder();
                            for (User user : User.getListofAllUsers()) {
                                userList.append(user.toString()).append("\n");
                            }
                            userListArea.setText(userList.toString());
                        } else {
                            JOptionPane.showMessageDialog(this, "No signed-in administrator found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
