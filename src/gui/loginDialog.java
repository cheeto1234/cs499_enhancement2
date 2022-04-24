/**
 * Packages
 */
package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import app.*;

/**
 * Login dialog logic
 */

@SuppressWarnings("serial")
public class loginDialog extends JFrame {
	private JPanel contentPane;
	private JTextField userField;
	private JPasswordField passwordField;

	/**
	 * Main application launcher
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginDialog frame = new loginDialog();
					frame.setUndecorated(true);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public loginDialog() {
		/**
		 * Content pane properties
		 */
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 318, 319);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(192, 192, 192)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/**
		 * Title label
		 */
		JLabel titleLabel = new JLabel("Login to Continue");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Gadugi", Font.BOLD, 24));
		titleLabel.setForeground(Color.GRAY);
		titleLabel.setBounds(17, 31, 282, 37);
		contentPane.add(titleLabel);
		
		/**
		 * Username field
		 */
		JPanel usernamePanel = new JPanel();
		usernamePanel.setBounds(22, 84, 272, 37);
		contentPane.add(usernamePanel);
		usernamePanel.setLayout(null);
		
		final boolean[] userFieldClicked = { false };
		userField = new JTextField();
		userField.addFocusListener(new FocusAdapter() {
			@Override
			// If field get focused, delete the preset text ("Username")
			public void focusGained(FocusEvent e) {
				if(!userFieldClicked[0]) {
					userFieldClicked[0] = true;
					userField.setText("");
				}
			}
			@Override
			// If nothing is entered into the field, reset the preset text
			public void focusLost(FocusEvent e) {
				if(userFieldClicked[0] && userField.getText().isBlank()) {
					userFieldClicked[0] = false;
					userField.setText("Username");
				}
			}
		});
		userField.setText("Username");
		userField.setBounds(10, 0, 252, 37);
		usernamePanel.add(userField);
		userField.setBorder(null);
		userField.setToolTipText("Username");
		userField.setForeground(Color.GRAY);
		userField.setBackground(UIManager.getColor("Button.background"));
		userField.setFont(new Font("Gadugi", Font.PLAIN, 14));
		userField.setColumns(10);
		
		/**
		 * Close program buttons
		 */
		JLabel closeProgramButton = new JLabel("X");
		closeProgramButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		
		/**
		 * Password field
		 */
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(null);
		passwordPanel.setBounds(22, 141, 272, 37);
		contentPane.add(passwordPanel);
		
		JLabel passLabel = new JLabel("Password");
		passLabel.setFont(new Font("Gadugi", Font.PLAIN, 14));
		passLabel.setForeground(Color.GRAY);
		passLabel.setBackground(Color.WHITE);
		passLabel.setBounds(10, 11, 104, 14);
		passwordPanel.add(passLabel);
		
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			// Get rid of the preset text when field is focused ("Password")
			public void focusGained(FocusEvent e) {
				passLabel.setVisible(false);
			}
			@Override
			// If field is empty, bring back the preset text
			public void focusLost(FocusEvent e) {
				if(!passLabel.isVisible() && passwordField.getPassword().length == 0) {
					passwordField.setText("");
					passLabel.setVisible(true);
				}
			}
		});
		passwordField.setToolTipText("Password");
		passwordField.setForeground(Color.GRAY);
		passwordField.setFont(new Font("Georgia", Font.PLAIN, 14));
		passwordField.setColumns(10);
		passwordField.setBorder(null);
		passwordField.setBackground(SystemColor.menu);
		passwordField.setBounds(10, 0, 252, 37);
		passwordPanel.add(passwordField);
		closeProgramButton.setFont(new Font("Gadugi", Font.BOLD, 22));
		closeProgramButton.setForeground(Color.RED);
		closeProgramButton.setHorizontalAlignment(SwingConstants.CENTER);
		closeProgramButton.setBounds(287, 7, 24, 22);
		contentPane.add(closeProgramButton);
		
		/**
		 * New file notification label
		 */
		JLabel newFileLabel = new JLabel();
		newFileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newFileLabel.setFont(new Font("Gadugi", Font.PLAIN, 11));
		newFileLabel.setText("<html><body style='text-align:center'>"+ "If you have not logged in before, a file will be created for you." +"</body></html>");
		newFileLabel.setBounds(22, 254, 270, 30);
		contentPane.add(newFileLabel);
		
		/**
		 * Error notification label
		 */
		JLabel errorLabel = new JLabel();
		errorLabel.setForeground(Color.RED);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setFont(new Font("Gadugi", Font.PLAIN, 11));
		errorLabel.setBounds(23, 252, 270, 18);
		errorLabel.setVisible(false);
		contentPane.add(errorLabel);
		
		/**
		 * Login button
		 */
		JButton loginButton = new JButton("Login");
		loginButton.addMouseListener(new MouseAdapter() {
			/**
			 * Login button click event
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!loginButton.isEnabled()) {
					return;
				}
				String formattedUsername = validateCredentials.formatUsername(userField.getText());
				if(userField.getText().equals("Username") && passwordField.getPassword().length == 0) {
					newFileLabel.setVisible(false);
					errorLabel.setText("You have not entered any credentials!");
					errorLabel.setVisible(true);
				} else if (userField.getText().equals("Username") && passwordField.getPassword().length != 0) {
					newFileLabel.setVisible(false);
					errorLabel.setText("You must enter a username!");
					errorLabel.setVisible(true);
				} else if (!userField.getText().equals("Username") && passwordField.getPassword().length == 0) {
					newFileLabel.setVisible(false);
					errorLabel.setText("You must enter a password!");
					errorLabel.setVisible(true);
				} else if (validateCredentials.formatUsername(userField.getText()).isBlank()) {
					newFileLabel.setVisible(false);
					errorLabel.setText("You must have an alphanumeric username!");
					errorLabel.setVisible(true);
				} else if (!validateCredentials.validatePassword(passwordField.getPassword())){
					newFileLabel.setVisible(false);
					errorLabel.setText("Password not secure! Secure example: Very@Secure1");
					errorLabel.setVisible(true);
				} else {
					userField.setText(formattedUsername);
					newFileLabel.setVisible(true);
					errorLabel.setVisible(false);
					boolean folderExists = fileManager.createDirectory();
					if(!folderExists) {
						newFileLabel.setText("Data folder does not exist, creating...");
					} else {
						newFileLabel.setText("Data folder found!");
					}
					try {
						fileManager.createFile(formattedUsername, passwordField.getPassword());
						if(validateCredentials.checkLoginSuccessful(formattedUsername, passwordField.getPassword())) {
							try {
								loginButton.setEnabled(false);
								loginButton.setFocusable(false);
								mainGUI frame = new mainGUI(formattedUsername, passwordField.getPassword());
								frame.setUndecorated(true);
								frame.setVisible(true);
								frame.setLocationRelativeTo(null);
								dispose();
							} catch (Exception e1) {
								e1.printStackTrace();
							}						
						} else {
							newFileLabel.setVisible(false);
							errorLabel.setText("Wrong password!");
							errorLabel.setVisible(true);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		loginButton.setBackground(new Color(0, 191, 255));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Gadugi", Font.BOLD, 14));
		loginButton.setBounds(22, 198, 271, 39);
		contentPane.add(loginButton);
	}
}