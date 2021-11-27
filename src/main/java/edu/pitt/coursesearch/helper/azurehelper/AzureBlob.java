package edu.pitt.coursesearch.helper.azurehelper;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AzureBlob {

    AzureBlobBuilder azureBlobBuilder;

    public AzureBlob(String connectionString, String containerName) throws URISyntaxException, InvalidKeyException, StorageException {
        this.azureBlobBuilder = new AzureBlobBuilder()
                .setStorageAccount(connectionString)
                .buildBlobClient()
                .getContainer(containerName)
                .build();
    }

    public String readFiles(String fileName) throws URISyntaxException, StorageException, IOException {
        CloudBlockBlob azureBlob = this.azureBlobBuilder.getBlockBlobClient(fileName);
        String res;
        res = azureBlob.downloadText();

        return res;
    }

    public boolean uploadFiles(String fileName, String content) throws URISyntaxException, StorageException, IOException {
        CloudBlockBlob azureBlob = this.azureBlobBuilder.getBlockBlobClient(fileName);
        azureBlob.uploadText(content);

        return false;
    }

    public List<String> getAllFileNames() {
        CloudBlobContainer cloudBlobContainer = this.azureBlobBuilder.getCloudBlobContainer();
        ArrayList<String> fileNames = new ArrayList<>();
        for (ListBlobItem listBlobItem : cloudBlobContainer.listBlobs()) {
            CloudBlob cloudBlob = (CloudBlob) listBlobItem;
            fileNames.add(cloudBlob.getName());
        }

        return fileNames;
    }
}