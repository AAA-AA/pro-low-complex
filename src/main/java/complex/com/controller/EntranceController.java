package complex.com.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import complex.com.domain.ParameterDto;
import complex.com.domain.PyParamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : hongqiangren.
 * @since: 2019/8/25 13:45
 */
@Controller
@RequestMapping(value = "/")
@Slf4j
public class EntranceController {

    @Value("${py.file.savePath}")
    private String pyFileSavePath;

    @Value("${py.file.readFilePath}")
    private String readFilePath;

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

    @RequestMapping(value = "/doPy", method = RequestMethod.POST)
    @ResponseBody
    public List<String> doPython(@RequestBody PyParamDto paramDto) {
        log.info("do py start");
        List<String> data = new ArrayList<>();
        if (StringUtils.isEmpty(readFilePath)) {
            log.info("py has't config, use py directory files");
            String resourcePath = this.getClass().getResource("/py/demo_with_param.py").getPath();
            readFilePath = resourcePath;
        }

        try {
            log.info("input filePath is: {}, param: {}", readFilePath, JSON.toJSONString(paramDto));
            String[] args = new String[]{"python", readFilePath, "参数1", "参数2", paramDto == null ? "参数3" : JSON.toJSONString(paramDto)};
            Process pr = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //此处读取的是py脚本执行时print的值
                log.info("py execute result: {}", line);
                data.add(line);
            }
            in.close();
            pr.waitFor();
        } catch (IOException e) {
            log.error("doPython error", e);
        } catch (InterruptedException e) {
            log.error("doPython error", e);
        }
        log.info("end");
        return data;
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
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (suffix.equals("py")) {
            try {
                // todo window 下目录结构为\\
                File parentDr = new File(pyFileSavePath);
                if (!parentDr.exists()) {
                    parentDr.mkdirs();
                }
                File moveFile = new File(pyFileSavePath + "/" + file.getOriginalFilename());
                if (!moveFile.exists()) {
                    moveFile.createNewFile();
                }
                file.transferTo(moveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


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
