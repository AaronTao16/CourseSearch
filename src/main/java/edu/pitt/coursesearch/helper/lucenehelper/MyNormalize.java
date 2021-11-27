package edu.pitt.coursesearch.helper.lucenehelper;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import edu.pitt.coursesearch.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MyNormalize {

    private final AzureBlob blobReader;
    private final AzureBlob blobWriter;
    private final Analyzer analyzer;

    public MyNormalize(String connectionKey, String readContainerName, String writeContainerName, Analyzer analyzer) throws URISyntaxException, InvalidKeyException, StorageException {
        this.blobReader = new AzureBlob(connectionKey, readContainerName);
        this.blobWriter = new AzureBlob(connectionKey, writeContainerName);
        this.analyzer = analyzer;
    }

    /**
     * normalize the JSON data
     */
    public void normalize() {
        List<String> blobNames = this.blobReader.getAllFileNames();

        blobNames.stream().parallel().forEach(name -> {
            if(!name.equals("test.json"))  return;
            JSONArray jsonArray;
            List<String> collections = new ArrayList<>();

            try {
                jsonArray = new JSONArray(this.blobReader.readFiles(name));

                for (int i = 0; i < jsonArray.length(); i++) {
                    List<String> content = new ArrayList<>();
                    content.add(jsonArray.getJSONObject(i).get("id").toString());
                    content.add(((JSONObject)jsonArray.getJSONObject(i).get("courseCode")).get("dept").toString());
                    content.add(((JSONObject)jsonArray.getJSONObject(i).get("courseCode")).get("number").toString());
                    content.add(jsonArray.getJSONObject(i).get("description").toString());
                    content.add(jsonArray.getJSONObject(i).get("instructor").toString());
                    JSONArray section = jsonArray.getJSONObject(i).getJSONArray("sections");
                    for(int j = 0; j < section.length(); j++){
                        content.add(section.getJSONObject(j).get("classNumber").toString());
                        content.add(section.getJSONObject(j).get("beginTime").toString());
                        content.add(section.getJSONObject(j).get("endTime").toString());
                        content.add(section.getJSONObject(j).get("sectionType").toString());
                        content.add(section.getJSONObject(j).get("building").toString());
                        content.add(section.getJSONObject(j).get("room").toString());
                    }

                    TokenStream tokenStream  = this.analyzer.tokenStream("content", new StringReader(content.toString()));
                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        collections.add(tokenStream.getAttribute(CharTermAttribute.class).toString());
                    }
                    collections.add("\n");
                    tokenStream.close();
                }

                String fileContent = collections.toString().replaceAll(",", "");
                this.blobWriter.uploadFiles(String.format("after_normalize_%s", name), fileContent);

            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }
}
