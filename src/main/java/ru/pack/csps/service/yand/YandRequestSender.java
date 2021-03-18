//package ru.pack.csps.service.yand;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import ru.pack.csps.exceptions.PropertyFindException;
//import ru.pack.csps.exceptions.YandexServiceCommunicationException;
//import ru.pack.csps.service.SettingsService;
//
//import java.io.IOException;
//
//@Service
//public class YandRequestSender {
//
//    @Autowired
//    private SettingsService settingsService;
//
//    public JSONObject executeRequest(String url, JSONObject requestObject, String idempotenceKey) throws IOException, YandexServiceCommunicationException, ParseException, InterruptedException, PropertyFindException {
//        CredentialsProvider provider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials((String)settingsService.getProperty("yandexShopId"), (String)settingsService.getProperty("yandexShopPassword"));
//        provider.setCredentials(AuthScope.ANY, credentials);
//
//        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
//
//        HttpPost request = new HttpPost(url);
//
//        StringEntity params =new StringEntity(requestObject.toJSONString());
//        request.addHeader("content-type", "application/json");
//        request.addHeader("Idempotence-Key", idempotenceKey);
//        request.setEntity(params);
//        HttpResponse response = client.execute(request);
//
//        int statusCode = response.getStatusLine().getStatusCode();
//        if (statusCode == 200) {
//            JSONParser jsonParser = new JSONParser();
//            return  (JSONObject) jsonParser.parse(EntityUtils.toString(response.getEntity()));
//        } else if (statusCode == 202) {
//            JSONParser jsonParser = new JSONParser();
//            JSONObject resp = (JSONObject) jsonParser.parse(EntityUtils.toString(response.getEntity()));
//
//            Long l = Long.parseLong((String) resp.get("retry_after"));
//            Thread.sleep(l);
//            return executeRequest(url, requestObject, idempotenceKey); /*TODO warning!!!*/
//        } else {
//            throw new YandexServiceCommunicationException(EntityUtils.toString(response.getEntity()));
//        }
//    }
//}
