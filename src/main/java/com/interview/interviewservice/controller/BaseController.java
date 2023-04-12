package com.interview.interviewservice.controller;

import com.interview.interviewservice.model.Model;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public abstract class BaseController<M extends Model, ID extends Serializable> implements Serializable{

    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    private final char PATH_SEPARATOR = File.pathSeparatorChar;

    @PostConstruct
    private void init(){
        RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);

        if(requestMapping == null){
            logger.warn("{} does not have a RequestMapping annotation.", this.getClass().getSimpleName());
        }

    }
}
