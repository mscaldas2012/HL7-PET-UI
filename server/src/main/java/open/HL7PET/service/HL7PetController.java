package open.HL7PET.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.scheduling.annotation.Scheduled;
import open.HL7PET.tools.HL7ParseUtils;
import open.HL7PET.tools.model.Profile;
import scala.Option;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;


@Controller("/pet")
public class HL7PetController {

    private static final int  MAX_TIMEOUT = 60 * 30; //Timeout of unused cache - 30 min.

    Logger logger = Logger.getLogger(HL7PetController.class.getName());
    private String[][] emptyResults = new String[][] {{"No Results Match"}};

    private String[][] invalidToken = new String[][] {{"{No session present or session expired, please submit a new HL7 Message}"}};


    private static final String DEFAULT = "default";

    private HashMap<SessionToken, HL7ParseUtils> parsers = new HashMap<>();
    private HashMap<String, Profile> profiles = new HashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    public void init() throws IOException {
        mapper.registerModule(new DefaultScalaModule());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("PhinGuideProfile_NoORC.json");
        assert inputStream != null;
        Reader targetReader = new InputStreamReader(inputStream);
        Profile profile = mapper.readValue(targetReader, Profile.class);

        profiles.put(DEFAULT, profile);


    }

    @Post(value = "/message", produces = MediaType.APPLICATION_JSON, consumes = MediaType.TEXT_PLAIN)
    public HttpResponse resetMessage(@QueryValue String profile, @Body String newMessage) {
        logger.info("AUDIT::Resetting Message for profile " + profile);
        HL7ParseUtils newParser = new HL7ParseUtils(newMessage, profiles.get(profile));
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


    @Get("/profiles")
    public HttpResponse<String[]> getProfiles() {
        String[] result =  this.profiles.keySet().toArray(new String[this.profiles.size()]);
        return HttpResponse.ok(result);
    }
    @Post(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse upload(CompletedFileUpload file) throws IOException { // <7>
        logger.info("AUDIT::Uploading new profile " + file.getFilename());
        if ((file.getFilename() == null || file.getFilename().equals(""))) {
            return HttpResponse.badRequest();
        }
        Reader targetReader = new InputStreamReader(file.getInputStream());
        Profile newProfile = mapper.readValue(targetReader, Profile.class);

        profiles.put(file.getFilename(), newProfile);
        logger.info("profile successfully loaded");
        return HttpResponse.ok();
    }
    //end::upload[]


    @Scheduled(fixedDelay = "5m")
    public void cleanCache() {
        logger.fine("Executing clean cache");
        Calendar deadline = Calendar.getInstance();
        deadline.add(Calendar.SECOND, -MAX_TIMEOUT);

        parsers.entrySet().removeIf( e -> e.getKey().getLastUpdated().before(deadline.getTime()));
        logger.fine("\tCache size: " + parsers.size());
    }




    @Error
    public HttpResponse<ErrorReceipt> handleHL7ParserError(HttpRequest req, Exception e) {
        logger.severe("Unable to Parse message with error: " + e.getMessage());
        ErrorReceipt error = new ErrorReceipt("INVALID_MESSAGE", e.getMessage(), 400, req.getPath());
        return HttpResponse.badRequest(error);

    }
}
