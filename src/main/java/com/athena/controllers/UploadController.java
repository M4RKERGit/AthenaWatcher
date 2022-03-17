package com.athena.controllers;

import com.athena.database.RegistrationFormModel;
import com.athena.services.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController
{
    private final UploadService service;

    @GetMapping("/")
    public ModelAndView slashIndex()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) return service.responseSlash();
        return new ModelAndView("registrationForm.html", new HashMap<>());
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String registerUser(@RequestBody RegistrationFormModel model) {return service.registerUser(model);}

    @RequestMapping(method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {return service.singleUpload(file);}

    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = "application/json")
    public String getFiles() {return service.getAllFiles();}

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> sendFile(@PathVariable String filename) {return service.downloadFile(filename);}
}
