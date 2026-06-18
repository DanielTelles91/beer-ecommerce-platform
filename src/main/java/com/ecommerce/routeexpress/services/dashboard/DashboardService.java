package com.ecommerce.routeexpress.services.dashboard;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Daniel Arantes Telles
 */

@Service
public class DashboardService {

    private final JdbcTemplate jdbcTemplate;

    public DashboardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean checkAngular() {

        try {

            RestTemplate rt = new RestTemplate();

            rt.getForObject(
                    "http://127.0.0.1:4200/cervejas",
                    String.class);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public boolean checkMysql() {

        try {

            jdbcTemplate.queryForObject(
                    "SELECT 1",
                    Integer.class);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}


