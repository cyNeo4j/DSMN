## set up data IN:
degree <- c(1,	2,	3,	4,	5,	6,	7,	8,	9,	10,	11,	12,	13,	14,	15,	16,	17,	18,	19,	20,	22,	24,	25,	26,	28,	34,	35,	36,	38)
AllInteractions <- c(815, 427, 205, 115, 70, 39, 18, 14, 18, 9, 6, 4, 3, 1, 2, 2, 2, 3, NA, 1, 1, 2, 1, NA, NA, 1, NA, 1, 1) #ALL_IN
WikiPathwaysInteractions <- c(594,	274,	149,	62,	40,	27,	6,	7,	10,	3,	6,	1,	1,	NA,	1,	NA,	NA,	1,	1,	1,	NA,	NA,	NA,	NA,	NA,	NA,	1,	NA,	NA) #CurIN
ReactomeInteractions <- c(594,	274,	149,	62,	40,	27,	6,	7,	10,	3,	6,	1,	1,	NA,	1,	NA,	NA,	1,	1,	1,	NA,	NA,	NA,	NA,	NA,	NA,	1,	NA,	NA) #Reactome_IN
LIPIDMAPSInteractions <- c(99,	36,	34,	2,	5,	3,	1,	NA,	1,	NA,	1,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	1,	NA,	NA,	NA,	NA) #LIPIDSIN

## add extra space to right margin of plot within frame
par(mar=c(5, 4, 4, 6) + 0.1)

## Plot first set of data and draw its axis
plot(degree, AllInteractions, pch=16, axes=FALSE, ylim=c(0,1000), xlab="", ylab="", 
     type="p",col="black") 
axis(2, ylim=c(0,1000),col="black",las=1)  ## las=1 makes horizontal labels
mtext("Amount of Nodes",side=2,line=2.5)
box()

lines(WikiPathwaysInteractions, pch=1, type = "p", col = "blue")
lines(ReactomeInteractions, pch=2, type = "p", col = "orange")

## Allow a second plot on the same graph
par(new=TRUE)

## Plot the second plot and put axis scale on right
plot(degree, LIPIDMAPSInteractions, pch=15,  xlab="", ylab="", ylim=c(0,100), 
     axes=FALSE, type="p", col="red")
## a little farther out (line=4) to make room for labels
mtext("Amount of Nodes",side=4,col="red",line=4) 
axis(4, ylim=c(0,100), col="red",col.axis="red",las=1)

## Draw the vertical axis
axis(1,pretty(range(degree),10))
mtext("Degree",side=1,col="black",line=2.5)  

## Add Legend
legend("topright",legend=c("All Interactions", "WikiPathways", "Reactome", "LIPID MAPS"),
       text.col=c("black", "blue", "orange", "red"),pch=c(16,1,2,15),col=c("black","blue", "orange", "red"))


##############################

## set up data OUT:
degree <- c(1,	2,	3,	4,	5,	6,	7,	8,	9,	10,	11,	12,	13,	14,	16,	17,	18,	19,	20,	24,	30)
AllInteractions <- c(1165,	513,	175,	105,	49,	32,	19,	13,	10,	5,	7,	6,	2,	3,	3,	2,	2,	1,	NA,	1,	1) #ALL_OUT
WikiPathwaysInteractions <- c(912,	337,	114,	49,	26,	12,	10,	5,	6,	4,	3,	2,	NA,	2,	NA,	NA,	NA,	NA,	NA,	NA,	NA) #CurOUT
ReactomeInteractions <- c(519,	286,	84,	35,	26,	10,	7,	5,	7,	5,	1,	1,	2,	NA,	1,	NA,	NA,	1,	1,	NA,	1) #Reactome_OUT
LIPIDMAPSInteractions <- c(214,	52,	13,	1,	NA,	NA,	NA,	1,	NA,	1,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA,	NA) #LIPIDS_OUT

## add extra space to right margin of plot within frame
par(mar=c(5, 4, 4, 6) + 0.1)

## Plot first set of data and draw its axis
plot(degree, AllInteractions, pch=16, axes=FALSE, ylim=c(0,1200), xlab="", ylab="", 
     type="p",col="black") 
axis(2, ylim=c(0,1200),col="black",las=1)  ## las=1 makes horizontal labels
mtext("Amount of Nodes",side=2,line=2.5)
box()

lines(WikiPathwaysInteractions, pch=1, type = "p", col = "blue")
lines(ReactomeInteractions, pch=2, type = "p", col = "orange")

## Allow a second plot on the same graph
par(new=TRUE)

## Plot the second plot and put axis scale on right
plot(degree, LIPIDMAPSInteractions, pch=15,  xlab="", ylab="", ylim=c(0,250), 
     axes=FALSE, type="p", col="red")
## a little farther out (line=4) to make room for labels
mtext("Amount of Nodes",side=4,col="red",line=4) 
axis(4, ylim=c(0,250), col="red",col.axis="red",las=1)

## Draw the vertical axis
axis(1,pretty(range(degree),10))
mtext("Degree",side=1,col="black",line=2.5)  

## Add Legend
legend("topright",legend=c("All Interactions", "WikiPathways", "Reactome", "LIPID MAPS"),
       text.col=c("black", "blue", "orange", "red"),pch=c(16,1,2,15),col=c("black","blue", "orange", "red"))

