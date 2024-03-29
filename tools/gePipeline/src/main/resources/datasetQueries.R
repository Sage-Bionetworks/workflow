
# get all the dataset names in a given project
getAllDatasets <- function(projectId, verbose=T) {
	batchSize <- 20
	offset <- 1
	
	ans <- list()
	
	repeat {
		if (verbose) cat(c(offset, "\n"))
		response <- synapseQuery(paste("select * from dataset where parentId==", projectId, "LIMIT", batchSize, "OFFSET", offset))
		numResults <- dim(response)[1]
		
		if (numResults==0) break

		ans <- c(ans, response[,"dataset.name"])
		
		if (numResults<batchSize) break
		
		offset <- offset + numResults
	}
	
	ans
}

# get all the dataset names in a given project which encountered an error during QC
getFailedDatasets <- function(projectId, verbose=T, withReason=F) {
	batchSize <- 20
	offset <- 1
	
	ans <- list()
	
	repeat {
		if (verbose) cat(c(offset, "\n"))
		response <- synapseQuery(paste("select * from dataset where parentId==", projectId, "AND dataset.workflowStatusCode!=0 LIMIT", batchSize, "OFFSET", offset))
		numResults <- dim(response)[1]
		
		if (numResults==0) break
		
		for (i in 1:numResults) {
			if (withReason) {
				ds <- getEntity(response[i,"dataset.id"])
				ans <- rbind(ans, c(response[i,"dataset.name"], annotValue(ds, "workflowStatusMsg")))
			} else {
				ans <- c(ans, response[i,"dataset.name"])
			}
		}
		
		
		if (numResults<batchSize) break
		
		offset <- offset + numResults
	}
	
	ans
}

