package com.video.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Base64;

// [START videointelligence_quickstart]
import com.google.api.gax.grpc.OperationFuture;
import com.google.cloud.videointelligence.v1beta1.AnnotateVideoProgress;
import com.google.cloud.videointelligence.v1beta1.AnnotateVideoRequest;
import com.google.cloud.videointelligence.v1beta1.AnnotateVideoResponse;
import com.google.cloud.videointelligence.v1beta1.Feature;
import com.google.cloud.videointelligence.v1beta1.LabelAnnotation;
import com.google.cloud.videointelligence.v1beta1.LabelLocation;
import com.google.cloud.videointelligence.v1beta1.VideoAnnotationResults;
import com.google.cloud.videointelligence.v1beta1.VideoIntelligenceServiceClient;
import com.google.cloud.videointelligence.v1beta1.VideoIntelligenceServiceSettings;

public class QuickstartSample {
	
  public static void main(String[] args) throws
        ExecutionException, IOException, InterruptedException {
    // Instantiate the client
    System.out.println("Initiating.....1");
	  
    VideoIntelligenceServiceSettings settings =
        VideoIntelligenceServiceSettings.defaultBuilder().build();
    VideoIntelligenceServiceClient client = VideoIntelligenceServiceClient.create(settings);

    // The Google Cloud Storage path to the video to annotate.
    
    //Path path = Paths.get("C://Users/Joon/Downloads/Underwater_Waterfall.mp4");
//    Path path = Paths.get("C://Users/Joon/Downloads/mfund2.mp4");
//      Path path = Paths.get("C://Users/Joon/Downloads/fidvid5.mp4");
     //Path path = Paths.get("C://Users/Joon/Downloads/steve1.mp4");
      	Path path = Paths.get("C://Users/Joon/Downloads/zoo2.mp4");
    
    
    
    
    
    byte[] data = Files.readAllBytes(path);
    byte[] encodedBytes = Base64.encodeBase64(data);

    // Create an operation that will contain the response when the operation completes.
    AnnotateVideoRequest request = AnnotateVideoRequest.newBuilder()
            .setInputContent(new String(encodedBytes, "UTF-8"))
            .addFeatures(Feature.SAFE_SEARCH_DETECTION)
            .build();

    OperationFuture<AnnotateVideoResponse, AnnotateVideoProgress> operation =
            client.annotateVideoAsync(request);

    System.out.println("Waiting for operation to complete...");
    for (VideoAnnotationResults result : operation.get().getAnnotationResultsList()) {
      if (result.getLabelAnnotationsCount() > 0) {
        System.out.println("Labels:");
        for (LabelAnnotation annotation : result.getLabelAnnotationsList()) {
          System.out.println("\tDescription: " + annotation.getDescription());
          for (LabelLocation loc : annotation.getLocationsList()) {
            if (loc.getSegment().getStartTimeOffset() == -1
                && loc.getSegment().getEndTimeOffset() == -1) {
              System.out.println("\tLocation: Entire video");
            } else {
              System.out.printf(
                  "\tLocation: %fs - %fs\n",
                  loc.getSegment().getStartTimeOffset() / 1000000.0,
                  loc.getSegment().getEndTimeOffset() / 1000000.0);
            }
          }
          System.out.println();
        }
      } else {
        System.out.println("No labels detected ");
        UserDefinedFileAttributeView userDefinedFileAttributeView = null;
      }
    }
  }
}
// [END videointelligence_quickstart]