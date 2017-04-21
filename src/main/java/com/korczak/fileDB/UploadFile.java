package com.korczak.fileDB;

/**
 * Created by jkorczak on 13/04/2017.
 */
public class UploadFile
{
    private int id;
    private String fileName;
    private String filePath;
    private int numberOfChunks;

    public UploadFile(int id, String fileName, String filePath, int numberOfChunks)
    {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.numberOfChunks = numberOfChunks;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public int getNumberOfChunks()
    {
        return numberOfChunks;
    }

    public void setNumberOfChunks(int numberOfChunks)
    {
        this.numberOfChunks = numberOfChunks;
    }

    @Override
    public String toString()
    {
        return "UploadFile{" +
                       "id=" + id +
                       ", fileName='" + fileName + '\'' +
                       ", filePath='" + filePath + '\'' +
                       ", numberOfChunks=" + numberOfChunks +
                       '}';
    }
}
