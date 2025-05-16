package transport.core;

import java.io.*;
import java.util.*;
import transport.core.Complaint.EntityType;

public class DataManager {
    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.dat";
    private static final String TITLES_FILE = DATA_DIR + "titles.dat";
    private static final String COMPLAINTS_FILE = DATA_DIR + "complaints.dat";

    private List<User> users;
    private List<TransportTitle> titles;
    private List<Complaint> complaints;
    private int titleIdCounter = 1000; // Counter for sequential title IDs

    private static DataManager instance;

    private DataManager() {
        users = new ArrayList<>();
        titles = new ArrayList<>();
        complaints = new ArrayList<>();

        // Create the data directory if it doesn't exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        loadData();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // User methods
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Title methods
    public void addTitle(TransportTitle title) {
        titles.add(title);
        saveTransportTitles();
    }

    public List<TransportTitle> getTitles() {
        // Sort by purchase date (most recent first)
        List<TransportTitle> sortedTitles = new ArrayList<>(titles);
        sortedTitles.sort((t1, t2) -> t2.getPurchaseDate().compareTo(t1.getPurchaseDate()));
        return sortedTitles;
    }

    public TransportTitle getTitleById(String id) {
        return titles.stream()
                .filter(title -> title.getTitleId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Method to get next sequential title ID
    public synchronized String getNextTitleId() {
        return "T" + (++titleIdCounter);
    }

    // Complaint methods
    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
        saveComplaints();
    }

    public List<Complaint> getComplaints() {
        return new ArrayList<>(complaints);
    }

    // Enhanced complaint methods
    public int getComplaintsCountForEntity(String entity, EntityType entityType) {
        return (int) complaints.stream()
                .filter(c -> c.getEntityConcerned().equals(entity) && c.getEntityType() == entityType)
                .count();
    }

    // Keep backward compatibility
    public int getComplaintsCountForEntity(String entity) {
        return (int) complaints.stream()
                .filter(c -> c.getEntityConcerned().equals(entity))
                .count();
    }

    // Data persistence methods
    @SuppressWarnings("unchecked")
    private void loadData() {
        // Load users
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                // Safe cast with generics check
                users = (List<User>) obj;
            }
        } catch (FileNotFoundException e) {
            users = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            users = new ArrayList<>();
        }

        // Load transport titles
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TITLES_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                // Safe cast with generics check
                titles = (List<TransportTitle>) obj;

                // Update title counter based on existing IDs
                for (TransportTitle title : titles) {
                    String id = title.getTitleId();
                    if (id != null && id.startsWith("T")) {
                        try {
                            int numericId = Integer.parseInt(id.substring(1));
                            if (numericId > titleIdCounter) {
                                titleIdCounter = numericId;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore non-numeric IDs
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            titles = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading titles: " + e.getMessage());
            titles = new ArrayList<>();
        }

        // Load complaints
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COMPLAINTS_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                // Safe cast with generics check
                complaints = (List<Complaint>) obj;
            }
        } catch (FileNotFoundException e) {
            complaints = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading complaints: " + e.getMessage());
            complaints = new ArrayList<>();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public void saveTransportTitles() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TITLES_FILE))) {
            oos.writeObject(titles);
        } catch (IOException e) {
            System.err.println("Error saving transport titles: " + e.getMessage());
        }
    }

    public void saveComplaints() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COMPLAINTS_FILE))) {
            oos.writeObject(complaints);
        } catch (IOException e) {
            System.err.println("Error saving complaints: " + e.getMessage());
        }
    }

    public void saveAllData() {
        saveUsers();
        saveTransportTitles();
        saveComplaints();
    }
}
