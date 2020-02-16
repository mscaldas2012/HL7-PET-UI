package open.HL7PET.service;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import open.HL7PET.tools.HL7ParseUtils;
import scala.Option;

import java.util.HashMap;

@Controller("/pet")
public class HL7PetController {

    private HashMap<SessionToken, HL7ParseUtils> parsers = new HashMap();


    @Post(value = "/message", produces = MediaType.APPLICATION_JSON, consumes = MediaType.TEXT_PLAIN)
    public HttpResponse resetMessage(@Body String newMessage) {
        System.out.println(" resetting Message");
        HL7ParseUtils newParser = new HL7ParseUtils(newMessage);
        SessionToken newToken = new SessionToken();
        parsers.put(newToken, newParser);
        return HttpResponse.ok(newToken);
    }

    @Get(value="/extract", produces = MediaType.APPLICATION_JSON)
    public HttpResponse extract(@Parameter String path, @Parameter String token){
        System.out.println("Extracting " + path);
        HL7ParseUtils parser = parsers.get(new SessionToken(token));
        if (parser == null) {
            return HttpResponse.badRequest("Unable to find Message for " + token);
        }
        Option<String[][]> results = parser.getValue(path);
        return HttpResponse.ok(results.get());
    }
}
