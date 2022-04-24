package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import app.dataManager;
import app.fileManager;

import javax.swing.ListSelectionModel;

public class mainGUI extends JFrame {

	private JPanel contentPane;
	/**
	 * Create the frame.
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public mainGUI(String UID, char[] password) throws Exception {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 824, 511);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginDialog frame = new loginDialog();
				frame.setUndecorated(true);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				dispose();
			}
		});
		btnLogOut.setForeground(Color.WHITE);
		btnLogOut.setFont(new Font("Gadugi", Font.BOLD, 14));
		btnLogOut.setBackground(Color.DARK_GRAY);
		btnLogOut.setBounds(10, 462, 130, 38);
		contentPane.add(btnLogOut);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(292, 60, 520, 440);
		contentPane.add(scrollPane);
		
		JEditorPane editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);
		editorPane.setFont(new Font("Gadugi", Font.PLAIN, 12));
		
		JLabel editLabel = new JLabel("Currently Editting: "+UID+".json");
		editLabel.setFont(new Font("Gadugi", Font.BOLD, 20));
		editLabel.setBounds(300, 11, 498, 39);
		contentPane.add(editLabel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(10, 60, 272, 391);
		contentPane.add(scrollPane_1);
		
	    DefaultListModel<String> model = new DefaultListModel<>();
		JList list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					JSONObject docs = dataManager.getDocs(UID);
					editLabel.setText("Currently Editting: "+UID+".json / "+(list.getSelectedIndex()+1));
					editorPane.setText(dataManager.decryptDoc(docs, list.getSelectedIndex(), password));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		populateList(model, dataManager.getDocs(UID).size());
		scrollPane_1.setViewportView(list);
		
		JButton createButton = new JButton("Create");
		createButton.setFocusable(false);
		createButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(createButton.isEnabled()) {
					try {
						createButton.setEnabled(false);
						JSONObject docs = dataManager.getDocs(UID);
						dataManager.encryptDoc(docs, dataManager.getAvailableID(UID), password, "");
						dataManager.reloadDocs(docs, UID);
						populateList(model, docs.size());
						createButton.setEnabled(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		createButton.setFont(new Font("Gadugi", Font.BOLD, 14));
		createButton.setBounds(10, 11, 84, 38);
		createButton.setBackground(new Color(0, 191, 255));
		createButton.setForeground(Color.WHITE);
		contentPane.add(createButton);
		
		JButton deleteButton = new JButton("Delete");
		deleteButton.setFocusable(false);
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(deleteButton.isEnabled() && !list.isSelectionEmpty()) {
					try {
						deleteButton.setEnabled(false);
						JSONObject docs = dataManager.getDocs(UID);
						dataManager.deleteDoc(docs, list.getSelectedIndex());
						dataManager.reloadDocs(docs, UID);
						populateList(model, docs.size());
	    				deleteButton.setEnabled(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setFont(new Font("Gadugi", Font.BOLD, 14));
		deleteButton.setBackground(new Color(250, 128, 114));
		deleteButton.setBounds(198, 11, 84, 38);
		contentPane.add(deleteButton);
		
		JButton saveButton = new JButton("Save");
		saveButton.setFocusable(false);
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(saveButton.isEnabled() && !list.isSelectionEmpty()) {
					try {
						saveButton.setEnabled(false);
						JSONObject docs = dataManager.getDocs(UID);
						dataManager.encryptDoc(docs, list.getSelectedIndex(), password, editorPane.getText());
						dataManager.reloadDocs(docs, UID);
						saveButton.setEnabled(true);
					} catch(Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		saveButton.setForeground(Color.WHITE);
		saveButton.setFont(new Font("Gadugi", Font.BOLD, 14));
		saveButton.setBackground(new Color(127, 255, 212));
		saveButton.setBounds(104, 11, 84, 38);
		contentPane.add(saveButton);
		
		JButton btnDeleteUser = new JButton("Delete File");
		btnDeleteUser.setFocusable(false);
		btnDeleteUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnDeleteUser.isEnabled()) {
					try {
						btnDeleteUser.setEnabled(false);
						fileManager.deleteFile(UID);
						loginDialog frame = new loginDialog();
						frame.setUndecorated(true);
						frame.setVisible(true);
						frame.setLocationRelativeTo(null);
						dispose();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnDeleteUser.setForeground(Color.WHITE);
		btnDeleteUser.setFont(new Font("Gadugi", Font.BOLD, 14));
		btnDeleteUser.setBackground(Color.RED);
		btnDeleteUser.setBounds(150, 462, 132, 38);
		contentPane.add(btnDeleteUser);
	}
	public void populateList(DefaultListModel<String> model, int docNumber) {
		model.removeAllElements();
		for(int i=0; i<docNumber; i++) {
			model.addElement("Document "+String.valueOf(i+1));
		}
	}
}
