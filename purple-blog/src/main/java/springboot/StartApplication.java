package springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("springboot.dao")
@EnableScheduling
public class StartApplication {

    public static void main(String[] args) {



//        SpringApplication.run(DemoMybatisApplication.class, args);

        SpringApplication app=new SpringApplication(StartApplication.class);

        app.setBannerMode(Banner.Mode.CONSOLE);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);




    }





}
