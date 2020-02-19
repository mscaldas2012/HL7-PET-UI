package open.HL7PET.service;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ErrorReceipt {

    private String code;
    private String description;
    private int status;
    private String path;

    private String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    private String exeption;


    public ErrorReceipt(String code, String description, int status, String path) {
        this.code = code;
        this.description = description;
        this.status = status;
        this.path = path;

    }

}
