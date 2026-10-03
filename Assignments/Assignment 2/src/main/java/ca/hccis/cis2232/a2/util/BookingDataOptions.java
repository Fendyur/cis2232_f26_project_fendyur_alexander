package ca.hccis.cis2232.a2.util;

import ca.hccis.cis2232.a2.data.Booking;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.reflect.Type;
import java.io.Reader;
import java.io.Writer;

/**
* Utilities for writing to and from the JSON file.
*
* @author Alexander Fendyur
* @author Claude
* @since 27/9/2026
*/
public class BookingDataOptions {
    public static final String DIR = "c:\\cis2232";
    public static final String FILE_NAME = "data_fendyur_alexander.json";

    private static final Path FILE_PATH = Paths.get(DIR, FILE_NAME);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type BOOKING_LIST_TYPE = new TypeToken<List<Booking>>() {}.getType();

    /**
     * Ensures the desired directory exists, and creates it if it doesn't.
     *
     * @throws Exception if the directory cannot be created
     * @author Alexander Fendyur
     * @author Claude
     * @since 27/9/2026
     */
    public static void ensureDir() throws Exception{
        Files.createDirectories(FILE_PATH.getParent());
    }

    /**
     * Loads bookings from the JSON file, or creates a new list if they don't exist.
     *
     * @return the saved bookings, or an empty list if there are none
     * @throws Exception if the file exists but cannot be read
     * @author Alexander Fendyur
     * @author Claude
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-03 Renamed the Reader variable from read to reader.
     */
    public static List<Booking> load() throws Exception{
        //No file yet means no bookings have been saved
        if(!Files.exists(FILE_PATH)){
            return new ArrayList<>();
        }
        try(Reader reader = Files.newBufferedReader(FILE_PATH, StandardCharsets.UTF_8)){
            List<Booking> bookings = GSON.fromJson(reader, BOOKING_LIST_TYPE);
            //An empty file reads as null, so return an empty list instead
            return bookings != null ? bookings : new ArrayList<>();
        }
    }

    /**
     * Saves all bookings to the JSON file, replacing any preexisting ones.
     *
     * @param bookings the bookings to save
     * @throws Exception if the file cannot be written
     * @author Alexander Fendyur
     * @author Claude
     * @since 27/9/2026
     *
     * @modifiedby Claude (AI) 2026-10-03 Renamed the Writer variable from write to writer.
     */
    public static void save(List<Booking>bookings) throws Exception{
        ensureDir();
        try(Writer writer = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8)){
            GSON.toJson(bookings, BOOKING_LIST_TYPE, writer);
        }
    }

    public static Path getFilePath(){
        return FILE_PATH;
    }
}