package net.xiedada.eventinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import net.xiedada.eventinfo.eventutilities.*;
import java.io.*;
import net.xiedada.eventinfo.userutilities.User;

public class DataLoaderDumper {
    // hardcoded file names. Funny, isn't it?
    private static final String USER_DATA_FILE = "user_data.dat";
    private static final String EVENT_DATA_FILE = "event_data.dat";
    private static final String TICKET_DATA_FILE = "ticket_data.dat";

    // Dump user data to a file
    public static void dumpUserData() {
        User.clearLoginState();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(User.getListofAllUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dumpEventData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EVENT_DATA_FILE))) {
            oos.writeObject(Event.getListOfAllEvents());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dumpTicketData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TICKET_DATA_FILE))) {
            oos.writeObject(Ticket.getListOfAllTickets());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user data from a file
    public static void loadUserData() {
        try {
            File file = new File(USER_DATA_FILE);
            if (!file.exists()) {
                file.createNewFile(); // 创建文件
                User.loadUserList(new ArrayList<>()); // 初始化为空列表
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ArrayList<User> userList = (ArrayList<User>) ois.readObject(); // 无视警告，存的什么就读什么
                User.loadUserList(userList);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadEventData() {
        try {
            File file = new File(EVENT_DATA_FILE);
            if (!file.exists()) {
                file.createNewFile(); // 创建文件
                Event.loadEventList(new ArrayList<>()); // 初始化为空列表
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                ArrayList<Event> eventList = (ArrayList<Event>) ois.readObject(); // 无视警告，存的什么就读什么
                Event.loadEventList(eventList);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadTicketData() {
        try {
            File file = new File(TICKET_DATA_FILE);
            if (!file.exists()) {
                file.createNewFile(); // 创建文件
                Ticket.loadTicketData(new HashMap<>()); // 初始化为空哈希表
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                HashMap<UUID, Ticket> ticketList = (HashMap<UUID, Ticket>) ois.readObject(); // 无视警告，存的什么就读什么
                Ticket.loadTicketData(ticketList);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
