package ca.hccis.squash.controllers;

import ca.hccis.squash.bo.MonthlyRoomBookingsReportBO;
import ca.hccis.squash.bo.SkillsAssessmentSquashTechnicalBO;
import ca.hccis.squash.dao.SkillsAssessmentSquashTechnicalDAO;
import ca.hccis.squash.entity.ReportMonthlyRoomBookings;
import ca.hccis.squash.entity.ReportSquash;
import ca.hccis.squash.jpa.entity.SkillsAssessmentSquashTechnical;
import ca.hccis.squash.util.CisUtilityFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Controller to administer reports of the project.
 *
 * @author BJM
 * @since 20251009
 * @modifiedby Claude (AI) 2026-10-29 Added the monthly room bookings report (sprint 2)
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    /**
     * Send the user to list of reports view.
     *
     * @param model
     * @param session
     * @return To the appropriate view
     * @author BJM
     * @since 20251009
     */
    @RequestMapping("")
    public String home(Model model, HttpSession session) {

        //BJM 20200602 Issue#1 Set the current date in the session
        logger.info("Running the reports controller base method");
        return "report/list";
    }

    /**
     * Method to send user to the athlete/assessor name report.
     *
     * @param model
     * @return view for list
     * @author BJM
     * @since 2025-10-06
     */
    @RequestMapping("/squash/name")
    public String reportSquashName(Model model) {
        logger.info("Running the reports controller squash name method");
        model.addAttribute("reportInput",new ReportSquash());
        return "report/reportSquashName";
    }

    /**
     * Method to send user to the score name report.
     *
     * @param model
     * @return view for list
     * @author BJM
     * @since 2025-10-09
     */
    @RequestMapping("/squash/score")
    public String reportSquashScore(Model model) {
        logger.info("Running the reports controller squash score method");
        model.addAttribute("reportInput",new ReportSquash());
        return "report/reportSquashScore";
    }


    /**
     * Process the report - name
     *
     * @param model
     * @param reportSquash Object containing inputs for the report
     * @return view to show report
     * @author BJM
     * @since 2025-10-06
     */
    @RequestMapping("/squash/name/submit")
    public String reportSquashNameSubmit(Model model, @ModelAttribute("reportInput") ReportSquash reportSquash) {

        System.out.println("Name from input form:"+reportSquash.getName());

        //Write some model code to go to the db and get the appropriate assessments
        //Add them to a collection in the ReportSquash class
        SkillsAssessmentSquashTechnicalBO skillsAssessmentSquashTechnicalBO = new SkillsAssessmentSquashTechnicalBO();
        ArrayList<SkillsAssessmentSquashTechnical> theList = skillsAssessmentSquashTechnicalBO.processSelectAllByAthleteAssessorName(reportSquash.getName());
        reportSquash.setSkillsAssessmentSquashTechnicals(theList);

        //Add a message in case the report does not contain any data
        if (theList != null && theList.isEmpty()) {
            model.addAttribute("message", "No assessments found for that name");
            System.out.println("BJM - no data found");
        }

        //Put object in model so it can be used on the view (html)
        model.addAttribute("reportInput", reportSquash);

        return "report/reportSquashName"; //Send user to another view.
    }

    /**
     * Process the report - name
     *
     * @param model
     * @param reportSquash Object containing inputs for the report
     * @return view to show report
     * @author BJM
     * @since 2025-10-06
     */
    @RequestMapping("/squash/score/submit")
    public String reportSquashScoreSubmit(Model model, @ModelAttribute("reportInput") ReportSquash reportSquash) {

        System.out.println("Min from input form:"+reportSquash.getMinScore());

        //Write some model code to go to the db and get the appropriate assessments
        //Add them to a collection in the ReportSquash class
        SkillsAssessmentSquashTechnicalDAO squashDAO = new SkillsAssessmentSquashTechnicalDAO();
        ArrayList<SkillsAssessmentSquashTechnical> theList = squashDAO.selectAllByScoreMinMax(reportSquash.getMinScore(), reportSquash.getMaxScore());
        reportSquash.setSkillsAssessmentSquashTechnicals(theList);

        //Add a message in case the report does not contain any data
        if (theList != null && theList.isEmpty()) {
            model.addAttribute("message", "No assessments found for that name");
            System.out.println("BJM - no data found");
        }

        //Put object in model so it can be used on the view (html)
        model.addAttribute("reportInput", reportSquash);

        return "report/reportSquashScore"; //Send user to another view.
    }

    //Claude (AI) 2026-10-29 Added monthly room bookings report

    /**
     * Send the user to the Monthly Room Bookings report input view.
     *
     * @param model Model for the view
     * @return Monthly room bookings report view
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    @RequestMapping("/booking/monthly")
    public String reportMonthlyRoomBookings(Model model) {
        logger.info("Running the reports controller monthly room bookings method");
        model.addAttribute("reportInput", new ReportMonthlyRoomBookings());
        return "report/reportMonthlyRoomBookings";
    }

    /**
     * Process the Monthly Room Bookings report: validate the year/month, get the
     * booking counts from the database (JDBC), write the report file and send
     * the user back to the report view to see the results.
     *
     * @param model         Model for the view
     * @param report        Object containing the year and month entered
     * @param bindingResult Binding/validation errors for the inputs
     * @return Monthly room bookings report view showing errors or the results
     * @author Alexander Fendyur
     * @author Claude (AI)
     * @since 29/10/2026
     */
    @RequestMapping("/booking/monthly/submit")
    public String reportMonthlyRoomBookingsSubmit(Model model,
                                                  @ModelAttribute("reportInput") ReportMonthlyRoomBookings report,
                                                  BindingResult bindingResult) {

        //Input validation (only if the value could be converted to a number)
        if (!bindingResult.hasFieldErrors("year")) {
            String yearError = MonthlyRoomBookingsReportBO.validateYear(report.getYear());
            if (yearError != null) {
                bindingResult.rejectValue("year", "invalid.year", yearError);
            }
        }
        if (!bindingResult.hasFieldErrors("month")) {
            String monthError = MonthlyRoomBookingsReportBO.validateMonth(report.getMonth());
            if (monthError != null) {
                bindingResult.rejectValue("month", "invalid.month", monthError);
            }
        }
        if (bindingResult.hasErrors()) {
            logger.info("Monthly room bookings report validation errors: {}", bindingResult.getErrorCount());
            return "report/reportMonthlyRoomBookings";
        }

        //Get the counts from the database and build the 12 variations
        try {
            MonthlyRoomBookingsReportBO.processReport(report);
        } catch (SQLException e) {
            logger.error("Monthly room bookings report database error: {}", e.getMessage());
            model.addAttribute("errorMessage", "Could not load the bookings from the database.");
            return "report/reportMonthlyRoomBookings";
        }

        //Output: period description for the view and a message if there are no bookings
        model.addAttribute("reportPeriod",
                MonthlyRoomBookingsReportBO.buildPeriodDescription(report.getYear(), report.getMonth()));
        if (report.getTotalBookings() == 0) {
            model.addAttribute("message", "No bookings found for that month");
        }

        //Write the results to the report file in c:\cis2232\
        try {
            Path reportFilePath = MonthlyRoomBookingsReportBO.writeReportFile(report,
                    CisUtilityFile.REPORT_FOLDER_NAME, LocalDateTime.now());
            report.setReportFilePath(reportFilePath.toString());
            logger.info("Monthly room bookings report written to {}", reportFilePath);
        } catch (IOException e) {
            logger.error("Monthly room bookings report file error: {}", e.getMessage());
            model.addAttribute("fileErrorMessage", "The report file could not be written: " + e.getMessage());
        }

        //Put object in model so it can be used on the view (html)
        model.addAttribute("reportInput", report);
        return "report/reportMonthlyRoomBookings";
    }
}
