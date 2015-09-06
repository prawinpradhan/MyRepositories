library(shiny)


shinyUI(fluidPage(
  
  
  titlePanel(
     "Word Predictor"
    ),
  
  sidebarLayout(
    sidebarPanel(
      textInput("text", label = h3("Type in few words!!"), 
                           value = "Good Morning!!"),
      br(),
      br(),
      
      h6("This App is build as a Capstone Project for:"),   
      a("Data Science Specilization Course", href = "https://www.coursera.org/course/dsscapstone"),
      
      h6("Created by:"),
      a("Prawin Pradhan", href = "https://www.linkedin.com/in/prawinpradhan")
      
      ),
    mainPanel(br(),
              h3("Predicted Word!!"),
              h4(textOutput("text1"),
              plotOutput("PredictionStrength"))
       )
    )
  )
)