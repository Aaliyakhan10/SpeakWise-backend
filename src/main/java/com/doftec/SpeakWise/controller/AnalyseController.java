package com.doftec.SpeakWise.controller;

import com.doftec.SpeakWise.dto.AiResponse;
import com.doftec.SpeakWise.dto.AnalyseDto;
import com.doftec.SpeakWise.service.AiService;
import com.doftec.SpeakWise.service.AnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyseController {

    public AnalyseService analyseService;
    public final AiService aiService;
    public AnalyseController(AnalyseService analyseService , AiService aiService){
        this.analyseService=analyseService;
        this.aiService=aiService;
    }
    @PostMapping("/analyse")
    public ResponseEntity<String> getAnalyses(@RequestBody AnalyseDto analyseDto){
       return ResponseEntity.ok(analyseService.analyseTest(analyseDto.getSpeech()));

    }
    @PostMapping("/analyseAi")
    public ResponseEntity<AiResponse> getAIAnalysis(@RequestBody AnalyseDto analyseDto){
        return ResponseEntity.ok(aiService.callAi(analyseDto.getSpeech()));

    }

}
