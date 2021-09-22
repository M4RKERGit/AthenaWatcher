package com.athena.controllers;

import com.athena.services.UploadService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/upload")
public class UploadController
{
    private final UploadService service = new UploadService();

    @GetMapping("/")
    public ModelAndView slashIndex() {return service.responseSlash();}

    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {return service.singleUpload(file);}

    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
    public String getFiles() {return service.getAllFiles();}

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> sendFile(@PathVariable String filename) {return service.downloadFile(filename);}
}
