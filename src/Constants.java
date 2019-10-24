import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Constants {
	static final int start_year = 2010;
	static final int start_month = 0; //January
	static final int first_day = 0;
	static int start_day = 5; //Jan 1, 2010 = Friday
	
	static List<ArrayList<Integer>> month_list = new ArrayList<ArrayList<Integer>>();;
	
	static final int max_years = 20; //Go to 2030
	static final int max_months = 12; //12 - 1 (start month), offset by 1 because start at 0.
	static final int number_days_per_week = 7;
	static final int total_months = max_years * max_months - 1;
	static int[] max_days = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	static final int max_days_per_month = Collections.max(
			Arrays.stream(max_days)
			.boxed()
			.collect(Collectors.toList()));
	
	static final HashMap<Integer, String> day_of_week_map = new HashMap<Integer, String>() {{
			put(0, "Sunday");
			put(0, "Sunday");
			put(1, "Monday");
			put(2, "Tuesday");
			put(3, "Wednesday");
			put(4, "Thursday");
			put(5, "Friday");
			put(6, "Saturday");
		}};
		
	static final String[] monthLabels = 
			{"January", 
			"February", 
			"March", 
			"April", 
			"May", 
			"June", 
			"July", 
			"August", 
			"September", 
			"October", 
			"November", 
			"December"};
		
	static final String[] yearLabels = 
			IntStream.range(start_year, start_year + max_years)
			.mapToObj(String::valueOf)
			.collect(Collectors.joining(","))
			.split(",");
	

	public static void initializeDates()
	{
		for (int year = 0; year < Constants.max_years; year++)
		{
			
			for (int month = 0; month < Constants.max_months; month++)
			{
				//If leap year, add a day to february
				ArrayList<Integer> temp_month = new ArrayList<Integer>();
				if (month == 1 && (year + Constants.start_year) % 4 == 0) //February
				{
					Constants.max_days[month] += 1;
				}
				
				//Get the days of the week for the month
				for (int day = 0; day < Constants.max_days[month]; day++)
				{
					temp_month.add((day + Constants.start_day) % 7);
				}
				
				//Add the list
				month_list.add(temp_month);
				//Update start day for next monthW
				Constants.start_day = ((Constants.max_days[month] + Constants.start_day) % 7); 
				
				if (month == 1 && (year + Constants.start_year) % 4 == 0) //February
				{
					Constants.max_days[month] -= 1;
				}
			}
		}
	}
				
	
	public static int getDifference(String date_selected)
	{
		String[] date_parsed = date_selected.split("/");
		int year = Integer.parseInt(date_parsed[2]);
		int month = Integer.parseInt(date_parsed[0]) - 1;
		int day = Integer.parseInt(date_parsed[1]) - 1;
		int count = 0;
		
		while (true)
		{
			if (year == start_year && month == start_month && day == first_day)
			{
				return count;
			}
			if (day != 0)
			{
				day--;
			}
			else
			{
				if (month != 0)
				{
					//Reset day to next highest
					month--;
					day = month_list.get((year - start_year) * 12 + month).size() - 1;			
				}
				else //first month, lower the year
				{
					year--;
					month=11;
					day = month_list.get((year - start_year) * 12 + month).size() - 1;
				}
			}
			count++;
			
		}
	}

}
