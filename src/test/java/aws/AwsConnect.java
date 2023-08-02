package aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AwsConnect {
    private static AWSCredentials getSessionCredentials() {
        /*return new BasicSessionCredentials(
                System.getProperty("accessKey"),
                System.getProperty("secretKey"),
                System.getProperty("sessionToken")
        );*/
        return new BasicAWSCredentials(System.getenv("clientid"),
                System.getenv("clientsecret")
        );
    }

    public static Map<String, Object> invokeLambda(String functionName, String region, String payload) {
        InvokeRequest lmbRequest = new InvokeRequest()
                .withFunctionName(functionName)
                .withPayload(payload)
                .withLogType("Tail");
        lmbRequest.setInvocationType(InvocationType.RequestResponse);
        AWSLambda lambda = AWSLambdaClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(getSessionCredentials()))
                .build();

        InvokeResult lmbResult = lambda.invoke(lmbRequest);
        String decodedLogString = new String(Base64.decodeBase64(lmbResult.getLogResult().getBytes()));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("statusLambda", lmbResult.getStatusCode());
        result.put("log", decodedLogString);
        return result;
    }

}
