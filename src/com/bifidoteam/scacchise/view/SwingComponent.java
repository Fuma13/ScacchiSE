package com.bifidoteam.scacchise.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.sun.javafx.scene.layout.region.Margins.Converter;

public class SwingComponent implements Runnable, ActionListener {

	ArrayDeque<Integer> eventsQueue;
	
	public SwingComponent(){
		eventsQueue= new ArrayDeque<Integer>();
	}
	
	@Override
	public void run() {
		SwingInit();
	}


	private void SwingInit(){
		JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Badass Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        composeGrid(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
	}


	private void composeGrid(Container pane) {
		JButton jbnButton;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gBC = new GridBagConstraints();
        gBC.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon clap = new ImageIcon("res/claptrap.png");
        jbnButton = new JButton(clap);
        jbnButton.setName("0");
        jbnButton.setBackground(Color.white);
        jbnButton.setBorder(null);
        jbnButton.addActionListener(this);
        gBC.weightx = 0.5;
        gBC.gridx = 0;
        gBC.gridy = 0;
        pane.add(jbnButton, gBC);

        JTextField jtf = new JTextField("TextField 1");
        gBC.gridx = 2;
        gBC.gridy = 0;
        jtf.setEditable(false);
        pane.add(jtf, gBC);

        jbnButton = new JButton("Button 3");
        gBC.gridx = 2;
        gBC.gridy = 0;
        pane.add(jbnButton, gBC);

        jbnButton = new JButton("Button 4");
        gBC.ipady = 40;     //This component has more breadth compared to other buttons
        gBC.weightx = 0.0;
        gBC.gridwidth = 3;
        gBC.gridx = 0;
        gBC.gridy = 1;
        pane.add(jbnButton, gBC);

        JComboBox jcmbSample = new JComboBox(new String[]{"ComboBox 1", "hi", "hello"});
        gBC.ipady = 0;
        gBC.weighty = 1.0;
        gBC.anchor = GridBagConstraints.PAGE_END;
        gBC.insets = new Insets(10,0,0,0);  //Padding
        gBC.gridx = 1;
        gBC.gridwidth = 2;
        gBC.gridy = 2;
        pane.add(jcmbSample, gBC);
	}

	
	
	// *************************************************************************************************************************
	// **************************************************** EVENTS MANAGEMENT **************************************************
	// *************************************************************************************************************************
	
	@Override
	// Event listener: Thread safe.
	public synchronized void actionPerformed(ActionEvent arg0) {
		if(((JButton) arg0.getSource()) != null){
			JButton x = (JButton) arg0.getSource();
			eventsQueue.push(Integer.valueOf(x.getName()));

			System.out.println("Thread id:" + Thread.currentThread().getId());
			System.out.println("Added event in the queue: " + eventsQueue.peekLast() + ". Size of new events: " + eventsQueue.size());
		}
	}
	
	// Event peek: Thread safe.
	public synchronized boolean HasNewEvent(){
		return !eventsQueue.isEmpty();
	}
	
	// Event eater: Thread safe.
	public synchronized int GetNextEvent(){
		System.out.println("Thread id:" + Thread.currentThread().getId());
		System.out.println("Consumed event : " + eventsQueue.peekFirst() + ". Number of new events: " + eventsQueue.size());
		return eventsQueue.removeFirst();
	}

	// *************************************************************************************************************************
	// *********************************************************** END *********************************************************
	// *************************************************************************************************************************
	
}
