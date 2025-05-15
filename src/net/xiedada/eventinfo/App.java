package net.xiedada.eventinfo;

public class App {
    public static void main(String[] args) {
        // Load data from files
        DataLoaderDumper.loadEventData();
        DataLoaderDumper.loadTicketData();
        DataLoaderDumper.loadUserData();
        // Main entry point for the application
        // will be replaced by a Swing GUI in the future
        net.xiedada.eventinfo.ui.icli.iCLIMain.WelcomePage(args);
        // End of the application
        // Dump data to files
        DataLoaderDumper.dumpEventData();
        DataLoaderDumper.dumpTicketData();
        DataLoaderDumper.dumpUserData();
        // Bye bye
    }
}


