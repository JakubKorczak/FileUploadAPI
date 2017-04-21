package com.korczak.domain;

import org.springframework.web.multipart.MultipartFile;


/**
 * Created by Korczi on 2017-04-01.
 */
public class FileU
{
    private MultipartFile multipartFile;
    private String filePath;

    public FileU()
    {
    }

    public MultipartFile getMultipartFile()
    {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile)
    {
        this.multipartFile = multipartFile;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

}
