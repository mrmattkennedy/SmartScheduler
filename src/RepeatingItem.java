import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class RepeatingItem extends JDialog implements FocusListener, ActionListener {
	
	private Dimension screenSize;
	private JPanel main_panel;
	
	//Top panel
	private JPanel top_panel;
	private JComboBox<Integer> repeat_times;
	private JComboBox<String> repeat_cycles;
	
	//Item panel
	private JPanel item_panel;
	private JScrollPane item_panel_pane;
	
	private static final int min_repeats = 1;
	private static final int max_repeats = 20;
	private static final String[] cycles = {"Daily", "Weekly", "Monthly", "Yearly"};
	private static final Integer[] repeats = IntStream
			.range(min_repeats, max_repeats + min_repeats)
			.boxed()
			.toArray(Integer[]::new);
	
	
	/*
	 * Repeat x times daily/weekly/monthly/yearly basis
	 * 	if daily, no other options other than the item (continue until completed?)
	 * 	if weekly, select the day(s) of the week
	 * 	if monthly, select the day(s) of the month (last saturday, etc, specific day)
	 *  if yearly, select the day(s) of the year (last saturday, specific day)
	 *  
	 *  For multiple items, need to have multiple rows to fill in.
	 *  Need scrolling jpanel.
	 *  
	 * 	Item: all are rows containing drop down for date?
	*/
	public RepeatingItem()
	{		
		//Get the screen size
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.width /= 3;
		screenSize.height /= 3;
		
		createTopPanel();
		createItemPanel();
		
		main_panel = new JPanel(new BorderLayout());
		main_panel.add(top_panel, BorderLayout.NORTH);
		
		add(main_panel);
		
		setSize(screenSize);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true); //Makes it so can only click on this dialog and not main GUI
		setVisible(true);
	}
	
	public void createTopPanel()
	{
		top_panel = new JPanel();
		repeat_times = new JComboBox<Integer>(repeats);
		repeat_cycles = new JComboBox<String>(cycles);
		
		repeat_times.addActionListener(this);
		repeat_cycles.addActionListener(this);
		
		top_panel.add(new JLabel("Repeat "));
		top_panel.add(repeat_times);
		top_panel.add(new JLabel(" times "));
		top_panel.add(repeat_cycles);
	}
	
	public void createItemPanel()
	{
		item_panel = new JPanel();
		item_panel_pane = new JScrollPane(item_panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}
