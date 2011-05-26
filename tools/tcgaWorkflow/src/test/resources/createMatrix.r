#!/usr/bin/env Rscript

library(sbnClient)

setClientConfig(new('ClientConfig', host='http://localhost:8080', sslhost='http://auth-sagebase-org.elasticbeanstalk.com', session.token='NULL'))

inputLayerId <- getInputLayerId()
inputDatasetId <- getInputDatasetId()

# TODO we are making our urls shorter so we'll be able to do away with
# the first two parts of this url soon
dataset <- synapseGet(paste('/dataset', inputDatasetId, sep='/'))
layer <- synapseGet(paste('/dataset', inputDatasetId, 'layer', inputLayerId, sep='/'))

queryResult <- synapseQuery(paste('select * from layer where layer.parentId == ', inputDatasetId, ' and layer.name == "clinical_patient_public_', dataset$name, '"', sep=''))

if(1 != attr(queryResult,'totalNumberOfResults')) {
  stop('found too many clinical datasets, something is busted')
}
                      
data <- read.table(getLocalFilepath(), sep='\t')

setOutputLayerId(inputLayerId)
