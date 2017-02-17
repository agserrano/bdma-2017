setwd("/Users/tonet/Documents/posgrado/analytics/s3/Sessio3_cursBigData_CART/R")
library(FactoMineR)
churn <- read.csv("churn.txt",header=T,sep=" ",dec=".")

# 1
head(churn)
churn_NoNA <- churn[complete.cases(churn),]
dim(churn_NoNA)
summary(churn_NoNA)
churn_NoNA$antig <- as.factor(churn_NoNA$antig)

#2
churnAct <- churn_NoNA[13:17]
pca.churnAct <- PCA(churnAct,graph=T,scale.unit=F)
# 2.1 bis
pca.churn_NoNA <- PCA(churn_NoNA,quali.sup=c(1:12,18),quanti.sup=c(13:17),graph=T,scale.unit=F)
pca.churn_NoNA$eig
plot(pca.churn_NoNA$eig$eigenvalue,type="l",main="Screeplot")

nd=5

# 3 Rotacion
pca.churn_NoNA.rot <- varimax(pca.churn_NoNA$var$cor[,1:nd])
pca.churn_NoNA.rot
plot(pac.churnAct.rot, axes = c(1, 2), choix = c("var"), title="Plot of variables")

summary(pca.churnAct.rot)

#4
summary(churn)
plot(pca.churn_NoNA, axes = c(2, 3), choix = c("ind"), habillage=1, label="quali", title="Plot of individuals", cex=0.7)
abline(h=mean(pca.churnAct$eig$eigenvalue),col="gray")

#5
churn <- read.csv("churn.txt",header=T,sep=" ",dec=".")
churn_NoNA <- churn[complete.cases(churn),]
churn_NoNA$antig <- as.factor(churn_NoNA$antig)
churn_NoNA$Baja <- as.numeric(churn_NoNA$Baja)
summary(churn_NoNA)
pca.churn_NoNA <- PCA(churn_NoNA,quali.sup=c(2:12,18),quanti.sup=c(1,13:17),graph=T,scale.unit=F)
plot(pca.churn_NoNA, axes = c(1, 2), choix = c("var"), title="Plot of variables")
