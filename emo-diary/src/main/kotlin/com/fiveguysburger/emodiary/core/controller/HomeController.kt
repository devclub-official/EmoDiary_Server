package com.fiveguysburger.emodiary.core.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {
    @GetMapping("/")
    fun home(): String {
        return "redirect:/swagger-ui.html"
    }
}
