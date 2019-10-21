import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class SmartScheduler {
	public static void main(String... args)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.width -= 50;
		screenSize.height -= 50;
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JLabel label1 = new JLabel();
		
		//5, 0 is Sunday, 6 is Saturday.
		int start_year = 2010;
		int start_month = 0; //January
		int start_day = 5; //Jan 1, 2010 = Friday
		
		int max_years = 20; //Go to 2030
		int max_months = 12; //12 - 1 (start month), offset by 1 because start at 0.
		int[] max_days = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		HashMap<Integer, String> day_of_week = new HashMap<Integer, String>();
		day_of_week.put(0, "Sunday");
		day_of_week.put(1, "Monday");
		day_of_week.put(2, "Tuesday");
		day_of_week.put(3, "Wednesday");
		day_of_week.put(4, "Thursday");
		day_of_week.put(5, "Friday");
		day_of_week.put(6, "Saturday");
		
		
		//Get current month
		int current_month = 9 * 12 + 9;
		//Get max days in month
		int current_max = max_days[(current_month % 12) - 1]; //117
		//Create array list to hold all days
		List<ArrayList<String>> month_list = new ArrayList<ArrayList<String>>();
		
		for (int year = 0; year < max_years; year++)
		{
			
			for (int month = 0; month < max_months; month++)
			{
				//If leap year, add a day to february
				ArrayList<String> temp_month = new ArrayList<String>();
				if (month == 1 && (year + start_year) % 4 == 0) //February
				{
					max_days[month] += 1;
				}
				
				//Get the days of the week for the month
				for (int day = 0; day < max_days[month]; day++)
				{
					temp_month.add(day_of_week.get((day + start_day) % 7));
				}
				
				//Add the list
				month_list.add((ArrayList<String>) temp_month.clone());
				//Update start day for next monthW
				start_day = ((max_days[month] + start_day) % 7); 
				
				if (month == 1 && (year + start_year) % 4 == 0) //February
				{
					max_days[month] -= 1;
				}
			}
		}
		System.out.println(month_list.get(current_month + 1));
		label1.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		panel.setLayout(new GridLayout(5, 5));
		panel.add(label1);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenSize);
		//frame.setVisible(true);
	}
	/*
	public static <T> ArrayList<T> deep_copy_list(ArrayList<T> original)
	{
		ArrayList<T> list_clone = new ArrayList<T>();
		Iterator<T> iterator = original.iterator();
		 
		while(iterator.hasNext())
		{
		    //Add the object clones
			list_clone.add( (T) iterator.next()); 
		}
		return list_clone;
	}
	*/
}