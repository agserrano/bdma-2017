################################################
# UPC School - Big Data Management and Analytics
# Laboratory 1: Association rules
################################################
# setwd("/Users/tonet/Documents/posgrado/algorithms-association-rules")
# install.packages("arules");
# library (arules)

################################################
# Example 3: Adult dataset (also used for the exercise)

data("AdultUCI")
# dim(AdultUCI)
summary(AdultUCI)
# lets remove all na values
row.has.na <- apply(AdultUCI, 1, function(x){any(is.na(x))})
# sum(row.has.na)
completeAdultUCI <- AdultUCI[!row.has.na,]
# dim(completeAdultUCI)
summary(completeAdultUCI)
# group simmilar values
groupedAdultUCI <- completeAdultUCI
levels(groupedAdultUCI$workclass) <- c(levels(groupedAdultUCI$workclass), "Public-servant")
groupedAdultUCI$workclass[completeAdultUCI$workclass == "State-gov"] <- "Public-servant"
groupedAdultUCI$workclass[completeAdultUCI$workclass == "Federal-gov"] <- "Public-servant"
groupedAdultUCI$workclass[completeAdultUCI$workclass == "Local-gov"] <- "Public-servant"
summary(groupedAdultUCI)
levels(groupedAdultUCI$workclass) <- c(levels(groupedAdultUCI$workclass), "Self-emp")
groupedAdultUCI$workclass[completeAdultUCI$workclass == "Self-emp-not-inc"] <- "Self-emp"
groupedAdultUCI$workclass[completeAdultUCI$workclass == "Self-emp-inc"] <- "Self-emp"
summary(groupedAdultUCI)

levels(groupedAdultUCI$relationship) <- c(levels(groupedAdultUCI$relationship), "Married")
groupedAdultUCI$relationship[completeAdultUCI$relationship == "Husband"] <- "Married"
groupedAdultUCI$relationship[completeAdultUCI$relationship == "Other-relative"] <- "Married"
groupedAdultUCI$relationship[completeAdultUCI$relationship == "Wife"] <- "Married"
summary(groupedAdultUCI)

levels(groupedAdultUCI$"native-country") <- c(levels(groupedAdultUCI$"native-country"), "Latam")
levels(groupedAdultUCI$"native-country") <- c(levels(groupedAdultUCI$"native-country"), "Asia")
levels(groupedAdultUCI$"native-country") <- c(levels(groupedAdultUCI$"native-country"), "Europe")
levels(groupedAdultUCI$"native-country") <- c(levels(groupedAdultUCI$"native-country"), "Caribbean")

groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Germany"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "England"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Poland"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Portugal"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "France"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Italy"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Yugoslavia"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Scotland"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Greece"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Ireland"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Hungary"] <- "Europe"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Holand-Netherlands"] <- "Europe"

groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "India"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Philippines"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Iran"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Cambodia"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Thailand"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Taiwan"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Japan"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Vietnam"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Hong"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "China"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "South"] <- "Asia"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Laos"] <- "Asia"

groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Mexico"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Puerto-Rico"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "El-Salvador"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Cuba"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Honduras"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Columbia"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Ecuador"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Peru"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Guatemala"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Nicaragua"] <- "Latam"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Dominican-Republic"] <- "Latam"

groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Jamaica"] <- "Caribbean"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Haiti"] <- "Caribbean"
groupedAdultUCI$"native-country"[completeAdultUCI$"native-country" == "Trinadad&Tobago"] <- "Caribbean"

levels(groupedAdultUCI$occupation) <- c(levels(groupedAdultUCI$occupation), "lowQualif")
levels(groupedAdultUCI$occupation) <- c(levels(groupedAdultUCI$occupation), "medQualif")
levels(groupedAdultUCI$occupation) <- c(levels(groupedAdultUCI$occupation), "highQualif")
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Adm-clerical"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Exec-managerial"] <- "highQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Handlers-cleaners"] <- "lowQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Prof-specialty"] <- "highQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Other-service"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Sales"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Transport-moving"] <- "lowQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Farming-fishing"] <- "lowQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Machine-op-inspct"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Tech-support"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Craft-repair"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Protective-serv"] <- "lowQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Armed-Forces"] <- "medQualif"
groupedAdultUCI$occupation[completeAdultUCI$occupation == "Priv-house-serv"] <- "lowQualif"

levels(groupedAdultUCI$education) <- c(levels(groupedAdultUCI$education), "School")
groupedAdultUCI$education[completeAdultUCI$education == "12th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "11th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "10th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "9th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "7th-8th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "5th-6th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "1st-4th"] <- "School"
groupedAdultUCI$education[completeAdultUCI$education == "Preschool"] <- "School"

unique(groupedAdultUCI$education)
summary(groupedAdultUCI)

# young group is changed up to
groupedAdultUCI[["age"]] <- ordered(cut(groupedAdultUCI[["age"]], c(15,25,45,65,100)),
                              labels = c("Young", "Middle-aged", "Senior", "Old"))
summary(groupedAdultUCI)

groupedAdultUCI[[ "hours-per-week"]] <- ordered(cut(groupedAdultUCI[["hours-per-week"]], 
                                             c(0,25,40,60,168)),
                        labels = c("Part-time", "Full-time", "Over-time", "Workaholic"))
			    
# Probably 99999 is a NA, but let's leave it as it is
groupedAdultUCI[[ "capital-gain"]] <- ordered(cut(groupedAdultUCI[[ "capital-gain"]],
      c(-Inf,0,median(groupedAdultUCI[[ "capital-gain"]][groupedAdultUCI[[ "capital-gain"]]>0]),Inf)),
               labels = c("None", "Low", "High"))

groupedAdultUCI[[ "capital-loss"]] <- ordered(cut(groupedAdultUCI[[ "capital-loss"]],
      c(-Inf,0, median(groupedAdultUCI[[ "capital-loss"]][groupedAdultUCI[[ "capital-loss"]]>0]),Inf)), labels = c("none", "low", "high"))

summary(groupedAdultUCI)

### unknown meaning and repeated
groupedAdultUCI[["fnlwgt"]] <- NULL
groupedAdultUCI[["education-num"]] <- NULL
groupedAdultUCI[["marital-status"]] <- NULL

# what to do with NA's (especially those in 'income'?) let's leave them to try to find relationships

### coerce to transactions
Adult <- as(groupedAdultUCI, "transactions")
Adult

# summary(Adult)

# dev.off()
# par(mar = rep(2, 4))
itemFrequencyPlot(Adult, support = 0.1, cex.names=1.0)

# first attempt is absolutely crazy
# rules1 <- apriori(Adult, parameter = list(support = 0.01, confidence = 0.6))
# rules1
# summary(rules1)

### one option is obviously to increase the support and confidence to get less rules. Era 0.2

library("arules");
library("arulesViz");
# install.packages("arulesViz")

rulesap <- apriori(Adult, parameter = list(support = 0.01, confidence = 0.80))
plot(rulesap, measure=c("support","lift"), shading="confidence");
rules <- sort(rulesap, by = "lift")[1:10]
inspect(rules)

### another option is to sort by lift

rules3 <- sort(rules1, by = "lift")[1:10]
inspect(rules3)

### still another option is to zoom on some specific rules
rulesIncomeSmall <- subset(rules, subset = rhs %in% "age=Young" & lift > 1.2)

inspect(head(sort(rulesIncomeSmall, by = "confidence"), n = 3))

### if you wish to save the results on a file
write(rulesIncomeSmall, file = "ADULT-rulesIncomeSmall.csv", sep = ",", col.names = NA)
