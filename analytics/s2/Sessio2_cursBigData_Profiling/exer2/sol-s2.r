setwd("/Users/tonet/Documents/posgrado/analytics/s2/Sessio2_cursBigData_Profiling/exer2")
d <- read.csv(file="churn.txt", sep=" ")
# 1. Hacer summary y corregir
summary(d$sexo)
d <- transform(d, sexo_new= ifelse(sexo=="No informado", "MUJER", "HOMBRE"))
d$sexo <- d$sexo_new
d$sexo_new <- NULL

# 3. Charts
# Barchart para la variable categorica sexo
#install.packages("ggplot2")
library(ggplot2)
mysubset <- subset(d, select=c("edatcat","Baja"))
ggplot(mysubset, aes(mysubset$edatcat, fill=mysubset$Baja) ) + geom_bar(position="dodge") + scale_x_discrete("edatcat") + theme(legend.title=element_blank())


# Histograma para la variable cuantitativa antig
mysubset <- subset(d, select=c("dif_Ahorro","Baja"))
max_val <- max(mysubset$dif_Ahorro)
ggplot(mysubset, aes(mysubset$dif_Ahorro, fill=mysubset$Baja)) + geom_bar(aes(y = (..count..)/sum(..count..))) + labs(x="dif_Ahorro", y="count") + geom_histogram(breaks=seq(0, max_val, 100), col="black", aes(fill=mysubset$Baja)) + theme(legend.title=element_blank())

# 4. Profiling
# install.packages("ggplot2", dependencies = TRUE)
# install.packages("FactoMineR")
library(FactoMineR)
catdes(d, num.var=1, proba=0.05)



d$dif_Ahorro_new<-cut(d$dif_Ahorro, breaks=c(0,0.0001,150,400,1000,3000,99000),include.lowest=T, na.rm=TRUE)
max_val <- max(mysubset$dif_Ahorro_new)
ggplot(mysubset, aes(aux, fill=mysubset$Baja)) + labs(x="aux", y="count") + geom_histogram(breaks=seq(0, 10, 100), col="black", aes(fill=mysubset$Baja)) + theme(legend.title=element_blank())

churn <- read.csv(file="churn.txt", sep=" ")
churn2 = churn
churn2$Rec_dif_Ahorro = cut(churn$dif_Ahorro, breaks=c(-99000,-100,-0.0001,0,20,200,1000,99000))
mysubset <- subset(churn2, select=c("Rec_dif_Ahorro","Baja"))
ggplot(mysubset, aes(mysubset$Rec_dif_Ahorro, fill=mysubset$Baja) ) + geom_bar(position="dodge") + scale_x_discrete("Rec_dif_Ahorro") + theme(legend.title=element_blank())
                     
        
#6
20*100/393
6*100/322
393*0.0509
3*100/179
4*100/297
17*100/214
2*100/25
                     