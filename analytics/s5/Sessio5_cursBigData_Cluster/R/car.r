############################################################### CLUSTERING OF CAR DATA

# ESPECIFICAR EL DIRECTORIO DE TRABAJO DONDE TENEMOS LOS DATOS
# > setwd("C:/path/to/directory")
# O EN Archivo - Cambiar dir ... ESPECIFICAR EL ARCHIVO DE TRABAJO

setwd("D:/docent/curs Big Data/2a edicio/Sessio4/R")

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

# EJECUCION DEL ANALISIS DE COMPONENTES PRINCIPALES
nd = 4    # numero de dimensiones retenidas (SESION 4)
pca.car <- PCA(car,ncp=nd, quali.sup=c(3,14,18),quanti.sup=17)

Psi = pca.car$ind$coord

# CLUSTERING DE LOS COCHES SEGUN SUS CARACTERISTICAS TECNICAS

# CALCULO DE LA MATRIZ DE DISTANCIAS ENTRE COCHES A PARTIR DE LAS LAS COMPONENTES SIGNFICATIVAS
dist.car <- dist(Psi)

# CLUSTERING JERARQUICO, METODO DE Ward
hclus.car <- hclust(dist.car,method="ward.D2")

# PLOT DEL ARBOL JERAQUICO OBTENIDO
plot(hclus.car,cex=0.3)

# DIAGRAMA DE BARRAS DEL INDICE DE AGREGACION DE LAS ULTIMAS 29 AGREGACIONES FORMADAS
barplot(hclus.car$height[(nrow(car)-30):(nrow(car)-1)])

# CUANTAS CLASES (CLUSTERS) DE COCHES HAY?
nc = 6

# CORTE DEL ARBOL DE AGREGACION EN nc CLASES
cut6 <- cutree(hclus.car,nc)

# GRAFICO DE LAS nc CLASES EN EL PRIMER PLANO FACTORIAL
plot(Psi[,1],Psi[,2],type="n",main="Clustering of cars in 6 classes")
text(Psi[,1],Psi[,2],col=cut6,labels=iden,cex = 0.6)
abline(h=0,v=0,col="gray")
legend("topleft",c("c1","c2","c3","c4","c5","c6"),pch=20,col=c(1:6))

# NUMERO DE COCHES POR CLASE
table(cut6)

# CALIDAD DEL CORTE DEL ARBOL JERARQUICO

cdg <- aggregate(as.data.frame(Psi),list(cut6),mean)[,2:(nd+1)]
cdg

Bss <- sum(rowSums(cdg^2)*as.numeric(table(cut6)))
Tss <- sum(Psi^2)

100*Bss/Tss


# CONSOLIDACION DE LA PARTICION

# CALCULO DE LOS CENTROIDES DE LAS nc CLASES OBTENIDAS POR CORTE DEL ARBOL JERARQUICO
cdg.nc <- aggregate(as.data.frame(Psi),list(cut6),mean)[,2:(nd+1)]


# ALGORITMO kmeans CON CENTROS INICIALES EN LOS CENTROIDES cdg.nc
kmeans <- kmeans(Psi,centers=cdg.nc)

# NUMERO DE COCHES POR CLASE FINAL
kmeans$size

# CALIDAD DE LA PARTICION FINAL EN 6 CLASSES

100*kmeans$betweenss/kmeans$totss

# VISUALIZACION DE LAS nc CLASES FINALES EN EL PRIMER PLANO FACTORIAL
plot(Psi[,1],Psi[,2],type="n",main="Clustering of cars in 6 classes")
text(Psi[,1],Psi[,2],col=kmeans$cluster,labels=iden,cex = 0.6)
abline(h=0,v=0,col="gray")
legend("topleft",c("c1","c2","c3","c4","c5","c6"),pch=20,col=c(1:6))


# INTERPRETACION DE LAS nc CLASES FINALES OBTENIDAS
catdes(cbind(as.factor(kmeans$cluster),car),num.var=1,proba=0.001)

