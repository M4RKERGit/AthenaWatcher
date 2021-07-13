package com.athena;

import com.athena.hardware.HWInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AthenaAPI {

    private final VisitsRepository visitsRepository;

    public AthenaAPI(VisitsRepository visitsRepository) {this.visitsRepository = visitsRepository;}

    @GetMapping("/visits")
    public Iterable<Visitor> getVisits() {return visitsRepository.findAll();}

    @RequestMapping(value = "/hwinfo", method = RequestMethod.GET, produces = "application/json")
    public String getHWInfo()
    {
        HWInfo info = new HWInfo();
        ObjectMapper JSONMapper = new ObjectMapper();
        JSONMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String buf = "";
        try {buf = JSONMapper.writeValueAsString(info);}
        catch (JsonProcessingException e) {e.printStackTrace();}
        return buf;
    }
}