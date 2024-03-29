package org.sagebionetworks.gepipeline;

import java.io.IOException;

import org.json.JSONException;
import org.sagebionetworks.workflow.Constants;
import org.sagebionetworks.workflow.ActivityScriptResult;
import org.sagebionetworks.workflow.UnrecoverableException;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.ExponentialRetry;

/**
 * 
 */
@Activities(version = GEPWorkflow.VERSION)
@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = Constants.ONE_DAY_OF_SECONDS, defaultTaskStartToCloseTimeoutSeconds = Constants.ONE_DAY_OF_SECONDS, defaultTaskList = GEPWorkflow.SMALL_ACTIVITY_TASK_LIST)
public interface GEPActivities {

	/**
	 * Workflow and Activity Annotations Constants
	 * 
	 * Dev Note: annotation values must be resolvable at compilation time, not
	 * runtime, therefore we cannot move this into a config file
	 */

	/**
	 * Retries for script processing
	 */
	static final int NUM_RETRIES = 2;
	/**
	 * First amount of time to wait before retrying
	 */
	static final int INITIAL_RETRY_INTERVAL_SECONDS = 300;

	/**
	 * @param script
	 * @param activityInput
	 * @return results of the run of the script
	 * @throws JSONException 
	 * @throws UnrecoverableException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Activity(version = GEPWorkflow.VERSION)
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = Constants.ONE_DAY_OF_SECONDS, defaultTaskStartToCloseTimeoutSeconds = Constants.ONE_DAY_OF_SECONDS)
	@ExponentialRetry(initialRetryIntervalSeconds = INITIAL_RETRY_INTERVAL_SECONDS, maximumAttempts = NUM_RETRIES)
	ActivityScriptResult processData(String script, String activityInput) throws IOException, InterruptedException, UnrecoverableException, JSONException;

	/**
	 * @param recipient
	 * @param subject
	 * @param message
	 */
	@Activity(version = GEPWorkflow.VERSION)
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = Constants.ONE_DAY_OF_SECONDS, defaultTaskStartToCloseTimeoutSeconds = Constants.ONE_DAY_OF_SECONDS)
	void notifyFollower(String recipient, String subject, String message);

}
