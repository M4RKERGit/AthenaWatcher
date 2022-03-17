package com.athena.services;

import com.athena.AthenaSettings;
import com.athena.linuxtools.Additional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

@Service
@Slf4j
public class UploadService
{
    private static final String UPLOADED_FOLDER = AthenaSettings.getUploadFolder();

    public UploadService()
    {
        if (Files.exists(Path.of(UPLOADED_FOLDER))) {log.info("Upload folder found");}
        else try
        {
            var path = Files.createDirectory(Path.of(UPLOADED_FOLDER));
            log.info("Created directory: " + path);
        }
        catch (IOException e) {log.info("Error creating a directory");}
    }

    public ModelAndView responseSlash()
    {
        log.info("Visited Cloud");
        return new ModelAndView("uploadForm.html", new HashMap<>());
    }

    public String singleUpload(MultipartFile file)
    {
        try
        {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            log.info("Saved " + path);
            Files.write(path, bytes);
        }
        catch (IOException e) {log.info("Error saving file");}
        return "Upload finished, go back";
    }

    public String getAllFiles()
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
        catch (JsonProcessingException e) {log.info("Files refresh error");}
        return "";
    }

    public ResponseEntity<Resource> downloadFile(String filename)
    {
        Resource file = null;
        try
        {
            Path filePath = Path.of(UPLOADED_FOLDER).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {file = resource;}
            else {log.info("Error");}
        }
        catch (MalformedURLException e) {log.info("Error");}
        assert file != null;
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
