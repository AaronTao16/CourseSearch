package edu.pitt.coursesearch.helper.azurehelper;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Slf4j
public class AzureBlobBuilder {
    private CloudStorageAccount cloudStorageAccount = null;
    private CloudBlobClient cloudBlobClient = null;
    private CloudBlobContainer cloudBlobContainer = null;
    private CloudBlockBlob cloudBlockBlob = null;

    public AzureBlobBuilder setStorageAccount(String connectionString) throws URISyntaxException, InvalidKeyException {
        this.cloudStorageAccount = CloudStorageAccount.parse(connectionString);
        return this;
    }

    public AzureBlobBuilder buildBlobClient() {
        this.cloudBlobClient = this.cloudStorageAccount.createCloudBlobClient();
        return this;
    }

    public AzureBlobBuilder getContainer(String containerName) throws URISyntaxException, StorageException {
        this.cloudBlobContainer = this.cloudBlobClient.getContainerReference(containerName);
        return this;
    }

    public AzureBlobBuilder build() {
        if (this.cloudBlobContainer == null) throw new RuntimeException("build fails");
        return this;
    }

    public CloudBlockBlob getBlockBlobClient(String filename) throws URISyntaxException, StorageException {
        this.cloudBlockBlob = this.cloudBlobContainer.getBlockBlobReference(filename);
        return this.cloudBlockBlob;
    }

    public CloudBlobContainer getCloudBlobContainer() {
        if (this.cloudBlobContainer == null) throw new RuntimeException("Get cloud blob container fails");
        return this.cloudBlobContainer;
    }


}
