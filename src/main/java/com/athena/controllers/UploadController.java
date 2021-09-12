package com.athena.controllers;

import com.athena.linuxtools.Additional;
import com.athena.linuxtools.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class UploadController
{
    private static final String UPLOADED_FOLDER = "uploads/";
    private static final Logger logger = new Logger("[UPL]");

    public UploadController()
    {
        if (Files.exists(Path.of(UPLOADED_FOLDER))) {logger.createLog("Upload folder found");}
        else try
            {
                var path = Files.createDirectory(Path.of(UPLOADED_FOLDER));
                logger.createLog("Created directory: " + path);
            }
            catch (IOException e) {logger.createLog("Error creating a directory");}
    }

    @GetMapping("/")
    public ModelAndView slashIndex()
    {
        logger.createLog("Visited Cloud");
        return new ModelAndView("uploadForm.html", new HashMap<>());
    }

    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String singleFileUpload(@RequestParam("file") MultipartFile file)
    {
        try
        {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            logger.createLog("Saved " + path);
            Files.write(path, bytes);
        }
        catch (IOException e) {logger.createLog("Error saving file");}
        return "Upload finished, go back";
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
    public String getFiles()
    {
        List<Path> buf = Additional.listUploadedFiles(UPLOADED_FOLDER);
        if (buf == null) return "";
        ArrayList<String> toRet = new ArrayList<>();
        for (Path path : buf)
        {
            String[] fileName = path.toString().split("/");
            toRet.add(fileName[fileName.length - 1]);
        }
        ObjectMapper JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {return JSONMapper.writeValueAsString(toRet);}
        catch (JsonProcessingException e) {logger.createLog("Files refresh error");}
        return "";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> sendFile(@PathVariable String filename)
    {
        Resource file = null;
        try
        {
            Path filePath = Path.of(UPLOADED_FOLDER).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {file = resource;}
            else {logger.createLog("Error");}
        }
        catch (MalformedURLException e) {logger.createLog("Error");}
        assert file != null;
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
