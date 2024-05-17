package com.seanatives.SurfCoursePlanner;

import com.seanatives.SurfCoursePlanner.services.DataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartUp implements CommandLineRunner {

    @Autowired
    private DataLoaderService dataLoaderService;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("do nothing for now. The loading has been commented out");

        // dataLoadingService.loadData();
    }
}

