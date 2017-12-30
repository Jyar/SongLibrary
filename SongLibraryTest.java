import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.junit.Test;

import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyDialog;
import edu.cnu.cs.gooey.GooeyFrame;

public class SongLibraryTest {

    @Test
    public void testMenu() {

        Gooey.capture(new GooeyFrame() {

            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {

                // Testing MenuBar Menu MenuItems
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu songLibraryMenu = Gooey.getMenu(menubar, "SongLibrary");
                JMenu tableMenu = Gooey.getMenu(menubar, "Table");
                JMenuItem jmAboutItem = Gooey.getMenu(songLibraryMenu,
                        "About...");
                JMenuItem jmExit = Gooey.getMenu(songLibraryMenu, "Exit");
                JMenuItem jmNew = Gooey.getMenu(tableMenu, "New");
                JMenuItem jmOpen = Gooey.getMenu(tableMenu, "Open...");
                JMenuItem jmSaveAs = Gooey.getMenu(tableMenu, "Save As...");

            }
        });
    }

    @Test
    public void testDeleteButton() {
        Gooey.capture(new GooeyFrame() {

            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {

                // Check delete
                JButton delete = Gooey.getButton(frame, "Delete");
                if (delete.isEnabled()) {
                    fail("Delete button enabled... should be disabled");
                }
                // Check add
                JButton add = Gooey.getButton(frame, "Add");
                if (!add.isEnabled()) {
                    fail("Add button disabled... should be enabled");
                }
            }
        });
    }

    @Test
    public void testTitleAndDimension() {
        Gooey.capture(new GooeyFrame() {

            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {
                // Check title
                if (frame.getTitle() != "SongLibrary") {
                    fail("The title was " + frame.getTitle()
                            + " but should be SongLibrary");
                }
                // getting dimensions of the actual frame in the computer screen
                Dimension actualS = frame.getSize();
                frame.pack();
                Dimension expected = frame.getSize();
                assertEquals("Frame was not packed", expected, actualS);

            }
        });
    }

    @Test
    public void testDefaultCloseOperationAndBorderLayout() {
        Gooey.capture(new GooeyFrame() {

            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {
                // Checking default close operation
                int actual = frame.getDefaultCloseOperation();
                assertTrue("Incorrect result, Frame should dispose on close",
                        actual == JFrame.DO_NOTHING_ON_CLOSE);

                // check Border Layout
                LayoutManager lm = frame.getContentPane().getLayout();
                assertEquals("Frame doesn't have a FlowLayout",
                        BorderLayout.class, lm.getClass());
            }
        });
    }

    @Test
    public void testExitOperationLayoutWindowClose() {
        Gooey.capture(new GooeyFrame() {

            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {

                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu songLibraryMenu = Gooey.getMenu(menubar, "SongLibrary");
                JMenuItem jmExit = Gooey.getMenu(songLibraryMenu, "Exit");

                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        jmExit.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        assertEquals("Incorrect title", "Select an Option",
                                dialog.getTitle());
                        Gooey.getLabel(dialog, "Do you want to exit?");
                        JButton cancel = Gooey.getButton(dialog, "Cancel");
                        cancel.doClick();

                        JButton jmExitButton = Gooey.getButton(dialog,
                                "Cancel");

                        assertTrue("JFrame should be displayed",
                                frame.isShowing());
                        jmExitButton.doClick();
                        assertTrue("JFrame should not be hidden",
                                frame.isShowing());

                    }
                });

                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        jmExit.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        assertEquals("Incorrect title", "Select an Option",
                                dialog.getTitle());
                        Gooey.getLabel(dialog, "Do you want to exit?");
                        JButton no = Gooey.getButton(dialog, "No");
                        no.doClick();

                        JButton jmExitButton = Gooey.getButton(dialog, "No");

                        assertTrue("JFrame should be displayed",
                                frame.isShowing());
                        jmExitButton.doClick();
                        assertTrue("JFrame should not be hidden",
                                frame.isShowing());

                    }
                });

                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        jmExit.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        assertEquals("Incorrect title", "Select an Option",
                                dialog.getTitle());
                        Gooey.getLabel(dialog, "Do you want to exit?");
                        JButton yes = Gooey.getButton(dialog, "Yes");
                        yes.doClick();

                        JButton jmExitButton = Gooey.getButton(dialog, "Yes");

                        assertTrue("JFrame should be displayed",
                                frame.isShowing());
                        jmExitButton.doClick();
                        assertFalse("JFrame should be hidden",
                                frame.isShowing());

                    }
                });
            }

        });
    }

    @Test
    public void testAboutDialog() {
        Gooey.capture(new GooeyFrame() {

            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu songLibraryMenu = Gooey.getMenu(menubar, "SongLibrary");
                JMenuItem jmAbout = Gooey.getMenu(songLibraryMenu, "About...");

                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        jmAbout.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        assertEquals("Incorrect title", "Message",
                                dialog.getTitle());
                        Gooey.getLabel(dialog,
                                "<html>" + "<hr> "
                                        + "<b>SongLibrary</b><br>by Jordan Yarros"
                                        + "<hr></html>");
                        JButton ok = Gooey.getButton(dialog, "OK");
                        ok.doClick();

          
                        assertTrue("JFrame should be displayed",
                                frame.isShowing());
                    }
                });
            }
        });

    }

    @Test
    public void testLoadingFile() {

        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {
                JMenuBar menubar = Gooey.getMenuBar(frame);
                JMenu table = Gooey.getMenu(menubar, "Table");
                JMenuItem jmOpen = Gooey.getMenu(table, "Open...");

                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        jmOpen.doClick();
                    }

                    @Override
                    public void test(JDialog dialog) {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
//                                    JFileChooser choose = Gooey.getComponent(dialog,
//                                            JFileChooser.class);
//                                    choose.setSelectedFile(file); // selects
//                                                                //   file with
//                                                                 //  song data.
//                                    choose.approveSelection(); // presses the
//                                                               // Open button to
                                                               // proceed.
                                }
                            });
                        }
                        catch (InvocationTargetException
                                | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    
    @Test
    public void testAdd() {

        Gooey.capture(new GooeyFrame() {
            @Override
            public void invoke() {
                SongLibrary.main(new String[] {});

            }

            @Override
            public void test(JFrame frame) {
                JScrollPane sp = (JScrollPane) Gooey.getComponents(frame, JScrollPane.class);
                  JTable  table = (JTable) Gooey.getComponents(sp, JTable.class);
                  TableModel model = table.getModel();
                  
                Gooey.capture(new GooeyDialog() {
                    @Override
                    public void invoke() {
                        SongLibrary.main(new String[] {});
                    }

                    @Override
                    public void test(JDialog dialog) {
                        JButton add = Gooey.getButton(frame, "Add");
                        add.doClick();
                        
                        model.setValueAt("JimmyBuffet", 0, 0);
                        if (model.getRowCount() < 1){
                            fail("Should habe reported at least one row");
                        }
                    }
                });
            }
        });
    }
}
