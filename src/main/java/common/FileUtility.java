package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * simple utility class for some File IO functionality
 *
 * @author Shariar
 */
public final class FileUtility {

    /**
     * create a directory in a given path
     *
     * @param path of directory
     * @throws IOException
     */
    public static void createDirectory(String path) throws IOException {
        Objects.requireNonNull(path, "path of image directory cannot be null");
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Files.createDirectory(p);
        }
    }

    /**
     * get the name of a file from URL. look for last instance of / or \ and
     * remove everything before it including / or \. IMPORTANT - only useful if
     * the name of the file is in the last bit of the URL.
     *
     * @param url - path of file
     * @return only the name of file with out \ or /
     */
    public static String getFileName(String url) {
        int last = Math.max(url.lastIndexOf("/"), url.lastIndexOf("\\")) + 1;
        return url.substring(last);
    }

    /**
     * download a file from given URL and save it at destination directory.
     *
     * @param url - path of remote file.
     * @param dest - path of directory to save file, must end with '/' or '\\'.
     * @return true if file is saved.
     */
    public static boolean downloadAndSaveFile(String url, String dest) {
        return downloadAndSaveFile(url, dest, null);
    }

    /**
     * download a file from given URL and save it at destination directory.
     *
     * @param url - path of remote file.
     * @param dest - path of directory to save file, must end with '/' or '\\'.
     * @param name - name of the file to save.
     * @return true if file is saved.
     */
    public static boolean downloadAndSaveFile(String url, String dest, String name) {
        try {
            URL urlObj = new URL(url);
            //if name is null use the name in url
            if (name == null || name.isEmpty()) {
                name = urlObj.getFile();
            }
            if(name.contains("?")){
                name = name.substring(0, name.indexOf("?"));
            }
            File file = new File(dest, name);
            String absolutePath = file.getAbsolutePath();
            if (file.exists()) {
                Logger.getLogger(FileUtility.class.getName()).log(Level.WARNING, "File: {0} from: {1} Already Exists", new String[]{absolutePath, url});
                return false;
            }
            try (InputStream inStream = urlObj.openStream();
                    ReadableByteChannel rbcObj = Channels.newChannel(inStream);
                    FileOutputStream fOutStream = new FileOutputStream(absolutePath)) {

                fOutStream.getChannel().transferFrom(rbcObj, 0, Long.MAX_VALUE);
            }
            Logger.getLogger(FileUtility.class.getName()).log(Level.INFO, "File: {0} saved from: {1}", new String[]{absolutePath, url});
            return true;
        } catch (InvalidPathException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, "Invalid Path", ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, "Incorrect URL", ex);
        } catch (IOException ex) {
            Logger.getLogger(FileUtility.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        }
        return false;
    }
}
