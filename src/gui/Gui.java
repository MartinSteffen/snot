/*
 * Gui.java
 *
 * Created on 16. Mai 2001, 22:27
 */


package gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Integer;
import java.lang.String;
import java.util.*;
import absynt.*;
import editor.*;
import smv.*;
import io.*;  
import utils.*;
import simulator.*;
import checks.*;


/**
 *  The GUI!
 *
 * @authors Ingo Schiller and Hans Theman
 * @version $Revision: 1.49 $
 */
public class Gui extends javax.swing.JFrame {

    /** private declarations */
    private JOptionPane SnotOptionPane = null; // hierin werden jegliche popups dargestellt
    private Session session = null;
    private Project activeProject = null;  // referes to the focused Project
    private GuiUtilities Utilities = new Gui.GuiUtilities();

    private final String TITLE = "Snot";    // the Gui title
    private final String SessionFileExtension = "snot"; // the session file extension
    private final String ProjectFileExtension = "sfc";  // the exported SFC file extension
    private final String SmvFileExtension = "smv";   // the SMV - translated file extension
    private final String ParserFileExtension = "tsfc";  // Parser file extension
    private final Point GuiLocation = new Point(0,0);
    private final Point EditorLocation = new Point(0,170);
    private Point ProjectListLocation = new Point(672,0);
    private Point HelpLocation = new Point(672,0);
    private File globalDirectory = null;


    /** Creates new form Gui */
    public Gui(Session _session) {
        // preparing startup
        SnotOptionPane = new JOptionPane();
        session = _session;

        // set GUI L&F
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // the standard
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc.getMessage());
        }

        // preparing visual components
        initComponents ();
        ToolBarTools.setFloatable(false);
	ToolBarFiles.setFloatable(false);

        if (session == null)
            enableSession(false);
	    enableFilesToolBar(false);

        this.setResizable(false);
	this.pack();
	//this.setSize(1000,170);
        setLocation(GuiLocation);
	
    }

	/** This method is called from within the constructor to
	 * initialize the form.
	 */
    private void initComponents() {
	
	jMenuBar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        OpenSession = new javax.swing.JMenuItem();
        NewSession = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        SaveSession = new javax.swing.JMenuItem();
        SaveAsSession = new javax.swing.JMenuItem();
        CloseSession = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        ExitSnot = new javax.swing.JMenuItem();
        Edit = new javax.swing.JMenu();
        NewSFC = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        ImportSFC = new javax.swing.JMenuItem();
        ExportSFC = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        RenameSFC = new javax.swing.JMenuItem();
        RemoveSFC = new javax.swing.JMenuItem();
        ImportExample1 = new javax.swing.JMenuItem();
        ToolsMenu = new javax.swing.JMenu();
        Editor = new javax.swing.JMenuItem();
        CheckSFC = new javax.swing.JMenuItem();
        Simulator = new javax.swing.JMenuItem();
        SMV = new javax.swing.JMenuItem();
	Parser = new javax.swing.JMenuItem();
	PrettyPrinter = new javax.swing.JMenuItem();
        View = new javax.swing.JMenu();
        SFCBrowser = new javax.swing.JCheckBoxMenuItem();
        ShowToolBar = new javax.swing.JCheckBoxMenuItem();
        HelpMenu = new javax.swing.JMenu();
        About = new javax.swing.JMenuItem();
	Help = new javax.swing.JMenuItem();
        ToolBarTools = new javax.swing.JToolBar();
        ButtonEditor = new javax.swing.JButton();
        ButtonCheckSFC = new javax.swing.JButton();
        ButtonSimulator = new javax.swing.JButton();
        ButtonSMV = new javax.swing.JButton();
        PanelStatus = new javax.swing.JPanel();
        Status = new javax.swing.JLabel();

	ButtonParser = new javax.swing.JButton();
	ButtonPrettyPrinter = new javax.swing.JButton();
      //the files option toolbar
	ToolBarFiles = new javax.swing.JToolBar();
 	ButtonOpenSession = new javax.swing.JButton();
	ButtonNewSession = new javax.swing.JButton();
	ButtonSaveSession = new javax.swing.JButton();
        ButtonSaveAsSession = new javax.swing.JButton();
        ButtonCloseSession = new javax.swing.JButton();
        ButtonImportSFC = new javax.swing.JButton();
        ButtonExportSFC = new javax.swing.JButton();
	ShowFilesToolBar = new javax.swing.JCheckBoxMenuItem();

        FileMenu.setName("Session");
          FileMenu.setActionCommand(null);
          FileMenu.setText("Session");


          NewSession.setLabel("New Session");
          NewSession.setName("newSession");
          NewSession.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  NewSessionActionPerformed(evt);
              }
          }
          );
          FileMenu.add(NewSession);

	  OpenSession.setLabel("Open Session");
            OpenSession.setName("openSession");
            OpenSession.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    OpenSessionActionPerformed(evt);
                }
            }
            );
            FileMenu.add(OpenSession);


	 CloseSession.setLabel("Close Session");
            CloseSession.setName("closeSession");
            CloseSession.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CloseSessionActionPerformed(evt);
                }
            }
            );
            FileMenu.add(CloseSession);



          FileMenu.add(jSeparator2);

          SaveSession.setLabel("Save Session");
            SaveSession.setName("saveSession");
            SaveSession.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SaveSessionActionPerformed(evt);
                }
            }
            );
            FileMenu.add(SaveSession);

          SaveAsSession.setLabel("Save Session as");
            SaveAsSession.setName("saveAsSession");
            SaveAsSession.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SaveAsSessionActionPerformed(evt);
                }
            }
            );
            FileMenu.add(SaveAsSession);


          FileMenu.add(jSeparator3);

          ExitSnot.setLabel("Exit");
            ExitSnot.setName("exitSnot");
            ExitSnot.setText("Exit Snot");
            ExitSnot.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ExitSnotActionPerformed(evt);
                }
            }
            );
            FileMenu.add(ExitSnot);
            jMenuBar.add(FileMenu);

        Edit.setLabel("Edit");
          Edit.setName("SFC");
          Edit.setText("SFC");


          NewSFC.setLabel("New SFC");
            NewSFC.setName("newSFC");
            NewSFC.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    NewSFCActionPerformed(evt);
                }
            }
            );
            Edit.add(NewSFC);

          Edit.add(jSeparator4);

          ImportSFC.setLabel("Import SFC");
            ImportSFC.setName("importSFC");
            ImportSFC.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ImportSFCActionPerformed(evt);
                }
            }
            );
            Edit.add(ImportSFC);

          ExportSFC.setLabel("Export SFC");
            ExportSFC.setName("exportSFC");
            ExportSFC.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ExportSFCActionPerformed(evt);
                }
            }
            );
            Edit.add(ExportSFC);

          Edit.add(jSeparator5);

          RenameSFC.setLabel("Rename SFC");
            RenameSFC.setName("renameSFC");
            RenameSFC.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    RenameSFCActionPerformed(evt);
                }
            }
            );
            Edit.add(RenameSFC);

          RemoveSFC.setLabel("Remove SFC");
            RemoveSFC.setName("removeSFC");
            RemoveSFC.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    RemoveSFCActionPerformed(evt);
                }
            }
            );
            Edit.add(RemoveSFC);

          ImportExample1.setLabel("Import Example1 SFC");
            ImportExample1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ImportExample1ActionPerformed(evt);
                }
            }
            );
            Edit.add(ImportExample1);
            jMenuBar.add(Edit);

        ToolsMenu.setText("Tools");

          Editor.setLabel("Editor");
            Editor.setName("editor");
            Editor.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    EditorActionPerformed(evt);
                }
            }
            );
            ToolsMenu.add(Editor);

          CheckSFC.setLabel("Check SFC");
            CheckSFC.setName("check");
            CheckSFC.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    CheckSFCActionPerformed(evt);
                }
            }
            );
            ToolsMenu.add(CheckSFC);

          Simulator.setLabel("Simulator");
            Simulator.setName("simulator");
            Simulator.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SimulatorActionPerformed(evt);
                }
            }
            );
            ToolsMenu.add(Simulator);

          SMV.setLabel("SMV translation");
            SMV.setName("smv");
            SMV.setText("SMV");
            SMV.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SMVActionPerformed(evt);
                }
            }
            );
            ToolsMenu.add(SMV);


	  Parser.setLabel("Parser");
            Parser.setName("Parser");
            Parser.setText("Parser");
            Parser.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ParserActionPerformed(evt);
                }
            }
            );
            ToolsMenu.add(Parser);

        PrettyPrinter.setLabel("PrettyPrinter");
            PrettyPrinter.setName("PrettyPrinter");
            PrettyPrinter.setText("PrettyPrinter");
            PrettyPrinter.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    PrettyPrinterActionPerformed(evt);
                }
            }
            );
            ToolsMenu.add(PrettyPrinter);

      jMenuBar.add(ToolsMenu);




        View.setLabel("View");
          View.setName("View");

          SFCBrowser.setLabel("SFC Browser");
            SFCBrowser.setName("SFCBrowser");
            SFCBrowser.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    SFCBrowserActionPerformed(evt);
                }
            }
            );
            View.add(SFCBrowser);

          ShowToolBar.setLabel("Tool Bar");
            ShowToolBar.setSelected(true);
            ShowToolBar.setName("showToolBar");
            ShowToolBar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ShowToolBarActionPerformed(evt);
                }
            }
            );
            View.add(ShowToolBar);
            jMenuBar.add(View);


	  ShowFilesToolBar.setLabel("Files Toolbar");
            ShowFilesToolBar.setSelected(true);
            ShowFilesToolBar.setName("showFilesToolBar");
            ShowFilesToolBar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ShowFilesToolBarActionPerformed(evt);
                }
            }
            );
            View.add(ShowFilesToolBar);
            jMenuBar.add(View);

        HelpMenu.setText("? (Help) ");

          About.setLabel("About");
            About.setName("about");
            About.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    AboutActionPerformed(evt);
                }
            }
            );
            HelpMenu.add(About);

	    Help.setLabel("Documentation");
            Help.setName("Help");
            Help.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    HelpActionPerformed(evt);
                }
            }
            );
            HelpMenu.add(Help);




	    jMenuBar.add(HelpMenu);
          setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setName("frameGUI");
        setTitle("Snot");
        setBackground(java.awt.Color.lightGray);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setFont(new java.awt.Font ("Abadi MT Condensed Light", 0, 12));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        }
        );

        ToolBarTools.setName("ToolBarTools");
        ToolBarTools.setMinimumSize(new java.awt.Dimension(378, 39));

        ButtonEditor.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonEditor.setToolTipText("Launch editor");
          ButtonEditor.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonEditor.setName("buttonEditor");
          ButtonEditor.setText("Editor");
          ButtonEditor.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  EditorActionPerformed(evt);
              }
          }
          );
          ToolBarTools.add(ButtonEditor);


        ButtonCheckSFC.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonCheckSFC.setToolTipText("Check SFC");
          ButtonCheckSFC.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonCheckSFC.setName("buttonCheck");
          ButtonCheckSFC.setText("Check");
          ButtonCheckSFC.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  CheckSFCActionPerformed(evt);
              }
          }
          );
          ToolBarTools.add(ButtonCheckSFC);


        ButtonSimulator.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonSimulator.setToolTipText("Simulate SFC");
          ButtonSimulator.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonSimulator.setName("buttonSimulator");
          ButtonSimulator.setText("Simulator");
          ButtonSimulator.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  SimulatorActionPerformed(evt);
              }
          }
          );
          ToolBarTools.add(ButtonSimulator);


        ButtonSMV.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonSMV.setToolTipText("Transform SFC to SMV");
          ButtonSMV.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonSMV.setName("buttonSMV");
          ButtonSMV.setText("SMV");
          ButtonSMV.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  SMVActionPerformed(evt);
              }
          }
          );
          ToolBarTools.add(ButtonSMV);



	ButtonParser.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonParser.setToolTipText("Start Parser");
          ButtonParser.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonParser.setName("buttonParser");
          ButtonParser.setText("Parser");
          ButtonParser.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  ParserActionPerformed(evt);
              }
          }
          );
	  ToolBarTools.add(ButtonParser);
	ButtonPrettyPrinter.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonPrettyPrinter.setToolTipText("Start PrettyPrinter");
          ButtonPrettyPrinter.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonPrettyPrinter.setName("buttonPrettyPrinter");
          ButtonPrettyPrinter.setText("PrettyPrinter");
          ButtonPrettyPrinter.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  PrettyPrinterActionPerformed(evt);
              }
          }
          );

	ToolBarTools.add(ButtonPrettyPrinter);


	/**
	 * The Toolbar for the Files options
	 */
	ToolBarFiles.setName("ToolBarFiles");
	ToolBarFiles.setSize(378,39);



	//The NewSession Button
	ButtonNewSession.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonNewSession.setToolTipText("New Session");
          ButtonNewSession.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonNewSession.setName("buttonNewSession");
          ButtonNewSession.setText("New Session");
	  //ImageIcon nsicon = new ImageIcon("gui/icons/newsession.gif");
	  //ButtonNewSession.setIcon(nsicon);
          ButtonNewSession.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  NewSessionActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonNewSession);


	//The OpenSession Button
	ButtonOpenSession.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonOpenSession.setToolTipText("Open Session");
          ButtonOpenSession.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonOpenSession.setName("buttonOpenSession");
	  ButtonOpenSession.setText("Open Session");
	  //ImageIcon osicon = new ImageIcon("gui/icons/opensession.gif");
	  //ButtonOpenSession.setIcon(osicon);
          ButtonOpenSession.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  OpenSessionActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonOpenSession);

	  // The CloseSession Button
       ButtonCloseSession.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonCloseSession.setToolTipText("Close Session");
          ButtonCloseSession.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonCloseSession.setName("buttonCloseSession");
          ButtonCloseSession.setText("Close Session");
	  //ImageIcon csicon = new ImageIcon("gui/icons/closesession.gif");
	  //ButtonCloseSession.setIcon(csicon);
          ButtonCloseSession.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  CloseSessionActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonCloseSession);


	// The SaveSession Button
       ButtonSaveSession.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonSaveSession.setToolTipText("Save Session");
          ButtonSaveSession.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonSaveSession.setName("buttonSaveSession");
          ButtonSaveSession.setText("Save Session");
	  //ImageIcon ssicon = new ImageIcon("gui/icons/savesession.gif");
	  //ButtonSaveSession.setIcon(ssicon);
          ButtonSaveSession.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  SaveSessionActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonSaveSession);

       // The SaveAsSession Button
       ButtonSaveAsSession.setPreferredSize(new java.awt.Dimension(120, 35));
          ButtonSaveAsSession.setToolTipText("Save Session As");
          ButtonSaveAsSession.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonSaveAsSession.setName("buttonSaveAsSession");
          ButtonSaveAsSession.setText("Save Session As");
	  //ImageIcon ssaicon = new ImageIcon("gui/icons/savesessionas.gif");
	  //ButtonSaveAsSession.setIcon((Icon)ssaicon);
          ButtonSaveAsSession.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  SaveAsSessionActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonSaveAsSession);




	 // The ImportSFC Button
       ButtonImportSFC.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonImportSFC.setToolTipText("Import SFC");
          ButtonImportSFC.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonImportSFC.setName("buttonImportSFC");
          ButtonImportSFC.setText("Import SFC");
	  //ImageIcon isfcicon = new ImageIcon("gui/icons/importsfc.gif");
	  //ButtonImportSFC.setIcon(isfcicon);
          ButtonImportSFC.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  ImportSFCActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonImportSFC);

	 // The ExportSFC Button
       ButtonExportSFC.setPreferredSize(new java.awt.Dimension(90, 35));
          ButtonExportSFC.setToolTipText("Export SFC");
          ButtonExportSFC.setMaximumSize(new java.awt.Dimension(180, 35));
          ButtonExportSFC.setName("buttonExportSFC");
          ButtonExportSFC.setText("Export SFC");
	  //ImageIcon esfcicon = new ImageIcon("gui/icons/exportsfc.gif");
	  //ButtonExportSFC.setIcon(esfcicon);
          ButtonExportSFC.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  ExportSFCActionPerformed(evt);
              }
          }
          );
          ToolBarFiles.add(ButtonExportSFC);



	//Add the ToolBarFiles Menu first
	getContentPane().add(ToolBarFiles, java.awt.BorderLayout.NORTH);
	//Then add the ToolBarTools
	getContentPane().add(ToolBarTools, java.awt.BorderLayout.CENTER);

        PanelStatus.setLayout(new java.awt.GridLayout(2, 1, 20, 0));
        PanelStatus.setPreferredSize(new java.awt.Dimension(400, 20));
        PanelStatus.setName("panelStatus");
        PanelStatus.setMinimumSize(new java.awt.Dimension(200, 20));

        Status.setName("statusLine");
        Status.setText("  No active project");
        PanelStatus.add(Status);


        // Feed the main frame with all bits n bytes

        getContentPane().setLayout(new GridLayout(3,1));
        getContentPane().add(PanelStatus);

        setJMenuBar(jMenuBar);

    }


/**********************************************************************************
 *
 *      Some ActionListeners
 *
 **********************************************************************************/


    private void createEditor(Project p) {
	int result = -1;
	Editor e = null;
	try {
	    e = p.getEditor();
	    if (e != null)
		if (e.isVisible()){
		    e.toFront();
//		    e.setFocus();
		}
		else
		    (p.getEditor()).show();
	    else {
		if (!p.hasEditor()) {
		    // show warning dialog
		    result = SnotOptionPane.showConfirmDialog(null, "ATTENTION:\n\nThis SFC has no editor. If this \nSFC was parsed , the editor may mess up!\nShow editor anyway?\n(continue at own risk!)", "Launch editor?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		}
		if ((result == JOptionPane.YES_OPTION) || p.hasEditor()) {
if (p.getSFC()==null)
System.out.println("SFC ist null!!!!!!");
		    e = new Editor(p.getSFC());
		    e.setLocation(EditorLocation);
		    e.addWindowListener(new GuiWindowListener());
		    p.setEditor(e);		
		}
	    }
	}
	catch (EditorException edex){
	    SnotOptionPane.showMessageDialog(null, edex.getMessage(), "Editor-Error", JOptionPane.ERROR_MESSAGE);
	}
	catch (Exception ex) {
	    SnotOptionPane.showMessageDialog(null, "An unexpected error occured!\nWe told ya!\n"+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	}
    }


  //For the FilesToolBar
  private void ShowFilesToolBarActionPerformed(java.awt.event.ActionEvent evt) {
      // switch the Tools ToolBar on and off in the Gui
      if (ShowFilesToolBar.isSelected())
          ToolBarFiles.setVisible(true);
      else
          ToolBarFiles.setVisible(false);
      pack();
  }



  private void ShowToolBarActionPerformed(java.awt.event.ActionEvent evt) {
      // switch the Tools ToolBar on and off in the Gui
      if (ShowToolBar.isSelected())
          ToolBarTools.setVisible(true);
      else
          ToolBarTools.setVisible(false);
      pack();
  }


  private void ImportExample1ActionPerformed(java.awt.event.ActionEvent evt) {

      Project project = null;
      if (session == null) {
           SnotOptionPane.showMessageDialog(null, "Importing the Example1 SFC failed!\nCannot import a SFC without an active session.\nPlease open or create a new session first!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
           return;
      }

      SFC sfc1 = Example.getExample1();
       try {
              project = new Project();
              project.setSFC(sfc1);
              session.addProject(project);
          }
	  catch (Exception ex) {
               SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }

          project.setChecked(true);
	  project.setOnlyBool(true);
          project.setName("Example1");
          activeProject = project;
          updateProjectList();
	  setStatusLine(true, "Example1 imported.");
  }

  private void RemoveSFCActionPerformed(java.awt.event.ActionEvent evt) {
    Project project = activeProject; // for security reason
    String msg = null;
    int result = 0;

// check for valid session. This should never be entered!!!
     if (session == null) {
          System.out.print("\n An error occured! RenameSFC was called without an active Session!!");
          return;
     }

     // check for active Projects
     if (session.isEmpty() || project == null) {
         if (session.isEmpty())
             msg = new String("There is no SFC in this session!\n");
         else
             msg = new String("Please select a SFC first!\n");

         SnotOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
         return;
     }

     // show Remove Dialog
     result = SnotOptionPane.showConfirmDialog(null, "You are about to remove SFC \""+project.getName()+"\" from the current session.\nIf it is not exportet or saved its content will be lost!\n\n Do you wish to proceed?",
                                        "Remove SFC from Session", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

     if (result == JOptionPane.NO_OPTION)
         return;

     // removing SFC from session
     session.removeProject(project);
     activeProject = null;
     updateProjectList();
     setStatusLine(false, "SFC "+project.getName() +" removed.");
  }

  private void RenameSFCActionPerformed(java.awt.event.ActionEvent evt) {
     String input = null;
     String msg = null;
     Project project = activeProject; // for security reason

// check for valid session. This should never be entered!!!
     if (session == null) {
          System.out.print("\n An error occured! RenameSFC was called without an active Session!!");
          return;
     }

     // check for active Projects
     if (session.isEmpty() || project == null) {
         if (session.isEmpty())
            msg = new String("There is no SFC in the session!\n");
         else
            msg = new String("Please select a SFC first!\n");

         SnotOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
         return;
     }

     // show Rename Dialog
     input = SnotOptionPane.showInputDialog(null, "The current SFC's name is \""+project.getName()+"\".\nPlease enter a new name and hit \"OK\"",
                                        "Rename SFC", JOptionPane.PLAIN_MESSAGE);
     // set new name
     if (input != null && input.length()>0) {
          project.setName(input);
          session.setModified(true);
     }
     updateProjectList();
     setStatusLine(true, "SFC renamed.");
     if (project.getEditor() != null)
	 project.getEditor().toFront();
  }

  private void NewSessionActionPerformed(java.awt.event.ActionEvent evt) {
      int response;

      // check for active session
      if (session != null) {
          if (session.isModified()) {
              response = SnotOptionPane.showConfirmDialog(null, "There already is an active unsaved session!\n Without saving the changes will be lost!\n\nDo you want to save the changes before opening a new session?\n",
                                            "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          }
          else if (session.isSaved()) {
              response = SnotOptionPane.showConfirmDialog(null, "There already is an active session!\nOpening a new once will close the current session.\n\n Do you wish to proceed?\n",
                                            "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          }
          else {
              response = SnotOptionPane.showConfirmDialog(null, "The active session is not saved!\n Without saving its content will be lost!\n\nDo you want to save it before opening a new session?\n",
                                            "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          }

          switch (response) {
              case JOptionPane.YES_OPTION: if(!SaveSessionActionPerformed(null))
                                                return;
                                           break;
              case JOptionPane.NO_OPTION: break;
              case JOptionPane.CANCEL_OPTION:
              case JOptionPane.CLOSED_OPTION: return;
          }

          closeSession();
      }

      try {
          session = new Session();
      }
      catch (Exception ex) {
          SnotOptionPane.showMessageDialog(null, ex.getMessage(),
                                        "Error", JOptionPane.WARNING_MESSAGE);
          session = null;
          return;
      }

      enableSession(true);
      enableFilesToolBar(true);
      setTitle(TITLE+"  "+session.getName());
      setStatusLine(false, "New session opened.");
      globalDirectory = null;

  }


  private void SFCBrowserActionPerformed(java.awt.event.ActionEvent evt) {
      if (SFCBrowser.isSelected())
          {showProjectList();}
      else {
	ProjectListLocation = projectFrame.getLocation();
	projectFrame.dispose();
      }
  }


 /**
  * The Parser routine
  */

 private void ParserActionPerformed(java.awt.event.ActionEvent evt) {
      int result;
      File file = null;
      Project project = null;

      // check for valid session
      if (session == null) {
           SnotOptionPane.showMessageDialog(null, "Please open or create a new session first!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
           return;
      }

      setStatusLine(true, "Starting Parser");
      // initializing FileChooser
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new SnotFileFilter(ParserFileExtension,"ASCII-File"));
      chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
      chooser.setApproveButtonToolTipText("Parse File");
      chooser.setDialogTitle("Parse textfile to SFC");

      // set prefered Directory
      if (globalDirectory != null){chooser.setCurrentDirectory(globalDirectory);}
      else if (session.isSaved())
          chooser.setCurrentDirectory(session.getFile());

      // show FileChooser
      result = chooser.showDialog(null, "Parse");
      if (result == JFileChooser.APPROVE_OPTION) {
          file = chooser.getSelectedFile();
	  globalDirectory = file;

          // check choosen file
          try {
              Utilities.validateFile(file,ParserFileExtension);
          }
          catch (Exception ex) {
	      setStatusLine(true, "Parser failed, file error!");
              SnotOptionPane.showMessageDialog(null, new String(ex.getMessage()+"\n\nMaybe file is not valid or not accessible"), "File error", JOptionPane.ERROR_MESSAGE);
          }

      // parse file to SFC and add new Project to Session
          try {
              io.Parser parser = new io.Parser();
	      project = new Project();
	      project.setSFC(parser.parseFile(file));
System.out.println("File Parsed");
	      project.setName(file.getName());
	      project.setEnvironment();
              session.addProject(project);
	      activeProject = project;
	      setStatusLine(true,"Parsed file \""+file.getName()+"\" succesfully");

          }
	  catch (ParseException pex) {
	      setStatusLine(true, "Parser failed");
	      SnotOptionPane.showMessageDialog(null, pex.getMessage(), "Parse-Error", JOptionPane.ERROR_MESSAGE);
	  }
	  catch (Exception ex) {
System.out.println("\n"+ex.getClass());
ex.printStackTrace();
	       setStatusLine(true, "Parser failed");
               SnotOptionPane.showMessageDialog(null, "Abnormal Error!\nError code 0-8-15\nPlease consult your local cofe machine ...\n\n"+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
      updateProjectList();
  }
  else {
      setStatusLine(true, "Parser aborted.");
  }
}

  /**
   * The routine for the pretty Printer
   */
  private void PrettyPrinterActionPerformed(java.awt.event.ActionEvent evt) {

      if(activeProject!=null){
            try{
	        setStatusLine(true, "Starting PrettyPrinter");
	        PrettyPrint pp = new PrettyPrint();
	        SFC sfc = activeProject.getSFC();
	        System.out.println(activeProject.getName());
	        pp.print(sfc);
	    }
	    catch(Exception e){
	      setStatusLine(true, "PrettyPrinter failed");
	      SnotOptionPane.showMessageDialog(null, "Pretty Printer failed.\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	      }
      }
      else{SnotOptionPane.showMessageDialog(null, "Please select a SFC first!\n", "Error: no SFC", JOptionPane.ERROR_MESSAGE);}
   }

   /**
   * The help routine
   */
  private void HelpActionPerformed(java.awt.event.ActionEvent evt) {

    Help.setEnabled(false);
    FileReader in = null;
    File f = null;

    helpFrame = new JFrame("Documentation");
    helpFrame.setResizable(true);
    helpFrame.setSize(480,700);
    helpFrame.setLocation(HelpLocation);


    //On close the window will be disposed.
    helpFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e){Help.setEnabled(true);
					       HelpLocation = helpFrame.getLocation();
					       helpFrame.dispose();}});

    try{
	f = new File("gui/help.txt");
	in = new FileReader(f);
	int size = (int)f.length();
	char[] data = new char[size];
	int chars_read = 0;
	while (chars_read <size)
	  chars_read += in.read(data, chars_read, size-chars_read);
	textarea = new JTextArea(new String(data));
	textarea.setEditable(false);
	textarea.setLineWrap(true);
	helpscrollpane = new JScrollPane(textarea);
	Container pane = helpFrame.getContentPane();
	pane.add(helpscrollpane);

	}catch(IOException fe){
	  SnotOptionPane.showMessageDialog(null, "Help not available!\n"+fe.getMessage(), "File not found", JOptionPane.ERROR_MESSAGE);
	  }
	 catch(Exception e){
	  SnotOptionPane.showMessageDialog(null, "Help not available!\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	  }
	finally{try {in.close();}catch(IOException e){
	    SnotOptionPane.showMessageDialog(null, "Help error.\n"+e.getMessage(), "File not found", JOptionPane.ERROR_MESSAGE);}}
    helpFrame.show();
  }


  /**
   * The SMV - Translator routine.
   */

  private void SMVActionPerformed(java.awt.event.ActionEvent evt) {
      int response;
      File file = null;
      ByteArrayOutputStream stream = null;

      if (activeProject == null) {
          SnotOptionPane.showMessageDialog(null, "Please select a SFC first!\n", "Error: no SFC", JOptionPane.ERROR_MESSAGE);
          return;
      }

      if (!activeProject.isChecked()) {
          response = SnotOptionPane.showConfirmDialog(null, "The SFC \""+activeProject.getName()+"\" needs to be checked\nbefore the SMV translator can be run!\n\nDo you want to check the SFC now?\n",
                                            "Warning: SFC is not checked", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          if (response == JOptionPane.OK_OPTION)
              CheckSFCActionPerformed(null);
          else
              return;

	  // war der check erfolgreich???
	  if (!activeProject.isChecked())
	      return;
      }

      if (!activeProject.isOnlyBool()) {
	  SnotOptionPane.showMessageDialog(null, "The SFC \""+activeProject.getName()+"\" is not boolean!\nThe SMV translator can only\nhandle SFCs with boolean operations.\nAborting SMV translation", "Error: SFC is not boolean", JOptionPane.ERROR_MESSAGE);
          return;
      }

      try {
          SMVTranslator trans = new SMVTranslator(activeProject.getSFC());
	  stream = new ByteArrayOutputStream();
	  stream = trans.toStream();
	  setStatusLine(true, "launching SMV translator ...");
          JFileChooser chooser = new JFileChooser();
	  
	  // set prefered Directory
	  if (globalDirectory != null){chooser.setCurrentDirectory(globalDirectory);}
	  else if (session.isSaved())
	      chooser.setCurrentDirectory(session.getFile());

          chooser.setFileFilter(new SnotFileFilter("smv","SMV output"));
          chooser.setDialogTitle("Save SMV translation output as");
          int result = chooser.showDialog(null, "Save as");
          if (result == JFileChooser.APPROVE_OPTION) {
             file = Utilities.createFileDialog(chooser.getSelectedFile(), SmvFileExtension);
	     globalDirectory = file;
	     FileOutputStream output = new FileOutputStream(file);
	     byte[] array =  stream.toByteArray();
	     output.write(array);
	     output.flush();
	     output.close();
	     setStatusLine(true, "SMV translation succeeded!");
            }
      }
      catch (SMVException smvex) {
	 setStatusLine(true, "SMV translation failed.");
         SnotOptionPane.showMessageDialog(null, smvex.getMessage(), "SMV - Error", JOptionPane.ERROR_MESSAGE);
      }
      catch(FileNotFoundException fnfe){
	 setStatusLine(true, "SMV translation failed.");
         SnotOptionPane.showMessageDialog(null, fnfe.getMessage(), "File - Error", JOptionPane.ERROR_MESSAGE);
      }
      catch(Exception e){
	 setStatusLine(true, "SMV translation failed.");
	 SnotOptionPane.showMessageDialog(null, ("An unexpected condition occured!\n"+e.getMessage()), "Abnormal Error", JOptionPane.ERROR_MESSAGE);
e.printStackTrace();
      }

  }

  /**
   *The Simulator
   */

  private void SimulatorActionPerformed(java.awt.event.ActionEvent evt) {
     int response = 0;

     if (activeProject == null) {
          SnotOptionPane.showMessageDialog(null, "Please select a SFC first!\n"
					   , "Error: no SFC", JOptionPane.ERROR_MESSAGE);
          return;
     }

     // check for valid SFC
     if (!activeProject.isChecked()) {
	 response = SnotOptionPane.showConfirmDialog(null, "The SFC \""+activeProject.getName()+"\" needs to be checked\nbefore the simulator can be run!\n\nDo you want to check the SFC now?\n", "Warning: SFC is not checked"
						     , JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          if (response == JOptionPane.OK_OPTION)
              CheckSFCActionPerformed(null);
          else
              return;

	  // was the check successful???
	  if (!activeProject.isChecked())
	      return;
     }

     try {
	 setStatusLine(true, "launching simulator ...");
	 SFC _sfc = activeProject.getSFC();
	 Simulator sim = new Simulator(_sfc);	// Simulator erzeugen

	 sim.Initialize();						// Simulator initialisieren

	 System.out.print("Anfangszustand: ");
	 sim.PrintConfiguration(System.out);				// Zustand ausgeben

	 for (int i=1; i<=15; i++) {

	     sim.SingleStep();

	     System.out.print("nach "+i+". Schritt: ");

			sim.PrintConfiguration(System.out);
	 }
     } catch (Exception e) {
	 setStatusLine(true, "Simulator failed.");
System.out.println("Error while simulating the SFC"+e.getMessage());
	 SnotOptionPane.showMessageDialog(null, ("Simulator execution aborted due to an unexpected error./n"+e.getMessage())
					  , "Error", JOptionPane.ERROR_MESSAGE);
     }
  }

  /**
   *The Checker Routine
   */
  private void CheckSFCActionPerformed(java.awt.event.ActionEvent evt) {
      boolean status = false;
      boolean status2 = false;
      int result;
      if (activeProject == null) {
          SnotOptionPane.showMessageDialog(null, "Please select a SFC first!\n", "Error: no SFC", JOptionPane.ERROR_MESSAGE);
          return;
      }
     

     // show Check - Dialog
     result = SnotOptionPane.showConfirmDialog(null, "Checks may fail, due to bad programming.\nDo you want to override the checks\nand set checked = true??","Override checks", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

     if (result == JOptionPane.YES_OPTION){
	 activeProject.setChecked(true);
	 activeProject.setOnlyBool(true);
	 
	
	 setStatusLine(true, "Checks passed");
	 SnotOptionPane.showMessageDialog(null, "SFC set checked.\n","Check Info", JOptionPane.INFORMATION_MESSAGE);
         return;
     }


      try {
	  status = Snotcheck.isWellDefined(activeProject.getSFC());
	  // Checked setzen falls alle Tests bestanden wurden.
	  activeProject.setChecked(status);          
	  status2 = Snotcheck.isOnlyBool(activeProject.getSFC());
	  //Den Only Bool Wert des Projects setzen
	  activeProject.setOnlyBool(status2);
         
      }
      catch (CheckException checkEx) {
          if (checkEx instanceof IStepException){
                SnotOptionPane.showMessageDialog(null, checkEx.getMessage(),
                                        "No Istep Exception", JOptionPane.ERROR_MESSAGE);
		status = false;
		activeProject.setChecked(status);
		return;
	  }
          else if(checkEx instanceof DecListFailure) {
	        status = false;
		activeProject.setChecked(status);
		if(activeProject.getEditor()!= null)		
		    activeProject.getEditor().highlight_state(((DecListFailure)checkEx).get_Declaration(),true);
		SnotOptionPane.showMessageDialog(null, checkEx.getMessage(),
                                        "Error in the declaration list", JOptionPane.ERROR_MESSAGE);
		return;
          }
          else if(checkEx instanceof ActionFailure) {
	        status = false;
		activeProject.setChecked(status);
		if(activeProject.getEditor()!= null)
		    activeProject.getEditor().highlight_state(((ActionFailure)checkEx).get_Action(),true);
                SnotOptionPane.showMessageDialog(null, checkEx.getMessage(),
                                        "Action Failure", JOptionPane.ERROR_MESSAGE);
		return;
          }
          else if(checkEx instanceof StepFailure) {
	        status = false;
		activeProject.setChecked(status);
		if(activeProject.getEditor()!= null)
		    activeProject.getEditor().highlight_state(((StepFailure)checkEx).get_Step(),true);
                SnotOptionPane.showMessageDialog(null, checkEx.getMessage(),
                                        "Step Failure", JOptionPane.ERROR_MESSAGE);
		return;
          }
          else if(checkEx instanceof TransitionFailure) {
	        status = false;
		activeProject.setChecked(status);
		if(activeProject.getEditor()!= null)		
		    activeProject.getEditor().highlight_state(((TransitionFailure)checkEx).get_Trans(),true);
                SnotOptionPane.showMessageDialog(null, checkEx.getMessage(),
                                        "Transition Failure", JOptionPane.ERROR_MESSAGE);
		return;
          }
	  else {
	    status = false;
	    activeProject.setChecked(status);
	    SnotOptionPane.showMessageDialog(null, checkEx.getMessage(),
                                        "Check Error", JOptionPane.ERROR_MESSAGE);
	     return;
	  }
      }
      catch (Exception ex) {
          status = false;
	  activeProject.setChecked(status);
	  ex.printStackTrace();
	  SnotOptionPane.showMessageDialog(null, "Abnormal Error \n"+ ex.getClass(),
                                        "Check Error", JOptionPane.ERROR_MESSAGE);
	  return;
      }

     if(activeProject.isChecked()) {
	 setStatusLine(true, "Checks passed");
	 SnotOptionPane.showMessageDialog(null, "All checks passed succesfully.\nOnly boolean expressions occured: "+status2,
                                        "Check Info", JOptionPane.INFORMATION_MESSAGE);
     }

  }

  private void EditorActionPerformed(java.awt.event.ActionEvent evt) {
      NewSFCActionPerformed(null);
  }

  private void ExitSnotActionPerformed(java.awt.event.ActionEvent evt) {
      prepareForExitSnot(true);
  }

  /**
   * The ExportSFC function:
   * Exports a single SFC. It catches events from the ToolBarButtons and Menu.
   * @param evt the ActionEvent
   * @see #Project.saveSFC
   */

  private void ExportSFCActionPerformed(java.awt.event.ActionEvent evt) {
      Project project = activeProject;
      File file = null;
      String name = null;

      // check for empty session
      if (session.isEmpty()) {
          SnotOptionPane.showMessageDialog(null, "Export SFC error:\nThere are no SFCs in the current session!",
                                           "Error", JOptionPane.ERROR_MESSAGE);
          return;
      }

      if (project == null) {
          SnotOptionPane.showMessageDialog(null, "Please select a SFC first!\n", "Error: no SFC", JOptionPane.ERROR_MESSAGE);
          return;
      }

      // initialize FileChooser
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new SnotFileFilter("sfc","Sequ.Func.Chart"));
      chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
      chooser.setApproveButtonToolTipText("Export SFC");
      chooser.setDialogTitle("Export SFC");

      // set prefered Directory and filename
      if (globalDirectory != null){chooser.setCurrentDirectory(globalDirectory);}
      else if (session.isSaved())
          chooser.setCurrentDirectory(session.getFile());

      chooser.setSelectedFile(new File(project.getName()));

      // finally display FileChooser
      int result = chooser.showDialog(null, "Export");
      if (result == JFileChooser.APPROVE_OPTION) {
          file = Utilities.createFileDialog(chooser.getSelectedFile(), ProjectFileExtension);
          if (file == null)
              return;
	  //set the Global Directoty value
	  globalDirectory = file;
          // set name out ouf filename
          if (!project.isNamed()) {
              name = file.getName();
              name = name.substring(0,name.lastIndexOf(ProjectFileExtension));
              project.setName(name);
          }

          // save the editor's environment
          project.setEnvironment();

          // save the session
          try {
              project.exportSFC(file);
          }
          catch (IOException ex) {
              SnotOptionPane.showMessageDialog(null, ex.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
              return;
          }
          catch (Exception ex) {
System.out.print(ex.getClass());
              SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
              return;
          }
          updateProjectList();
	  setStatusLine(true, "SFC exported");
       }
  }

  /**
   * The ImportSFC function:
   * Imports a single SFC. It catches events from the ToolBarButtons and Menu.
   * @param evt the ActionEvent
   * @see #Project.openSFC
   */

  private void ImportSFCActionPerformed(java.awt.event.ActionEvent evt) {
      Project project = null;
      File file = null;

      // check for valid session
      if (session == null) {
           SnotOptionPane.showMessageDialog(null, "Importing a new SFC failed!\nCannot import a SFC without an active session.\nPlease open or create a new session first!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
           return;
      }

      // initializing FileChooser
      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new SnotFileFilter(ProjectFileExtension,"Sequential Function Chart"));
      chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
      chooser.setApproveButtonToolTipText("Import SFC");
      chooser.setDialogTitle("Import SFC");

      // set prefered Directory
      if (globalDirectory != null){chooser.setCurrentDirectory(globalDirectory);}
      else if (session.isSaved())
          chooser.setCurrentDirectory(session.getFile());      



      // show FileChooser
      int result = chooser.showDialog(null, "Import");
      if (result == JFileChooser.APPROVE_OPTION) {
          file = chooser.getSelectedFile();
	  globalDirectory = file;

          // check choosen file
          try {
              Utilities.validateFile(file,ProjectFileExtension);
          }
          catch (Exception ex) {
              SnotOptionPane.showMessageDialog(null, ex.getMessage(), "File error", JOptionPane.ERROR_MESSAGE);
          }

          // read and add Project
          try {
              project = Project.importSFC(file);
	      project.restoreEnvironment();
              session.addProject(project);
          }
	  catch (Exception ex) {
System.out.print("\n"+ex.getClass());
ex.printStackTrace();
               SnotOptionPane.showMessageDialog(null, "An unexpected condition occured!\n\n"+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }

      updateProjectList();
      setStatusLine(true, "SFC "+project.getName()+ " imported");
      }
  }

  /**
   * The NewSFC function:
   * Opens a new Project and adds it to the current session.
   * This function catches events from the ToolBarButtons and Menu.
   * @param evt the ActionEvent
   * @see #Session.addProject
   */

  private void NewSFCActionPerformed(java.awt.event.ActionEvent evt) {
      Project project = null;
      Editor editor = null;

      // check for valid session
      if (session == null) {
           SnotOptionPane.showMessageDialog(null, "Creating a new SFC failed!\nCannot load a SFC without an active session.\nPlease open or create a new session first!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
           return;
      }

      // launch Editor with new SFC
      project = new Project();
      try {
          editor = new Editor(project.getSFC());
	  editor.setLocation(EditorLocation);
	  //editor.setSize(800,420);
      }
      catch (EditorException editorEx) {
           SnotOptionPane.showMessageDialog(null, editorEx.getMessage(), "Editor error", JOptionPane.ERROR_MESSAGE);
           return;
      }
      catch (Exception ex) {
           SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Editor error", JOptionPane.ERROR_MESSAGE);
           return;
      }

      // set editor and project parameters
      
      editor.addWindowListener(new GuiWindowListener());
      project.setEditor(editor);
      project.setChecked(false);
      project.setOnlyBool(false);

      // add new Project to session
      try {
          session.addProject(project);
      }
      catch (Exception ex) {
           SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           return;
      }
      // set environmental parameters
      updateProjectList();
      activeProject = project;    
      setStatusLine(true, " New SFC created.");
 }



  private void CloseSessionActionPerformed(java.awt.event.ActionEvent evt) {
      if (!prepareForExitSnot(false))
          return;
      closeSession();
      setStatusLine(false, "Session closed");
  }

  private boolean SaveSessionActionPerformed(java.awt.event.ActionEvent evt) {
      if (session.isSaved()) {
          try {
              session.save(null);
	      setStatusLine(true, "Session saved.");
          }
          catch (Exception ex) {
System.out.print("\n"+ex.getClass());
              SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
              return false;
          }
          return true;
      }
      else
          return SaveAsSessionActionPerformed(null);
  }

  private void OpenSessionActionPerformed(java.awt.event.ActionEvent evt) {
      int response;
      boolean state = false;

      // check for active session
      if (session != null) {
          if (session.isModified()) {
              response = SnotOptionPane.showConfirmDialog(null, "There already is an active unsaved session!\n Without saving the changes will be lost!\n\nDo you want to save the changes before opening a new session?\n",
                                            "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          }
          else if (session.isSaved()) {
              response = SnotOptionPane.showConfirmDialog(null, "There already is an active session!\nOpening a new once will close the current session.\n\n Do you wish to proceed?\n",
                                            "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          }
          else {
              response = SnotOptionPane.showConfirmDialog(null, "The active session is not saved!\n Without saving its contents will be lost!\n\nDo you want to save it before opening a new session?\n",
                                            "Alert", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          }

          switch (response) {
              case JOptionPane.YES_OPTION: if(!SaveSessionActionPerformed(null))
                                              return;
                                           break;
              case JOptionPane.NO_OPTION: break;
              case JOptionPane.CANCEL_OPTION:
              case JOptionPane.CLOSED_OPTION: return;
          }


      }

      JFileChooser chooser = new JFileChooser();
      chooser.setFileFilter(new SnotFileFilter("snot","Snot sessions"));
      chooser.setDialogTitle("Open session");

 // set prefered Directory
      if (globalDirectory != null){chooser.setCurrentDirectory(globalDirectory);}
      else if (session != null && session.isSaved())
          chooser.setCurrentDirectory(session.getFile());            


      int result = chooser.showOpenDialog(null);
      if (result == JFileChooser.APPROVE_OPTION) {
          closeSession();
          session = openSession(chooser.getSelectedFile());
	  globalDirectory = chooser.getSelectedFile();
	  enableSession(true);
	  setStatusLine(false, "Session opened");
      }
  }

  private boolean SaveAsSessionActionPerformed(java.awt.event.ActionEvent evt) {
      int response;
      //boolean status = false;
      File file = null;

      JFileChooser chooser = new JFileChooser();
      
      // set prefered Directory
      if (globalDirectory != null){chooser.setCurrentDirectory(globalDirectory);}
      else if (session != null && session.isSaved())
          chooser.setCurrentDirectory(session.getFile());          

      chooser.setFileFilter(new SnotFileFilter(SessionFileExtension,"Snot sessions"));
      chooser.setDialogTitle("Save session as");
      int result = chooser.showDialog(null, "Save as");
      if (result == JFileChooser.APPROVE_OPTION) {
          file = Utilities.createFileDialog(chooser.getSelectedFile(), SessionFileExtension);
          if (file == null)
              return false;
	  globalDirectory = file;
          // save the session
          try {
               session.save(file);
          }
          catch (IOException ex){
ex.printStackTrace();
              SnotOptionPane.showMessageDialog(null, ex.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
              return false;
          }
          catch (Exception ex) {
System.out.print(ex.getClass());
              SnotOptionPane.showMessageDialog(null, "An unexpected condition occured!\n\n"+ex.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
              return false;
          }
          setTitle(TITLE+"  "+ session.getName());
	  setStatusLine(false, "Session saved.");
          return true;
      }
      else
          return false;
  }

  private void AboutActionPerformed(java.awt.event.ActionEvent evt) {
      SnotOptionPane.showMessageDialog(null, "Snot v1.0\nDeveloped: summer term 2001\nat the CAU Kiel.\n\nProject coordination and Parser:\nMartin Steffen, Karsten Stahl.\n\nAuthors:\n Andreas Lukosch, Natalia Freudenberg (Editor)\nKevin Koeser, Tobias Kloss (SMV - Translation)\nCarsten Heine,  Joern Fiebelkorn (Simulator)\nTobias Pugatschov, Dimitri Schultheis (Checks)\nHans Theman, Ingo Schiller (Gui)", "About", JOptionPane.INFORMATION_MESSAGE);
  }


  /**
   * Method to show the ProjektList.
   */
  private void showProjectList(){
    projectFrame = new JFrame("SFC - Browser");
    projectFrame.setResizable(true);
    projectFrame.setSize(250,170);
    projectFrame.setLocation(ProjectListLocation);

    //On close the window will be disposed.
    projectFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e){SFCBrowser.setState(false);
					       ProjectListLocation = projectFrame.getLocation();
					       projectFrame.dispose();}});

    list = new JList(session.getNamesList());
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(0);

    // A mouselistener to determine the clicked Project and to show it.
    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
           if (e.getClickCount() == 2) {
               int index = list.locationToIndex(e.getPoint());
    	       Vector p = session.getProjectList();
	       try {
                    activeProject = (Project)p.elementAt(index);
		    setStatusLine(true);
		    createEditor(activeProject);

	       }catch(Exception ex){/*System.out.println("Daneben!!");*/}
                                                     // hihi! Daneben!! so, so ...
	   }
           if (e.getClickCount() == 1) {
	       int index2 = list.locationToIndex(e.getPoint());
               Vector p = session.getProjectList();
	       try {
                    activeProject = (Project)p.elementAt(index2);
		    setStatusLine(true);
		
	       }catch(Exception ex){/*System.out.println("Daneben!!");*/}
                                                     // hihi! Daneben!! so, so ...
	      
	   }
       }
    };

    list.addMouseListener(mouseListener);
    scrollpane = new JScrollPane(list);

    Container pane = projectFrame.getContentPane();
    pane.add(scrollpane);

    projectFrame.show();
    }


    private void setStatusLine(boolean state){
	Project project = activeProject;
	if(state == true && project != null)
	    Status.setText("   "+project.getName() +" is active");
	else
	    Status.setText("   No active project");
    }


    private void setStatusLine(boolean state, String msg){
	Project project = activeProject;
	if(state == true && project != null)
	    Status.setText("   "+project.getName() +" is active. "+ msg);
	else Status.setText("   No active project. "+ msg);
    }


    private void updateProjectList(){
      if(SFCBrowser.isSelected()) {
	ProjectListLocation = projectFrame.getLocation();
	projectFrame.dispose();
	showProjectList();
      }
    }


	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {
            prepareForExitSnot(true);
	}

/*******************************************************************************
 *
 *      Some selfmade utilities
 *
 *******************************************************************************/


    //en-disable the Files MenuBar.
    private void enableFilesToolBar(boolean state){
      ButtonSaveSession.setEnabled(state);
      ButtonSaveAsSession.setEnabled(state);
      ButtonCloseSession.setEnabled(state);
      ButtonImportSFC.setEnabled(state);
      ButtonExportSFC.setEnabled(state);
    }


    private void enableSession(boolean state) {
        // en-disable menus
        SaveSession.setEnabled(state);
        SaveAsSession.setEnabled(state);
        CloseSession.setEnabled(state);

        Edit.setEnabled(state);
        ToolsMenu.setEnabled(state);
        View.setEnabled(state);

        // en-disable buttons
        ButtonEditor.setEnabled(state);
        ButtonCheckSFC.setEnabled(state);
        ButtonSimulator.setEnabled(state);
        ButtonSMV.setEnabled(state);
	ButtonParser.setEnabled(state);
	ButtonPrettyPrinter.setEnabled(state);
	SFCBrowser.setState(state);
	if(state)
	    showProjectList();
	
    }

    private void closeSession() {
        if (session == null)
            return; // just in case ...

        // dispose editorframes
        session.disposeEditors();

	//Terminates the ProjectListFrame if active
        if(SFCBrowser.isSelected()){
                SFCBrowser.setState(false);
                projectFrame.dispose();}

        enableSession(false);
	enableFilesToolBar(false);
        session = null;
	activeProject = null;
        this.setTitle(TITLE);
    }

    private Session openSession(File file) {
        Session newSession = null;
        Vector v = null;

        try {
            Utilities.validateFile(file, SessionFileExtension);
        }
        catch (Exception ex) {
            String msg = (ex.getMessage()+"\n\nError type:\n"+ex.getClass().getName());
            SnotOptionPane.showMessageDialog(null, msg, "File error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            newSession = Session.read(file);
            v = newSession.getProjectList();
            
//////////////////////////
/*for (int i=0; i<v.size(); i++) {
                if(((Project)v.elementAt(i)).hasEditor())
		(createEditor((Project)v.elementAt(i)));}*/
	
        }
        catch (IOException ex) {
            SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Read error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        catch (Exception ex) {
            String msg = (ex.getMessage()+"\n\nError type:\n"+ex.getClass().getName());
            SnotOptionPane.showMessageDialog(null, msg, "Read error", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
            return null;
        }
        setTitle(TITLE+"  "+newSession.getName());
       	enableFilesToolBar(true);
	return newSession;
    }


    /** Prepares the program for exit.
     *  Checks the session states and i.g. forces to save before exit.
     *  @param exit Defines whether to exit or just to close the session without exiting Snot.
     */
    private boolean prepareForExitSnot(boolean exit) {
        int result = JOptionPane.CANCEL_OPTION;

        // check for active session: if no one found, just exit.
        if (session == null)
            return exitSnot(true);

        // session found: check its status
        if (session.isModified())
            result = SnotOptionPane.showConfirmDialog(null, "The session has changed!\nWithout saving the changes will be lost!\n\nDo you want to save it?",
                                            "Alert: session is not saved!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        else if (!session.isSaved())
            result = SnotOptionPane.showConfirmDialog(null, "The session is not saved!\nIf exiting its contents will be lost.\n\nDo you want to save it now?",
                                            "Alert: session will be lost!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        else if (exit) {
            return exitSnot(true);
        }
        else
             return true; // simulates a successful exit state (needed for close Session)

        switch (result) {
            case JOptionPane.YES_OPTION: return(SaveSessionActionPerformed(null));
            case JOptionPane.NO_OPTION: if (exit) return exitSnot(false); else return true;
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION: return false; // do not exit!
        }

        // this line should never be reached but just in case ...
        return false;
    }


    private boolean exitSnot(boolean remind) {
        int result;
        // function must terminate Snot!
        // collect all opened Frames/Windows and close em all!!!

        if (remind) {
            result = SnotOptionPane.showConfirmDialog(null, "You are about to leave Snot!\nDo you wish to exit?",
                                            "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result != JOptionPane.YES_OPTION)
                return false;
        }
        closeSession();
        System.exit(0);
        return true; // weil sonst der Compiler jammert ...
    }




	/**
         * The main function:
	 * @param args the command line arguments
	 */
	public static void main (String args[]) {
            new Gui (null).show ();
	}

/*******************************************************************************
 *
 *      The subclass GuiWindowListener
 *
 *******************************************************************************/

    class GuiWindowListener implements WindowListener {

        public void windowActivated(java.awt.event.WindowEvent evt) {
            // set the active Project
            activeProject = session.getProject((Object)((Editor)evt.getSource()).getSFC());
	    setStatusLine(true);
//            System.out.print("\n Window "+activeProject+" Activated");
        }

        public void windowDeactivated(java.awt.event.WindowEvent evt) {
            if (activeProject != null && !session.isModified())
                session.setModified(activeProject.isModified());
//            System.out.print("\n Window "+activeProject+" Deactivated and session modified is "+session.isModified());
        }

        public void windowClosed(java.awt.event.WindowEvent evt) {
//            System.out.print("\n Window "+activeProject+" Closed");
        }

	public void windowDeiconified(java.awt.event.WindowEvent evt) {
//            System.out.print("\n Window "+activeProject+" Deiconified");
        }

        public void windowOpened(java.awt.event.WindowEvent evt) {

//            System.out.print("\n Window "+activeProject+" Opened");
        }

        public void windowIconified(java.awt.event.WindowEvent evt) {
//            System.out.print("\n Window "+activeProject+" Iconified");
        }

        public void windowClosing(java.awt.event.WindowEvent evt) {
//          System.out.print("\n Window "+activeProject+" Closing");
            activeProject = null;
	    setStatusLine(true);
        }
    }



 /*******************************************************************************
 *
 *      The subclass GuiUtilities
 *
 *******************************************************************************/

    class GuiUtilities {

        public File createFileDialog(File _file, String extension) {
            int response;
            boolean status = false;

            try {
                // check file extension
                if (!_file.getName().endsWith(extension))
                    _file = new File(_file.getPath().concat("."+extension));   // throws Exception
                // create file
                status = Utilities.createFile(_file); // throws a lot ...
            }
            catch (Exception ex) {
System.out.print(ex.getClass());
                SnotOptionPane.showMessageDialog(null, ex.getMessage(), "Create file error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            // file already exists?
            if (!status) {
                response = SnotOptionPane.showConfirmDialog(null, "File \""+_file.getName()+"\" already exists!\n Overwrite it?",
                                                            "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                switch (response) {
                    case JOptionPane.NO_OPTION:
                    case JOptionPane.CANCEL_OPTION:
                    case JOptionPane.CLOSED_OPTION: return null;
                    case JOptionPane.YES_OPTION: break;
                }
            }
            return _file;
        }

        public void validateFile(File file, String extension) throws Exception {
            boolean status = false;
            int index;

            if (file == null)
                throw new Exception("NullPointer in function validateFile()!");
            try {
                status = file.exists();
            }
            catch (SecurityException ioEx) {
                throw new Exception(ioEx.getMessage());
            }
            if (!status)
                throw new Exception("File \""+file.getName()+"\" does not exist!");

            // compare file extension
            if (!file.getName().endsWith(extension)) {
                throw new Exception("File \""+file.getName()+"\" is not a \"*."+extension+"\" file!");
            }
        }

        public boolean createFile(File file) throws Exception {
            boolean status = false;

            if (file == null)
                throw new Exception("NullPointer!");

            try {
                status = file.createNewFile();
            }
            catch (IOException ioEx) {
                throw new Exception(ioEx.getMessage());
            }
            catch (SecurityException sEx) {
                throw new Exception(sEx.getMessage());
            }
            return status;
        }
    }

   //For the ToolBarFiles
    private javax.swing.JButton ButtonOpenSession;
    private javax.swing.JButton ButtonNewSession;
    private javax.swing.JButton ButtonSaveSession;
    private javax.swing.JButton ButtonSaveAsSession;
    private javax.swing.JButton ButtonCloseSession;
    private javax.swing.JButton ButtonExportSFC;
    private javax.swing.JButton ButtonImportSFC;
    private javax.swing.JToolBar ToolBarFiles;
    private javax.swing.JCheckBoxMenuItem ShowFilesToolBar;

    private javax.swing.JScrollPane scrollpane;
    private javax.swing.JScrollPane helpscrollpane;
    private javax.swing.JList list;
    private javax.swing.JFrame projectFrame;
    private javax.swing.JFrame helpFrame;
    private javax.swing.JTextArea textarea;

    // Variables declaration - do not modify
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenuItem OpenSession;
    private javax.swing.JMenuItem NewSession;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JMenuItem SaveSession;
    private javax.swing.JMenuItem SaveAsSession;
    private javax.swing.JMenuItem CloseSession;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem ExitSnot;
    private javax.swing.JMenu Edit;
    private javax.swing.JMenuItem NewSFC;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JMenuItem ImportSFC;
    private javax.swing.JMenuItem ExportSFC;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JMenuItem RenameSFC;
    private javax.swing.JMenuItem RemoveSFC;
    private javax.swing.JMenuItem ImportExample1;
    private javax.swing.JMenu ToolsMenu;
    private javax.swing.JMenuItem Editor;
    private javax.swing.JMenuItem CheckSFC;
    private javax.swing.JMenuItem Simulator;
    private javax.swing.JMenuItem SMV;
    private javax.swing.JMenuItem Parser;
    private javax.swing.JMenuItem PrettyPrinter;
    private javax.swing.JMenu View;
    private javax.swing.JCheckBoxMenuItem SFCBrowser;
    private javax.swing.JCheckBoxMenuItem ShowToolBar;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JMenuItem About;
    private javax.swing.JMenuItem Help;
    private javax.swing.JToolBar ToolBarTools;
    private javax.swing.JButton ButtonEditor;
    private javax.swing.JButton ButtonCheckSFC;
    private javax.swing.JButton ButtonSimulator;
    private javax.swing.JButton ButtonSMV;

    private javax.swing.JButton ButtonParser;
    private javax.swing.JButton ButtonPrettyPrinter;

    private javax.swing.JPanel PanelStatus;
    private javax.swing.JLabel Status;

    private javax.swing.ImageIcon nsicon;
    private javax.swing.ImageIcon osicon;
    private javax.swing.ImageIcon ssaicon;
    private javax.swing.ImageIcon ssicon;
    private javax.swing.ImageIcon csicon;
    private javax.swing.ImageIcon isfcicon;
    private javax.swing.ImageIcon esfcicon;
    // End of variables declaration

}
