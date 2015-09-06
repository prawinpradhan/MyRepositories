QuadDF <- readRDS("data/QuadDF.rds")
TriDF <- readRDS("data/TriDF.rds")
BiDF <- readRDS("data/BiDF.rds")

predict <- function(text_input) {


inputCrps <- Corpus(VectorSource(text_input))
inputCrps <- tm_map(inputCrps, content_transformer(tolower))
inputCrps <- tm_map(inputCrps, removeWords, stopwords("english"))
inputCrps[[1]]$content <- gsub(pattern = "[^a-z]", replacement = " ", inputCrps[[1]]$content)
inputCrps <-tm_map(inputCrps,stripWhitespace)
text_input <- inputCrps[[1]]$content
text_input <- gsub(pattern="^\\s+|\\s+$", x=text_input,replacement="")
text_input <- unlist(strsplit(text_input," "))
inputLength <- length(text_input)
if(length(text_input) != 0 ){
  
if(inputLength >= 3) {
  
  quadgramInput <- paste(text_input[(inputLength-2):inputLength],collapse=" ")
  quadgramInput <- paste("^",quadgramInput,"$",sep="")
  trigramInput <- paste(text_input[(inputLength-1):inputLength],collapse=" ")
  trigramInput <- paste("^",trigramInput,"$",sep="")
  bigramInput <- paste("^",text_input[inputLength],"$",sep="")


   predictedDF <- data.frame()
   
   quadGramIndex <- grep(pattern=quadgramInput,x=QuadDF$Prior)
  if(length(quadGramIndex) != 0){ 
  predictedDF <- rbind(predictedDF,data.frame(Predict=QuadDF$Prediction[quadGramIndex],Score=QuadDF$Probability[quadGramIndex]))
  }
  
   triGramIndex <- grep(pattern=trigramInput,x=TriDF$Prior)
  if(length(triGramIndex) != 0){
   predictedDF <- rbind(predictedDF,data.frame(Predict=TriDF$Prediction[triGramIndex],Score=0.4*TriDF$Probability[triGramIndex]))
  }
  
   biGramIndex <- grep(pattern=bigramInput,x=BiDF$Prior)
  if(length(biGramIndex) != 0){
   predictedDF <- rbind(predictedDF,data.frame(Predict=BiDF$Prediction[biGramIndex],Score=0.4*BiDF$Probability[biGramIndex]))
  }
  
  if(length(predictedDF) != 0 ){
   attach(predictedDF)
   predictedDF <- predictedDF[ order(-predictedDF$Score),]
   predictedDF <- predictedDF[!duplicated(predictedDF$Predict),]
  } 
   
 } else if (inputLength == 2) {
   trigramInput <- paste(text_input[(inputLength-1):inputLength],collapse=" ")
   trigramInput <- paste("^",trigramInput,"$",sep="")
   bigramInput <- paste("^",text_input[inputLength],"$",sep="")
   
   predictedDF <- data.frame()
   triGramIndex <- grep(pattern=trigramInput,x=TriDF$Prior)
   if(length(triGramIndex) != 0){
   predictedDF <- rbind(predictedDF,data.frame(Predict=TriDF$Prediction[triGramIndex],Score=TriDF$Probability[triGramIndex]))
   }
   
   biGramIndex <- grep(pattern=bigramInput,x=BiDF$Prior)
   if(length(biGramIndex) != 0){
   predictedDF <- rbind(predictedDF,data.frame(Predict=BiDF$Prediction[biGramIndex],Score=0.4*BiDF$Probability[biGramIndex]))
   }
   if(length(predictedDF) != 0 ){
   attach(predictedDF)
   predictedDF <- predictedDF[ order(-predictedDF$Score),]
   predictedDF <- predictedDF[!duplicated(predictedDF$Predict),]
   }
 } else if (inputLength == 1) {
   
   bigramInput <- paste("^",text_input,"$",sep="")
   
   predictedDF <- data.frame()
   biGramIndex <- grep(pattern=bigramInput,x=BiDF$Prior)
   if(length(biGramIndex) != 0){
   predictedDF <- data.frame(Predict=BiDF$Prediction[biGramIndex],Score=BiDF$Probability[biGramIndex])
   
   if(length(predictedDF) != 0 ){
   attach(predictedDF)
   predictedDF <- predictedDF[ order(-predictedDF$Score),]
   predictedDF <- predictedDF[!duplicated(predictedDF$Predict),]
   }
   
 }
}
 if(length(predictedDF$Predict)>5) {
   predictedDF = predictedDF[1:5,]
 }
 if(length(predictedDF)== 0 ){
   return(list("Sorry!!No Suggestions Available",NULL))
 } else if(length(predictedDF$Predict) > 1 ){ 
   ggplotObj<- ggplot(predictedDF,aes(x=reorder(Predict,Score),y=Score,fill=Score)) + geom_bar(stat="identity") +xlab("Predicton") + coord_flip() + theme(text = element_text(colour="blue",size=20))
   return(list(predictedDF$Predict[1],ggplotObj))
}  else if(length(predictedDF$Predict) == 1 ){ 
  ggplotObj<- ggplot(predictedDF,aes(x=Predict,y=Score)) + geom_bar(stat="identity",fill="blue") +xlab("Predicton") + coord_flip() + theme(text = element_text(colour="blue",size=20))
  return(list(predictedDF$Predict[1],ggplotObj))
}
}

else{
  return(list("The",NULL))
}
}