## Reading the Dataset.
data <- read.csv("exdata-data-household_power_consumption//household_power_consumption.txt", na.strings = "?" , sep = ";" ,stringsAsFactors=FALSE)

##Getting the required Values from the Dataset.
reqDF <- data[data$Date == "1/2/2007" | data$Date == "2/2/2007", ]

##Plotting the required histogram.
with(reqDF, hist(Global_active_power,col=c("red"),xlab="Global Active Power (kilowatts)" , main = "Global Active Power"))

##Generating the required png file.
dev.copy(png,file="plot1.png",width=480,height=480,units="px")
dev.off()

