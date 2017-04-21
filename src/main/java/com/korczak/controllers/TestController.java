package com.korczak.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Korczi on 2017-01-27.
 */
@Controller
@RequestMapping("/test")
public class TestController
{
    @RequestMapping
    public String test(Model m)
    {
        String nap = "Success!";
        m.addAttribute("napis", nap);

        String nap2 = "File sucessfully uploaded!";
        m.addAttribute("nap", nap2);

        return "welcome";
    }
}
