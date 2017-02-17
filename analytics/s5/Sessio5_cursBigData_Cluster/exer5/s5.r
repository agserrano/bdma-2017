setwd("/Users/tonet/Documents/posgrado/analytics/s3/Sessio3_cursBigData_CART/R")
library(FactoMineR)
churn <- read.csv("churn.txt",header=T,sep=" ",dec=".")
churn_NoNA <- churn[complete.cases(churn),]
churn_NoNA$antig <- as.factor(churn_NoNA$antig)
# PCA
pca.churn_NoNA <- PCA(churn_NoNA,quali.sup=c(1:12,18),quanti.sup=c(13:17),graph=T,scale.unit=F)

#1
Psi = pca.churn_NoNA$ind$coord
dist.churn <- dist(Psi)
hclus.churn <- hclust(dist.churn,method="ward.D2")
plot(hclus.churn,cex=0.3)

#2
barplot(hclus.churn$height[(nrow(churn)-30):(nrow(churn)-1)])
nc = 5

#3
cut5 <- cutree(hclus.churn,nc)
plot(Psi[,1],Psi[,2],type="n",main="Clustering of churn customers in 5 classes")
text(Psi[,1],Psi[,2],col=cut5,cex = 0.6)
abline(h=0,v=0,col="gray")
legend("topleft",c("c1","c2","c3","c4","c5","c6"),pch=20,col=c(1:6))

#3 bis
nd <- nc
cdg <- aggregate(as.data.frame(Psi),list(cut5),mean)[,2:(nd+1)]
cdg

Bss <- sum(rowSums(cdg^2)*as.numeric(table(cut5)))
Tss <- sum(Psi^2)

100*Bss/Tss

#4 Consolidation
k_def <- kmeans(Psi,centers=cdg)
100*k_def$betweenss/k_def$totss

#6
catdes(cbind(as.factor(k_def$cluster),churn_NoNA),num.var=1,proba=0.001)
