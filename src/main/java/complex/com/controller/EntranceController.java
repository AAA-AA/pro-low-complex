package complex.com.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import complex.com.domain.FormDto;
import complex.com.domain.ParameterDto;
import complex.com.domain.PyParamDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : hongqiangren.
 * @since: 2019/8/25 13:45
 */
@Controller
@RequestMapping(value = "/")
@Slf4j
public class EntranceController {

	@Value("${py.file.savePath}")
	private String fileSavePath;

	@Value("${py.file.readFilePath}")
	private String readFilePath;

	private static Map<String, String> cache = new ConcurrentHashMap<>();

	private static final Integer MAX_SIZE = 100 * 100 * 100;

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

	@RequestMapping(value = "/copy_index", method = RequestMethod.GET)
	public ModelAndView examples() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("IUPred2A");
		return mv;
	}

	/**
	 * 类型为为text或者json
	 *
	 * @return
	 */
	@RequestMapping(value = "/result/{resourceKey}", method = RequestMethod.POST)
	@ResponseBody
	public String getResult(@PathVariable String resourceKey) {
		if (StringUtils.isEmpty(resourceKey)) {
			log.error("resourceKey can't be null");
			return "error";
		}
		if (cache.size() > MAX_SIZE) {
			cache.clear();
			log.info("reach it't max save!");
			return "try from first again";
		}
		String result = cache.get(resourceKey);
		if (StringUtils.isEmpty(result)) {
			log.info("can't find corresponding result");
			return "empty";
		}
		return result;
	}

	@RequestMapping(value = "/submitForm", method = RequestMethod.POST)
	public ModelAndView submitForm(FormDto formDto) throws IOException {
		log.info("receive param: {}", JSON.toJSONString(formDto));
		String filePath = fileSavePath;
		String fileName = formDto.getFile().getOriginalFilename();
		File dest = new File(filePath + fileName);
		try {
			formDto.getFile().transferTo(dest);
			log.info("transfer file success, target dir: {}", dest.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
		List<String> data = new ArrayList<>();
		if (StringUtils.isEmpty(readFilePath)) {
			log.info("py has't config, use py directory files");
			readFilePath = this.getClass().getResource("/py/Model_Predict.py").getPath();
		}
		try {
			log.info("input filePath is: {}, param: {}", readFilePath, JSON.toJSONString(formDto));
			String[] args = new String[]{"python", readFilePath, formDto == null ? "" : JSON.toJSONString(formDto)};
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
		//没有获取到py的执行结果，你先不执行py逻辑，直接返回图片和txt文件试试
		String key = UUID.randomUUID().toString();
		cache.put(key,readTxt(data.get(0)));

		ModelAndView mv = new ModelAndView();
		mv.addObject("imageSrc","/innerImage/"+ data.get(1));
		mv.addObject("resourceKey","/result/"+ key);
		mv.setViewName("plot");
		//提交的请求会在这
		return mv;
	}

	private String readTxt(String txtFilePath) throws IOException {
		String result = "";
		String[] extensions = {"txt"};
		Collection<File> files = FileUtils.listFiles(new File(fileSavePath), extensions, true);
		if (CollectionUtils.isEmpty(files)) {
			throw new RuntimeException("can't not find any file with " + fileSavePath);
		}
		for (File file : files) {
			if (file.getName().equals(txtFilePath)) {
				byte[] bytes = FileUtils.readFileToByteArray(file);
				result = new String(bytes, Charsets.UTF_8.name());
			}
		}
		return result;
	}

	@RequestMapping(value = "/doPy", method = RequestMethod.POST)
	@ResponseBody
	public List<String> doPython(@RequestBody @Valid PyParamDto paramDto) {
		log.info("do py start");
		List<String> data = new ArrayList<>();
		if (StringUtils.isEmpty(readFilePath)) {
			log.info("py has't config, use py directory files");
			String resourcePath = this.getClass().getResource("/py/demo_with_param.py").getPath();
			readFilePath = resourcePath;
		}
		try {
			log.info("input filePath is: {}, param: {}", readFilePath, JSON.toJSONString(paramDto));
			String[] args = new String[]{"python", readFilePath, paramDto == null ? "" : JSON.toJSONString(paramDto)};
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
				File parentDr = new File(fileSavePath);
				if (!parentDr.exists()) {
					parentDr.mkdirs();
				}
				File moveFile = new File(fileSavePath + "/" + file.getOriginalFilename());
				if (!moveFile.exists()) {
					moveFile.createNewFile();
				}
				file.transferTo(moveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

	@RequestMapping(value = "/innerImage/{imageName}", method = RequestMethod.GET)
	@CrossOrigin(origins = "*")
	public void image(@PathVariable String imageName, HttpServletResponse response) {
		if (StringUtils.isEmpty(imageName)) {
			return;
		}
		ByteArrayOutputStream out = null;
		try {
			File file = new File(fileSavePath + "/results/" + imageName);
			if (!file.exists()) {
				log.error("文件不存在！");
			}
			out = new ByteArrayOutputStream();
			BufferedImage bufferedImage = ImageIO.read(file);
			ImageIO.write(bufferedImage, "png", out);
			response.setContentType("image/png");// 设置response内容的类型
			OutputStream os = response.getOutputStream();
			os.write(out.toByteArray());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {

			}
		}
	}

}
