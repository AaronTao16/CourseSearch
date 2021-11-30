package edu.pitt.coursesearch.controller;

import com.microsoft.azure.storage.StorageException;
import edu.pitt.coursesearch.helper.azurehelper.AzureBlob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@RestController
//@RequestMapping("/course")
public class CourseController {

    @Value("${azure.storage.connectionKey}")
    private String connectionKey;

    @Value("${azure.storage.containerName}")
    private String containerName;

    @Value("${azure.storage.containerNameAfterIndex}")
    private String containerNameAfterIndex;

    @GetMapping("/course")
    public String getCourses() throws URISyntaxException, InvalidKeyException, StorageException, IOException {
//        model.addAllAttributes("list", "123");
        AzureBlob azureBlobAfterNormalize = new AzureBlob(this.connectionKey, this.containerNameAfterIndex);
        return azureBlobAfterNormalize.readFiles("after_normalize_test.json");
    }

}
