/**
 * (C) Copyright IBM Corp. 2016,2022. All Rights Reserved. US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import javax.swing.*;
import java.awt.Container;
import com.ibm.vie.mazerunner.*;
import com.ibm.vie.mazerunner.gui.GameData;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import javax.swing.SwingUtilities;
import java.awt.event.*;
import java.io.File;

public class Main {
  private static String mapFile;
  private static BoardPanel boardPanel;
  private static JFrame frame;
  private static Thread thread;
  
  public static void main(String[] args) {
    GameData data = GameData.getInstance();
   try {
    data.loadSprites("TreasureHunt.png",  "TreasureHunt.xml");
   } catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Failed to load sprite sheet", "IO Error",
        JOptionPane.ERROR_MESSAGE);
    e.printStackTrace();
    }
    boardPanel = new BoardPanel();
    createGuiAndShow();
    loadBoardAndPlay(MapBoard.dummyBoard("Dummy", 30, 40, 30*40), new SimplePlayer());
    frame.pack();
    frame.setVisible(true);      
  }
  private static void loadBoardAndPlay(final IBoard board, final IPlayer player) {
    try {
      if (thread != null) {
        thread.stop();
      }
    }catch (Exception e) {
    }
    
    try {
      boardPanel.loadBoard(board);
      final IBoard boardView = new MapBoardView(board);
      thread = new Thread(new Runnable() {
        public void run(){
          int moveCount = 0;
          try {
            while (!board.isComplete()) {
              GameData.currentLocation.setLocation(board.getPlayerLocation());
              board.move(player.selectMove(boardView));
              GameData.previousLocation.setLocation(board.getPlayerLocation());
              ++moveCount;
              update();
              Thread.sleep(50);
            }
            if (board.getTreasures().size() == 0) {
              System.out.println("Congratulations! You found the treasure in " + moveCount + " moves!");
            } else {
              System.out.println("You ran out of turns and collected " + board.getObtainedTreasureCount() + " treasures");
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        private void update() throws Exception {
            SwingUtilities.invokeAndWait(new Runnable() {
              public void run() {
                boardPanel.repaint();
              }
            });
        }
      });
      thread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private static void menuItemOpenAction() {
    final JFileChooser fc = new JFileChooser(new File("./maps"));
    int returnVal = fc.showOpenDialog(frame);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      try {
        loadBoardAndPlay(MapBoard.parseBoard(file.toString()), new SimplePlayer());
      } catch (Exception e) {
      }
      frame.pack();
      frame.setVisible(true);      
    } else {
    }
  }
  private static void createGuiAndShow() {
    
    JMenuBar menuBar = new JMenuBar();
    
    JMenu menuFile = new JMenu("File");
    menuFile.setMnemonic(KeyEvent.VK_F);
    JMenuItem menuItemOpen = new JMenuItem("Open");
    menuItemOpen.setMnemonic(KeyEvent.VK_O);
    menuItemOpen.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent e) {
        menuItemOpenAction();
      }
    });
    menuFile.add(menuItemOpen);
    JMenuItem menuItemExit = new JMenuItem("Exit");
    menuFile.add(menuItemExit);
    menuItemOpen.setMnemonic(KeyEvent.VK_X);
    menuBar.add(menuFile);
    
    JMenu menuHelp = new JMenu("Help");
    menuHelp.setMnemonic(KeyEvent.VK_H);
    JMenuItem menuItemAbout = new JMenuItem("About Mazerunner");
    menuHelp.add(menuItemAbout);
    menuBar.add(menuHelp);
    
    frame = new JFrame("MazeRunner");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setJMenuBar(menuBar);
    
    Container c = frame.getContentPane();
    c.add(boardPanel);
  }
  
}
