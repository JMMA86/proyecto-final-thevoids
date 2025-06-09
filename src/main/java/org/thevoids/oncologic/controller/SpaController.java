package org.thevoids.oncologic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to handle SPA (Single Page Application) routing.
 * This ensures that React Router handles client-side routing properly.
 */
@Controller
public class SpaController {

    /**
     * Forward any unmapped paths to React's index.html
     * This handles React Router's client-side routing
     */
    @RequestMapping(value = {
            "/",
            "/dashboard",
            "/login",
            "/appointments/**",
            "/appointment-types/**",
            "/schedules/**",
            "/tasks/**",
            "/clinic-assignments/**",
            "/patients/**",
            "/medical-histories/**",
            "/doctors/**",
            "/labs/**",
            "/clinics/**",
            "/specialties/**",
            "/users/**",
            "/permissions/**",
            "/profile/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
