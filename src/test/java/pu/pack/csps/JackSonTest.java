package pu.pack.csps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.pack.csps.entity.DocumentsTypes;
import ru.pack.csps.entity.UserDocuments;
import ru.pack.csps.entity.Users;
import ru.pack.csps.util.JSONResponse;

@RunWith(MockitoJUnitRunner.class)
public class JackSonTest {


    @Test
    public void test() throws Exception {
        UserDocuments userDocuments = new UserDocuments();
        userDocuments.setUserDocId(1l);
        userDocuments.setUserDocUser(new Users(12l));

        DocumentsTypes dt = new DocumentsTypes();
        dt.setDtHtmlTemplate("<HTML></HTML>");
        dt.setDtName("NAME");
        userDocuments.setUserDocType(dt);

        //ObjectMapper objectMapper = new ObjectMapper();
        //JSONObject json = objectMapper.readValue(objectMapper.writeValueAsString(userDocuments), JSONObject.class);

        //System.out.println(json);


//        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String str = objectMapper.writeValueAsString(userDocuments);
//
//        JSONParser parser = new JSONParser();
//        parser.parse(str);
//
//        System.out.println(str);

        System.out.println(JSONResponse.createOKResponse("TEST", userDocuments));
    }
}
