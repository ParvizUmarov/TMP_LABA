package app;

public interface Settings {
    String HOST = "localhost";
    int PORT = 5566;
    boolean DEBUG_ON = false;
    String SERVER_FILE_STORAGE_BASE_PATH = "/home/parviz/file_storage";
    String FILE_STORAGE_PATH = "/home/parviz/";
    String ROOT_PATH = "/home/parviz/Desktop/";
    String DEFAULT_FILENAME_TO_UPLOAD = "/home/parviz/Desktop/img.jpeg";
    String DEFAULT_FILENAME_TO_DOWNLOAD = "img.jpeg";
    String URL = "jdbc:mysql://localhost:3306/file_remote_storage";
    String USER = "parviz";
    String PASSWORD = "chaga2023";
}
