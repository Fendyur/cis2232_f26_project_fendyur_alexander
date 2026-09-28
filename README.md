# cis2232_f26_project_fendyur_alexander
##Sports and Rec Rentals

###Project Group
BA / Business Client	Julian
Developer	Alex
Project Manager / QA	Chandler

###Description
This web application will allow a user to book a room at a recreational facility and keep track of booking details. It allows the user to enter information like room type, date, time, and number of people after selecting a room. A booking may also include equipment rental or birthday party add-ons such as decorations and cake. The application will calculate the total cost and track the status of each booking based on the information entered.

###Project Base Color
Steel Blue
(#4682B4)

###Fields
Name:	(Data Type)	Description
id:	(int)	Unique Identifier 
groupNum:	(int)	How many people are attending
isBirthday:	(bool)	Is it someone’s birthday decoration and cake cost
equipmentNeeded:	(bool)	Do they need equipment
roomType:	(String)	Room type(party room, gym, small rec room)
bookingDate:	(String)	Date of the booking (yyyy-mm-dd)
startTime:	(String)	Booking start time
endTime:	(String)	Booking end time
bookingName:	(String)	Name for the booking
phone:	(String)	Phone number for the booking
email:	(String)	Email information for the booking
basePrice:	(double)	Cost of the room
tax:	(double)	Standard tax fee (15%)
totalPrice:	(double)	The price for what has been booked
birthdayCost:	(double)	Birthday cost

###Calculation
When a user enters a new booking, the application will calculate the total cost of the booking. The entry will be analyzed to check whether equipment rental is needed and whether the booking is marked as a birthday, and the corresponding costs will be added to the base room price. The final total cost will be added to the row of the database.
