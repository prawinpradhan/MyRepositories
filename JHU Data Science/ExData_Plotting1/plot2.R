## Reading the Dataset.
data <- read.csv("exdata-data-household_power_consumption//household_power_consumption.txt", na.strings = "?" , sep = ";" ,stringsAsFactors=FALSE)

##Getting the required Values from the Dataset.
reqDF <- data[data$Date == "1/2/2007" | data$Date == "2/2/2007", ]

##Generating the required Values for the x-axis
wDays <- strptime(paste(reqDF$Date, reqDF$Time), format='%d/%m/%Y %H:%M:%S')

##Creating the required plot
plot(wDays, reqDF$Global_active_power, type="l", xlab="", ylab="Global Active Power (kilowatts)")

##Generating the required png file.
dev.copy(png,file="plot2.png",width=480,height=480,units="px")
dev.off()
