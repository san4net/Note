package com.copypaste;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class HistoricalCombo<E> extends JComboBox<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -132164630628000093L;
	private LinkedList<E> browsingHistory = new LinkedList<E>();

	public HistoricalCombo() {
		super();
		addActionListener(new HistoricalActionListener());
		setEditable(true);
		browsingHistory.addFirst((E) "first");
		browsingHistory.addFirst((E) "second");
		setModel(prepareModel());
	}

	@Override
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	private class HistoricalActionListener implements
			java.awt.event.ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = getSelectedItem();
			if (o != null) {
				browsingHistory.addFirst((E) o);
				setModel((ComboBoxModel<E>) prepareModel());
			}
		}

	}

	private ComboBoxModel<E> prepareModel() {
		Object[] elements = browsingHistory.toArray();
		return (ComboBoxModel<E>) new DefaultComboBoxModel<>(elements);

	}
}
