package com.korczak.controllers;

import com.korczak.domain.FileU;
import com.korczak.fileDB.Database;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Korczi on 2017-03-03.
 */
@Controller
@RequestMapping("/form")
public class FormController
{

    private final static String DRV = "org.sqlite.JDBC";
    private final static String DB = "jdbc:sqlite:FileInfo.db";

    public static void splitFile(File f) throws IOException
    {
        int partCounter = 1;
        String tempFolderPath = "/Users/jkorczak/Desktop/TempFolder";
        new File(tempFolderPath).mkdir(); //Create temporary folder



        int sizeOfFiles = 1024;// 1KB - one chunk size
        byte[] buffer = new byte[sizeOfFiles];

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f)))
        {
            String name = f.getName();

            int tmp = 0;
            while ((tmp = bis.read(buffer)) > 0)
            {
                File newFile = new File(tempFolderPath,  name + "." + String.format("%03d", partCounter++));

                try (FileOutputStream out = new FileOutputStream(newFile))
                {
                    out.write(buffer, 0, tmp);
                }
            }
        }
    }

    public static void mergeFiles(List<File> files, File into)
            throws IOException
    {
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(new FileOutputStream(into)))
        {

            for (File f : files)
            {
                Files.copy(f.toPath(), mergingStream);
            }

        }
    }

    public File convertMultipartToFile (MultipartFile file) throws IOException
    {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public List<File> createListOfChunks()
    {
        File f = new File("/Users/jkorczak/Desktop/TempFolder/");
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));

        for (int i = 0; i < files.size(); i++)
        {
            System.out.println(files.get(i));
        }

        //files.stream().forEach(e -> System.out.println(e.getName()));
        //files.stream().forEach(System.out::println);

        return files;
    }

    public List<MultipartFile> convertFileToMultipartFiles(List<File> files) throws IOException
    {
        List<MultipartFile> listaMultipart = new ArrayList<>();
        for (File f: files)
        {
            FileInputStream input = new FileInputStream(f);
            MultipartFile multipartFile = new MockMultipartFile("file", f.getName(), "text/plain", IOUtils.toByteArray(input));

            listaMultipart.add(multipartFile);
        }

        return listaMultipart;
    }


    @RequestMapping(value = "" , method = RequestMethod.GET)
    public String formGet() throws IOException
    {
        List<MultipartFile> listaMultipart = convertFileToMultipartFiles(createListOfChunks());


        Database.connect(DRV, DB);
        Database.createTables();

        File f = new File("/Users/jkorczak/Desktop/TempFolder/");
        Database.insertFile(f.getName(), f.getAbsolutePath(), listaMultipart.size());


        //System.out.println("DATABASE:");
        //System.out.println(Database.selectFiles());

        return "form";
    }

    @RequestMapping(value = "" , method = RequestMethod.POST)
    public String formPost(@ModelAttribute FileU p, BindingResult result, HttpServletRequest request, Model model) throws IOException
    {
        if (result.hasErrors())
        {

            System.out.println("================BINDING RESULTS=====================");
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println (error.getObjectName() + " - " + error.getDefaultMessage());
            }
            System.out.println("====================================================");

            FileU p1 = new FileU();
            model.addAttribute("form", p1);

            return "form";
        }

        //********** UPLOADING ***************

        try
        {
            List<MultipartFile> listaMultipart = convertFileToMultipartFiles(createListOfChunks());

            int chunkNumb = listaMultipart.size();
            int x = 1;
            int idx = 0;

            for(int i = 0; i < listaMultipart.size(); i++)
            {
                FileCopyUtils.copy(listaMultipart.get(i).getBytes(), new File("/Users/jkorczak/Desktop/Test2/" + listaMultipart.get(i).getOriginalFilename()));
                System.out.println(listaMultipart.get(i).getOriginalFilename());
                Database.updateChunksNumber(1, chunkNumb - x);
                x++;

                System.out.println(Database.selectFiles() + " liczna chunksow? \n");

                idx = i;

                if (result.hasErrors())
                {
                    System.out.println("Error! Only " + i + " chunks uploaded");
                    break;
                }

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return "redirect:/test";
    }
}
