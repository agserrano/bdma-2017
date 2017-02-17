###############################################################
# ESPECIFICAR EL DIRECTORIO DE TRABAJO DONDE TENEMOS LOS DATOS
# > setwd("C:/path/to/directory")
# O EN Archivo - Cambiar dir ... ESPECIFICAR EL ARCHIVO DE TRABAJO

setwd("/Users/tonet/Documents/posgrado/analytics/s4/Sessio4_cursBig_Data_PCA/R")

# CARGAMOS LAs LIBRERIAS NECESARIAS 
library(FactoMineR)

# LECTURA DEL FICHERO DE DATOS
car <- read.csv("car.csv",header=T,sep=";",dec=".")

# DIMENSIONES DEL FICHERO (FILAS Y COLUMNAS)
dim(car) 

# LOS PRIMEROS REGISTROS DEL FICHERO    
head(car)    

# LA PRIMERA COLUMNA CONTIENE LOS IDENTIFICADORES DE LOS COCHES
row.names(car) <- car$iden   

# ELIMINAMOS LA PRIMERA COLUMNA DEL FICHERO
car <- car[,-1]  

# DESCRIPCION SUMARIA DE LOS DATOS
summary(car) 

# EJECUCION DEL ANALISIS DE COMPONENTES PRINCIPALES
pca.car <- PCA(car,quali.sup=c(3,14,18),quanti.sup=17,graph=T)

# ELECCION DEL NUMERO DE COMPONENTES SIGNIFICATIVAS
pca.car$eig
plot(pca.car$eig$eigenvalue,type="l",main="Screeplot")

# CUANTAS COMPONETES SON SIGNFICATIVAS?
nd = 4

# GRAFICO DE LOS INDIVIDUOS EN LAS DIMENSIONES 1 Y 2
plot(pca.car, axes = c(1, 2), choix = c("ind"), habillage=18, label="quali", title="Plot of individuals", cex=0.7)

# GRAFICO DE LAS VARIABLES EN LAS DIMENSIONES 1 Y 2
plot(pca.car, axes = c(1, 2), choix = c("var"), title="Plot of variables")

# GRAFICO DE LOS INDIVIDUOS EN LAS DIMENSIONES 3 Y 4
plot(pca.car, axes = c(3, 4), choix = c("ind"), habillage=18, label="quali", title="Plot of individuals", cex=0.7)

# GRAFICO DE LAS VARIABLES EN LAS DIMENSIONES 1 Y 2
plot(pca.car, axes = c(3, 4), choix = c("var"), title="Plot of variables")

# ROTACION DE LAS COMPONENTES PARA BUSCAR COMPONENTES MAS INTERPRETABLES
pca.car.rot <- varimax(pca.car$var$cor[,1:nd])
pca.car.rot

# INTERPRETACION DE LAS COMPONENTES ROTADAS
# PROGRAMA PARA CALCULAR LAS COMPONENTES ROTADAS Psi.rot A PARTIR DE LAS COMPONENTES ORIGINALES Psi
Psi = pca.car$ind$coord[,1:nd]   # Psi contiene las componentes principales significativas originales (no rotadas)
Phi = pca.car$var$coord[,1:nd]   # Phi contiene las correlaciones de las variables con las componentes principales no rotadas 
X   = car[,c(1,2,4:13,15,16)]    # X contiene los datos activos
p = ncol(X)                      # p contiene el numero de variables activas
Xs = scale(X)                    # Xs contiene los datos estandarizados

iden = row.names(X)              # identificadores de los coches
etiq = names(X)                  # etiquetas de las variables


Phi.rot = pca.car.rot$loadings[1:p,]  # Phi.rot contiene las correlaciones de las variables con las componentes principales rotadas  


lmb.rot = diag(t(pca.car.rot$loadings) %*% pca.car.rot$loadings)
Psi_stan.rot = Xs %*% solve(cor(X)) %*% Phi.rot
Psi.rot = Psi_stan.rot %*% diag(sqrt(lmb.rot))

# DESCRIPCI?N DE LAS COMPONENTES ROTADAS SIGNIFICATIVAS
for (k in 1:nd) {print(paste("Descripci?n de la componente rotada",k)); print(condes(cbind(Psi.rot[,k],car),1))}


