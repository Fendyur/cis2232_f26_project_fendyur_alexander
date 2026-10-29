package ca.hccis.squash.dao;

import ca.hccis.squash.entity.RoomBookingVariation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * DAO class which uses JDBC to access the booking table for the Monthly Room
 * Bookings report.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 29/10/2026
 */
public class BookingReportDAO {

    private static final Logger logger = LoggerFactory.getLogger(BookingReportDAO.class);

    private static final String PROPERTIES_FILE_NAME = "application";

    //Count the bookings for one year/month grouped by room type and add-ons.
    //The single parameter is a LIKE pattern such as 2026-10-% (bookingDate is yyyy-mm-dd).
    private static final String SELECT_COUNTS_BY_YEAR_MONTH =
            "SELECT roomType, equipmentNeeded, isBirthday, COUNT(*) AS bookingCount "
                    + "FROM booking "
                    + "WHERE bookingDate LIKE ? "
                    + "GROUP BY roomType, equipmentNeeded, isBirthday";

    private final String connectionString;
    private final String userName;
    private final String password;

    /**
     * Constructor which loads the database connection settings from
     * application.properties.
     *
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public BookingReportDAO() {
        //Read the spring datasource settings so the DAO uses the same database as the app
        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME);
        connectionString = resourceBundle.getString("spring.datasource.url");
        userName = resourceBundle.getString("spring.datasource.username");
        password = resourceBundle.getString("spring.datasource.password");
    }

    /**
     * Select the number of bookings in a year/month for each combination of
     * room type, equipment needed and birthday that has at least one booking.
     * Combinations with no bookings are not returned (the BO fills those in).
     *
     * @param bookingDatePattern LIKE pattern for the booking date, e.g. 2026-10-%
     * @return List of variations with their booking counts
     * @throws SQLException If the database can not be reached or the query fails
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    public ArrayList<RoomBookingVariation> selectBookingCountsByYearMonth(String bookingDatePattern) throws SQLException {
        ArrayList<RoomBookingVariation> bookingCounts = new ArrayList<>();

        //try-with-resources closes the connection, statement and result set
        try (Connection connection = DriverManager.getConnection(connectionString, userName, password);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNTS_BY_YEAR_MONTH)) {

            //Input: bind the date pattern to the ? parameter (no string concatenation)
            preparedStatement.setString(1, bookingDatePattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                //Output: one row per room type / add-on combination that was booked
                while (resultSet.next()) {
                    RoomBookingVariation roomBookingVariation = new RoomBookingVariation(
                            resultSet.getString("roomType"),
                            resultSet.getBoolean("equipmentNeeded"),
                            resultSet.getBoolean("isBirthday"),
                            resultSet.getInt("bookingCount"));
                    bookingCounts.add(roomBookingVariation);
                }
            }
        }

        logger.info("Monthly room bookings query for {} returned {} groups", bookingDatePattern, bookingCounts.size());
        return bookingCounts;
    }
}
