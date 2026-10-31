package ca.hccis.squash.entity;

import java.util.ArrayList;

/**
 * Entity class to hold the inputs (year and month) and the results of the
 * Monthly Room Bookings report.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 29/10/2026
 */
public class ReportMonthlyRoomBookings {

    //Inputs from the report form (Integer so an empty field can be detected)
    private Integer year;
    private Integer month;

    //Outputs of the report
    private ArrayList<RoomBookingVariation> roomBookingVariations;
    private int totalBookings;
    private String reportFilePath;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public ArrayList<RoomBookingVariation> getRoomBookingVariations() {
        return roomBookingVariations;
    }

    public void setRoomBookingVariations(ArrayList<RoomBookingVariation> roomBookingVariations) {
        this.roomBookingVariations = roomBookingVariations;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }
}
