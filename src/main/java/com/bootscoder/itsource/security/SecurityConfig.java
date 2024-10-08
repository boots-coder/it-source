package com.bootscoder.itsource.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Security配置类
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // Spring Security配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义表单登录
        http.formLogin()
                .loginPage("/backstage/admin_login") // 自定义登录页面
                .usernameParameter("username") // 用户名项
                .passwordParameter("password") // 密码项
                .loginProcessingUrl("/backstage/admin/login") // 表单提交路径
                .successForwardUrl("/backstage/index") // 登录成功页面
                .failureForwardUrl("/backstage/admin_fail"); // 登录失败页面


        // 权限拦截配置
        http.authorizeRequests()
//                .antMatchers("/backstage/product/upload").permitAll() 测试和修改图片上传功能的时候弄的
                .antMatchers("/backstage/admin/login").permitAll() // 登录不需要认证
                .antMatchers("/backstage/admin_fail").permitAll()// 登录失败不需要认证
                .antMatchers("/backstage/admin_login").permitAll()// 登录页不需要认证
                .antMatchers("/**/*.css","/**/*.js").permitAll() // 放行静态资源
                .antMatchers("/backstage/**").authenticated();// 其余都需要认证

        // 退出登录配置
        http.logout()
                .logoutUrl("/backstage/admin/logout") // 退出登录路径
                .logoutSuccessUrl("/backstage/admin_login") // 退出登录成功后跳转的页面
                .clearAuthentication(true) // 退出成功后清除认证信息
                .invalidateHttpSession(true); // 退出成功后清除session

        // 异常处理
        http.exceptionHandling()
                .accessDeniedHandler(new MyAccessDeniedHandler()); //权限不足处理器

        // 关闭csrf防护
        http.csrf().disable();
        // 开启跨域访问
        http.cors();
    }

    // 密码加密器
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
