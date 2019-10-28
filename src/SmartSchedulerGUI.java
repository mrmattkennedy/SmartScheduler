import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SmartSchedulerGUI implements ActionListener{
	
	private int current_month;
	private int max_slots;
	
	//Top panel
	private JComboBox yearCombo;
	private JComboBox monthCombo;
	private JPanel topPanel;
	
	//Dates panel
	private JPanel dates_panel;
	private JLabel[] days_of_week;
	private JButton[] days_buttons;
	private JButton left_button;
	private JButton right_button;
	private int selected_year;
	private int selected_month;
	
	//Bottom panel
	private JPanel bottom_panel;
	private JButton repeating_item_button;
	
	//Overall GUI
	private JFrame frame;
	private JPanel panel;
	private Dimension screenSize;
	
	
	public SmartSchedulerGUI()
	{
		current_month = ((Calendar.getInstance().get(Calendar.YEAR) - Constants.start_year) * Constants.max_months) + Calendar.getInstance().get(Calendar.MONTH);
		System.out.println(current_month);
		
		Constants.initializeDates();
		initializeGui();
	}
	
	
	
	private void initializeGui()
	{
		dates_panel = new JPanel();
		createTopPanel();
		createDatesPanel();
		createBottomPanel();
		
		frame = new JFrame();
		panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(dates_panel, BorderLayout.CENTER);
		panel.add(bottom_panel, BorderLayout.SOUTH);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.width -= 50;
		screenSize.height -= 50;
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenSize);
		frame.setVisible(true);
	}
	
	private void createTopPanel()
	{
		topPanel = new JPanel();
		yearCombo = new JComboBox(Constants.yearLabels);
		monthCombo = new JComboBox(Constants.monthLabels);
		
		//Set the indexes before action listener added or only month changes
		yearCombo.setSelectedIndex(current_month / 12);
		monthCombo.setSelectedIndex(current_month % 12);
		
		yearCombo.addActionListener(this);
		monthCombo.addActionListener(this);
		
		//Create left and right buttons. Just add left to start, right goes last
		left_button = new JButton("<-");
		left_button.addActionListener(this);
		right_button = new JButton("->");
		right_button.addActionListener(this);
		
		topPanel.add(left_button);
		topPanel.add(yearCombo);
		topPanel.add(monthCombo);
		topPanel.add(right_button);
		
		selected_year = yearCombo.getSelectedIndex();
		selected_month = monthCombo.getSelectedIndex();
	}
	
	private void createDatesPanel()
	{
		//Initialize variables
		dates_panel.removeAll();
		days_of_week = new JLabel[Constants.number_days_per_week];
		
		//Set the layout
		if (Constants.month_list.get(current_month).size() + Constants.month_list.get(current_month).get(0) > 35)
		{
			max_slots = Constants.number_days_per_week * 7;
			dates_panel.setLayout(new GridLayout(max_slots/Constants.number_days_per_week, Constants.number_days_per_week));
		}
		else
		{
			max_slots = Constants.number_days_per_week * 6;
			dates_panel.setLayout(new GridLayout(max_slots/Constants.number_days_per_week, Constants.number_days_per_week));
		}

		//Create the JLabels, one for each day of the week
		for (int i = 0; i < Constants.number_days_per_week; i++)
		{
			days_of_week[i] = new JLabel(Constants.day_of_week_map.get(i));
			days_of_week[i].setBorder(BorderFactory.createLineBorder(Color.black));
			
			dates_panel.add(days_of_week[i]);
		}
		
		//Get start of the month, end of the month, and everything in-between.
		
		//Create the buttons for each day of the month
		days_buttons = new JButton[Constants.max_days_per_month];
		for (int i = 0; i < Constants.max_days_per_month; i++)
		{
			days_buttons[i] = new JButton(Integer.toString(i+1));
			days_buttons[i].setBorder(BorderFactory.createLineBorder(Color.black));
			days_buttons[i].setHorizontalAlignment(SwingConstants.LEFT);
			days_buttons[i].setVerticalAlignment(SwingConstants.TOP);
			days_buttons[i].setFont(new Font("Helvetica", Font.BOLD, 18));
			days_buttons[i].addActionListener(this);
		}

		
		//First value in the month gets the starting day of the week, add boxes before
		for (int i = 0; i < Constants.month_list.get(current_month).get(0); i++)
		{
			dates_panel.add(Box.createRigidArea(new Dimension(5,0)));
		}
		
		//Loop through Month
		for (int i = 0; i < Constants.month_list.get(current_month).size(); i++)
		{
			dates_panel.add(days_buttons[i]);
		}
		
		//Fill in the r
		int remaining_empty_spaces = max_slots - days_of_week.length - Constants.month_list.get(current_month).size() - Constants.month_list.get(current_month).get(0);
		for (int i = 0; i < remaining_empty_spaces; i++)
		{
			dates_panel.add(Box.createRigidArea(new Dimension(5,0)));
		}
		
		//Highlight the current day
		if (((Calendar.getInstance().get(Calendar.YEAR) - Constants.start_year) * Constants.max_months) + Calendar.getInstance().get(Calendar.MONTH) == current_month)
		{
			days_buttons[Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1].setBackground(Color.RED);
		}
		dates_panel.repaint();
		dates_panel.revalidate();
	}
	
	private void createBottomPanel()
	{
		bottom_panel = new JPanel();
		repeating_item_button = new JButton("Add repeating item");
		repeating_item_button.addActionListener(this);
		bottom_panel.add(repeating_item_button);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == yearCombo)
		{
			selected_year = yearCombo.getSelectedIndex();
			current_month = (selected_year * 12) + selected_month;
		}
		else if (e.getSource() == monthCombo)
		{
			selected_month = monthCombo.getSelectedIndex();
			current_month = (selected_year * 12) + selected_month;
		}
		else if (e.getSource() == left_button)
		{
			if (current_month == 0)
			{
				return;
			}
			current_month -= 1;
			//If it's not January, move the month down
			if (monthCombo.getSelectedIndex() > 0)
			{
				monthCombo.setSelectedIndex(monthCombo.getSelectedIndex() - 1);
			}
			else
			{
				//If it is January, and it's not the first year, move the year down and the month to December
				if (yearCombo.getSelectedIndex() > 0)
				{
					yearCombo.setSelectedIndex(yearCombo.getSelectedIndex() - 1);
					monthCombo.setSelectedIndex(Constants.max_months - 1);
				}
				//It's January 2010, do nothing
			}
		}
		else if (e.getSource() == right_button)
		{
			if (current_month == Constants.total_months) //max months
			{
				return;
			}
			current_month += 1;
			//If it's not December, move the month up
			if (monthCombo.getSelectedIndex() < Constants.max_months - 1)
			{
				monthCombo.setSelectedIndex(monthCombo.getSelectedIndex() + 1);
			}
			else
			{
				//If it is December, and it's not the last year, move the year up and the month to January
				if (yearCombo.getSelectedIndex() < Constants.max_years)
				{
					yearCombo.setSelectedIndex(yearCombo.getSelectedIndex() + 1);
					monthCombo.setSelectedIndex(0);
				}
				//It's December 2029, do nothing
			}
			
		}
		else if (e.getSource() == repeating_item_button)
		{
			new RepeatingItem();
		}
		else
		{
			for (int i = 0; i < Constants.max_days_per_month; i++)
			{
				if (e.getSource() == days_buttons[i])
				{
					//TODO: Come up with system for tracking dates and maintaining file, etc.
					String date =  Integer.toString(monthCombo.getSelectedIndex() + 1) + "/" + days_buttons[i].getText() + "/" + yearCombo.getSelectedItem().toString();
					int ID = Constants.getDifference(date);
					System.out.println(ID);
					new DailyGUI(date, ID); //Note: this thread FREEZES. Can just repaint after.
				}
			}
		}
		
		createDatesPanel();
	}
}
