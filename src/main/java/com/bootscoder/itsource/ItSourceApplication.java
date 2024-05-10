package com.bootscoder.itsource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@ServletComponentScan// 图片验证码要用这个玩意儿
@MapperScan("com.bootscoder.itsource.mapper")
public class ItSourceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ItSourceApplication.class, args);
    }

    // 注册分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
