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

/*
* Utilities for writing to and from the JSON file.
*
* Alexander Fendyur
* Claude
* 27/9/2026
*/
public class BookingDataOptions {
    public static final String DIR = "c:\\cis2232";
    public static final String FILE_NAME = "data_fendyur_alexander.json";

    private static final Path FILE_PATH = Paths.get(DIR, FILE_NAME);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type BOOKING_LIST_TYPE = new TypeToken<List<Booking>>() {}.getType();

    /*
     * Ensures the desired directory exists, and creates it if it doesn't.
     *
     * Alexander Fendyur
     * Claude
     * 27/9/2026
     */
    public static void ensureDir() throws Exception{
        Files.createDirectories(FILE_PATH.getParent());
    }

    /*
     * Loads bookings from the JSON file, or creates a new list if they don't exist.
     *
     * Alexander Fendyur
     * Claude
     * 27/9/2026
     */
    public static List<Booking> load() throws Exception{
        if(!Files.exists(FILE_PATH)){
            return new ArrayList<>();
        }
        try(Reader read = Files.newBufferedReader(FILE_PATH, StandardCharsets.UTF_8)){
            List<Booking> bookings = GSON.fromJson(read, BOOKING_LIST_TYPE);
            return bookings != null ? bookings : new ArrayList<>();
        }
    }

    /*
     * Saves all bookings to the JSON file, replacing any preexisting ones.
     *
     * Alexander Fendyur
     * Claude
     * 27/9/2026
     */
    public static void save(List<Booking>bookings) throws Exception{
        ensureDir();
        try(Writer write = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8)){
            GSON.toJson(bookings, BOOKING_LIST_TYPE, write);
        }
    }

    public static Path getFilePath(){
        return FILE_PATH;
    }
}