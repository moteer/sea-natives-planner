package com.seanatives.SurfCoursePlanner.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.seanatives.SurfCoursePlanner.domain.Booking;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BookingParser {

    public static List<Booking> parseBookings(Path csvPath) throws Exception {
        try (Reader reader = Files. newBufferedReader(csvPath)) {
            CsvToBean<Booking> csvToBean = new CsvToBeanBuilder<Booking>(reader)
                    .withType(Booking.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
