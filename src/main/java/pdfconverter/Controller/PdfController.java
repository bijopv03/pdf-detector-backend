package pdfconverter.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pdfconverter.Dto.PdfRequest;
import pdfconverter.Service.GroqService;
import pdfconverter.Service.PdfService;

import java.util.Map;

@RestController
@RequestMapping("/api/pdfcon")
@CrossOrigin("*")

public class PdfController {
    private final PdfService pdfService;
    private final GroqService groqService;

    public PdfController(PdfService pdfService, GroqService groqService) {
        this.pdfService = pdfService;
        this.groqService = groqService;
    }
    @PostMapping("/analyze")
    public ResponseEntity<?>analyzePdf(@RequestBody PdfRequest request){
        System.out.println("Pdf Url Recieved"+request.getPdfUrl());
        if (request.getPdfUrl()==null|| request.getPdfUrl().trim().isEmpty()){
            return ResponseEntity.badRequest().body(Map.of(
                    "error",
                    "PDF URL is required"
            ));
        }try{
            String Text=pdfService.extractText(request.getPdfUrl());
            String result=groqService.convertDocument(Text);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", "Failed to process PDF",
                            "details", e.getMessage()
                    ));
        }
    }
}
