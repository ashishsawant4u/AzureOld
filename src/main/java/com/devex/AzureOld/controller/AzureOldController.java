package com.devex.AzureOld.controller;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ListBlobsOptions;

@RestController
@RequestMapping("/azold")
public class AzureOldController 
{
	
	@Resource(name = "blobServiceClientSAS")
	BlobServiceClient blobServiceClientSAS;
	
	@GetMapping("/test")
	public String azureBlob()
	{
		return "Azure Old App Running";
	}
	
	@PostConstruct
	//@GetMapping(value="/blobList",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> blobList()
	{
		List<String> allFiles = new ArrayList<>();
		
		BlobContainerClient blobContainerClient = blobServiceClientSAS.getBlobContainerClient("mtc");
		
		
		String SOURCE = "dataload-poc/Denmans/den/import";
		
		System.out.println("LIST SOURCE ==> "+SOURCE);
		
		
		try 
	    {
			 String delimiter = "/";
			 ListBlobsOptions options = new ListBlobsOptions()
			            .setPrefix(SOURCE);
			
			 Iterator<BlobItem> it =  blobContainerClient.listBlobs().iterator();
			//Iterator<BlobItem> it =  blobContainerClient.listBlobsByHierarchy(delimiter,options,null).iterator();
			
//			while (it.hasNext()) {
//				BlobItem bi = it.next();
//				
//				boolean flag = bi.getName().startsWith(SOURCE);
//				System.out.println("isMatching ==> "+flag);
//				if(flag == true);
//				{
//					System.out.println("BLOB DETAILS ==> "+bi.getName()+" isPrefix ==> "+bi.isPrefix());	
//				}
//							
//			}
			
			int sourceDirLength = SOURCE.split("/").length+1;
			
			BiPredicate<String,Integer> noNestedDirectories = (s,l) -> s.split("/").length <= l; 
			
			List<BlobItem> actualList = new ArrayList<>();
			it.forEachRemaining(actualList::add);
			
			for(BlobItem bi : actualList)
			{
				if((null==bi.isPrefix() || !bi.isPrefix()) && bi.getName().startsWith(SOURCE) && noNestedDirectories.test(bi.getName(), sourceDirLength))
				{
					System.out.println("BLOB DETAILS ==> "+bi.getName()+" isPrefix ==> "+bi.isPrefix());
					System.out.println("file name "+ bi.getName().replace(SOURCE, "").replace("/", ""));
				}
			}
			
			
			
//			Iterable<BlobItem> blobs = blobContainerClient.listBlobsByHierarchy(SOURCE);
//			
//			blobs.forEach(blob -> {
//				
//				if(!blob.isPrefix())
//				{
//					allFiles.add(blob.getName());
//				}
//				else
//				{
//					System.out.println("directory found "+blob.getName());
//				}
//				
//			});
//			
//			System.out.println("all files "+allFiles);
		
		} 
	    catch (UncheckedIOException ex) 
	    {
	    	System.out.println("Failed to list file:"+ ex.getMessage());
	    }
		 
		 return allFiles;
	}
}
