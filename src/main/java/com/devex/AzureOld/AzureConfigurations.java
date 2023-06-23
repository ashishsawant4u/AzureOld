package com.devex.AzureOld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Configuration
public class AzureConfigurations 
{
	
	@Bean
	public BlobServiceClient blobServiceClientSAS() 
	{
		
		String endpoint = "https://0k1v5rk4z8tzuv8zcnk35zb.blob.core.windows.net/";
		String sasToken = "sv=2020-04-08&st=2023-06-19T08%3A02%3A17Z&se=2023-08-20T08%3A02%3A00Z&sr=c&sp=racwdl&sig=iAYBj3v9GrACMVBSTG4PseHEDBH7mrw83oXOzQO8qow%3D";
		
		System.out.println("CONNECTING ==> "+endpoint);
		
	    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
	            .endpoint(endpoint)
	            .sasToken(sasToken)
	            .buildClient();

	    return blobServiceClient;
	}
	
	@Bean
	public BlobContainerClient blobContainerClient()
	{
		return blobServiceClientSAS().getBlobContainerClient("mtc");
	}
}

