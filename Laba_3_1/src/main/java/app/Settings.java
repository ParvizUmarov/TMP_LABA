package app;

public interface Settings {
    String HOST = "localhost";
    int PORT = 8080;
    String url = "jdbc:postgresql://localhost:5432/barber_shop_db";
    String username = "parviz";
    String password = "qwerty";
    boolean DEBUG_ON = false;
    String SERVER_FILE_STORAGE_BASE_PATH = "/home/parviz/file_storage";
    String FILE_STORAGE_PATH = "/home/parviz/";
    String ROOT_PATH = "/home/parviz/Desktop/";
    String DEFAULT_FILENAME_TO_UPLOAD = "/home/parviz/Desktop/img.jpeg";
    String DEFAULT_FILENAME_TO_DOWNLOAD = "img.jpeg";
}
