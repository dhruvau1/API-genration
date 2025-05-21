package com.mphasis.app.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mphasis.app.APIAutomation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GenerateController {

    @PostMapping("/generate")
    public ResponseEntity<String> generateCode(@RequestBody JsonNode inputJson,
            @RequestParam String savePath) {
        
        try{

            APIAutomation.run(inputJson, savePath);
            return ResponseEntity.ok("Code generation complete.");
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/testConnection")
    ResponseEntity<?> testFunction()
    {
        return ResponseEntity.ok("the connection is working");
    }
}
