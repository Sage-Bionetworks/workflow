/*
 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.services.simpleworkflow.flow.examples.helloworld;

import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

/**
 * Implementation of the hello world workflow
 */
public class HelloWorldWorkflowImpl implements HelloWorldWorkflow{

    HelloWorldActivitiesClient client = new HelloWorldActivitiesClientImpl();

    @Override
    public void helloWorld(String name) {
        client.printHello(name);

        Promise<String> name2 = client.getName();
        printGreeting(name2);
    }

    @Asynchronous
    private void printGreeting(Promise<String> name) {
        client.printGreeting("Hello " + name.get() + "!");
    }
    
}