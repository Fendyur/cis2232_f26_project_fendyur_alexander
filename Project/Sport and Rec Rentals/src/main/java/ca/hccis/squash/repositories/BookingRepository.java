package ca.hccis.squash.repositories;

import ca.hccis.squash.jpa.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the booking table. The basic crud methods
 * (findAll, findById, save, deleteById, existsById) come from CrudRepository.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {

    /**
     * Use Spring Data JPA query derivation to get all of the bookings sorted by
     * booking date, then start time, then id (dates and times are stored as
     * zero padded text so text order is the same as date/time order).
     *
     * @return All bookings in date and time order
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    List<Booking> findAllByOrderByBookingDateAscStartTimeAscIdAsc();
}
