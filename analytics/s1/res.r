setwd("/Users/tonet/Documents/posgrado/analytics/s1")
library(qualityTools)
x <- c(7.80, 12.60, 13.50, 8.25, 31.29, 16.46)
length(x)
dotPlot(x, pch=20, main="prices of a 3 room apart in l'Eixample")

summary(x)
Min. 1st Qu.  Median    Mean 3rd Qu.    Max. 
7.800   9.338  13.050  14.980  15.720  31.290 

boxplot(x, main="Precios de pisos de 3 habitaciones", ylab="Precio")

mean(x)
sd(x)
t <- (8-mean(x))/sd(x)
pt(t, df=5)
