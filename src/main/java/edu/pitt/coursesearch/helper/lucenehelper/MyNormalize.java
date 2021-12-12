package edu.pitt.coursesearch.helper.lucenehelper;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
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
            List<String> original = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(this.blobReader.readFiles(name));

                for (int i = 0; i < jsonObject.names().length(); i++) {
                    List<String> content = new ArrayList<>();

                    String id = (String) jsonObject.names().get(i);
                    JSONObject course = (JSONObject) jsonObject.get(id);
                    String courseName = course.get("name").toString();
                    String courseDep = ((JSONObject) course.get("courseCode")).get("dept").toString();
                    String courseNum = ((JSONObject) course.get("courseCode")).get("number").toString();
                    String courseInstructor = course.get("instructor").toString();
                    String grad = course.get("grad").toString().equals("false") ? "undergraduate" : "graduate";
                    String des = course.get("description").toString();
                    content.add(id);
                    content.add(courseName);
                    content.add(courseDep);
                    content.add(courseNum);
                    content.add(des);
                    content.add(courseInstructor);
                    content.add(grad);

                    collections.add(courseName + "\n");
                    collections.add(courseDep + courseNum + "\n");
                    collections.add(courseInstructor + "\n");

                    JSONArray section = course.getJSONArray("sections");
                    for(int j = 0; j < section.length(); j++){
                        String classNumber = section.getJSONObject(j).get("classNumber").toString();
                        String days = section.getJSONObject(j).get("days").toString();
                        String beginTime = section.getJSONObject(j).get("beginTime").toString();
                        String endTime = section.getJSONObject(j).get("endTime").toString();
                        String sectionType = section.getJSONObject(j).get("sectionType").toString();
                        String building = section.getJSONObject(j).get("building").toString();
                        String room = section.getJSONObject(j).get("room").toString();
                        content.add(classNumber);
                        content.add(days);
                        content.add(beginTime);
                        content.add(endTime);
                        content.add(sectionType);
                        content.add(building);
                        content.add(room);
                    }

                    TokenStream tokenStream  = this.analyzer.tokenStream("content", new StringReader(content.toString()));

                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        collections.add(tokenStream.getAttribute(CharTermAttribute.class).toString().trim());
                    }
                    collections.add("\n");
                    original.add(id + " " + course.toString() + "\n");
                    tokenStream.close();
                }

                this.blobWriter.uploadFiles(String.format("after_normalize_%s", name), String.join(" ", collections));
                this.blobWriter.uploadFiles(String.format("after_normalize_%s", "original"), String.join("", original));
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }
}
