package com.korczak.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/**
 * Created by Korczi on 2017-04-01.
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{

    protected Class<?>[] getRootConfigClasses()
    {
        return new Class<?>[]{AppConfiguration.class};
    }

    protected Class<?>[] getServletConfigClasses()
    {
        return null;
    }

    protected String[] getServletMappings()
    {
        return new String[]{"/"};
    }


    //************* MULTIPART SECTION ********************
    private static final String LOCATION = "/Users/jkorczak/Desktop/Temp";

    private static final long MAX_FILE_SIZE = 20971520;
    private static final long MAX_REQUEST_SIZE = 20971520;
    private static final int FILE_SIZE_THRESHOLD = 0;


    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration)
    {

        registration.setMultipartConfig(getMultipartConfigElement());
    }

    private MultipartConfigElement getMultipartConfigElement()
    {
        MultipartConfigElement multipartConfigElement =
                new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }
}
