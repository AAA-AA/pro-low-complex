package complex.com.controller;

import com.alibaba.fastjson.JSON;
import complex.com.domain.ParameterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : hongqiangren.
 * @since: 2019/8/25 13:45
 */
@Controller
@Slf4j
public class EntranceController {


    /**
     * 获取首页接口
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

    /**
     * 上传文件接口, 业务逻辑 todo
     *
     * @param file
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public void upload(@RequestParam("file") MultipartFile file) {
        log.info("upload file: {}", JSON.toJSONString(file.getOriginalFilename()));
    }

    /**
     * 参数设置接口, 业务逻辑 todo
     */
    @RequestMapping(value = "/parameter", method = RequestMethod.POST)
    @ResponseBody
    public void parameter(@RequestBody ParameterDto parameterDto) {
        log.info("receive parameterDto: {}", JSON.toJSONString(parameterDto));

    }

    @GetMapping("/helloWorld")
    public Map helloWorld() {
        Map map = new HashMap();
        map.put("code", 0);
        map.put("data", "message");
        return map;
    }


}
