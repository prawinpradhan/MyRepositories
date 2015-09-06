## Reading the Dataset.
data <- read.csv("exdata-data-household_power_consumption//household_power_consumption.txt", na.strings = "?" , sep = ";" ,stringsAsFactors=FALSE)

##Getting the required Values from the Dataset.
reqDF <- data[data$Date == "1/2/2007" | data$Date == "2/2/2007", ]

##Generating the required Values for the x-axis
wDays <- strptime(paste(reqDF$Date, reqDF$Time), format='%d/%m/%Y %H:%M:%S')

##Creating the required plot
plot(wDays, reqDF$Sub_metering_1, type="l", xlab="", ylab="Energy sub metering")
##Appending Submeetering_2 Data on the same plot
lines(wDays, reqDF$Sub_metering_2, type="l",col="red")
##Appending Submeetering_3 Data on the same plot
lines(wDays, reqDF$Sub_metering_3, type="l",col="blue")
##Adding Legend to the plot
legend("topright", col=c("black", "red", "blue"), cex = 0.80, lty=1,legend = c("Sub_metering_1", "Sub_metering_2", "Sub_metering_3"))

##Generating the required png file.
dev.copy(png,file="plot3.png",width=480,height=480,units="px")
dev.off()
