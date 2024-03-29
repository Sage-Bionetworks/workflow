#!/usr/bin/env Rscript

###############################################################################
# Run the metaGeo QC and load the results to Synapse
# 
# Author: Matt Furia
###############################################################################

## load the workflow utilities
source('./src/main/resources/synapseWorkflow.R')

source('./src/main/resources/metaGeoQC.R')

# in the long term we would just install all libraries (rSynapseClient, MetaGEO) in a public place
.libPaths("./rLibrary")



## get config args
userName <- getUsernameArg()
secretKey <- getSecretKeyArg()
authEndpoint <- getAuthEndpointArg()
repoEndpoint <- getRepoEndpointArg()
urlEncodedInputData <- getInputDataArg()

metaGeoQC(userName, secretKey, authEndpoint, repoEndpoint, urlEncodedInputData)
