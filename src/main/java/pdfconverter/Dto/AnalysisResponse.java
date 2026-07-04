package pdfconverter.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AnalysisResponse {
    private String documentType;
    private String title;
    private String authors;
    private String summary;
    private String keyTakeAway;
}
