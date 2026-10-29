package ca.hccis.squash.bo;

import ca.hccis.squash.entity.ReportMonthlyRoomBookings;
import ca.hccis.squash.entity.RoomBookingVariation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Monthly Room Bookings report business logic. These tests
 * do not need a database and only write files to a JUnit temporary folder.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 29/10/2026
 */
class MonthlyRoomBookingsReportBOTest {

    /**
     * Find the variation for a room type and add-ons in the report rows.
     *
     * @param roomBookingVariations Report rows
     * @param roomType              Room type to find
     * @param equipmentNeeded       Equipment flag to find
     * @param birthday              Birthday flag to find
     * @return Matching variation
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    private RoomBookingVariation findVariation(List<RoomBookingVariation> roomBookingVariations, String roomType,
                                               boolean equipmentNeeded, boolean birthday) {
        for (RoomBookingVariation roomBookingVariation : roomBookingVariations) {
            if (roomBookingVariation.getRoomType().equals(roomType)
                    && roomBookingVariation.isEquipmentNeeded() == equipmentNeeded
                    && roomBookingVariation.isBirthday() == birthday) {
                return roomBookingVariation;
            }
        }
        return null;
    }

    @Test
    void buildRoomBookingVariations_noDataBuildsTwelveZeroRows() {
        ArrayList<RoomBookingVariation> roomBookingVariations =
                MonthlyRoomBookingsReportBO.buildRoomBookingVariations(new ArrayList<>());

        assertEquals(12, roomBookingVariations.size());
        assertEquals(12, MonthlyRoomBookingsReportBO.VARIATION_COUNT);
        for (RoomBookingVariation roomBookingVariation : roomBookingVariations) {
            assertEquals(0, roomBookingVariation.getBookingCount());
        }
        assertEquals(0, MonthlyRoomBookingsReportBO.calculateTotalBookings(roomBookingVariations));
    }

    @Test
    void buildRoomBookingVariations_nullDataBuildsTwelveZeroRows() {
        ArrayList<RoomBookingVariation> roomBookingVariations =
                MonthlyRoomBookingsReportBO.buildRoomBookingVariations(null);

        assertEquals(12, roomBookingVariations.size());
        assertEquals(0, MonthlyRoomBookingsReportBO.calculateTotalBookings(roomBookingVariations));
    }

    @Test
    void buildRoomBookingVariations_orderIsRoomThenAddOns() {
        ArrayList<RoomBookingVariation> roomBookingVariations =
                MonthlyRoomBookingsReportBO.buildRoomBookingVariations(new ArrayList<>());

        assertEquals("Party Room - Room only", roomBookingVariations.get(0).getVariationName());
        assertEquals("Party Room - Room + Equipment", roomBookingVariations.get(1).getVariationName());
        assertEquals("Party Room - Room + Birthday decorations", roomBookingVariations.get(2).getVariationName());
        assertEquals("Party Room - Room + Equipment + Birthday decorations",
                roomBookingVariations.get(3).getVariationName());
        assertEquals("Gym - Room only", roomBookingVariations.get(4).getVariationName());
        assertEquals("Small Rec Room - Room + Equipment + Birthday decorations",
                roomBookingVariations.get(11).getVariationName());
    }

    @Test
    void buildRoomBookingVariations_countsMappedToCorrectVariationAndZerosFilled() {
        List<RoomBookingVariation> bookingCounts = new ArrayList<>();
        bookingCounts.add(new RoomBookingVariation("Party Room", false, true, 2));
        bookingCounts.add(new RoomBookingVariation("Gym", true, false, 3));
        bookingCounts.add(new RoomBookingVariation("Small Rec Room", true, true, 1));

        ArrayList<RoomBookingVariation> roomBookingVariations =
                MonthlyRoomBookingsReportBO.buildRoomBookingVariations(bookingCounts);

        assertEquals(12, roomBookingVariations.size());
        assertEquals(2, findVariation(roomBookingVariations, "Party Room", false, true).getBookingCount());
        assertEquals(3, findVariation(roomBookingVariations, "Gym", true, false).getBookingCount());
        assertEquals(1, findVariation(roomBookingVariations, "Small Rec Room", true, true).getBookingCount());
        //Not in the database rows so must be zero
        assertEquals(0, findVariation(roomBookingVariations, "Party Room", true, true).getBookingCount());
        assertEquals(0, findVariation(roomBookingVariations, "Gym", false, false).getBookingCount());
        assertEquals(6, MonthlyRoomBookingsReportBO.calculateTotalBookings(roomBookingVariations));
    }

    @Test
    void buildRoomBookingVariations_roomTypeMatchIgnoresCaseAndSpaces() {
        List<RoomBookingVariation> bookingCounts = new ArrayList<>();
        bookingCounts.add(new RoomBookingVariation("party room", false, false, 1));
        bookingCounts.add(new RoomBookingVariation(" Party Room ", false, false, 2));

        ArrayList<RoomBookingVariation> roomBookingVariations =
                MonthlyRoomBookingsReportBO.buildRoomBookingVariations(bookingCounts);

        assertEquals(3, findVariation(roomBookingVariations, "Party Room", false, false).getBookingCount());
    }

    @Test
    void buildRoomBookingVariations_unknownRoomTypeIgnored() {
        List<RoomBookingVariation> bookingCounts = new ArrayList<>();
        bookingCounts.add(new RoomBookingVariation("Pool", false, false, 5));

        ArrayList<RoomBookingVariation> roomBookingVariations =
                MonthlyRoomBookingsReportBO.buildRoomBookingVariations(bookingCounts);

        assertEquals(0, MonthlyRoomBookingsReportBO.calculateTotalBookings(roomBookingVariations));
    }

    @Test
    void buildBookingDatePattern_monthZeroPadded() {
        assertEquals("2026-01-%", MonthlyRoomBookingsReportBO.buildBookingDatePattern(2026, 1));
        assertEquals("2026-10-%", MonthlyRoomBookingsReportBO.buildBookingDatePattern(2026, 10));
    }

    @Test
    void buildReportFileName_usesReportNameAnd24HourDateTime() {
        LocalDateTime generatedDateTime = LocalDateTime.of(2026, 10, 29, 15, 37, 12);
        assertEquals("MonthlyRoomBookings202610291537.txt",
                MonthlyRoomBookingsReportBO.buildReportFileName(generatedDateTime));

        LocalDateTime morningDateTime = LocalDateTime.of(2026, 1, 5, 9, 4);
        assertEquals("MonthlyRoomBookings202601050904.txt",
                MonthlyRoomBookingsReportBO.buildReportFileName(morningDateTime));
    }

    @Test
    void validateYear_validAndInvalidValues() {
        assertNull(MonthlyRoomBookingsReportBO.validateYear(2026));
        assertNull(MonthlyRoomBookingsReportBO.validateYear(2000));
        assertNull(MonthlyRoomBookingsReportBO.validateYear(2100));
        assertNotNull(MonthlyRoomBookingsReportBO.validateYear(null));
        assertNotNull(MonthlyRoomBookingsReportBO.validateYear(1999));
        assertNotNull(MonthlyRoomBookingsReportBO.validateYear(2101));
    }

    @Test
    void validateMonth_validAndInvalidValues() {
        assertNull(MonthlyRoomBookingsReportBO.validateMonth(1));
        assertNull(MonthlyRoomBookingsReportBO.validateMonth(12));
        assertNotNull(MonthlyRoomBookingsReportBO.validateMonth(null));
        assertNotNull(MonthlyRoomBookingsReportBO.validateMonth(0));
        assertNotNull(MonthlyRoomBookingsReportBO.validateMonth(13));
    }

    @Test
    void buildPeriodDescription_monthNameAndYear() {
        assertEquals("October 2026", MonthlyRoomBookingsReportBO.buildPeriodDescription(2026, 10));
    }

    @Test
    void writeReportFile_writesTitlePeriodAllRowsAndTotal(@TempDir Path tempFolder) throws IOException {
        List<RoomBookingVariation> bookingCounts = new ArrayList<>();
        bookingCounts.add(new RoomBookingVariation("Gym", true, false, 3));
        ReportMonthlyRoomBookings report = new ReportMonthlyRoomBookings();
        report.setYear(2026);
        report.setMonth(10);
        report.setRoomBookingVariations(MonthlyRoomBookingsReportBO.buildRoomBookingVariations(bookingCounts));
        report.setTotalBookings(MonthlyRoomBookingsReportBO.calculateTotalBookings(report.getRoomBookingVariations()));
        LocalDateTime generatedDateTime = LocalDateTime.of(2026, 10, 29, 15, 37);

        //Write to a sub folder that does not exist yet to check it is created
        Path reportFolder = tempFolder.resolve("cis2232");
        Path reportFilePath = MonthlyRoomBookingsReportBO.writeReportFile(report,
                reportFolder.toString(), generatedDateTime);

        assertEquals("MonthlyRoomBookings202610291537.txt", reportFilePath.getFileName().toString());
        assertTrue(Files.exists(reportFilePath));
        List<String> reportLines = Files.readAllLines(reportFilePath);
        String reportText = String.join("\n", reportLines);
        assertTrue(reportLines.get(0).contains("Monthly Room Bookings"));
        assertTrue(reportText.contains("2026-10"));
        assertTrue(reportText.contains("October 2026"));
        //Header + 12 variation rows + total
        long variationLineCount = reportLines.stream()
                .filter(line -> line.startsWith("Party Room") || line.startsWith("Gym")
                        || line.startsWith("Small Rec Room"))
                .count();
        assertEquals(12, variationLineCount);
        assertTrue(reportText.matches("(?s).*Gym\\s+Room \\+ Equipment\\s+3.*"));
        assertTrue(reportLines.get(reportLines.size() - 1).matches("Total\\s+3"));
        assertFalse(reportText.contains("null"));
    }
}
