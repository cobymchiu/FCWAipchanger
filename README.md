# FCWA IP Changer

## Objective
Employees at Fairfax Water frequently travel between different sites to work on different pumps and systems within the company. 
This requires them to change the IP address of their machines in order to communicate with each site's associated devices, thus creating a demand for a user-friendly means of doing so.
A previous intern started this task and was able to achieve the desired functionality for the most part, but they wrote code that was difficult to read and modify, 
and there were improvements that could have been made to the GUI. Our task was to create a more sustainable version of the application and improve it. 

## Description
When launched, the user is presented with a graphical interface displaying their current IP address and a dropdown menu with a variety of button options. 
To change IP address, select a site from the dropdown and select the "Change IP address" button. The site;s PLC or router can also be pinged.

The list of sites and their IP addresses is populated from a Database.csv file. This list can be edited using the buttons displayed below the dropdown bar. 
A new list can also be imported to the application using the button in the lower left of the GUI. From that button, the current list can be also exported to a new CSV file.

The GUI also has a help menu and refresh button at the upper right corner.

To run this code from an IDE, run IPAddressDriver.java. In practice, this project was distributed as a JAR file.
