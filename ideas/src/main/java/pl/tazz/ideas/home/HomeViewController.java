package pl.tazz.ideas.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeViewController {

    @GetMapping("")
    public String homeView() {
        return "home/index";
    }
}
