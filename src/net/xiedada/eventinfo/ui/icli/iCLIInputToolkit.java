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
package net.xiedada.eventinfo.ui.icli;

public class iCLIInputToolkit {
    private java.io.Console console = System.console();

    public int Menu(String[] selections, String title, int defaultSelection) {
        int defaultChoice = defaultSelection;
        System.out.println(title);
        for (int i = 0; i < selections.length; i++) {
            System.out.println((i + 1) + ". " + selections[i]);
        }
        if(selections.length == 0) {
            System.out.println("No options available.");
            return 0;
        }
        int choice = defaultChoice;
        while (true) {
            System.out.printf("Select an option (1-%d, default=%d): ", selections.length, defaultSelection);
            try {
                String input = console.readLine();
                if (input.isEmpty()) {
                    choice = defaultSelection;
                    break;
                }
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > selections.length) {
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    public int Menu(String[] selections, String title) {
        System.out.println(title);
        for (int i = 0; i < selections.length; i++) {
            System.out.println((i + 1) + ". " + selections[i]);
        }

        int choice = 0;
        if (selections.length == 0) {
            System.out.println("No options available.");
            return 0;
        }
        while (true) {
            System.out.print("Select an option (1-" + selections.length + "): ");
            try {
                choice = Integer.parseInt(console.readLine());
                if (choice < 1 || choice > selections.length) {
                    System.out.println("Invalid choice. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }
    public void PrintMSG(String message) {
        System.out.println(message);
    }
    public String Input(String prompt) {
        System.out.print(prompt);
        return console.readLine();
    }
    public String NonEmptyInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = console.readLine();
            if (!input.isEmpty()) {
                break;
            }
        }
        return input;
    }

    public String PasswordInput(String prompt) {
        System.out.print(prompt);
        char[] password = console.readPassword();
        return new String(password);
    }

    public void PrintSeparator() {
        System.out.println(String.valueOf('-').repeat(40));
    }

    public void PrintSeparator(int n) {
        System.out.println(String.valueOf('-').repeat(n));
    }

    public void PrintSeparator(char c) {
        System.out.println(String.valueOf(c).repeat(40));
    }

    public void PrintSeparator(int n, char c) {
        System.out.println(String.valueOf(c).repeat(n));
    }
}
