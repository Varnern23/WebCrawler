package webScraper;
//Jsoup will be the library we use to read html and actual do the scraping
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
//BufferedWriter and FileWriter is what we will use to output a csv file with the desired information
import java.io.BufferedWriter;
import java.io.FileWriter;
//will be used to catch exceptions made by the file writer and Jsoup
import java.io.IOException;

public class Scraper {
	String URL = "https://portal.nosscr.org/iSamples/SharedContent/ContactManagement/Directory.aspx";
	
	public void scrape(Document doc) {
		// we store the url of the site here
				String URL = "https://portal.nosscr.org/iSamples/SharedContent/ContactManagement/Directory.aspx";
				//we keep track of how many people we've gone through here
				int count = 0;
				// We use a try catch block to test the code and catch exceptions while being executed
				try {
					//Create a buffered writer which just describes how the file writer inputs strings
					BufferedWriter writer = new BufferedWriter(new FileWriter("out.csv"));
					//This lets us access the websites html
					//Here we go through the html and find a class that holds the desired data
					Elements people = doc.select(".Description");
					//we create a loop that goes through all the elements
					for(Element person: people) {
						//Here we use.html to be able to easily see each indivdual type of data since they are all in the same <span> seperated by <br>
						String info = person.html().replace("<br>", "\n");
						String info3 = info.replace(">", "\n");
						String[] info2 = info3.split("\n");
						// These all let me access specific parts of the persons info easily
						String[] names = info2[3].split("\\s");
						String[] cities = info2[5].replace(",", "").split("\\s");
						String first = names[0];
						String last = names[names.length - 1];
						//gets rid of anchor around email so we just see the email
						String email = info2[7].replace("</a", "");
						String city = "";
						String zip = "";
						String state = "";
						//Stores if we do not have an address
						boolean lenore = false;
						//The next few lines discover if the current person has an address attached and changes their info to match otherwise sets their address correctly
						if(cities.length == 1) {
							email = info2[5].replace("</a", "");
							lenore = true;
						}
						else {
							for (int i = 0; i < cities.length-2;i++) {
								city += cities[i];
							}
							state = cities[cities.length-2];
							zip = cities[cities.length-1];
						}
						//These are conditionals to test cases of different sizes since we need all the info except the membership which is always the last thing.
						
						if(info2.length == 11 && !lenore) {
							writer.write(first + "," + last + "," + city + "," + state + "," + zip + "," + email + ",");
						}
						else if(info2.length == 13){
							writer.write(first + "," + last + "," + city + "," + state + "," + zip + "," + email + "," + info2[10].replace(",", "") + ",");
						}
						else if(info2.length == 15) {
							writer.write(first + "," + last + "," + city + "," + state + "," + zip + "," + email + "," + info2[10].replace(",", "") + "," + info2[12].replace(",", "") + ",");
						}
						else if(info2.length == 11 && lenore) {
							writer.write(first + "," + last + "," + email + "," + info2[8].replace(",", "") + ",");
						}
						else if (info2.length == 17) {
							writer.write(first + "," + last + "," + city + "," + state + "," + zip + "," + email + "," + info2[10].replace(",", "") + "," + info2[12].replace(",", "") + "," + info2[14].replace(",", "") + ",");
						}
						//Here we have a count to make sure we don't do more then 1000 people even though in this instance their can only be 1000
						count++;
						if (count >= 1000) {
							break;
						}
					}
					//close the writer so we get a filled in csv file
					writer.close();
					//here we catch exceptions and trace them to find the issue.
				}catch(IOException e) {
					e.printStackTrace();
				}
	}
	
}
