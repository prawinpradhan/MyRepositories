library(shiny)
shinyUI(pageWithSidebar(
  headerPanel("Simple Adder!!!"),
  sidebarPanel(
      textInput(inputId="text1", label = "First Input"),
      textInput(inputId="text2", label = "Second Input"),
      actionButton("goButton", "Add")
  ),
  mainPanel(
      p('Your First Input is'),
      textOutput('text1'),
      p('Your Second Input is'),
      textOutput('text2'),
      p('Added Output'),
      textOutput('text3')
  )
))
