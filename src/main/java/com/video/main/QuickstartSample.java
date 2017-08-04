package com.video.main;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
// [START videointelligence_quickstart]
import com.google.api.gax.grpc.OperationFuture;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
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
	  GoogleCredential credential = GoogleCredential.getApplicationDefault();
	  
	  Compute compute = new Compute.Builder
			    (Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), credential)
			    .setApplicationName("VideoFilteration").build();
//	  
	  Collection COMPUTE_SCOPES =
			    Collections.singletonList(ComputeScopes.COMPUTE);
			if (credential.createScopedRequired()) {
			    credential = credential.createScoped(COMPUTE_SCOPES);
			}
	  
    VideoIntelligenceServiceSettings settings =
        VideoIntelligenceServiceSettings.defaultBuilder().build();
    VideoIntelligenceServiceClient client = VideoIntelligenceServiceClient.create(settings);

    // The Google Cloud Storage path to the video to annotate.
    String gcsUri = "C://Users/Suchi/Downloads/Underwater_Waterfall.mp4";

    // Create an operation that will contain the response when the operation completes.
    AnnotateVideoRequest request = AnnotateVideoRequest.newBuilder()
            .setInputUri(gcsUri)
            .addFeatures(Feature.LABEL_DETECTION)
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
        System.out.println("No labels detected in " + gcsUri);
      }
    }
  }
}
// [END videointelligence_quickstart]