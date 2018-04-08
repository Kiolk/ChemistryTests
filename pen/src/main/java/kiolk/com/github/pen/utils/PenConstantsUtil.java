package kiolk.com.github.pen.utils;

public final class PenConstantsUtil {

    public interface Log {

        String LOG_TAG = "MyLogs";
    }

    public static final String STORAGE_FILE_FORMAT = ".jpg";
    public static final int KILOBYTE_SIZE = 1024;
    public static final int PART_OF_MEMORY_CACHE = 4;
    static final String EMPTY_STRING = "";
    public static final String LOG = "MyLogs";
    public static final int WITHOUT_CACHE = 0;
    public static final int MEMORY_CACHE = 1;
    public static final int INNER_FILE_CACHE = 2;
    public static final int SAVE_SCALING_IMAGE_STRATEGY = 0;
    public static final int SAVE_FULL_IMAGE_STRATEGY = 1;
    public static final int MIN_COMPRESSION = 0;
    public static final int MAX_COMPRESSION = 100;
    public static final int DEFAULT_COMPRESSION = 80;
    public static final long DEFAULT_FILE_CACHE_SIZE = 10 * 1024 * 1024;
    public static final String IMAGE_CACHE_DESTINATION = "ImageCache";

}
