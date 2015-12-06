package com.harlyn.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by wannabe on 15.11.15.
 */
@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig extends WebMvcAutoConfiguration {
}
