import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

//TODO: get the google doodles for each day, only display for the current date or when DailyGUI opened for a day.
//Items are added in a list, with each item having an X at the end of it. Maybe a checkmark too, for done?
public class DailyGUI extends JDialog implements FocusListener, ActionListener {
	
	//Set in the ID, and convert to bytes for writing to the file.
	private int ID;
	private String date;
	
	private byte[] IDToBytes;
	private String file_name;
	private Path path;
	private List<String> file_contents;
	private int file_position = -1;
	private String to_do; 
	
	//GUI
	private JPanel main_panel;
	private Dimension screenSize;
	
	//Top panel
	private JPanel top_panel;
	private JLabel current_date_label;
	
	//Items panel
	private JPanel items_panel;
	private JScrollPane items_panel_pane;
	private List<JTextArea> item_texts;
	private List<String> items;
	
	//Bottom panel
	private JPanel bottom_panel;
	private JTextArea item_subject;
	private JScrollPane item_subject_pane;
	private JTextArea item_to_add;
	private JScrollPane item_to_add_pane;
	private JButton add_btn;
	private JButton done_btn;
	private JButton cancel_btn;
	
	//Constants
	private final String description_placeholder = "Enter description here (optional)";
	private final String subject_placeholder = "Enter subject here";
	private final String no_items_placeholder = "--None listed currently--";
	private final String daily_delimiter = "-=-";
	private final String item_delimiter = ",,,";
	private final int offset_constant = 100;
	
	//Read what is in there. If nothing, then just display nothing.
	//Give users option to add stuff, set a priority. Automatically add cleaning? Others?
	//Have subject input (what's displayed), additional info.
	public DailyGUI(String date, int ID)
	{
		this.ID = ID;
		this.date = date;
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.width /= 2;
		screenSize.height /= 2;
		
		createTopPanel();
		createItemsPanel();
		createBottomPanel();

		main_panel = new JPanel();
		
		main_panel.setLayout(new BorderLayout());
		main_panel.add(top_panel, BorderLayout.NORTH);
		main_panel.add(items_panel_pane, BorderLayout.CENTER);
		main_panel.add(bottom_panel, BorderLayout.SOUTH);
		
		add(main_panel);
		
		setSize(screenSize);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true); //Makes it so can only click on this dialog and not main GUI
		setVisible(true);
	}
	private void createTopPanel()
	{
		top_panel = new JPanel();
		current_date_label = new JLabel("Current date: " + date);
		current_date_label.setFont(new Font("Helvetica", Font.BOLD, 16));
		top_panel.add(current_date_label);
		top_panel.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	//TODO: Create a button to loop through, 10 at a time.
	private void createItemsPanel()
	{
		items_panel = new JPanel(); //0 to add items vertically expanding (new GridLayout(0, 1, 5, 5))
		items_panel_pane = new JScrollPane(items_panel);
		item_texts = new ArrayList<JTextArea>();
		items = new ArrayList<String>();
		
		items_panel.setPreferredSize(new Dimension(screenSize.width - offset_constant, screenSize.height));
		
		items_panel_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		readFileContents();
		
		for (String item : items)
		{
			JTextArea temp = new JTextArea(item);
			temp.setPreferredSize(new Dimension(screenSize.width - offset_constant, offset_constant/2));
			item_texts.add(temp);
			items_panel.add(item_texts.get(item_texts.size() - 1));
		}
		
		//items_text = new JTextPane();
		//items_text.setContentType("text/html");
		//items_text.setText(to_do);
		//items_text.setFont(new Font("Helvetica", Font.BOLD, 16));
		
		//items_pane = new JScrollPane(items_text);
		//items_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//items_panel.add(items_pane);
	}
	
	private void createBottomPanel()
	{
		bottom_panel = new JPanel(new GridLayout(1, 3, 5, 5));
		bottom_panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		item_subject = new JTextArea(subject_placeholder);
		item_subject.setFont(new Font("Helvetica", Font.BOLD, 16));
		item_subject.setForeground(Color.LIGHT_GRAY);
		item_subject.setLineWrap(true);
		item_subject.addFocusListener(this);
		
		item_to_add = new JTextArea(description_placeholder);
		item_to_add.setFont(new Font("Helvetica", Font.BOLD, 16));
		item_to_add.setForeground(Color.LIGHT_GRAY);
		item_to_add.setLineWrap(true);
		item_to_add.addFocusListener(this);
		
		item_subject_pane = new JScrollPane(item_subject);
		item_subject_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		item_subject_pane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		item_to_add_pane = new JScrollPane(item_to_add);
		item_to_add_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		item_to_add_pane.setBorder(BorderFactory.createLineBorder(Color.black));
	
		add_btn = new JButton("Add");
		done_btn = new JButton("Done");
		cancel_btn = new JButton("Cancel");
		
		add_btn.addActionListener(this);
		done_btn.addActionListener(this);
		cancel_btn.addActionListener(this);

		item_subject_pane.setPreferredSize(new Dimension(screenSize.width / 3, screenSize.height / 15));
		bottom_panel.add(item_subject_pane);
		item_to_add_pane.setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height / 15));
		bottom_panel.add(item_to_add_pane);
		
		JPanel bottom_button_panel = new JPanel(new GridLayout(1, 3, 5, 5));
		bottom_button_panel.setBorder(BorderFactory.createLineBorder(Color.black));
		bottom_button_panel.add(add_btn);
		bottom_button_panel.add(done_btn);
		bottom_button_panel.add(cancel_btn);
		bottom_panel.add(bottom_button_panel);
	}
	
	private void readFileContents()
	{
		List<Integer> temp_ints = new ArrayList<Integer>();
		file_name = "daily_file.dat";
		path = Paths.get(file_name);
		
		//Load all the contents, search for a given key
		//Need a better system eventually, going to be more than just ints on each line
		//Solution 1: Read everything in, toss every int into a list, search for it, get pos
		try {
			file_contents = Files.readAllLines(path);
			
			temp_ints = file_contents
					.stream()
					.map(x -> Integer.parseInt(x.substring(0, x.indexOf(daily_delimiter))))
					.collect(Collectors.toList());
			
		} catch (IOException e) {
			return;
		}
		
		//See if the line exists. If not, default.
		Collections.sort(temp_ints);
		if (Collections.binarySearch(temp_ints, ID) == -1)
		{
			items.add(no_items_placeholder);
		}
		else
		{
			//Get the line position
			Iterator<String> i = file_contents.iterator();
			int count = 0;
			while (i.hasNext())
			{
				if (i.next().contains(Integer.toString(ID)))
				{
					file_position = count;
					break;
				}
				count++;
			}
			
			//If the line position exists, then get the contents
			if (file_contents.get(file_position).substring(0, Integer.toString(ID).length()).equals(Integer.toString(ID)))
			{
				//Get the line temporarily, then get everything after the ID in the line
				String line = file_contents.get(file_position);
				items = Arrays.asList(line.substring(line.indexOf(daily_delimiter) + daily_delimiter.length())
						.split(item_delimiter));
			}
		}
	}
	
	/*
	private void addItem(String item)
	{
		String current_text = items_text.getText();
		if (current_text.equals(no_items_placeholder))
		{
			items_text.setText("Daily items:\n" + item);
		}
		else
		{
			items_text.setText(current_text + "\n" + item);
		}
	}
	*/

	//Hint text should be there when text is empty and when focus is not there.
	//Focus gained = remove
	//Focus lost = add
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == item_subject && item_subject.getText().equals(subject_placeholder))
		{
			item_subject.setText("");
			item_subject.setForeground(Color.BLACK);
		}
		else if (e.getSource() == item_to_add && item_to_add.getText().equals(description_placeholder))
		{
			item_to_add.setText("");
			item_to_add.setForeground(Color.BLACK);
		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == item_subject && item_subject.getText().isEmpty())
		{
			item_subject.setText(subject_placeholder);
			item_subject.setForeground(Color.LIGHT_GRAY);
		}
		else if (e.getSource() == item_to_add && item_to_add.getText().isEmpty())
		{
			item_to_add.setText(description_placeholder);
			item_to_add.setForeground(Color.LIGHT_GRAY);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add_btn)
		{
			if (item_subject.getText().equals(subject_placeholder))
			{
				return;
			}
			else
			{
				String text_to_add = item_subject.getText();
				if (!item_to_add.getText().equals(description_placeholder))
				{
					text_to_add += ": " + item_to_add.getText();
				}
				//addItem(text_to_add);
			}
		}
		
	}
}
