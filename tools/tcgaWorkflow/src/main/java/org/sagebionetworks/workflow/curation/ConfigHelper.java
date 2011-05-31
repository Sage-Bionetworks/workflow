package org.sagebionetworks.workflow.curation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.sagebionetworks.client.Synapse;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 * Configuration Helper to used to create Synapse and AWS service clients.
 */
public class ConfigHelper {

	/**
	 * The system property key with which to pass the AWS access key id
	 */
	public static final String ACCESS_ID_KEY = "AWS_ACCESS_KEY_ID";
	/**
	 * The system property key with which to pass the AWS secret key
	 */
	public static final String SECRET_KEY_KEY = "AWS_SECRET_KEY";

	private static final String PROPERTIES_FILENAME = "workflow.properties";

	private static final String SYNAPSE_AUTH_ENDPOINT_KEY = "synapse.auth.endpoint";
	private static final String SYNAPSE_REPO_ENDPOINT_KEY = "synapse.repo.endpoint";
	private static final String SWF_ENDPOINT_KEY = "swf.endpoint";
	private static final String SNS_ENDPOINT_KEY = "sns.endpoint";
	private static final String SNS_TOPIC_KEY = "sns.topic";
	private static final String S3_BUCKET_KEY = "s3.bucket";

	private String synapseRepoEndpoint;
	private String synapseAuthEndpoint;

	private String snsEndpoint;
	private String swfEndpoint;

	private String snsTopic;
	private String s3Bucket;

	private String awsAccessId;
	private String awsSecretKey;

	private volatile static ConfigHelper theInstance = null;

	private ConfigHelper() {

		URL url = ClassLoader.getSystemResource(PROPERTIES_FILENAME);
		if (null == url) {
			throw new Error("unable to find in classpath "
					+ PROPERTIES_FILENAME);
		}
		Properties serviceProperties = new Properties();
		try {
			serviceProperties
					.load(new FileInputStream(new File(url.getFile())));
		} catch (FileNotFoundException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		}

		synapseRepoEndpoint = serviceProperties
				.getProperty(SYNAPSE_REPO_ENDPOINT_KEY);
		synapseAuthEndpoint = serviceProperties
				.getProperty(SYNAPSE_AUTH_ENDPOINT_KEY);

		snsEndpoint = serviceProperties.getProperty(SNS_ENDPOINT_KEY);
		swfEndpoint = serviceProperties.getProperty(SWF_ENDPOINT_KEY);

		snsTopic = serviceProperties.getProperty(SNS_TOPIC_KEY);
		s3Bucket = serviceProperties.getProperty(S3_BUCKET_KEY);

		// These come from the environment, not a config file so that we do not
		// check credentials into source control
		awsAccessId = System.getProperty(ACCESS_ID_KEY);
		awsSecretKey = System.getProperty(SECRET_KEY_KEY);

		if ((null == awsAccessId) || (null == awsSecretKey)) {
			throw new Error(
					"AWS credentials are missing, pass them as JVM args -D"
							+ ACCESS_ID_KEY + "=theAccessKey -D"
							+ SECRET_KEY_KEY + "=theSecretKey");
		}

	}

	/**
	 * Factory method for Configuration Helper
	 * 
	 * @return the configuration singleton
	 */
	public static ConfigHelper createConfig() {
		if (null == theInstance) {
			synchronized (ConfigHelper.class) {
				if (null == theInstance) {
					theInstance = new ConfigHelper();
				}
			}
		}
		return theInstance;
	}

	/**
	 * Create a synchronous Simple Workflow Framework (SWF) Client
	 * 
	 * @return the SWF client
	 */
	public AmazonSimpleWorkflow createSWFClient() {
		ClientConfiguration config = new ClientConfiguration()
				.withSocketTimeout(70 * 1000);
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessId,
				awsSecretKey);
		AmazonSimpleWorkflow client = new AmazonSimpleWorkflowClient(
				awsCredentials, config);
		client.setEndpoint(swfEndpoint);
		return client;
	}

	/**
	 * Create a synchronous Simple Storage Service (S3) client
	 * 
	 * @return the S3 Client
	 */
	public AmazonS3 createS3Client() {
		AWSCredentials s3AWSCredentials = new BasicAWSCredentials(awsAccessId,
				awsSecretKey);
		AmazonS3 client = new AmazonS3Client(s3AWSCredentials);
		return client;
	}

	/**
	 * Create a synchronous Simple Notification Service (SNS) client
	 * 
	 * @return the SNS Client
	 */
	public AmazonSNS createSNSClient() {
		AWSCredentials snsAWSCredentials = new BasicAWSCredentials(awsAccessId,
				awsSecretKey);
		AmazonSNS client = new AmazonSNSClient(snsAWSCredentials);
		client.setEndpoint(snsEndpoint);
		return client;
	}

	/**
	 * @return the Synapse client
	 * @throws MalformedURLException 
	 */
	public Synapse createSynapseClient() throws MalformedURLException {
		Synapse synapse = new Synapse();
		synapse.setRepositoryEndpoint(synapseRepoEndpoint);
		synapse.setAuthEndpoint(synapseAuthEndpoint);
		return synapse;
	}

	/**
	 * @return the s3Bucket
	 */
	public String getS3Bucket() {
		return s3Bucket;
	}

	/**
	 * @return the snsTopic
	 */
	public String getSnsTopic() {
		return snsTopic;
	}
}
