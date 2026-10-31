package ca.hccis.squash.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for writing report files.
 *
 * @author BJM
 * @modifiedby Claude (AI) 2026-10-29 Added REPORT_FOLDER_NAME and writeLinesToFile for the
 * monthly room bookings report (sprint 2 asks for c:\cis2232\ and a 24 hour file name)
 */
public class CisUtilityFile {

    public static final String FOLDER_NAME = "c:\\CIS2232\\";

    //Claude (AI) 2026-10-29 Added folder for the monthly room bookings report file
    public static final String REPORT_FOLDER_NAME = "c:\\cis2232\\";

    public static void writeReportToFile(String fileName, ArrayList theObjects) {

        String fullName = FOLDER_NAME + fileName + CisUtility.getCurrentDate("_yyyyMMddhhmmss") + ".txt";
        Path path = Paths.get(FOLDER_NAME);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.out.println("BJM-CisUtilityFile-Error creating directories");
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fullName, true));

            for (Object current : theObjects) {
                writer.write(current.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("CisUtilityFile exception with file: " + fullName);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("CisUtilityFile exception with file: " + fullName);
            }
        }

    }

    //Claude (AI) 2026-10-29 Added method to write the monthly room bookings report file

    /**
     * Write lines of text to a new file in a folder. The folder is created if
     * it does not exist and an existing file with the same name is replaced.
     *
     * @param folderName Folder to write the file to (for example c:\cis2232\)
     * @param fileName   Name of the file (for example MonthlyRoomBookings202610291537.txt)
     * @param lines      Lines of text to write to the file
     * @return Path of the file that was written
     * @throws IOException If the folder or the file can not be written
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static Path writeLinesToFile(String folderName, String fileName, List<String> lines) throws IOException {
        //Create the folder if missing, then write each line to the file
        Path folderPath = Paths.get(folderName);
        Files.createDirectories(folderPath);
        Path filePath = folderPath.resolve(fileName);
        Files.write(filePath, lines);
        return filePath;
    }
}
