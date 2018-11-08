package springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("springboot.dao")
public class StartApplication {

    public static void main(String[] args) {



//        SpringApplication.run(DemoMybatisApplication.class, args);

        SpringApplication app=new SpringApplication(StartApplication.class);

        app.setBannerMode(Banner.Mode.CONSOLE);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);




    }





}
