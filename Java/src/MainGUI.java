
/*
    @author
          ______         _                  _
         |  ____|       (_)           /\   | |
         | |__ __ _ _ __ _ ___       /  \  | | __ _ ___ _ __ ___   __ _ _ __ _   _
         |  __/ _` | '__| / __|     / /\ \ | |/ _` / __| '_ ` _ \ / _` | '__| | | |
         | | | (_| | |  | \__ \    / ____ \| | (_| \__ \ | | | | | (_| | |  | |_| |
         |_|  \__,_|_|  |_|___/   /_/    \_\_|\__,_|___/_| |_| |_|\__,_|_|   \__, |
                                                                              __/ |
                                                                             |___/
            Email: farisalasmary@gmail.com
            Date:  Apr 7, 2016
*/


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.*;

public class MainGUI extends JFrame implements ActionListener, WindowListener{

	private static final long serialVersionUID = -5778856184731491899L;

	public static final double VERSION = 1.0;
	
	private JLabel CRNsLbls[];
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem miOpen, miSaveAs, miExit, miAbout;
	private JTextField CRNsTxts[];
	private JPanel CRNsPanel, controlPanel;
	private JButton startBtn, stopBtn;
	private Registrar reg;
	
	public MainGUI(){
		super("Registrar " + VERSION);
		this.setLayout(new FlowLayout());
		this.setSize(360, 250);
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		
		this.menuBar = new JMenuBar();
		
		this.menuFile = new JMenu("File");
		
		this.miAbout = new JMenuItem("About...");
		this.miOpen = new JMenuItem("Open...");
		this.miSaveAs = new JMenuItem("Save As...");
		this.miExit = new JMenuItem("Exit");
		
		this.miAbout.addActionListener(this);
		this.miOpen.addActionListener(this);
		this.miSaveAs.addActionListener(this);
		this.miExit.addActionListener(this);
		
		this.menuFile.add(this.miOpen);
		this.menuFile.add(this.miSaveAs);
		this.menuFile.addSeparator();
		this.menuFile.add(this.miExit);
		this.menuBar.add(this.menuFile);
		this.menuBar.add(this.miAbout);
		
		this.CRNsPanel = new JPanel(new GridLayout(4, 4));
//		this.CRNsPanel.setPreferredSize(new Dimension(150, 200));
		this.CRNsPanel.setBorder(BorderFactory.createTitledBorder("CRNs"));
		
		this.controlPanel = new JPanel(new FlowLayout());
		this.controlPanel.setBorder(BorderFactory.createTitledBorder("Control"));
		this.controlPanel.setPreferredSize(new Dimension(180, 60));
		
		int size = 8;
		this.CRNsLbls = new JLabel[size];
		this.CRNsTxts = new JTextField[size];
		
		for(int i = 0; i < size; i++){
			this.CRNsLbls[i] = new JLabel("CRN #" + (i + 1));
			this.CRNsTxts[i] = new JTextField(5);
			this.CRNsPanel.add(this.CRNsLbls[i]);
			this.CRNsPanel.add(this.CRNsTxts[i]);
		}
		
		this.startBtn = new JButton("Start");
		this.stopBtn = new JButton("Stop");
		
		this.startBtn.addActionListener(this);
		this.stopBtn.addActionListener(this);
		
		this.stopBtn.setEnabled(false);
		
		this.controlPanel.add(this.startBtn);
		this.controlPanel.add(this.stopBtn);
		
		this.add(this.CRNsPanel);
		this.add(this.controlPanel);
		
		this.setJMenuBar(this.menuBar);
		this.setVisible(true);
	}
	
	private void start(){
		int size = 0;
		
		// to find the number of entered CRNs
		for(int i = 0; i < this.CRNsTxts.length; i++)
			if(!this.CRNsTxts[i].equals(""))
				size++;
		
		String CRNs[] = new String[size];
		for(int i = 0; i < CRNs.length; i++)
			CRNs[i] = this.CRNsTxts[i].getText().trim();
		this.reg = new Registrar(CRNs);
		if(this.reg.startMonitoring())
			JOptionPane.showMessageDialog(null, "The program started correctly!", "Info", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Something went worng!!", "Error", JOptionPane.ERROR_MESSAGE);
		
		this.startBtn.setEnabled(false);
		this.stopBtn.setEnabled(true);
		for(int i = 0; i < this.CRNsTxts.length; i++)
			this.CRNsTxts[i].setEnabled(false);
	}
	
	private void stop(){
		this.reg.stopMonitoring();
		this.startBtn.setEnabled(true);
		this.stopBtn.setEnabled(false);
		for(int i = 0; i < this.CRNsTxts.length; i++)
			this.CRNsTxts[i].setEnabled(true);
	}
	
	private void loadFromFile(){
		JFileChooser jchooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		
		jchooser.setFileFilter(filter);
		
		int result = jchooser.showOpenDialog(null);
		
		if(result != JFileChooser.APPROVE_OPTION)
			return; // exit the method
		
		try {
			FileInputStream input = new FileInputStream(jchooser.getSelectedFile());
			Scanner scan = new Scanner(input);
			
			for(int i = 0; scan.hasNextInt() && i < this.CRNsTxts.length; i++){
				this.CRNsTxts[i].setText("" + scan.nextInt());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveAs(){
		JFileChooser jchooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		
		jchooser.setFileFilter(filter);
		
		int result = jchooser.showSaveDialog(null);
		
		if(result != JFileChooser.APPROVE_OPTION)
			return; // exit the method
		
		String path = jchooser.getSelectedFile().toString();
		if(!path.endsWith(".txt"))
			path += ".txt";
		
		try {
			FileOutputStream output = new FileOutputStream(path);
			PrintWriter pw = new PrintWriter(output);
			
			for(int i = 0; i < this.CRNsTxts.length; i++){
				pw.println(this.CRNsTxts[i].getText());
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.startBtn){
			start();
		}else if(e.getSource() == this.stopBtn){
			int result = JOptionPane.showConfirmDialog(null, "Are you sure??", "Info",  JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(result == JOptionPane.YES_OPTION)
				stop();
		}else if(e.getSource() == this.miOpen){
			loadFromFile();
		}else if(e.getSource() == this.miAbout){
			String msg = "Registrar " + VERSION + "\n\n"
					   + "Developed by:\n"
					   + "Faris Alasmary\n\n"
					   + "Email:\n"
					   + "farisalasmary@gmail.com\n";
			JOptionPane.showMessageDialog(null, msg, "About...", JOptionPane.INFORMATION_MESSAGE);
		}else if(e.getSource() == this.miSaveAs){
			saveAs();
		}else if(e.getSource() == this.miExit){
			windowClosing(null); // no need to redundant the code again here, just call the method
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		int result = JOptionPane.showConfirmDialog(null, "Are you sure??", "Info", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
	
	
	public static void main(String[] args) {
		new MainGUI();
	}
}
