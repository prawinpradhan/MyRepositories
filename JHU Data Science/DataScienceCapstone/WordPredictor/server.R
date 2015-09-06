library(shiny)
library(tm)
library(ggplot2)

source("predict.R")

shinyServer(
  
  function(input, output) {
    predictedList <- reactive({ 
        predict(input$text)
                          })
    output$text1 <- renderText({ 
      #predictedList <- predict(input$text)
      as.character( predictedList()[[1]][1])
    })
    
    output$PredictionStrength <- renderPlot({
      predictedList()[[2]]
    })
    
  }
)