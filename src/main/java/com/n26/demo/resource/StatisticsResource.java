package com.n26.demo.resource;

import com.n26.demo.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsResource {

    @Autowired
    private StatisticService statisticService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String getStatistics() {
        return statisticService.getStatistics();
    }
}
