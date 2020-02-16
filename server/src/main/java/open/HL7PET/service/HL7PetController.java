package open.HL7PET.service;

import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.annotation.Scheduled;
import open.HL7PET.tools.HL7ParseUtils;
import scala.Option;

import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

@Controller("/pet")
public class HL7PetController {

    private static final int  MAX_TIMEOUT = 60 * 30; //Timeout of unused cache - 30 min.

    Logger logger = Logger.getLogger(HL7PetController.class.getName());
    private String[][] emptyResults = new String[][] {{"No Results Match"}};

    private String[][] invalidToken = new String[][] {{"{No session present or session expired, please submit a new HL7 Message}"}};

    private HashMap<SessionToken, HL7ParseUtils> parsers = new HashMap();


    @Post(value = "/message", produces = MediaType.APPLICATION_JSON, consumes = MediaType.TEXT_PLAIN)
    public HttpResponse resetMessage(@Body String newMessage) {
        logger.info("AUDIT::Resetting Message");
        HL7ParseUtils newParser = new HL7ParseUtils(newMessage);
        SessionToken newToken = new SessionToken();
        parsers.put(newToken, newParser);
        return HttpResponse.ok(newToken);
    }

    @Get(value="/extract", produces = MediaType.APPLICATION_JSON)
    public HttpResponse extract(@Parameter String path, @Parameter String token){
        logger.info("AUDIT::Extracting " + path);
        SessionToken tokenKey = new SessionToken(token);
        HL7ParseUtils parser = parsers.get(tokenKey);
        if (parser == null) {
            return HttpResponse.status(HttpStatus.GONE, invalidToken[0][0] );
        }
        //timestamp usage:
        parsers.put(tokenKey, parser);
        Option<String[][]> results = parser.getValue(path);
        if (results.isDefined())
            return HttpResponse.ok(results.get());
        else
            return HttpResponse.ok(emptyResults);
    }

    @Scheduled(fixedDelay = "5m")
    public void cleanCache() {
        logger.fine("Executing clean cache");
        Calendar deadline = Calendar.getInstance();
        deadline.add(Calendar.SECOND, -MAX_TIMEOUT);

        parsers.entrySet().removeIf( e -> e.getKey().getLastUpdated().before(deadline.getTime()));
        logger.fine("\tCache size: " + parsers.size());
    }
}
