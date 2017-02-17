setwd("/Users/tonet/Documents/posgrado/analytics/s3/Sessio3_cursBigData_CART/R")
install.packages("rpart")

d <- read.csv(file="churn.txt", sep=" ")
library(rpart)
m<-rpart(Baja ~ ., data=d, control=rpart.control( cp = 0.0001, xval=10))
printcp(m)
plot(m)

m$cptable = as.data.frame(m$cptable)
ind = which.min(m$cptable$xerror)
xerr <- m$cptable$xerror[ind]
xstd <- m$cptable$xstd[ind]
i=1
while (m$cptable$xerror[i] > xerr+xstd) i = i+1
alfa = m$cptable$CP[i]
# AND PRUNE THE TREE ACCORDINGLY
m1 <- prune(m,cp=alfa)
plot(m1)

install.packages("rpart.plot")
install.packages("rpart")
library("rpart.plot")
library("rpart")
rpart.plot(m1)
m1$splits
m1$splits[m1$splits[,'ncat']==1,]


install.packages("RGtk2")
install.packages("tidyr")
install.packages("dplyr")
install.packages("rattle", repos="http://rattle.togaware.com")
library("rattle")
asRules(m1)


#install.packages("ROCR")
library(ROCR)
n <- nrow(d)
set.seed(7)
learn <- sample(1:n, round(0.67*n))
pred_test = as.data.frame(predict(m1, newdata=d[- learn,],type="prob"))
pred_test$"Baja SI"
pred <- prediction(pred_test$"Baja SI", d$Baja[-learn])
# Curva ROC
roc <- performance(pred,measure="tpr",x.measure="fpr")
plot(roc, main="ROC curve")
# Curva de concentraci??n
con <- performance(pred,measure="tpr",x.measure="rpp")
plot(con, main="Concentration curve")

pred
?prediction
d$Baja

pt1 = predict(m1, newdata=d[-learn,],type="vector")
head(pt1)
pt1=cbind(pt1,d$Dictamen[-learn])
head(pt1)
t1.leaf = as.data.frame.matrix(table(pt1[,1],pt1[,1]))

?asRules()

?rpart
fit <- rpart(Kyphosis ~ Age + Number + Start, data = kyphosis)
fit
plot(fit)
text(fit, use.n = TRUE)
plot(fit2)
text(fit2, use.n = TRUE)
printcp(fit)

#PREGUNTA 6
#Datos de salida.
m1.leaf=subset(m1$frame, var=="<leaf>",select=c(n,yval2))
num_leaf = row.names(m1.leaf)
m1.leaf=data.frame(m1.leaf$n,m1.leaf$yval2)
names(m1.leaf) = c("n_train","class_train","n1_train","n2_train","p1_train","p2_train","probnode_train")
row.names(m1.leaf) = num_leaf
m1.leaf=m1.leaf[order(-m1.leaf$p2_train),] # ordering by decreasing positive probabilities
print(m1.leaf)

install.packages('xlsx')
install.packages('rJava')
install.packages('xlsxjars')
library(xlsx)

write.xlsx(m1.leaf, "resultats.xlsx")

[5:35]  
# EXPORTING THE RESULTS TO EXCEL
tab_results = data.frame(matrix(NA, nrow=nrow(ot.leaf), ncol=4))
row.names(tab_results) = ot.leaf$Row.names
tab_results[,1] = ot.leaf$n_train + ot.leaf$n_test
tab_results[,2] = ot.leaf$n1_train + ot.leaf$n1_test
tab_results[,3] = ot.leaf$n2_train + ot.leaf$n2_test
tab_results[,4] = tab_results[,3]/tab_results[,1]
names(tab_results) = c("n","n1","n2","p2")

# RANKING THE LEAVES BY DECREASING PROBABILITIES
tab_results = tab_results[order(-tab_results$p2),]
print(tab_results)
