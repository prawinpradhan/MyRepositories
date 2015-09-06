## Reading the Dataset.
data <- read.csv("exdata-data-household_power_consumption//household_power_consumption.txt", na.strings = "?" , sep = ";" ,stringsAsFactors=FALSE)

##Getting the required Values from the Dataset.
reqDF <- data[data$Date == "1/2/2007" | data$Date == "2/2/2007", ]

##Generating the required Values for the x-axis
wDays <- strptime(paste(reqDF$Date, reqDF$Time), format='%d/%m/%Y %H:%M:%S')

##Storing old mfrow value and reassigning new value to fit multiple plots in a single screen
oldMfrow <- par("mfrow")
par(mfrow = c(2, 2))

##Plotting the first graph
plot(wDays,reqDF$Global_active_power,type="l",col=c("black"),ylab="Global Active Power",xlab="")
##Plotting the second graph
plot(wDays, reqDF$Voltage , type="l", xlab="datetime", ylab="Voltage")
##Plotting the third graph
plot(wDays, reqDF$Sub_metering_1, type="l", xlab="", ylab="Energy sub metering")
lines(wDays, reqDF$Sub_metering_2, type="l",col="red")
lines(wDays, reqDF$Sub_metering_3, type="l",col="blue")
legend("topright", col=c("black", "red", "blue"), cex = 0.70, lty=1,legend = c("Sub_metering_1", "Sub_metering_2", "Sub_metering_3"),bty = "n")
##Plotting the fourth graph
plot(wDays, reqDF$Global_reactive_power, type="l", xlab="datetime", ylab="Global_reactive_power")

##Printing the results to png file
dev.copy(png,file="plot4.png",width=480,height=480,units="px")
dev.off()

##Resetting the old value of mfrow
par(mfrow = c(1, 1))
