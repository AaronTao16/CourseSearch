package edu.pitt.coursesearch.helper.lucenehelper;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.StringReader;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

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
            List<String> collections = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(this.blobReader.readFiles(name));

                for (int i = 0; i < jsonObject.names().length(); i++) {
                    List<String> content = new ArrayList<>();
                    String id = (String) jsonObject.names().get(i);
                    JSONObject course = (JSONObject) jsonObject.get(id);
                    content.add(id);
                    content.add(((JSONObject)course.get("courseCode")).get("dept").toString());
                    content.add(((JSONObject)course.get("courseCode")).get("number").toString());
                    content.add(course.get("description").toString());
                    content.add(course.get("instructor").toString());
                    content.add(course.get("instructor").toString().equals("false")? "undergraduate" : "graduate");
                    JSONArray section = course.getJSONArray("sections");
                    for(int j = 0; j < section.length(); j++){
                        content.add(section.getJSONObject(j).get("classNumber").toString());
//                        content.add(section.getJSONObject(j).get("beginTime").toString());
//                        content.add(section.getJSONObject(j).get("endTime").toString());
                        content.add(section.getJSONObject(j).get("sectionType").toString());
//                        content.add(section.getJSONObject(j).get("building").toString());
//                        content.add(section.getJSONObject(j).get("room").toString());
                    }

//                    collections.add(jsonArray.getJSONObject(i).get("id").toString() + "\n");
//                    collections.add(((JSONObject)jsonArray.getJSONObject(i).get("courseCode")).get("dept").toString() + " " + ((JSONObject)jsonArray.getJSONObject(i).get("courseCode")).get("number").toString() + "\n");

                    TokenStream tokenStream  = this.analyzer.tokenStream("content", new StringReader(content.toString()));
                    tokenStream = new StopFilter(tokenStream, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
//                    tokenStream = new PorterStemFilter(tokenStream);
                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        collections.add(tokenStream.getAttribute(CharTermAttribute.class).toString().trim());
                    }
                    collections.add("\n");
                    tokenStream.close();
                }

                String fileContent = collections.toString().replaceAll(",", "").replaceAll("\\[", "").replaceAll("]", "");
                this.blobWriter.uploadFiles(String.format("after_normalize_%s", name), fileContent);

            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }
}
