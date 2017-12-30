import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Creates an application for storing and displaying one's Song Library Uses
 * GUI, JFrame - tables,models, vertical boxes,
 * 
 * @author jordanyarros
 * @version 1.25
 */
public class SongLibrary extends JFrame {
    /**
     * Constructor that initilizes all of SongLibrary application
     */
    public SongLibrary() {
        // calls JFrame
        super("SongLibrary");

        // sets layout of the frame
        setLayout(new BorderLayout());
        // sets default close to do nothing
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Creates the Panel that contains the vertical box and table of
        // information
        JPanel p1 = new JPanel(new BorderLayout());
        add(p1, BorderLayout.EAST);

        // Title of the columns
        String[] HEADERS = { "Song", "Artist", "Album", "Year" };
        // Potential data to be stored
        Object[][] DATA = null;

        // table that goes in the center of the border layout is being added to
        // the scrollable pane
        JTable table = new JTable(DATA, HEADERS);
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);
        // Scroll pane being added to the center
        JScrollPane listOfInfo = new JScrollPane(table);
        add(listOfInfo, BorderLayout.CENTER);

        // might have to delete the model to make an object array [0] then add
        // returns new array containing +1 rows
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(HEADERS);
        table.setModel(model);

        // creates the box container for the right section
        Box rightBox = Box.createVerticalBox();

        // creates buttons -supposed to be in VerticalBox
        JButton add = new JButton("Add");
        rightBox.add(add);
        JButton delete = new JButton("Delete");
        rightBox.add(delete);
        delete.disable();

        // Creates menubar - a devided section at the top of the frame
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        // adds the vertical box
        p1.add(rightBox);

        // Creates String inside the menubar
        JMenu slTitle = new JMenu("SongLibrary");
        // Creates String inside the menubar
        JMenu tTitle = new JMenu("Table");
        // adds menu's to menu bar
        menubar.add(slTitle);
        menubar.add(tTitle);

        // creates menu item for the menu in the menubar
        JMenuItem jmAbout = slTitle.add("About...");
        JMenuItem jmExit = slTitle.add("Exit");
        // creates line that separate's two items
        slTitle.add(jmAbout);
        slTitle.addSeparator();
        slTitle.add(jmExit);

        // creates actionListener for the about text, produces pop up containing
        // information
        jmAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SongLibrary.this,
                        new JLabel("<html>" + "<hr> "
                                + "<b>SongLibrary</b><br>by Jordan Yarros"
                                + "<hr></html>"),
                        "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // creates actionListener for the about text, produces pop up containing
        // information
        jmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(SongLibrary.this,
                        "Do you want to exit?");

                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                }

            }
        });

        // creates menu item for the menu in the menubar
        JMenuItem jmNew = tTitle.add("New");
        JMenuItem jmOpen = tTitle.add("Open...");
        JMenuItem jmSaveAs = tTitle.add("Save As...");
        // creates line that separate's two items
        tTitle.add(jmNew);
        tTitle.add(jmOpen);
        tTitle.add(jmSaveAs);

        Object[] row = new Object[4];
        // action listener for the add button to create a new row
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete.enable();

                row[0] = null;
                row[1] = null;
                row[2] = null;
                row[3] = null;

                model.addRow(row);
            }

        });

        // what happens if i accidently double click on a component of the row
        // and then press delete - CRASH
        // action listener for the delete button to create a new row
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowNum = table.getSelectedRow();
                if (rowNum >= 0) {
                    model.removeRow(rowNum);
                }
                else
                    deleteMessage();
                delete.disable();
            }

            // provides delete message indicating you have not selected a row to
            // delete
            private void deleteMessage() {
                JOptionPane.showMessageDialog(SongLibrary.this,
                        "No row selected");
            }

        });

        // Action Listener for selecting new under the table tab in the menu bar
        jmNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(SongLibrary.this,
                        "Clear all table data?");
                if (result == JOptionPane.YES_OPTION) {
                    model.setRowCount(0);
                    delete.disable();
                }

            }
        });

        // Action Listener for selecting Open... under the table tab in the menu
        // bar
        jmOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // delete enabled
                delete.enable();

                JFileChooser chooser = new JFileChooser();

                // makes sure the person presses open
                int result = chooser.showOpenDialog(SongLibrary.this);
                // gets selected file
                File file = chooser.getSelectedFile();

                // sets the title to the frame as the file name
                setTitle("SongLibrary [" + file.getPath() + "]");

                try {
                    Scanner scanner = new Scanner(file);
                    scanner.useDelimiter(",|\n");
                    if (!scanner.hasNext()) {
                        delete.disable();
                    }
                    else if (result == JFileChooser.OPEN_DIALOG) {
                        Object[] objectOfInput;
                        while (scanner.hasNextLine()) {

                            String Song = scanner.next();
                            String Artist = scanner.next();
                            String Album = scanner.next();
                            String Year = scanner.next();

                            objectOfInput = new Object[] { Song, Artist, Album,
                                    Year };
                            model.addRow(objectOfInput);

                        }

                    }

                    scanner.close();
                }
                catch (Exception error) {
                }
            }

        });

        // Action Listener for selecting Save... under the table tab in the menu
        // bar
        jmSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // something to invoke executable file choosers
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser chooser = new JFileChooser();
                        int result = chooser.showSaveDialog(SongLibrary.this);
                        // Why is this approve and not Save
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            // TODO save table data to file
                            try {

                                PrintWriter output = new PrintWriter(file);
                                for (int row = 0; row < table
                                        .getRowCount(); row++) {
                                    for (int col = 0; col < table
                                            .getColumnCount(); col++) {
                                        output.print(
                                                table.getValueAt(row, col));
                                        if (col != 3) {
                                            output.print(",");
                                        }
                                    }
                                    output.println("");
                                }
                                output.close();
                            }
                            catch (Exception e) {
                            }
                        }
                    }
                });

            }
        });

        // JTextField txtField = new JTextField( "Type Here" );
        // add( txtField );

        // adds window listener that responds to close icon
        addWindowListener(new WindowListener() {

            // creates class ask for closing which will produce dialog
            protected void askForClosing() {
                int result = JOptionPane.showConfirmDialog(SongLibrary.this,
                        "Do you want to exit?");

                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                }
                else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    setVisible(true);
                }
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                askForClosing();
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

        });

        pack();
        setLocationRelativeTo(null);
    }
/**
 * Invokes the program
 * @param args
 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new SongLibrary();
                f.setVisible(true);
            }
        });

    }

}
