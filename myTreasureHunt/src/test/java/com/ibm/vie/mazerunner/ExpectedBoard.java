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

package com.ibm.vie.mazerunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import com.ibm.vie.mazerunner.squares.Space;
import com.ibm.vie.mazerunner.squares.Treasure;
import com.ibm.vie.mazerunner.squares.Trees;
import com.ibm.vie.mazerunner.squares.Wall;

public class ExpectedBoard {
  private final Space[][] expectedBoard;
  private final int obtainedTreasures;
  private final int maxSteps;
  private final int remainingSteps;
  private final Location playerPos;
  private final List<Location> remainingTreasures;

  public ExpectedBoard(final String[][] board, final Location player,
      final List<Location> remainingTreasures, final int obtainedTreasures, final int maxSteps,
      final int remainingSteps) {

    this.obtainedTreasures = obtainedTreasures;
    this.maxSteps = maxSteps;
    this.remainingSteps = remainingSteps;
    this.playerPos = player;
    this.remainingTreasures = remainingTreasures;

    expectedBoard = new Space[board.length][];
    for (int j = 0; j < board.length; ++j) {
      expectedBoard[j] = new Space[board[j].length];
      for (int i = 0; i < board[j].length; ++i) {
        switch (board[j][i]) {
          case "W":
            expectedBoard[j][i] = new Wall(j, i);
            break;
          case "t":
            expectedBoard[j][i] = new Trees(j, i);
            break;
          case "P": // P is just to visualize the starting square.
            // The expected current position is passed in. If it is there,
            // it has to match the expected player position
          case " ":
            expectedBoard[j][i] = new Space(j, i);
            break;
          case "T":
            expectedBoard[j][i] = new Treasure(j, i);
            break;
          default:
            throw new RuntimeException("Test framework does not recoginize " + board[j][i]);
        }
      }
    }
  }

  public MapBoard asBoard() {
    String[][] boardCopy = new String[expectedBoard.length][];
    for (int row = 0; row < expectedBoard.length; row++) {
      boardCopy[row] = new String[expectedBoard[row].length];
      for (int col = 0; col < expectedBoard[row].length; col++) {
        if (expectedBoard[row][col] instanceof Wall) {
          boardCopy[row][col] = "W";
        } else if (expectedBoard[row][col] instanceof Trees) {
          boardCopy[row][col] = "t";
        } else if (expectedBoard[row][col] instanceof Treasure) {
          boardCopy[row][col] = "T";
        } else if (expectedBoard[row][col] instanceof Space) {
          boardCopy[row][col] = " ";
        }
      }
    }

    boardCopy[playerPos.getRow()][playerPos.getCol()] = "P";

    String boardCsv = Arrays.stream(boardCopy)//
        .map(row -> Arrays.stream(row)//
            .map(code -> "\"" + code + "\"") //
            .collect(Collectors.joining(","))) //
        .collect(Collectors.joining("\n")) + "\n" + maxSteps;

    System.out.println(boardCsv);
    try {
      System.out.println(boardCsv);
      File csv_file = File.createTempFile("test", ".csv");
      try (FileOutputStream fos = new FileOutputStream(csv_file)) {
        fos.write(boardCsv.getBytes());
      }
      try {
        return MapBoard.parseBoard(csv_file.getAbsolutePath());

      } finally {
        csv_file.delete();
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public void assertBoardIsAsExpected(IBoard other) {
    Assert.assertEquals(expectedBoard.length, other.getHeight());
    Assert.assertEquals(expectedBoard[0].length, other.getWidth());

    // correct position
    Location otherPlayerLocation = other.getPlayerLocation();
    Assert.assertEquals(otherPlayerLocation, playerPos);

    // correct initial steps and remaining steps
    Assert.assertEquals(maxSteps, other.getMaxSteps());
    Assert.assertEquals(remainingSteps, other.getRemainingSteps());

    // treasures
    Assert.assertEquals(this.obtainedTreasures, other.getObtainedTreasureCount());
    Assert.assertEquals(remainingTreasures.size(), other.getRemainingTreasureCount());

    for (Location expectedTreasure : remainingTreasures) {
      Assert.assertTrue(
          "The expected treasure at " + expectedTreasure + " was not returned in the treasure list",
          other.getTreasures().stream() //
              .anyMatch(treasure -> //
              (treasure.getLocation().equals(expectedTreasure))) //
      );
    }

    // board squares are correct, treasures are in treasure list
    for (int row = 0; row < other.getHeight(); row++) {
      for (int col = 0; col < other.getWidth(); col++) {
        Assert.assertEquals("Square at " + row + "," + col + " is not correct",
            expectedBoard[row][col].getClass(),
            other.getSquareAt(new Location(row, col)).getClass());
      }
    }
  }
}
