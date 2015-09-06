---
title :Human Activity Recognition by Machine Learning
author:Prawin Pradhan
date  :27th July 2014
---
    
  The goal of this document is to explain the how the Machine learning Algorithm analyses and predicts the manner of
Excercise that was performed.

* Background of Study

Using devices such as Jawbone Up, Nike FuelBand, and Fitbit it is now possible to collect a large amount of data 
about personal activity relatively inexpensively. These type of devices are part of the quantified self movement a group of enthusiasts who take measurements about themselves regularly to improve their health, to find patterns
in their behavior, or because they are tech geeks. One thing that people regularly do is quantify how much of a 
particular activity they do, but they rarely quantify how well they do it. In this project, your goal will be to 
use data from accelerometers on the belt, forearm, arm, and dumbell of 6 participants. They were asked to perform 
barbell lifts correctly and incorrectly in 5 different ways. 
More information is available from the website here: [http://groupware.les.inf.puc-rio.br/har](http://groupware.les.inf.puc-rio.br/har)


* Data Source

Training Data
  
  [https://d396qusza40orc.cloudfront.net/predmachlearn/pml-training.csv](https://d396qusza40orc.cloudfront.net/predmachlearn/pml-training.csv)

 Test Data
  
  [https://d396qusza40orc.cloudfront.net/predmachlearn/pml-testing.csv](https://d396qusza40orc.cloudfront.net/predmachlearn/pml-testing.csv)

## Analysis


```r

## Loading the required Training Data and packages
setwd("C:\\Users\\Prawin\\Desktop\\Coursera\\ML")
training_data <- read.csv("pml-training.csv", na.strings = c(NA, ""))
validation <- read.csv("pml-testing.csv", na.strings = c(NA, ""))
require(caret)
require(ggplot2)
require(data.table)
require(ipred)
require(plyr)
```


## Feature Selection

We can see that many fields predominantly dont have any values(i.e. NA). These are redundant features and will not be used for prediciton.


```r
NAColms <- sapply(training_data, function(x) {
    ifelse(0.8 * length(x) < sum(is.na(x)), TRUE, FALSE)
})
training_data <- training_data[, !NAColms]  # Removing the Columns which predominantly have NULL Values
training_data <- training_data[, -1]  # Removing the index Coloumn
## New Training Dataset consists of
names(training_data)
```

```
##  [1] "user_name"            "raw_timestamp_part_1" "raw_timestamp_part_2"
##  [4] "cvtd_timestamp"       "new_window"           "num_window"          
##  [7] "roll_belt"            "pitch_belt"           "yaw_belt"            
## [10] "total_accel_belt"     "gyros_belt_x"         "gyros_belt_y"        
## [13] "gyros_belt_z"         "accel_belt_x"         "accel_belt_y"        
## [16] "accel_belt_z"         "magnet_belt_x"        "magnet_belt_y"       
## [19] "magnet_belt_z"        "roll_arm"             "pitch_arm"           
## [22] "yaw_arm"              "total_accel_arm"      "gyros_arm_x"         
## [25] "gyros_arm_y"          "gyros_arm_z"          "accel_arm_x"         
## [28] "accel_arm_y"          "accel_arm_z"          "magnet_arm_x"        
## [31] "magnet_arm_y"         "magnet_arm_z"         "roll_dumbbell"       
## [34] "pitch_dumbbell"       "yaw_dumbbell"         "total_accel_dumbbell"
## [37] "gyros_dumbbell_x"     "gyros_dumbbell_y"     "gyros_dumbbell_z"    
## [40] "accel_dumbbell_x"     "accel_dumbbell_y"     "accel_dumbbell_z"    
## [43] "magnet_dumbbell_x"    "magnet_dumbbell_y"    "magnet_dumbbell_z"   
## [46] "roll_forearm"         "pitch_forearm"        "yaw_forearm"         
## [49] "total_accel_forearm"  "gyros_forearm_x"      "gyros_forearm_y"     
## [52] "gyros_forearm_z"      "accel_forearm_x"      "accel_forearm_y"     
## [55] "accel_forearm_z"      "magnet_forearm_x"     "magnet_forearm_y"    
## [58] "magnet_forearm_z"     "classe"
```


#### Removing the factor variables
The data provided here has few factor varibles which are observed to be not in any way related to the class output and hence such factors can be excluded.

```r
classeVar <- training_data$classe
training_data <- training_data[, !sapply(training_data, is.factor)]
training_data$classe <- classeVar
ncol(training_data)  #no of columns remaining after removing the factor variables
```

```
## [1] 56
```


Remove predictors unsuitable for modelling wherein any correlation that exists would likely be spurious 
and therefore cause the model to perform poorly.


```r
training_data <- training_data[, !names(training_data) %in% c("X", "user_name", 
    "raw_timestamp_part_1", "raw_timestamp_part_2", "cvtd_timestamp", "new_window", 
    "num_window")]
validation <- validation[, !names(validation) %in% c("X", "user_name", "raw_timestamp_part_1", 
    "raw_timestamp_part_2", "cvtd_timestamp", "new_window", "num_window")]
```

```
## Error: object 'validation' not found
```


#### Cross vaidation 
We will be splitting the data into training and test dataset for cross validation in order to test the out-of-sample performance of the fitted models.


```r
set.seed(1234)

inTrain = createDataPartition(training_data$classe, p = 0.6, list = FALSE)
training <- training_data[inTrain, ]
testing <- training_data[-inTrain, ]
dim(training)
```

```
## [1] 11776    53
```

```r
dim(testing)
```

```
## [1] 7846   53
```


## Modelling
Using the treebag modeling

```r
ModFit <- train(classe ~ ., data = training, method = "treebag", trControl = trainControl(method = "none"))
ModFit$finalModel
```

```
## 
## Bagging classification trees with 25 bootstrap replications
```


## Out of Sample Accuracy



```r
confusionMatrix(predict(ModFit, newdata = testing), testing$classe)
```

```
## Confusion Matrix and Statistics
## 
##           Reference
## Prediction    A    B    C    D    E
##          A 2223   24    3    1    1
##          B    4 1474   16    5    5
##          C    3   17 1333   21    6
##          D    2    3   16 1256    4
##          E    0    0    0    3 1426
## 
## Overall Statistics
##                                        
##                Accuracy : 0.983        
##                  95% CI : (0.98, 0.986)
##     No Information Rate : 0.284        
##     P-Value [Acc > NIR] : < 2e-16      
##                                        
##                   Kappa : 0.978        
##  Mcnemar's Test P-Value : 0.00183      
## 
## Statistics by Class:
## 
##                      Class: A Class: B Class: C Class: D Class: E
## Sensitivity             0.996    0.971    0.974    0.977    0.989
## Specificity             0.995    0.995    0.993    0.996    1.000
## Pos Pred Value          0.987    0.980    0.966    0.980    0.998
## Neg Pred Value          0.998    0.993    0.995    0.995    0.998
## Prevalence              0.284    0.193    0.174    0.164    0.184
## Detection Rate          0.283    0.188    0.170    0.160    0.182
## Detection Prevalence    0.287    0.192    0.176    0.163    0.182
## Balanced Accuracy       0.995    0.983    0.984    0.986    0.994
```

This Model has a good accuracy of 98.3%. So we will use this model for our prediction.

## Predictions


```r
answers <- predict(ModFit, newdata = validation)
answers
```

```
##  [1] B A B A A E D B A A B C B A E E A B B B
## Levels: A B C D E
```


Function for writing answer files 


```r
pml_write_files = function(x) {
    n = length(x)
    for (i in 1:n) {
        filename = paste0("problem_id_", i, ".txt")
        write.table(x[i], file = filename, quote = FALSE, row.names = FALSE, 
            col.names = FALSE)
    }
}
```


Write answer files indexed by problem_id


```r
pml_write_files(answers)
```

```
## Error: object 'answers' not found
```

