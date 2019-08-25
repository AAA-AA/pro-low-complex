package complex.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : hongqiangren.
 * @since: 2019/8/25 13:45
 */
@Controller
public class ApiController {


    @RequestMapping(value = "/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/helloWorld")
    public Map helloWorld() {
        Map map = new HashMap();
        map.put("code", 0);
        map.put("data", "message");
        return map  ;
    }



}
