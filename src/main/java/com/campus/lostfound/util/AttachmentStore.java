package com.campus.lostfound.util;

import com.campus.lostfound.exception.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * FILE HANDLING: saves the picture a student attaches to a report and finds it again for the admin.
 * - Only real pictures are accepted (the first bytes of the file are checked, not just the name): JPG, PNG, GIF, WEBP.
 * - Files are saved OUTSIDE the website folder, under a random name, so they survive a redeploy and cannot be
 *   run as a page or guessed from the original file name.
 * Folder: -Dlostfound.upload.dir, or the LOSTFOUND_UPLOAD_DIR environment variable, or  [user home]/CampusLostFoundUploads
 */
public final class AttachmentStore {
    public static final int MAX_BYTES = 5 * 1024 * 1024;            // 5 MB
    private static final Pattern STORED_NAME = Pattern.compile("^[0-9a-f\\-]{36}\\.(jpg|png|gif|webp)$");

    private AttachmentStore() { }

    private static Path folder() {
        String dir = System.getProperty("lostfound.upload.dir");
        if (dir == null || dir.isBlank()) dir = System.getenv("LOSTFOUND_UPLOAD_DIR");
        if (dir == null || dir.isBlank()) dir = Paths.get(System.getProperty("user.home"), "CampusLostFoundUploads").toString();
        return Paths.get(dir).toAbsolutePath().normalize();
    }

    /** Validates and saves the picture. Returns the stored file name, or null when the student attached nothing. */
    public static String save(InputStream in, String submittedName) throws IOException {
        if (in == null) return null;
        byte[] data = in.readNBytes(MAX_BYTES + 1);
        if (data.length == 0) return null;                                  // empty file field = no attachment
        if (data.length > MAX_BYTES) throw new ValidationException("The picture must be 5 MB or smaller.");
        String ext = detectExtension(data);
        if (ext == null) throw new ValidationException("Attach a picture file (JPG, PNG, GIF or WEBP).");

        Path dir = folder();
        Files.createDirectories(dir);
        String stored = UUID.randomUUID() + "." + ext;
        Files.write(dir.resolve(stored), data);
        return stored;
    }

    /** The saved file, or null if the name is not one of ours or the file is gone. */
    public static Path find(String storedName) {
        if (storedName == null || !STORED_NAME.matcher(storedName).matches()) return null;   // blocks "../" tricks
        Path file = folder().resolve(storedName).normalize();
        return Files.isRegularFile(file) ? file : null;
    }

    public static String contentType(String storedName) {
        String n = storedName == null ? "" : storedName.toLowerCase(Locale.ROOT);
        if (n.endsWith(".png"))  return "image/png";
        if (n.endsWith(".gif"))  return "image/gif";
        if (n.endsWith(".webp")) return "image/webp";
        return "image/jpeg";
    }

    public static void delete(String storedName) {
        Path file = find(storedName);
        if (file == null) return;
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            AppLogger.log("Could not delete attachment " + storedName + ": " + e.getMessage());
        }
    }

    /** Looks at the first bytes of the file ("magic numbers"). */
    private static String detectExtension(byte[] d) {
        if (d.length >= 3 && (d[0] & 0xFF) == 0xFF && (d[1] & 0xFF) == 0xD8 && (d[2] & 0xFF) == 0xFF) return "jpg";
        if (d.length >= 8 && (d[0] & 0xFF) == 0x89 && d[1] == 'P' && d[2] == 'N' && d[3] == 'G'
                && d[4] == 0x0D && d[5] == 0x0A && d[6] == 0x1A && d[7] == 0x0A) return "png";
        if (d.length >= 6 && d[0] == 'G' && d[1] == 'I' && d[2] == 'F' && d[3] == '8'
                && (d[4] == '7' || d[4] == '9') && d[5] == 'a') return "gif";
        if (d.length >= 12 && d[0] == 'R' && d[1] == 'I' && d[2] == 'F' && d[3] == 'F'
                && d[8] == 'W' && d[9] == 'E' && d[10] == 'B' && d[11] == 'P') return "webp";
        return null;
    }
}
