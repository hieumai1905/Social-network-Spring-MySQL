package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.services.statistics.StatisticsService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic")
public class ApisStatisticController extends ApplicationController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/{year}")
    public ResponseEntity<ApiResponse> statistic(@PathVariable(value = "year") Integer year){
        try{
            return responseApi(HttpStatus.OK, "Get statistic successfully!",
                    statisticsService.getStatisticByYear(year));
        }catch(Exception e){
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
