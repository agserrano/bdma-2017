
##############################################################
# SETTING THE WORKING PATH 
setwd("D:/docent/Curs Big Data/ppts/2a edicio/Sessio2/R/")

#  READING CREDSCO.TXT
dd <- read.table("credsco2.txt",header=T)

# DIMENSIONS AND SUMMARY OF DATA
dim(dd)
summary(dd)


###############################################################  PROFILING

library(FactoMineR)
desc_Dict <- catdes(dd,num.var=1,proba = 0.01)
desc_Dict


plot(desc_Dict,numchar=17)


##############################################################
#   PARALLEL PROFILING USING APPLY

library(nnet) #class.ind
library(parallel) #Parallelism

binclass <- class.ind(dd$Dictamen) #Binarize data
dd$Dictamen <- NULL #Remove dictamen

cl <- makeCluster(detectCores(logical=TRUE))

clusterExport(cl,"dd") #Export the data to the cluster to make it available.

a <- parApply(cl, binclass,2, 
           function(x) {
              library(FactoMineR) #catdes imported in the worker nodes
              data <- cbind(as.factor(x),dd) #Bind the current column with the rest of the dataset
              catdes(data,1) #Perform catdes
           }
    )

stopCluster(cl) #Stop the cluster to free resources
a$negatiu #Each profiling will be available with the class name (e.g, negatiu vs the others)
a$positiu


