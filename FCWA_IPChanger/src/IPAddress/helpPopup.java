package IPAddress;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class helpPopup extends JFrame implements Popup, ActionListener{
	
	private JTree jt;
	private JTextArea dText;
	private HashMap<String, String> descriptions;
	private boolean fileMissing = false;
	
	
	/**
	 * Constructor for helpPopup
	 * @throws Exception 
	 */
	public helpPopup() {
		readFile();
		createAndShowPopup();
	}

	/**
	 * Creates all the components and displays the GUI
	 */
	@Override
	public void createAndShowPopup() {
		setTitle("Help");
		setResizable(false);
		getContentPane().setLayout(new GridLayout(0, 3, 0, 0));
		getContentPane().setBackground(new Color(234, 245, 255));
		setPreferredSize(new Dimension(585,370));
		
		addComponents();
		
		pack();
		setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int)screenSize.getWidth()/2-getWidth()/2,(int)screenSize.getHeight()/2-getHeight()/2);
		toFront();		
	}

	/**
	 * Add all the components to the JFrame
	 */
	@Override
	public void addComponents() {
		// Creates the tree and description panel
		createTree();
		dText = new JTextArea();
		dText.setEditable(false);
		dText.setLineWrap(true);
		dText.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		dText.setBackground(Color.WHITE);
		JPanel dPanel = new JPanel();
		dPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
		dPanel.setLayout(new BorderLayout());
		dPanel.add(dText);
		
		// Formats the components
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(jt, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(dPanel, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(dPanel, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(jt, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
		);
		getContentPane().setLayout(groupLayout);
		
		if (!fileMissing) getDescription("[Contents]");		// Default description when opened
		else dText.setText("Help file not found.");
	}
	
	/**
	 * Reads the txt file for explanations and fills hashmap
	 * 
	 * @throws Exception
	 */
	public void readFile() {
		descriptions = new HashMap<String, String>();
		
		File file = new File("help.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			String key = "";
			while ((line = reader.readLine()) != null) {
			    if (line.contains(":")) key = line.substring(0, line.length()-1);
			    else if (!line.isBlank()){
			    	if (descriptions.containsKey(key)) 
			    		descriptions.replace(key, descriptions.get(key)+"\n"+line);
			    	else descriptions.put(key, line);
			    }
			} 
		} catch (FileNotFoundException e1) {
			System.out.println("File missing");
			fileMissing = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the tree containing the help contents
	 */
	public void createTree() {
		// Tree Nodes
		DefaultMutableTreeNode contents = new DefaultMutableTreeNode("Contents");  
		DefaultMutableTreeNode instructions = new DefaultMutableTreeNode("Instructions");  
		DefaultMutableTreeNode sites = new DefaultMutableTreeNode("Sites");
			DefaultMutableTreeNode add = new DefaultMutableTreeNode("Add");
			DefaultMutableTreeNode remove = new DefaultMutableTreeNode("Remove");
			DefaultMutableTreeNode edit = new DefaultMutableTreeNode("Edit");
		DefaultMutableTreeNode ipAddress = new DefaultMutableTreeNode("IP Address");  
		DefaultMutableTreeNode plcAddress = new DefaultMutableTreeNode("PLC Address");  
		DefaultMutableTreeNode ping = new DefaultMutableTreeNode("Ping");
			DefaultMutableTreeNode plc = new DefaultMutableTreeNode("PLC/Telnet");
			DefaultMutableTreeNode router = new DefaultMutableTreeNode("Router");
		DefaultMutableTreeNode csvFile = new DefaultMutableTreeNode("CSV File"); 
			DefaultMutableTreeNode impExp = new DefaultMutableTreeNode("Import/Export");

		// add nodes
		contents.add(instructions);  
		contents.add(sites);  
			sites.add(add);
			sites.add(remove);
			sites.add(edit);
		contents.add(ipAddress);
		contents.add(plcAddress);
		contents.add(ping);
			ping.add(plc);
			ping.add(router);
		contents.add(csvFile); 
			csvFile.add(impExp);
			
		// format tree
		jt = new JTree(contents); 
		jt.setBackground(new Color(234, 245, 255));
		jt.setOpaque(true);
		jt.setRowHeight(25);
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setOpaque(false);
		jt.setCellRenderer(renderer);
        jt.setShowsRootHandles(true);
        jt.setRootVisible(true);
        
        jt.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                getDescription(e.getPath().toString());
            }
        });
	}

	/**
	 * Gets the explanation of the selected content
	 */
	public void getDescription(String node) {
		if (fileMissing) return;
		else if (node.contains("[Contents]")) {
			dText.setText(descriptions.get("Contents"));
		} else if (node.contains("[Contents, Instructions]")) {
			dText.setText(descriptions.get("Instructions"));
		} else if (node.contains("[Contents, Sites]")) {
			dText.setText(descriptions.get("Sites"));
		} else if (node.contains("[Contents, Sites, Add]")) {
			dText.setText(descriptions.get("Add"));
		} else if (node.contains("[Contents, Sites, Remove]")) {
			dText.setText(descriptions.get("Remove"));
		} else if (node.contains("[Contents, Sites, Edit]")) {
			dText.setText(descriptions.get("Edit"));
		} else if (node.contains("[Contents, IP Address]")) {
			dText.setText(descriptions.get("IP Address"));
		} else if (node.contains("[Contents, PLC Address]")) {
			dText.setText(descriptions.get("PLC Address"));
		} else if (node.contains("[Contents, Ping]")) {
			dText.setText(descriptions.get("Ping"));
		} else if (node.contains("[Contents, Ping, PLC/Telnet]")) {
			dText.setText(descriptions.get("PLC/Telnet"));
		} else if (node.contains("[Contents, Ping, Router]")) {
			dText.setText(descriptions.get("Router"));
		} else if (node.contains("[Contents, CSV File]")) {
			dText.setText(descriptions.get("CSV File"));;
		} else if (node.contains("[Contents, CSV File, Import/Export]")) {
			dText.setText(descriptions.get("Import/Export"));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
	// TESTING -------------------------------------------------------------------------------------------------------------------------
	public static void main (String[] args) throws Exception {
		new helpPopup();
	}
}
