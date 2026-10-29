package ca.hccis.squash.bo;

import ca.hccis.squash.dao.BookingReportDAO;
import ca.hccis.squash.entity.ReportMonthlyRoomBookings;
import ca.hccis.squash.entity.RoomBookingVariation;
import ca.hccis.squash.util.CisUtilityFile;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Business logic for the Monthly Room Bookings report. Validates the year and
 * month, builds all 12 room/add-on variations (filling in zeros), totals the
 * bookings and builds the text file written for the report.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 29/10/2026
 */
public class MonthlyRoomBookingsReportBO {

    public static final String REPORT_NAME = "MonthlyRoomBookings";
    public static final String REPORT_TITLE = "Monthly Room Bookings";
    public static final String FILE_EXTENSION = ".txt";
    public static final String FILE_DATE_TIME_FORMAT = "yyyyMMddHHmm";
    public static final String GENERATED_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final int MIN_YEAR = 2000;
    public static final int MAX_YEAR = 2100;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;

    //Room types in the order they are shown on the report
    public static final String[] ROOM_TYPES = {"Party Room", "Gym", "Small Rec Room"};

    //Add-on combinations in report order: {equipmentNeeded, birthday}
    private static final boolean[][] ADD_ON_COMBINATIONS = {
            {false, false}, //Room only
            {true, false},  //Room + Equipment
            {false, true},  //Room + Birthday decorations
            {true, true}    //Room + Equipment + Birthday decorations
    };

    public static final int VARIATION_COUNT = ROOM_TYPES.length * ADD_ON_COMBINATIONS.length;

    /**
     * Validate the year entered on the report form.
     *
     * @param year Year entered by the user (null if blank)
     * @return Error message, or null if the year is valid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static String validateYear(Integer year) {
        if (year == null) {
            return "Year is required";
        }
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return "Year must be between " + MIN_YEAR + " and " + MAX_YEAR;
        }
        return null;
    }

    /**
     * Validate the month entered on the report form.
     *
     * @param month Month entered by the user (null if blank)
     * @return Error message, or null if the month is valid
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static String validateMonth(Integer month) {
        if (month == null) {
            return "Month is required";
        }
        if (month < MIN_MONTH || month > MAX_MONTH) {
            return "Month must be between " + MIN_MONTH + " and " + MAX_MONTH;
        }
        return null;
    }

    /**
     * Build the LIKE pattern used to select every booking in a year/month.
     * bookingDate is stored as yyyy-mm-dd so the month is zero padded and the
     * day is a wildcard (the day is ignored).
     *
     * @param year  Year of the report
     * @param month Month of the report (1-12)
     * @return Pattern such as 2026-10-%
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static String buildBookingDatePattern(int year, int month) {
        return String.format("%04d-%02d-%%", year, month);
    }

    /**
     * Build all 12 variations (3 room types x 4 add-on combinations) in report
     * order using the counts returned from the database. Any variation that was
     * not returned from the database gets a count of 0.
     *
     * @param bookingCounts Counts returned from the database (only booked variations)
     * @return List of all 12 variations with their counts
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static ArrayList<RoomBookingVariation> buildRoomBookingVariations(List<RoomBookingVariation> bookingCounts) {
        ArrayList<RoomBookingVariation> roomBookingVariations = new ArrayList<>();

        for (String roomType : ROOM_TYPES) {
            for (boolean[] addOnCombination : ADD_ON_COMBINATIONS) {
                boolean equipmentNeeded = addOnCombination[0];
                boolean birthday = addOnCombination[1];

                //Add up the matching database rows (0 if there are none)
                int bookingCount = 0;
                if (bookingCounts != null) {
                    for (RoomBookingVariation bookingCountRow : bookingCounts) {
                        if (isSameVariation(bookingCountRow, roomType, equipmentNeeded, birthday)) {
                            bookingCount += bookingCountRow.getBookingCount();
                        }
                    }
                }
                roomBookingVariations.add(new RoomBookingVariation(roomType, equipmentNeeded, birthday, bookingCount));
            }
        }
        return roomBookingVariations;
    }

    /**
     * Check if a database row is for the given room type and add-ons. The room
     * type is compared ignoring case and surrounding spaces.
     *
     * @param bookingCountRow Row from the database
     * @param roomType        Room type of the variation
     * @param equipmentNeeded Equipment flag of the variation
     * @param birthday        Birthday flag of the variation
     * @return True if the row belongs to the variation
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    private static boolean isSameVariation(RoomBookingVariation bookingCountRow, String roomType,
                                           boolean equipmentNeeded, boolean birthday) {
        return bookingCountRow.getRoomType() != null
                && bookingCountRow.getRoomType().trim().equalsIgnoreCase(roomType)
                && bookingCountRow.isEquipmentNeeded() == equipmentNeeded
                && bookingCountRow.isBirthday() == birthday;
    }

    /**
     * Calculate the total number of bookings over all variations.
     *
     * @param roomBookingVariations Variations of the report
     * @return Sum of the booking counts
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static int calculateTotalBookings(List<RoomBookingVariation> roomBookingVariations) {
        int totalBookings = 0;
        for (RoomBookingVariation roomBookingVariation : roomBookingVariations) {
            totalBookings += roomBookingVariation.getBookingCount();
        }
        return totalBookings;
    }

    /**
     * Run the report: query the database for the year/month entered, build the
     * 12 variations and the total and store them in the report object.
     *
     * @param report Report holding the validated year and month (updated with the results)
     * @throws SQLException If the database query fails
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static void processReport(ReportMonthlyRoomBookings report) throws SQLException {
        //Input: year/month -> LIKE pattern for the DAO
        String bookingDatePattern = buildBookingDatePattern(report.getYear(), report.getMonth());
        BookingReportDAO bookingReportDAO = new BookingReportDAO();
        ArrayList<RoomBookingVariation> bookingCounts = bookingReportDAO.selectBookingCountsByYearMonth(bookingDatePattern);

        //Output: all 12 variations and the total
        ArrayList<RoomBookingVariation> roomBookingVariations = buildRoomBookingVariations(bookingCounts);
        report.setRoomBookingVariations(roomBookingVariations);
        report.setTotalBookings(calculateTotalBookings(roomBookingVariations));
    }

    /**
     * Build the file name for the report from the report type and the date/time,
     * for example MonthlyRoomBookings202610291537.txt (24 hour clock).
     *
     * @param generatedDateTime Date/time the report was generated
     * @return File name of the report
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static String buildReportFileName(LocalDateTime generatedDateTime) {
        DateTimeFormatter fileDateTimeFormatter = DateTimeFormatter.ofPattern(FILE_DATE_TIME_FORMAT);
        return REPORT_NAME + generatedDateTime.format(fileDateTimeFormatter) + FILE_EXTENSION;
    }

    /**
     * Get the display name of the report period, for example "October 2026".
     *
     * @param year  Year of the report
     * @param month Month of the report (1-12)
     * @return Month name and year
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static String buildPeriodDescription(int year, int month) {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year;
    }

    /**
     * Build the lines of the text file for the report: title, year/month,
     * all 12 variations with their counts and the total.
     *
     * @param report            Report containing the inputs and results
     * @param generatedDateTime Date/time the report was generated
     * @return Lines to write to the file
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static ArrayList<String> buildReportFileLines(ReportMonthlyRoomBookings report, LocalDateTime generatedDateTime) {
        ArrayList<String> reportLines = new ArrayList<>();
        DateTimeFormatter generatedFormatter = DateTimeFormatter.ofPattern(GENERATED_DATE_TIME_FORMAT);
        String rowFormat = "%-16s %-40s %8s";

        //Header
        reportLines.add("Sport and Rec Rentals - " + REPORT_TITLE + " Report");
        reportLines.add("Year/Month: " + String.format("%04d-%02d", report.getYear(), report.getMonth())
                + " (" + buildPeriodDescription(report.getYear(), report.getMonth()) + ")");
        reportLines.add("Generated: " + generatedDateTime.format(generatedFormatter));
        reportLines.add("");

        //Table of all 12 variations (zero rows included in the file)
        reportLines.add(String.format(rowFormat, "Room", "Variation", "Bookings"));
        reportLines.add("-".repeat(66));
        for (RoomBookingVariation roomBookingVariation : report.getRoomBookingVariations()) {
            reportLines.add(String.format(rowFormat, roomBookingVariation.getRoomType(),
                    roomBookingVariation.getOptionDescription(),
                    roomBookingVariation.getBookingCount()));
        }
        reportLines.add("-".repeat(66));
        reportLines.add(String.format(rowFormat, "Total", "", report.getTotalBookings()));
        return reportLines;
    }

    /**
     * Write the report results to a text file in the report folder.
     *
     * @param report            Report containing the inputs and results
     * @param folderName        Folder to write the file to
     * @param generatedDateTime Date/time the report was generated (used in the file name)
     * @return Path of the file that was written
     * @throws IOException If the folder or the file can not be written
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public static Path writeReportFile(ReportMonthlyRoomBookings report, String folderName,
                                       LocalDateTime generatedDateTime) throws IOException {
        String fileName = buildReportFileName(generatedDateTime);
        ArrayList<String> reportLines = buildReportFileLines(report, generatedDateTime);
        return CisUtilityFile.writeLinesToFile(folderName, fileName, reportLines);
    }
}
