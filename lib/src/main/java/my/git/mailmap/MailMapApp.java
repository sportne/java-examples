package my.git.mailmap;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultTreeModel;

import my.git.mailmap.gui.MailMapAuthorTreeRoot;
import my.git.mailmap.gui.SortedListModel;

public class MailMapApp extends JFrame {

   private static final long serialVersionUID = -6112098575191717560L;

   public static void main(String[] args) {
      new MailMapApp();
   }

   private final Set<Author> unassignedAuthorsSet;
   private final JList<Author> unassignedAuthorsList;
   private final Set<Author> primaryAuthorSet;
   private final JTree mailmapTree;

   private JMenuBar menuBar;
   private JMenu fileMenu;
   private JMenuItem openMenuItem;
   private JMenuItem loadGitRepoItem;
   private JMenuItem saveMenuItem;

   private JButton addPrimaryAuthorButton;
   private JButton addAlternativeAuthorButton;
   private JButton removeEntryButton;
   private JButton clearButton;

   public MailMapApp() {
      // Create JMenuBar - Initializes a new JMenuBar, JMenu, and JMenuItems for file
      // options
      menuBar = new JMenuBar();
      fileMenu = new JMenu("File");
      openMenuItem = new JMenuItem("Load mailmap");
      loadGitRepoItem = new JMenuItem("Load git repo");
      saveMenuItem = new JMenuItem("Save mailmap");

      // Create JLists - Initializes a Comparator for sorting authors, sets for
      // unassigned and primary authors, and a JList for displaying the unassigned
      // authors
      Comparator<Author> authorComparator = (o1, o2) -> o1.toString()
            .compareToIgnoreCase(o2.toString());
      unassignedAuthorsSet = new HashSet<>();
      unassignedAuthorsList = new JList<>(new SortedListModel<>(authorComparator));

      primaryAuthorSet = new HashSet<>();

      // Initializes a root node for the mailmap tree and a DefaultTreeModel to be
      // used by the JTree
      MailMapAuthorTreeRoot root = new MailMapAuthorTreeRoot();
      DefaultTreeModel treeModel = new DefaultTreeModel(root);
      mailmapTree = new JTree(treeModel);

      // Create JButtons - Initializes buttons for adding primary and alternative
      // authors and removing entries
      addPrimaryAuthorButton = new JButton("NEW >>");
      addAlternativeAuthorButton = new JButton("ALT >>");
      removeEntryButton = new JButton("<<");
      clearButton = new JButton("CLEAR");

      // Method call to set up the layout of the components
      setupLayout();

      // Method call to set up the control and functionality of the components
      setupControl();

      // Sets the size of the JFrame to fit all components
      pack();

      // Makes the JFrame visible
      setVisible(true);
   }

   /**
    * Returns an ActionListener that is responsible for adding an alternative
    * author name to a primary author. The selected author from
    * unassignedAuthorsList will be added as an alternative name to the selected
    * primary author. If no primary author is selected, a message dialog will be
    * displayed.
    *
    * @return An ActionListener that adds an alternative author name to a primary
    *         author.
    */
   private ActionListener getAlternativeButtonActionListener() {
      return new AddPseudodym(unassignedAuthorsList, unassignedAuthorsSet, getRoot(), mailmapTree,
            getTreeModel(), addAlternativeAuthorButton);
   }

   private ActionListener getClearButtonActionListener() {
      return new ClearData(unassignedAuthorsList, unassignedAuthorsSet, getRoot(), getTreeModel(),
            primaryAuthorSet);
   }

   /**
    * Returns an ActionListener that when triggered, opens a JFileChooser to select
    * a directory containing a git repository. The authors in the repository are
    * read by the GitRepoUtils.getAuthors() method and added to the unassigned
    * authors list.
    *
    * @return an ActionListener that when triggered, opens a JFileChooser to select
    *         a directory containing a git repository.
    */
   private ActionListener getMailMapGitRepoActionListener() {
      return new LoadGitRepoAuthors(unassignedAuthorsList, unassignedAuthorsSet, primaryAuthorSet,
            this);
   }

   /**
    * This method creates and returns an ActionListener that will load a mailmap
    * file and populate the primaryAuthorsList and unassignedAuthorsList with the
    * primary and alternative authors, respectively.
    *
    * @return an ActionListener for loading mailmap files.
    */
   private ActionListener getMailMapLoaderActionListener() {
      return new LoadMailMap(unassignedAuthorsList, unassignedAuthorsSet, getRoot(), mailmapTree,
            getTreeModel(), primaryAuthorSet, this);
   }

   /**
    * Returns an ActionListener that saves the current state of the JLists in the
    * application to a file chosen by the user.
    *
    * @return an ActionListener that saves the current state of the JLists in the
    *         application to a file chosen by the user.
    */
   private ActionListener getMailMapWriterActionListener() {
      return new WriteMailMap(this, getRoot());
   }

   /**
    * Returns an ActionListener for the primary author button. When the button is
    * pressed, the selected author from the unassignedAuthorsList will be made a
    * primary author and added to the mailmapTree.
    *
    * @return ActionListener for the primary author button
    */
   private ActionListener getPrimaryAuthorButtonActionListener() {
      return new AddPrimaryAuthor(unassignedAuthorsList, unassignedAuthorsSet, getRoot(),
            mailmapTree, getTreeModel(), primaryAuthorSet, this);
   }

   /**
    * Returns an ActionListener for the remove entry button. When the button is
    * pressed, the selected node from the mailmapTree will be removed and its
    * author(s) will be added to the unassignedAuthorsList.
    *
    * @return ActionListener for the remove entry button
    */
   private ActionListener getRemoveEntryButtonActionListener() {
      return new RemoveAuthor(unassignedAuthorsList, mailmapTree, getTreeModel(), primaryAuthorSet);
   }

   /**
    * Returns the root of the MailMapAuthorTree.
    *
    * @return the root of the MailMapAuthorTree
    */
   private MailMapAuthorTreeRoot getRoot() {
      return (MailMapAuthorTreeRoot) getTreeModel().getRoot();
   }

   /**
    * Returns the TreeModel of the mailmapTree.
    *
    * @return the TreeModel of the mailmapTree as a DefaultTreeModel
    */
   private DefaultTreeModel getTreeModel() {
      // cast the returned TreeModel to a DefaultTreeModel
      return (DefaultTreeModel) mailmapTree.getModel();
   }

   /**
    * Sets up the control for the mailmap application, including adding
    * ActionListeners to JMenuItems and JButtons, and setting the transfer handler
    * for the mailmap tree.
    */
   private void setupControl() {
      // Add ActionListeners to JMenuItems
      openMenuItem.addActionListener(getMailMapLoaderActionListener());
      loadGitRepoItem.addActionListener(getMailMapGitRepoActionListener());
      saveMenuItem.addActionListener(getMailMapWriterActionListener());

      // Add ActionListeners to JButtons
      addPrimaryAuthorButton.addActionListener(getPrimaryAuthorButtonActionListener());
      addAlternativeAuthorButton.addActionListener(getAlternativeButtonActionListener());
      removeEntryButton.addActionListener(getRemoveEntryButtonActionListener());
      clearButton.addActionListener(getClearButtonActionListener());
   }

   private void setupLayout() {
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setTitle("MailMap");

      // Add JMenuBar to JFrame
      setJMenuBar(menuBar);
      menuBar.add(fileMenu);
      fileMenu.add(openMenuItem);
      fileMenu.add(loadGitRepoItem);
      fileMenu.add(saveMenuItem);

      // Create JPanel for JLists and JButtons
      JPanel listPanel = new JPanel();
      listPanel.setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();

      // Add unassignedAuthorsList to listPanel
      c.fill = GridBagConstraints.NONE;
      c.weightx = 0;
      c.weighty = 0;
      c.gridx = 0;
      c.gridy = 0;
      listPanel.add(new JLabel("Unassigned Authors"), c);
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      c.gridx = 0;
      c.gridy = 1;
      listPanel.add(new JScrollPane(unassignedAuthorsList), c);
      listPanel.setPreferredSize(new Dimension(200, 300));

      // create panel for buttons
      JPanel btnPanel = new JPanel();
      btnPanel.setLayout(new GridBagLayout());
      c.fill = GridBagConstraints.VERTICAL;
      c.weightx = 0;
      c.weighty = 0;
      c.gridx = 0;
      c.gridy = 0;
      btnPanel.add(Box.createVerticalGlue(), c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 1;
      btnPanel.add(addPrimaryAuthorButton, c);
      c.gridx = 0;
      c.gridy = 2;
      btnPanel.add(Box.createVerticalStrut(10), c);
      c.gridx = 0;
      c.gridy = 3;
      btnPanel.add(addAlternativeAuthorButton, c);
      c.gridx = 0;
      c.gridy = 4;
      btnPanel.add(Box.createVerticalStrut(10), c);
      c.gridx = 0;
      c.gridy = 5;
      btnPanel.add(removeEntryButton, c);
      c.fill = GridBagConstraints.VERTICAL;
      c.gridx = 0;
      c.gridy = 6;
      btnPanel.add(Box.createVerticalStrut(10), c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 7;
      btnPanel.add(clearButton, c);
      Insets margin = new Insets(1, 1, 1, 1); // top, left, bottom, right
      addPrimaryAuthorButton.setMargin(margin);
      addAlternativeAuthorButton.setMargin(margin);
      removeEntryButton.setMargin(margin);
      clearButton.setMargin(margin);

      // Create JPanel for JTree
      JPanel treePanel = new JPanel();
      treePanel.setLayout(new GridBagLayout());

      // Add mailmapTree to treePanel
      c.fill = GridBagConstraints.NONE;
      c.weightx = 0;
      c.weighty = 0;
      c.gridx = 0;
      c.gridy = 0;
      treePanel.add(new JLabel("Assigned Authors"), c);
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      c.gridx = 0;
      c.gridy = 1;
      treePanel.add(new JScrollPane(mailmapTree), c);
      treePanel.setPreferredSize(new Dimension(200, 300));

      mailmapTree.setRootVisible(false);
      mailmapTree.setShowsRootHandles(true);

      // Add listPanel and treePanel to JFrame using GridBagLayout
      setLayout(new GridBagLayout());

      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      c.gridx = 0;
      c.gridy = 0;
      add(listPanel, c);
      c.fill = GridBagConstraints.NONE;
      c.weightx = 0;
      c.weighty = 0;
      c.gridx = 1;
      c.gridy = 0;
      add(btnPanel, c);
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      c.gridx = 2;
      c.gridy = 0;
      add(treePanel, c);
   }
}
