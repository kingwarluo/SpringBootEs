package com.kingwarluo.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * 语言国际化 控制器
 * @author kingwarluo
 * @date 2022/11/29 15:24
 */
@RestController
public class LocaleController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping(path = "/test2",produces = "text/html;charset=utf-8")
    public Object test2(){
        Locale locale = LocaleContextHolder.getLocale();
        String hello = messageSource.getMessage("hello", null, locale);
        return hello;
    }

}
