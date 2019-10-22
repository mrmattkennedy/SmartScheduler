import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Constants {
	static final int start_year = 2010;
	static final int start_month = 0; //January
	static  int start_day = 5; //Jan 1, 2010 = Friday
	
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
				
				

}
