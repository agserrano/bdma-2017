# INSTALACION DE R
# http://cran.r-project.org/
# Download R for ...
# EJECUTAMOS EL FICHERO exe DESCARGADO. VEREMOS APARECER EL ICONO DE R PARA TRABAJAR CON EL

# INICIAR UNA SESION DE TRABAJO EN R
# CLICKAR SOBRE EL ICONO DE R

# INSTALACION DE PAQUETES NO INCLUIDOS EN R BASICO
# ENTRAR EN R
# EJECUTAR
install.packages("qualityTools")

# CARGAMOS LAs LIBRERIAS NECESARIAS (EN NUESTRO CASO 

library(qualityTools)

# 1. Muestra de precios de pisos de l'Eixample

x <- c(7.80, 12.60, 15.96, 12.75, 13.50)

length(x)
dotPlot(x, pch=20, main="prices of a 3room apart in l’Eixample") 

# 2. Muestra de precios de pisos de l'Eixample

x <- c(7.80, 12.60, 15.96, 12.75, 13.50, 8.25, 8.25, 15.05, 13.83, 20.61, 16.46, 18.00, 15.40, 16.25, 13.39, 25.99, 16.64, 11.03, 16.28, 23.40, 14.81, 17.60, 20.21, 18.04, 18.70, 11.10, 15.60, 8.83, 15.05, 16.15)
length(x)
dotPlot(x, pch=20, main="prices of a 3room apart in l’Eixample")

# 3. Muestra de precios de pisos de l'Eixample

x <- c(7.80, 12.60, 15.96, 12.75, 13.50, 8.25, 8.25, 15.05, 13.83, 20.61, 16.46, 18.00, 15.40, 16.25, 13.39, 25.99, 16.64, 11.03, 16.28, 23.40, 14.81, 17.60, 20.21, 18.04, 18.70, 11.10, 15.60, 8.83, 15.05, 16.15,
15.00, 11.10, 21.08, 28.66, 21.25, 20.47, 25.53, 10.89, 15.01, 11.78, 14.82, 12.17, 14.56, 16.96, 15.50, 14.43, 14.43, 12.71, 31.66, 15.75, 15.75, 11.69, 14.52, 17.35, 22.57, 20.00, 13.80, 13.68, 12.61, 19.00, 24.61, 16.80, 16.72, 23.95, 16.11, 19.41, 13.99, 16.48, 13.20, 13.47, 13.63, 14.76, 16.93, 31.31, 12.81, 21.81, 20.82, 35.77, 15.54, 12.62, 13.91, 21.18, 13.72, 12.00, 19.89, 16.46, 32.70, 22.73, 15.51, 16.26, 28.70, 18.90, 25.75, 16.89, 13.99, 13.99, 28.22, 20.79, 16.81, 20.25, 22.31, 24.03, 15.65, 27.28, 12.60, 17.55, 25.60)
length(x)
dotPlot(x, pch=20, main="prices of a 3room apart in l’Eixample")

# "Poblacion" de precios de pisos de l'Eixample

x0 <- c(7.80, 12.60, 15.96, 12.75, 13.50, 8.25, 8.25, 15.05, 13.83, 20.61, 16.46, 18.00, 15.40, 16.25, 13.39, 25.99, 16.64, 11.03, 16.28, 23.40, 14.81, 17.60, 20.21, 18.04, 18.70, 11.10, 15.60, 8.83, 15.05, 16.15,
15.00, 11.10, 21.08, 28.66, 21.25, 20.47, 25.53, 10.89, 15.01, 11.78, 14.82, 12.17, 14.56, 16.96, 15.50, 14.43, 14.43, 12.71, 31.66, 15.75, 15.75, 11.69, 14.52, 17.35, 22.57, 20.00, 13.80, 13.68, 12.61, 19.00, 24.61, 16.80, 16.72, 23.95, 16.11, 19.41, 13.99, 16.48, 13.20, 13.47, 13.63, 14.76, 16.93, 31.31, 12.81, 21.81, 20.82, 35.77, 15.54, 12.62, 13.91, 21.18, 13.72, 12.00, 19.89, 16.46, 32.70, 22.73, 15.51, 16.26, 28.70, 18.90, 25.75, 16.89, 13.99, 13.99, 28.22, 20.79, 16.81, 20.25, 22.31, 24.03, 15.65, 27.28, 12.60, 17.55, 25.60, 29.44, 27.44, 24.00, 30.10, 10.14, 11.31, 11.20, 15.73, 16.90, 25.40, 18.56, 17.55, 21.75, 21.68, 24.60, 46.32, 42.12, 21.70, 21.42, 22.31, 31.35, 10.00, 10.92, 15.72, 17.10, 15.39, 14.79, 14.11, 14.36, 14.53, 15.11, 18.00, 12.08, 12.53, 17.32, 23.96, 22.60, 6.21, 14.54, 16.59, 16.59, 18.26, 19.42, 10.66, 16.01, 16.14, 16.00, 10.15, 16.46, 16.20, 14.28, 12.10, 8.40, 11.10, 7.52, 13.44, 12.48, 14.72, 8.58, 14.87, 14.35, 13.12, 15.40, 15.00, 15.91, 15.20, 14.51, 22.36, 29.29, 22.70, 10.06, 13.89, 14.24, 17.64, 14.62, 16.23, 14.60, 15.99, 13.65, 15.69, 22.44, 7.14, 6.76, 11.02, 10.65, 12.00, 12.96, 10.71, 11.38, 12.63, 11.11, 11.50, 15.61, 14.80, 15.50, 14.51, 14.95, 13.57, 14.80, 11.84, 13.87, 14.43, 16.83, 14.43, 21.12, 22.62, 12.75, 13.92, 11.05, 10.56, 13.32, 22.75, 12.68, 17.75)
length(x0)
dotPlot(x0, pch=20, main="prices of a 3room apart in l’Eixample")

# Distribucion de los precios de pisos de 3hab en l'Eixemple

dotPlot(x0, pch=20, main="prices of a 3room apart in l’Eixample", col="gray")
dens_x <- density(x0, adjust=2)
lines (dens_x$x, 3*dens_x$y, col="blue")

# Com seria la distribució de referencia sota la hipotesi de normalitat

dotPlot(x0, pch=20, main="prices of a 3room apart in l’Eixample", col="gray")
lines(sort(x0),dnorm(sort(x0),mean(x0),sd(x0)),type="l",col="blue",ylim=c(0,1))

# Detecting outliers

summary(x0)
boxplot(x0, horizontal=T)

Q1 <- summary(x0)[2]
Q3 <- summary(x0)[5] 
iqr <- Q3 - Q1
iqr
sd(x0)

# Robust limits

Q1 - 3*iqr
Q3 + 3*iqr

which(x0 > Q3 + 3*iqr)
x0[which(x0 > Q3 + 3*iqr)]

# Histograms of data, sahpe changes with interval length

hist(x0)
hist(x0, breaks=seq(from=0, to=50, by=10))
hist(x0, breaks=seq(from=5, to=50, by=2.5))
hist(x0, breaks=seq(from=5, to=50, by=1))


# Distribution of the mean

mitj_x = NULL
for (i in 1:500) {mitj_x = c(mitj_x,mean(sample(x0,5)))}
dotPlot(mitj_x, pch=20, xlim=c(5,45))
dens_mitj_x <- density(mitj_x, adjust=2)
lines(dens_mitj_x$x,3.5*dens_mitj_x$y, col="red")


# Let's a 3room apartment for 8.45. Is it an opportunity?

p_val <- sum(x0<=8.45)/length(x0)
p_val


# With a sample of first 5 values

m <- mean(x0)
std <- sd(x0)

x <- c(7.80, 12.60, 15.96, 12.75, 13.50)
n <- length(x)

dotPlot(x, pch=20, xlim=c(5,45), ylim=c(0,0.2))
curve(dnorm(x, mean=m, sd=std), 
          col="darkblue", lwd=2, add=TRUE, yaxt="n")

z <- (8.45 - m)/std
pnorm(z)

# What if we don't know the mu and the sigma

m <- mean(x)
std <- sd(x)

# Reference distribution

t = (x-mean(x))/sd(x)
dotPlot(t, pch=20, ylim=c(0,0.5), xlim=c(-3,3))
curve(dt(x, df=4), 
          col="darkblue", lwd=2, add=TRUE, yaxt="n")
curve(dnorm(x, mean=0, sd=1), 
          col="green", lwd=2, add=TRUE, yaxt="n")

t0 <- (8.45-mean(x))/sd(x)
t0
pt(t0, df=(n-1))


# what if we take a bigger random sample
# without knowing the mu and the sigma

x <- c(7.80, 12.60, 15.96, 12.75, 13.50, 8.25, 8.25, 15.05, 13.83, 20.61, 16.46, 18.00, 15.40, 16.25, 13.39, 25.99, 16.64, 11.03, 16.28, 23.40, 14.81, 17.60, 20.21, 18.04, 18.70, 11.10, 15.60, 8.83, 15.05, 16.15)
n <- length(x)

m <- mean(x)
std <- sd(x)
n1 = n-1
t = (x-mean(x))/sd(x)
dotPlot(t, pch=20, ylim=c(0,0.5), xlim=c(-3,3))
curve(dt(x, df=n1), 
          col="darkblue", lwd=2, add=TRUE, yaxt="n")
curve(dnorm(x, mean=0, sd=1), 
          col="green", lwd=2, add=TRUE, yaxt="n")

t0 <- (8.45-mean(x))/sd(x)
pt(t0, df=(n-1))


# Average price in Barcelona = 15.289

mu0 = 15.289

# With historical data

n <- length(x0)

mitj_x = NULL
for (i in 1:1000) {mitj_x = c(mitj_x,mean(sample(x0,5)))}
sum(mitj_x < mu0)/1000

# Without historical data 

x <- c(7.80, 12.60, 15.96, 12.75, 13.50)
n <- length(x)

m <- mean(x)
std <- sd(x)/sqrt(n)

dotPlot(x, pch=20, xlim=c(5,45), ylim=c(0,0.4))
curve(dnorm(x, mean=m, sd=std), 
          col="darkblue", lwd=2, add=TRUE, yaxt="n")

t <- (mu0 - m)/std
pt(t, df=4, lower.tail=TRUE)


#############################################################
# Comparison of two means, A and B methods

x<-c(89.7,81.4,84.5,84.8,87.3,79.7,85.1,81.7,83.7,84.5,84.7,86.1,83.2,91.9,86.3,79.3,82.6,89.1,83.7,88.5)

met <- c(rep("A",10),rep("B",10))
n_met <- table(met)
nA <- n_met[1]
nB <- n_met[2]

mitj_met <- tapply(x,met,mean)
mitj_met
mitj_A <- mitj_met[1]
mitj_B <- mitj_met[2]

mA <- cbind(seq(1:10),rep(mitj_A,nA))
mB <- cbind(seq(1:20),c(rep(NA,nA),rep(mitj_B,nB)))

plot(x, pch=20, col=as.numeric(as.factor(met)), main="Performance of methods A and B")
lines(mA)
lines(mB, col="red")

# historical data of method A

hist_A <- c(85.5,
81.7,
80.6,
84.7,
88.2,
84.9,
81.8,
84.9,
85.2,
81.9,
89.4,
79,
81.4,
84.8,
85.9,
88,
80.3,
82.6,
83.5,
80.2,
85.2,
87.2,
83.5,
84.3,
82.9,
84.7,
82.9,
81.5,
83.4,
87.7,
81.8,
79.6,
85.8,
77.9,
89.7,
85.4,
86.3,
80.7,
83.8,
90.5,
84.5,
82.4,
86.7,
83,
81.8,
89.3,
79.3,
82.7,
88,
79.6,
87.8,
83.6,
79.5,
83.3,
88.4,
86.6,
84.6,
79.7,
86,
84.2,
83,
84.8,
83.6,
81.8,
85.9,
88.2,
83.5,
87.2,
83.7,
87.3,
83,
90.5,
80.7,
83.1,
86.5,
90,
77.5,
84.7,
84.6,
87.2,
80.5,
86.1,
82.6,
85.4,
84.7,
82.8,
81.9,
83.6,
86.8,
84,
84.2,
82.8,
83,
82,
84.7,
84.4,
88.9,
82.4,
83,
85,
82.2,
81.6,
86.2,
85.4,
82.1,
81.4,
85,
85.8,
84.2,
83.5,
86.5,
85,
80.4,
85.7,
86.7,
86.7,
82.3,
86.4,
82.5,
82,
79.6,
86.7,
80.5,
91.7,
81.6,
83.9,
85.6,
84.8,
78.4,
89.9,
85,
86.2,
83,
85.4,
84.4,
84.5,
86.2,
85.6,
83.2,
85.7,
83.5,
80.1,
82.2,
88.6,
82,
85,
85.2,
85.3,
84.3,
82.3,
89.7,
84.8,
83.1,
80.6,
87.4,
86.8,
83.5,
86.2,
84.1,
82.3,
84.8,
86.6,
83.5,
78.1,
88.8,
81.9,
83.3,
80,
87.2,
83.3,
86.6,
79.5,
84.1,
82.2,
90.8,
86.5,
79.7,
81,
87.2,
81.6,
84.4,
84.4,
82.2,
88.9,
80.9,
85.1,
87.1,
84,
76.5,
82.7,
85.1,
83.3,
90.4,
81,
80.3,
79.8,
89,
83.7,
80.9,
87.3,
81.1,
85.6,
86.6,
80,
86.6,
83.3,
83.1,
82.3,
86.7,
80.2)

plot(hist_A, pch=20, col="gray")

# p.value with distribution reference from historical data

dif_A = NULL
for (i in 1:191) {dif_A[i] = mean(hist_A[(10+i):(19+i)])-mean(hist_A[(i):(9+i)]) }

dotPlot(dif_A, pch=20)

sum(dif_A>=1.30)/length(dif_A)

# p.value without historical data, assuming known sigma

sigma_com <- sd(hist_A)

z <- (mitj_B - mitj_A)/(sigma_com*sqrt((1/nA)+(1/nB)))

pnorm(z,lower.tail=F)

# p.value without historical data, assuming unknown sigma
# now with student

var_met <- tapply(x,met,var)
var_A <- var_met[1]
var_B <- var_met[2]

s2_pool <- ((nA-1)*var_A + (nB-1)*var_B)/(nA+nB-2)
s_pool <- sqrt(s2_pool)

t <- (mitj_B - mitj_A)/(s_pool*sqrt((1/nA)+(1/nB)))

pt(t, df=(nA+nB-2), lower.tail=F)

# p.value without historical data and without any assumption
# permutation test

dif_per <- NULL
for (i in 1:1000) {rnd <- sample(1:20,10);
                   dif_per[i] = mean(x[rnd])-mean(x[-rnd])}

dotPlot(dif_per, pch=20)
                   
sum(dif_per>=1.30)/length(dif_per)


############################################################################################################################

# Binomial distribution

# B(5,0.8)

x = c(0,1,2,3,4,5)
dbinom(x,size=5,prob=0.8)
barplot(dbinom(x,size=5,prob=0.8),names.arg=x)

sum(x*dbinom(x,size=5,prob=0.8)) # THE MEAN

sum((x-4)^2*dbinom(x,size=5,prob=0.8)) # THE VARIANCE




# B(20,0.8)

x=0:20
barplot(dbinom(x,size=20,prob=0.8),names.arg=x)

sum(x*dbinom(x,size=20,prob=0.8))  # the mean

sum((x-16)^2*dbinom(x,size=20,prob=0.8))  # the variance


# B(20,0.5)

barplot(dbinom(x,size=20,prob=0.5),names.arg=x)

sum(x*dbinom(x,size=20,prob=0.5))  # the mean

sum((x-10)^2*dbinom(x,size=20,prob=0.5))   # the variance


###############################################
#  Inference with a proportion

# CEO survey
# 29 respondents out of 1240 has answered they voted for PP
# the true proportion of voters in 2012 elections was 0.1297

p_true = 0.1297

n = 1240

p_hat=29/n 

z = (p_hat-p_true)/sqrt(p_true*(1-p_true)/n)
z

pnorm(p_hat,mean=p_true,sd=sqrt(p_true*(1-p_true)/n) ) 


# we want to test whether the word "amor" is a significant characteristic of the poem "la Morada" of Miguel Hernandez.

# in all poems Amor appered 167 times upon 940 words considered

p_true = 167/940
p_true

# in La Morada it appeared 41 times upon 162 words

x = 41
n = 162

p_hat = x/n
p_hat

pbinom(x, n, p_true, lower.tail=F)

pnorm(p_hat,mean=p_true,sd=sqrt(p_true*(1-p_true)/n),lower.tail=F)

z = (p_hat-p_true)/sqrt(p_true*(1-p_true)/n)
pnorm(z,lower.tail=F)

pnorm(z)

x = rnorm(100,mean=p_true,sd=sqrt(p_true*(1-p_true)/n))
 

dotPlot(x, pch=20, xlim=c(0,0.4), ylim=c(0,15), col="white")
curve(dnorm(x, p_true,sd=sqrt(p_true*(1-p_true)/n)), 
          col="darkblue", lwd=2, add=TRUE, yaxt="n")



