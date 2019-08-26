package complex.com;

import complex.com.common.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;


@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication.run(Application.class, args);
        String port = SpringUtils.getProperty("server.port");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        //log.info("测试接口页面访问地址: http://{}:{}/doc.html",hostAddress,port);
    }
}
