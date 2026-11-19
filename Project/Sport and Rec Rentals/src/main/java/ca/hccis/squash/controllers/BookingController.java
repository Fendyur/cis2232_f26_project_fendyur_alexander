package ca.hccis.squash.controllers;

import ca.hccis.squash.bo.BookingBO;
import ca.hccis.squash.bo.BookingValidationBO;
import ca.hccis.squash.jpa.entity.Booking;
import ca.hccis.squash.repositories.BookingRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the booking crud (list, add, edit, delete and the details
 * page) using Spring Data JPA and Spring Validation. Prices are calculated by
 * BookingBO when a booking is added or edited and the user is then sent to
 * the details page to see the saved booking and its prices.
 *
 * @author Alexander Fendyur
 * @author Claude (AI)
 * @since 19/11/2026
 */
@Controller
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private static final String VIEW_LIST = "booking/list";
    private static final String VIEW_ADD = "booking/add";
    private static final String VIEW_DETAILS = "booking/details";
    private static final String REDIRECT_LIST = "redirect:/booking";

    private final BookingRepository bookingRepository;

    /**
     * Create the controller with the booking repository.
     *
     * @param bookingRepository Repository for the booking table
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @Autowired
    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Put the room types in the model so the add/edit form can show them in
     * the room type select list.
     *
     * @return Room types
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @ModelAttribute("roomTypes")
    public List<String> getRoomTypes() {
        return BookingBO.ROOM_TYPES;
    }

    /**
     * Show the list of all bookings sorted by date and start time.
     *
     * @param model Model for the view
     * @return Booking list view
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @GetMapping("")
    public String list(Model model) {
        List<Booking> bookings = bookingRepository.findAllByOrderByBookingDateAscStartTimeAscIdAsc();
        logger.info("Booking list: {} bookings", bookings.size());
        model.addAttribute("bookings", bookings);
        return VIEW_LIST;
    }

    /**
     * Show the form to add a new booking.
     *
     * @param model Model for the view
     * @return Add/edit view with an empty booking
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("booking", new Booking());
        return VIEW_ADD;
    }

    /**
     * Show the add/edit form filled in with an existing booking.
     *
     * @param id                 ID of the booking to edit
     * @param model              Model for the view
     * @param redirectAttributes Used to send an error message to the list if the booking is not found
     * @return Add/edit view, or the list if the booking does not exist
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            redirectAttributes.addFlashAttribute("messageError", "Could not find booking #" + id);
            return REDIRECT_LIST;
        }
        model.addAttribute("booking", booking.get());
        return VIEW_ADD;
    }

    /**
     * Process the add/edit form. Runs the annotation validation (@Valid) and
     * the business validation, calculates the prices, saves the booking and
     * sends the user to the details page. If there are errors the form is shown
     * again with the messages and nothing is saved.
     *
     * @param booking            Booking entered on the form (id is set when editing)
     * @param bindingResult      Validation errors for the booking
     * @param model              Model for the view
     * @param redirectAttributes Used to send the success/error message after the redirect
     * @return Add/edit view with errors, or a redirect to the details page
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @PostMapping("/submit")
    public String submit(@Valid @ModelAttribute("booking") Booking booking, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes) {

        //A booking without an id is a new booking (add); otherwise it is an edit
        boolean newBooking = booking.getId() == null;
        if (!newBooking && !bookingRepository.existsById(booking.getId())) {
            redirectAttributes.addFlashAttribute("messageError",
                    "Could not find booking #" + booking.getId() + " to update");
            return REDIRECT_LIST;
        }

        //Business validation (real date, future date on add, end after start)
        BookingValidationBO.validateBooking(booking, newBooking, LocalDate.now(), bindingResult);

        if (bindingResult.hasErrors()) {
            logger.info("Booking validation errors: {}", bindingResult.getErrorCount());
            bindingResult.getFieldErrors().forEach(fieldError ->
                    logger.debug("{} - {}", fieldError.getField(), fieldError.getDefaultMessage()));
            model.addAttribute("booking", booking);
            return VIEW_ADD;
        }

        //Calculate the prices before saving
        BookingBO.calculatePrices(booking);
        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking saved: {}", savedBooking);

        //Output: show the saved booking with its calculated prices
        redirectAttributes.addFlashAttribute("messageSuccess",
                newBooking ? "Booking added" : "Booking updated");
        return "redirect:/booking/details/" + savedBooking.getId();
    }

    /**
     * Show the details of a booking including the calculated prices.
     *
     * @param id                 ID of the booking
     * @param model              Model for the view
     * @param redirectAttributes Used to send an error message to the list if the booking is not found
     * @return Details view, or the list if the booking does not exist
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            redirectAttributes.addFlashAttribute("messageError", "Could not find booking #" + id);
            return REDIRECT_LIST;
        }
        model.addAttribute("booking", booking.get());
        return VIEW_DETAILS;
    }

    /**
     * Delete a booking (the list page asks the user to confirm first) and go
     * back to the list with a message.
     *
     * @param id                 ID of the booking to delete
     * @param redirectAttributes Used to send the success/error message to the list
     * @return Redirect to the booking list
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 19/11/2026
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (!bookingRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("messageError", "Could not find booking #" + id + " to delete");
            return REDIRECT_LIST;
        }
        try {
            bookingRepository.deleteById(id);
            logger.info("Booking deleted: {}", id);
            redirectAttributes.addFlashAttribute("messageSuccess", "Booking #" + id + " deleted");
        } catch (Exception e) {
            logger.error("Error deleting booking {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("messageError", "Could not delete booking #" + id);
        }
        return REDIRECT_LIST;
    }
}
