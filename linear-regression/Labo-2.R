################################################
# UPC School - Big Data Management and Analytics
# Laboratory 2: Ridge and standard regression
################################################

####################################################################
# Example 1. Illustration of ridge regression on synthetic data
####################################################################

## How can we avoid overfitting/underfitting? (most often the real danger is in overfitting; this
## is because many ML methods tend to be very flexible, i.e., they are able to represent complex models)

## There are several ways to do this:

## 1) Get more training data (typically out of our grasp)
## 2) Use (that is, sacrifice!) part of the data for validation
## 3) Use explicit complexity control (as regularization)

## We are going to use polynomials again to see the effect of regularization

set.seed (7)

N <- 20
N.test <- 1000
a <- 0
b <- 1
sigma.square <- 0.3^2

# Note that if you write an expression in parentheses, R evaluates and displays the result (that's why I include so many outer parentheses). Try:

pep <- 3

(pep <- 3)

# Generation of a training sample of size N

x <- seq(a,b,length.out=N)
t <- sin(2*pi*x) + rnorm(N, mean=0, sd=sqrt(sigma.square))
(sample <- data.frame(input=x,target=t))

plot(x,t, lwd=3, ylim = c(-1.1, 1.1))
curve (sin(2*pi*x), a, b, add=TRUE, ylim = c(-1.1, 1.1))
abline(0,0)

dim(sample)

# Generation of a validation set (VA) of size N.valid, and equi-spaced inputs x in (a,b)
# we will use it to make predictions

N.valid <- 1000

x.valid <- seq(a,b,length.out=N.valid)
t.valid <- sin(2*pi*x.valid) + rnorm(N.valid, mean=0, sd=sqrt(sigma.square))
valid.sample <- data.frame(input=x.valid,target=t.valid)

##################
# We begin with polynomials of order 1 (M=1)

model <- glm (target ~ input, data = sample, family = gaussian)

model # tells us that the model is y(x) = -1.4062??x + 0.8361

# make it predict the TR data (used to find the model) and calculate the mean squared error

(prediccio <- predict(model, data=sample))
abline(model,col="red", lwd=2)

attach(sample) # handle with care

(mean.square.error <- sum((target - prediccio)^2)/N)

# alternatively, glm() calculates it as
(mean.square.error <- model$deviance/N)

# if you look at the help of glm (doing ?glm) --which is a very powerful and general routine, and serves many types of regressions-- it says:

# "deviance": up to a constant, minus twice the maximized log-likelihood

# we have seen this in class: in the case of linear regression (the present case), we
# obtained the sum of squared errors (the deviance in statistical jargon) by maximizing the "minus log-likelihood".

# as a curiosity, 'null.deviance' is the error of a "null" model (only with the independent term) and corresponds to the model M = 0; The AIC will be seen later on

model$null.deviance/N

# We resume the thread and return to the mean squared error. It is convenient to work with their root, so to get the "length" of the vector error

(root.mse <- sqrt(model$deviance/N))

# and even better if we standardize the error, dividing it by the variance of targets, obtaining what is called the NRMSE (normalized root mean squared error)

(norm.root.mse <- sqrt(model$deviance/((N-1)*var(target))))


# Interpretation:
#   0) Obviously NRMSE >= 0, but has no upper bound
#   1) a model that predicts the average target (in fact, the best constant model) would have NRMSE = 1.
#   2) models with NRMSE > 1 are therefore terrible; a model begins to be considered acceptable from NRMSE < 0.2
#   3) noting that the square error is nothing more than the estimated variance of the targets, the NRMSE can be viewed as the fraction of the target variance explained (captured) by the model predictions. For example, NRMSE = 0.13 corresponds to a model able to explain an 87% of the target variability.

# Our starting model M=1 explains only a 26% of the target variability.

#######################
# Quadratic regression (polynomial of degree M=2; still a linear model)
# Why? because we are creating basis functions!
# phi_0(x) = 1, phi_1(x) = x, phi_2(x) = x^2

# for which we compute the coefficients w_0, w_1, w_2 using a linear method
# and we get the model y(x;w) = w_0 + w_1??phi_1(x) + w_2??phi_2(x)

model <- glm (target ~ poly(input, 2, raw=TRUE), data = sample, family = gaussian)

# summary () gives more information

summary(model)

# "Deviance Residuals" are the squared differences (example by example). What we see is basic estatistical info (min, median, etc) of the 20 examples we have

# the coefficients of the polynomial (the model) are:

model$coefficients

# therefore our model is y(x) = -0.9854053??x^2 -0.4208058??x + 0.6804892

# "Std. Error" is the uncertainty concerning the value of the coefficients; in this case they are very high, which is due to the fact that we only have 20 points

# The last column is a test on the probability that each coefficient is actually zero (and therefore the corresponding input x is unrelated to the target). To interpret it, see the code below:

# Signif. codes:  0 ???***??? 0.001 ???**??? 0.01 ???*??? 0.05 ???.??? 0.1 ??? ??? 1

# Inputs x with '***' are extremely important, followed by '**', etc. All this, assuming, of course, that the model is correct (which is not the case, as we know, because we have designed it)

# The coefficients cannot be interpreted as how important the inputs are, unless the inputs are standardized

# Now we plot everything again

plot(input,target, lwd=3, ylim = c(-1.1, 1.1))                # TR data
curve (sin(2*pi*x), a, b, add=TRUE, ylim = c(-1.1, 1.1))      # regular part to model
points(input, predict(model), type="l", col="red", lwd=2)     # the obtained model

# now we calculate the NRMSE in the TR sample
(norm.root.mse <- sqrt(model$deviance/((N-1)*var(target))))

# has improved somewhat ...

# Now we will see how to calculate the error in the validation sample
# We must do so explicitly as predict() gives only the predictions, not the errors

# we look first at the VA data
plot(valid.sample$input, valid.sample$target)

# and calculate the error
prediccions <- predict (model, newdata=valid.sample)
(root.mse.valid <- sqrt(sum((valid.sample$target - prediccions)^2)/((N.valid-1)*var(valid.sample$target))))

# When a model is incorrect, its prediction error is high. If the model is overfitted, usually we notice because the VA error is much higher than TR

# In case the model is underfitted (as is our case now), both are high and similar. Notice 0.7444193 against 0.7298313


######################################
# Right, let's do linear regression on polynomials (aka polynomial regression),
# from degrees 1 to N-1

p <- 1
q <- N-1

coef <- list()
model <- list()
root.mse.train <- NULL
root.mse.valid <- NULL

for (i in p:q)
{
  model[[i]] <- glm(target ~ poly(input, i, raw=TRUE), data = sample, family = gaussian)
  
  # store the model coefficients, as well as training and validation errors
  
  coef[[i]] <- model[[i]]$coefficients
  root.mse.train[i] <- sqrt(model[[i]]$deviance/N)
  
  prediccions <- predict (model[[i]], newdata=valid.sample)  
  root.mse.valid[i] <- sqrt(sum((valid.sample$target - prediccions)^2)/((N.valid-1)*var(valid.sample$target)))
}

# we gather everything together

resultats <- cbind (Grau=p:q, Coeficients=coef, Error.train=root.mse.train, Error.valid=root.mse.valid)

# now we plot 6 of the models (degrees M=1,2,3,4,9,19) against the training data

par(mfrow=c(2, 3))                # this creates a 2x3 plotting grid

graus <- c(1,2,3,4,9,19)

for (i in graus)
{
  plot(input,target, lwd=3)
  curve (sin(2*pi*x), a, b, add=TRUE)
  abline(0,0)
  points(input, predict(model[[i]]), type="l", col=25+i, lwd=2)
  title (main=paste('Degree',i))
}

# Now we draw the predictions of these models against validation data
# the function to be modelled is always in yellow, as a reference

for (i in graus)
{
  plot(valid.sample$input, valid.sample$target)
  curve (sin(2*pi*x), a, b, add=TRUE, col='yellow',lwd=2)
  points(valid.sample$input, predict(model[[i]], newdata=valid.sample), type="l", col=25+i, lwd=2)
  title (main=paste('Grau',i))
}

# Now we plot both TR and VA errors together, as a function of M; we'll observe the over and underfitting phenomenos very clearly:

# we omit grades from 17 on bacause the values fall out of the pictures, buy you can look at the results in the numerical matrix:

(r <- data.frame(resultats[,-2]))

par(mfrow=c(1,1))

# prepare an empty plot
plot(1:20, 1:20, xlim=c(1,16), ylim=c(0,1.5), type = "n", xlab="Grau", ylab="", xaxt="n")  

# fill it
axis(1, at=1:16,labels=1:16, col.axis="red", las=2)
points (x=r$Grau[1:16], y=r$Error.train[1:16], type='b', pch=0)
points (x=r$Grau[1:16], y=r$Error.valid[1:16], type='b', pch=3, lwd=3)

legend(x="topleft", legend=c("Training error", "Validation error"), pch=c(0, 3), lwd=c(1, 3))

# Clearly the lowest VA error is for M=3, hence the method would select the correct model (we call this process "model selection"). Note that "model selection" is basically the problem of identifying the right complexity for the problem (in our case, --within the polynomials-- corresponds to a cubic one).

## Last but not least, let's inspect the coefficients for the different degrees

# We will see that all coefficients of the same degree get large (in magnitude)
# as the *maximum* degree grows (except the coefficient of degree 0)

# the column is the maximum degree of the polynomial
# the row is the different terms of the polynomial

coefs.table <- matrix (nrow=10, ncol=9)

for (i in 1:10)
  for (j in 1:9)
    coefs.table[i,j] <- coef[[j]][i]

coefs.table

# the conclusion is obvious: we can limit the effective complexity by
# preventing this growth ---> this is what regularization does
# (we don't limit the maximum degree, we limit the coefficients of all terms)

##########################################################################
# Now we move to ridge regression using a larger TR sample (N=100)

library(MASS)

par(mfcol=c(1,1))

# This is the idea under regularization: depart from a model excessively complex (M=12) and limit explicitly the magnitude of the coefficients, via the lambda parameter

# The trick is now to discover a good value for lambda: it can be done very efficiently when the model is linear. So we try a long sequence of values:

lambdes <- seq(0.001,0.5,0.001)

length(lambdes)

# This would be the standard model (without regularization)
model <- glm (target ~ poly(input, 12, raw=TRUE), data = sample, family = gaussian)

# and this the regularized one
model.ridge <- lm.ridge (model, lambda = lambdes)
plot(model.ridge, lty=1:3)

# what we see on the graph (which is very nice) is how all coefficients (grades 1 to 12) go to zero with more regularization (higher lambdas)

# now we select the best lambda

select( lm.ridge(model, lambda = lambdes) )

# there are several ways of doing this; the most used one is the GCV (which computes the prediction LOOCV)

# it gives lambda = 0.029, so we refit a final model with this value:

model.final <- lm.ridge (model,lambda=0.029)

# Compare the two sets of coefficients of our models for M=12 (standard and regularized)

coef(model)         # M=12 (standard)

coef(model.final)   # M=12 (regularized)

# It remains to be seen that the regularized model is not worse than the standard!

# First we calculate the error for the standard method as usual:

prediccions.classic <- predict (model, newdata=valid.sample)
(NRMSE.VA.classic <- sqrt(sum((valid.sample$target - prediccions.classic)^2)/((N.valid-1)*var(valid.sample$target))))

# sadly predict() does not accept an object lm.ridge(), so we must do it ourselves:

# first we give a name to the coefficients

(c <- setNames(coef(model.final), paste0("c_", 0:12)))

# then compute the powers (the degrees) for the x data

pots <- outer (X=valid.sample$input, Y=0:12, FUN="^")

# now the polynomial (our model)

prediccions.regul <- pots %*% c

# finally we can calculate the error of these predictions

(NRMSE.VA.regul <- sqrt(sum((valid.sample$target - prediccions.regul)^2)/((N.valid-1)*var(valid.sample$target))))

# we get 0.5521224, while the standard method gave 0.5501569; so both are equally good, but the regularized one is much less complex and therefore preferable


####################################################################
# Example 2: Real data modelling with linear and ridge regression
####################################################################

## let us analyze body fat data

bodyfat.data <- read.table(file = "bodyfatdata.txt", header=FALSE, col.names = c('triceps', 'thigh', 'midarm', 'bodyfat'))

attach(bodyfat.data)

N <- nrow(bodyfat.data)

## let us start with standard linear regression
## this time we directly use the method lm(); 
## lm() is actually called by glm() for gaussian noise and is the workhorse for least squares

(model <- lm(bodyfat ~ ., data = bodyfat.data))
summary(model)

## How to read this output:

# x.tilde = (1,triceps, thigh, midarm)^T
# w.tilde = (117.085, 4.334, -2.857, -2.186)^T

# the model is y(x; w) = w.tilde^T x = 117.085 + 4.334*triceps -2.857*thigh -2.186*midarm

# The residuals are the differences (t_n - y(x_n; w)), n = 1,..N
# let's inspect model$residuals

dens <- density(model$residuals)
hist(model$residuals, prob=T)
lines(dens,col="red")

# do the residuals look Gaussian? this is a direct indication of model validity
# (since it was our departing assumption)
# let's do a more informative plot (a QQ-plot), which plots actual quantiles
# against theoretical quantiles of a comparison distribution (Gaussian in this case)

library(car)
qqPlot(model)

# the solid line corresponds to the theoretical quantiles
# therefore in this case the residuals are not even close 
# (the tails are heavier, the central part is flatter)

# this is how we can compute the mean square error
prediction <- predict(model)
(mean.square.error <- sum((bodyfat - prediction)^2)/N)

# is this number large or small? it depends on the magitude of the targets!
# a very good practice is to normalise it:

# first divide by the variance of the target, then take the square root:

(norm.root.mse <- sqrt(sum((bodyfat - prediction)^2)/((N-1)*var(bodyfat))))

# A model with 'norm.root.mse' equal to 1 is as good as the best constant model
# (namely, the model that always outputs the average of the target)
# models with 'norm.root.mse' above 0.5 are so so, beyond 0.7 they begin to be quite bad
# models with 'norm.root.mse' below 0.2 are quite good

# If we divide the mean square error by the variance of the targets t,
# we get the proportion of the variability of the target that is NOT explained by the model

# The Multiple R-squared is one minus this proportion, that is,
# the proportion of the variability of the target that is explained by the model
# in this case it reaches 80%

# The adjusted R-squared is the same thing as R-squared, but adjusted for the complexity of the model,
# i.e. the number of parameters in the model (three in our case)

# Now let us try to see how are the real predictions, we are going to plot
# real predictions against the targets:

plot(bodyfat, predict(model))

# it is difficult to see if the model is a good predictor, what we need is a
# numerical assessment of predictive ability: we compute the LOOCV as seen in class:

(LOOCV <- sum((model$residuals/(1-ls.diag(model)$hat))^2)/N)

# and the corresponding predictive R-square (usually used by statisticians)
(R2.LOOCV = 1 - LOOCV*N/((N-1)*var(bodyfat)))

# and the corresponding predictive normalized root MSE (usually used by machine learners like me)
(norm.root.mse.LOOCV <- sqrt( (LOOCV*N)/((N-1)*var(bodyfat)) ))

## this last number is the one I recommend to do model selection

## Let us continue now with regularized linear regression (aka ridge regression)

# notice this time we start with a wide logarithmic search
lambdas <- 10^seq(-6,2,0.1)

select(lm.ridge(bodyfat ~ triceps + thigh + midarm, lambda = lambdas))

# best value is 0.01995262

# we perform a finer search
lambdas <- seq(0,1,0.001)

select(lm.ridge(bodyfat ~ triceps + thigh + midarm, lambda = lambdas))

# definitely best value is 0.019
# so we refit the model with this precise value

(bodyfat.ridge.reg <- lm.ridge(bodyfat ~ triceps + thigh + midarm, lambda = 0.019))

# Now let us compare these results with those obtained by standard regression (without regularization)

# hand calculation, since we know the theory :-)

X <- cbind(rep(1,length=length(bodyfat)),triceps, thigh, midarm)

(w <- ginv(X) %*% bodyfat)

# call to lm()

model$coefficients

# Notice how the regularized weights are smaller (in absolute value), one by one

# Now we calculate the prediction errors
# First by standard regression (without regularization)
# (we already did this)

norm.root.mse.LOOCV

# Now those with ridge regression (with regularization)

sqrt(bodyfat.ridge.reg$GCV)

# the prediction errors are quite close and the model is way simpler: 
# we would probably prefer the regularized one

####################################################################
#################### Exercise: the Yacht Hydrodynamics Data Set
####################################################################

## Getting the dataset

# Download the `Yacht Hydrodynamics` dataset from the UCI repository:
# https://archive.ics.uci.edu/ml/datasets/Yacht+Hydrodynamics
# (you can find more documentation on the problem there)
setwd("/Users/tonet/Documents/posgrado/linear-regression")
yatch <- read.table(url("http://archive.ics.uci.edu/ml/machine-learning-databases/00243/yacht_hydrodynamics.data"), 
header=FALSE,
col.names = c("Longitudinal.pos","Prismatic.coeff","Length.displ","Beam.draught
","Length.beam","Froude.No", "Resistance"))

## Let's have a first glimpse at the data

summary(yatch)

## Let's confirm that R assigns correct types to our dataframe

str(yatch)

## Let's have a visual inspection of the variables

# Histograms

par (mfrow=c(2,3))         # set new graphic parameters
  
for (i in 1:6) 
  { hist (yatch[,i],main=names(yatch)[i],xlab=names(yatch)[i]) }
  
# Boxplots

for (i in 1:6) 
{ boxplot(yatch[,i], xlab=names(yatch)[i], col='blue') }

# Now for the target variable

par (mfrow=c(1,2))         # set new graphic parameters

hist (yatch[,"Resistance"],xlab="Resistance") 
boxplot (yatch[,"Resistance"],xlab="Resistance")

## Preparing dataset for modeling

# What about this?

hist (log10(yatch[,"Resistance"]),xlab="Resistance") 
boxplot (log10(yatch[,"Resistance"]),xlab="Resistance")

par (mfrow=c(1,1))         # set old graphic parameters

## Scale and center the data?

scaled.Yatch <- scale(yatch)
head(scaled.Yatch)

## Should we treat the predictors as ordered? e.g. Longitudinal.pos or Froude.No

yatch[,"Longitudinal.pos"]
yatch[,"Froude.No"]

sapply(yatch, function(x) length(unique(x)))

(Longitudinal.pos2 <- as.factor(yatch[,"Longitudinal.pos"]))

table(Longitudinal.pos2)










yatch <- read.table(url("http://archive.ics.uci.edu/ml/machine-learning-databases/00243/yacht_hydrodynamics.data"), 
                    header=FALSE,
                    col.names = c("Longitudinal.pos","Prismatic.coeff","Length.displ","Beam.draught
                                  ","Length.beam","Froude.No", "Resistance"))
summary(yatch)
library(lattice) 
xyplot(yatch$"Longitudinal.pos" ~ yatch$"Resistance", data = yatch,
       xlab = "Resistance",
       ylab = "Longitudinal position of the center of buoyancy",
       main = "Yacht Hydrodynamics Data Set. Longitudinal.pos Vs Froude.No"
)

attach(yatch)
N <- nrow(yatch)
# 308 lines
(modelY <- lm(Resistance ~ ., data = yatch))
summary(modelY)

# inspeccionamos el modelo
densY <- density(modelY$residuals)
hist(modelY$residuals, prob=T)
lines(densY,col="red")
# la linea no parecen gausinas, tiene tres maximos locales
# la regresion linear nos deberia mostrar si hay relacion entre la variable que queremos predecir, Resistance,
# y el resto de variables. El grafico nos dice que no, no hay relacion. Como era de esperar al ver el plot.

library(car)
qqPlot(modelY)

prediction <- predict(modelY)
(mean.square.error <- sum((yatch - prediction)^2)/N)
# [1] 1479.289
# la media del error cuadratico es muy grande, dado que el resto de valores oscilan en cualquier case por menos de centenas

(norm.root.mse <- sqrt(sum((Resistance - prediction)^2)/((N-1)*var(Resistance))))
# [1] 0.5851805
# el error cuadratico normalizado 

plot(Resistance, predict(modelY))

(LOOCV <- sum((modelY$residuals/(1-ls.diag(modelY)$hat))^2)/N)
# [1] 82.5794
(R2.LOOCV = 1 - LOOCV*N/((N-1)*var(Resistance)))
# [1] 0.6395396
(norm.root.mse.LOOCV <- sqrt( (LOOCV*N)/((N-1)*var(Resistance)) ))
# [1] 0.6003836
lambdas <- 10^seq(-6,2,0.1)
summary(yatch)
select(lm.ridge(Resistance ~ Longitudinal.pos + Prismatic.coeff + Length.displ + Beam.draught................................... + Length.beam + Froude.No, lambda = lambdas))
# modified HKB estimator is 2.088049 
# modified L-W estimator is 2.131503 
# smallest value of GCV  at 6.309573

lambdas <- seq(0,1,0.001)
select(lm.ridge(Resistance ~ Longitudinal.pos + Prismatic.coeff + Length.displ + Beam.draught................................... + Length.beam + Froude.No, lambda = lambdas))
# modified HKB estimator is 2.088049 
# modified L-W estimator is 2.131503 
# smallest value of GCV  at 1 

(yatch.ridge.reg <- lm.ridge(Resistance ~ Longitudinal.pos + Prismatic.coeff + Length.displ + Beam.draught................................... + Length.beam + Froude.No, lambda = 1))

X <- cbind(rep(1,length=length(Resistance)), Longitudinal.pos, Prismatic.coeff, Length.displ, Beam.draught..................................., Length.beam, Froude.No)

(w <- ginv(X) %*% Resistance)
#               [,1]
# [1,] -19.2366608
# [2,]   0.1938443
# [3,]  -6.4193759
# [4,]   4.2329986
# [5,]  -1.7656948
# [6,]  -4.5164318
# [7,] 121.6675724

modelY$coefficients

norm.root.mse.LOOCV
# [1] 0.6003836
sqrt(yatch.ridge.reg$GCV)
# 0.5141868

# y la version escalada???
scaled.Yatch <- scale(yatch)
head(scaled.Yatch)

# con el log
yatch$ResistanceLog <- log10(yatch[,"Resistance"])
attach(yatch)
summary(yatch)
yatch$Resistance <- NULL
(modelY <- lm(ResistanceLog ~ ., data = yatch))
summary(modelY)
densY <- density(modelY$residuals)
hist(modelY$residuals, prob=T)
lines(densY,col="red")


hist (log10(yatch[,"Resistance"]),xlab="Resistance") 
boxplot (log10(yatch[,"Resistance"]),xlab="Resistance")



## LASSO
yatch <- read.table(url("http://archive.ics.uci.edu/ml/machine-learning-databases/00243/yacht_hydrodynamics.data"), 
                    header=FALSE,
                    col.names = c("Longitudinal.pos","Prismatic.coeff","Length.displ","Beam.draught","Length.beam","Froude.No", "Resistance"))
#install.packages("lars")
library(lars)

yatch$ResistanceLog <- log10(yatch[,"Resistance"])
yatch$Resistance <- NULL
summary(yatch)
t <- as.numeric(yatch$"Resistance") # target
x <- as.matrix(yatch[,1:6]) 
# we draw the model
model.lasso <- lars(x, t, type="lasso")

plot(model.lasso)

lambda.lasso <- c(model.lasso$lambda,0)

beta.lasso <- coef(model.lasso)

colors <- rainbow(3)

beta.scale <- attr(model.lasso$beta, "scaled:scale")
beta.rescaled <- beta.lasso
for(j in 1:3) beta.rescaled[j,] <- beta.rescaled[j,]*beta.scale

matplot(lambda.lasso, beta.rescaled, xlim=c(8,-2), type="o", pch=20, xlab=expression(lambda), 
        ylab=expression(hat(beta.lasso)), col=colors)
text(rep(-0, 9), beta.rescaled[3,], colnames(x), pos=4, col=colors)

# that should give us an idea of which variables can be left out
abline(v=lambda.lasso[3], lty=2)
abline(h=0, lty=2)
(beta.lasso <- beta.lasso[3,])

?glmnet
X <- x
y <- t
fit <-glmnet(x = X, y = y, alpha = 1)
plot(fit, xvar = "lambda", label = TRUE)
coef(fit, s=0.1)

crossval <-  cv.glmnet(x = X, y = y)
summary(crossval)
plot(crossval)
penalty <- crossval$lambda.min #optimal lambda
penalty #minimal shrinkage
fit1 <-glmnet(x = X, y = y, alpha = 1, lambda = penalty ) #estimate the model with that
coef(fit1)

cv.lasso <- cv.glmnet(x, y, alpha=1,  nfolds =5, parallel=TRUE, standardize=TRUE)
plot(cv.lasso)
plot(cv.lasso$glmnet.fit, xvar="lambda", label=TRUE)
cv.lasso$lambda.min #0.6855139
cv.lasso$lambda.1se #2.767432

N <- nrow(yatch)
yatch_training <- yatch[1:200,]
yatch_test <- yatch[201:N,]
nrow(yatch_training)
nrow(yatch_test)






yatch <- read.table(url("http://archive.ics.uci.edu/ml/machine-learning-databases/00243/yacht_hydrodynamics.data"), 
                    header=FALSE,
                    col.names = c("Longitudinal.pos","Prismatic.coeff","Length.displ","Beam.draught","Length.beam","Froude.No", "Resistance"))
set.seed(101)
yatch$ResistanceLog <- log10(yatch[,"Resistance"])
yatch$Resistance <- NULL
sample <- sample.int(nrow(yatch), floor(.75*nrow(yatch)), replace = F)
train <- yatch[sample, ]
test <- yatch[-sample, ]
nrow(train)
nrow(test)

t <- as.numeric(train$ResistanceLog) # target
x <- as.matrix(train[,1:6]) # predictors
model.lasso <- lars(x, t, type="lasso")
plot(model.lasso)

model.lasso$lambda
# Primero Calculamos las predicciones del modelo con la tercera lambda 0.30993387
(predictions <- predict(model.lasso,
                        test[,c("Longitudinal.pos","Prismatic.coeff","Length.displ","Beam.draught","Length.beam","Froude.No")],
                        s=0.007, type="fit",
                        mode="lambda")$fit)

#Calculamos RMSE
(rmse <- sqrt(mean((test[,c("ResistanceLog")] - predictions)^2)))

#O sea, las diferencias entre datos observados y modelo al cuadrado
#sumadas y divididas entre n (o sea, su media) con raiz cuadrada para
#hacer el Root del Mean Square Error.
#Normalizamos con la desviaci??n standard.
(nrmse <- rmse/sd(test[,c("ResistanceLog")]))

# error 0.1731307


lambda.lasso <- c(model.lasso$lambda,0)
beta.lasso <- coef(model.lasso)
colors <- rainbow(3)
beta.scale <- attr(model.lasso$beta, "scaled:scale")
beta.rescaled <- beta.lasso
for(j in 1:3) beta.rescaled[j,] <- beta.rescaled[j,]*beta.scale

matplot(lambda.lasso, beta.rescaled, xlim=c(8,-2), type="o", pch=20, xlab=expression(lambda), 
        ylab=expression(hat(beta.lasso)), col=colors)
text(rep(-0, 9), beta.rescaled[3,], colnames(x), pos=4, col=colors)

min(model.lasso$lambda)


# RIDGE
library(car)
yatch <- read.table(url("http://archive.ics.uci.edu/ml/machine-learning-databases/00243/yacht_hydrodynamics.data"), 
                    header=FALSE,
                    col.names = c("Longitudinal.pos","Prismatic.coeff","Length.displ","Beam.draught","Length.beam","Froude.No", "Resistance"))
set.seed(101)
yatch$ResistanceLog <- log10(yatch[,"Resistance"])
yatch$Resistance <- NULL
sample <- sample.int(nrow(yatch), floor(.75*nrow(yatch)), replace = F)
train <- yatch[sample, ]
test <- yatch[-sample, ]
nrow(train)
nrow(test)
lambdes <- seq(0.001,0.5,0.001)
model <- glm (train$Resistance ~ train$Longitudinal.pos + train$Prismatic.coeff + train$Length.displ + train$Beam.draught + train$Length.beam + train$Froude.No, data=train, family=gaussian)
model.ridge <- lm.ridge (model, lambda = lambdes)
select( lm.ridge(model, lambda = lambdes) )
model.final <- lm.ridge (model,lambda=0.05)
coef(model)
coef(model.final)
prediccions.classic <- predict (model, newdata=train)
(NRMSE.VA.classic <- sqrt(sum((valid.sample$target - prediccions.classic)^2)/((N.valid-1)*var(valid.sample$target))))