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
    public static void launch(String[] args) {
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
