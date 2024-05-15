package com.seanatives.SurfCoursePlanner.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.seanatives.SurfCoursePlanner.domain.CsvBooking;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class BookingParserService {

    public static List<CsvBooking> parseBookings(Path csvPath) throws Exception {
        try (Reader reader = Files. newBufferedReader(csvPath)) {
            CsvToBean<CsvBooking> csvToBean = new CsvToBeanBuilder<CsvBooking>(reader)
                    .withType(CsvBooking.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
