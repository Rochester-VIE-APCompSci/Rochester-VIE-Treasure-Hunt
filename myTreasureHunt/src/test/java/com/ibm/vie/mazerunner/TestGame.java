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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import com.ibm.vie.mazerunner.util.TestHarness;
import com.ibm.vie.mazerunner.util.TestResult;



class TestGame {


  Location rowCol(int row, int col) {
    return new Location(row, col);
  }

  private final int TREE_COST = 37;
  private final int WALL_COST = Integer.MAX_VALUE;
  private final int SPACE_COST = 1;

  private static int getMoveCost(IBoard board, Move move) {
    return board.getSquareAt(move.apply(board.getPlayerLocation())).getStepCost();
  }

  private static Set<String> getUnexploredMoves(IBoard board) {
    return board.getUnexploredMoves().stream().map(mv -> mv.toString()).collect(Collectors.toSet());
  }

  private static Set<String> expectedMoves(String... moves) {
    return new HashSet<>(Arrays.asList(moves));
  }

  public static TestResult runBoard(final String jar, final IPlayer player, final MapBoard board) {
    TestResult result = TestHarness.runBoard(jar, player, board);

    if (result.getException() != null) {
      if (result.getException() instanceof AssertionError) {
        throw (AssertionError) result.getException();
      }
      result.getException().printStackTrace();
    }

    return result;
  }

  @Test
  void when_single_move_to_treasure_then_game_ends() {
    final String[][] test10x10Board = {//
        // 0...1....2....3....4....5....6....7....8....9
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}, // 0
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 1
        {"W", " ", " ", " ", " ", "t", "t", "t", "t", "W"}, // 2
        {"W", " ", " ", " ", " ", "t", " ", " ", "t", "W"}, // 3
        {"W", " ", " ", " ", " ", "t", "t", " ", "t", "W"}, // 4
        {"W", " ", " ", " ", " ", "t", "t", " ", "t", "W"}, // 5
        {"W", " ", " ", " ", " ", "t", " ", " ", "t", "W"}, // 6
        {"W", " ", " ", " ", " ", "t", "t", " ", "t", "W"}, // 7
        {"W", " ", " ", " ", "t", " ", " ", "T", "t", "W"}, // 8
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"} // 9
    };


    List<AssertableTurn> turns = new LinkedList<>(Arrays.asList(//
        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(8, 6), // cur pos
                Collections.singletonList(rowCol(8, 7)), // treasures
                0, // obtained treasures
                1000, // max steps
                1000), // remaining steps
            board -> {
              Assert.assertEquals(expectedMoves("NORTH", "SOUTH", "EAST", "WEST"),
                  getUnexploredMoves(board));
              Assert.assertEquals(WALL_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.WEST));
            }, Move.EAST //
        ) //
    ));


    IPlayer player = new TestPlayerFixedMoves(turns, "when_single_move_to_treasure_then_game_ends");
    MapBoard board = turns.get(0).getStartState().asBoard();
    TestResult result = runBoard("non-jar", player, board);

    Assert.assertNull(result.getException());
    Assert.assertTrue(result.getScore() > 0);
  }

  @Test
  void when_multi_move_to_treasure_then_game_ends() {
    final String[][] test10x10Board = {//
        // 0...1....2....3....4....5....6....7....8....9
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}, // 0
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 1
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 2
        {"W", " ", " ", " ", " ", "t", " ", " ", " ", "W"}, // 3
        {"W", " ", " ", " ", " ", "T", "t", " ", " ", "W"}, // 4
        {"W", " ", " ", " ", "t", "P", "t", " ", " ", "W"}, // 5
        {"W", " ", " ", "t", " ", " ", "t", " ", " ", "W"}, // 6
        {"W", " ", " ", " ", "t", " ", "t", " ", " ", "W"}, // 7
        {"W", " ", " ", " ", "t", "t", "t", " ", " ", "W"}, // 8
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"} // 9
    };


    List<AssertableTurn> turns = new LinkedList<>(Arrays.asList(//
        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(5, 5), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                1000), // remaining steps
            board -> {
              Assert.assertEquals(expectedMoves("NORTH", "SOUTH", "EAST", "WEST"),
                  getUnexploredMoves(board));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.WEST));
            }, Move.SOUTH), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(6, 5), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                999 // remaining steps
            ), //
            board -> {
              Assert.assertEquals(expectedMoves("SOUTH", "EAST", "WEST"),
                  getUnexploredMoves(board));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.WEST));
            }, Move.SOUTH),

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(7, 5), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                998 // remaining steps
            ), //
            board -> {
              Assert.assertEquals(expectedMoves("SOUTH", "EAST", "WEST"),
                  getUnexploredMoves(board));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.WEST));
            }, Move.BACKTRACK), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(6, 5), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                997 // remaining steps
            ), //
            board -> {
              Assert.assertEquals(expectedMoves("EAST", "WEST"), getUnexploredMoves(board));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.WEST));
            }, Move.WEST), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(6, 4), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                996 // remaining steps
            ), //
            board -> {
              Assert.assertEquals(expectedMoves("NORTH", "WEST", "SOUTH"),
                  getUnexploredMoves(board));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.WEST));

            }, Move.BACKTRACK), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(6, 5), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                995 // remaining steps
            ), //
            board -> {
              Assert.assertEquals(expectedMoves("EAST"), getUnexploredMoves(board));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.WEST));
            }, Move.BACKTRACK), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(5, 5), // cur pos
                Collections.singletonList(rowCol(4, 5)), // treasures
                0, // obtained treasures
                1000, // max steps
                994 // remaining steps
            ), //
            board -> {
              Assert.assertEquals(expectedMoves("NORTH", "EAST", "WEST"),
                  getUnexploredMoves(board));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.SOUTH));
              Assert.assertEquals(SPACE_COST, getMoveCost(board, Move.NORTH));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.EAST));
              Assert.assertEquals(TREE_COST, getMoveCost(board, Move.WEST));

            }, Move.NORTH) //


    ));


    IPlayer player = new TestPlayerFixedMoves(turns, "when_backtrack_then_path_is_correct");
    MapBoard board = turns.get(0).getStartState().asBoard();
    TestResult result = runBoard("non-jar", player, board);

    Assert.assertNull(result.getException());
    Assert.assertTrue(result.getScore() > 0);
  }

  @Test
  void when_one_of_two_treasures_then_points() {
    final String[][] test10x10Board = {//
        // 0...1....2....3....4....5....6....7....8....9
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}, // 0
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 1
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 2
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 3
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 4
        {"W", " ", " ", " ", " ", " ", "T", " ", " ", "W"}, // 5
        {"W", " ", " ", " ", " ", " ", "T", " ", " ", "W"}, // 6
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 7
        {"W", " ", " ", " ", " ", " ", "P", " ", " ", "W"}, // 8
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"} // 9
    };


    List<AssertableTurn> turns = new LinkedList<>(Arrays.asList(//
        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(8, 6), // cur pos
                Arrays.asList(rowCol(5, 6), rowCol(6, 6)), // treasures
                0, // obtained treasures
                4, // max steps
                4), // remaining steps
            board -> {
            }, //
            Move.NORTH //
        ), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(7, 6), // cur pos
                Arrays.asList(rowCol(5, 6), rowCol(6, 6)), // treasures
                0, // obtained treasures
                4, // max steps
                3), // remaining steps
            board -> {
            }, //
            Move.NORTH //
        ), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(6, 6), // cur pos
                Arrays.asList(rowCol(5, 6)), // treasures
                1, // obtained treasures
                4, // max steps
                2), // remaining steps
            board -> {
            }, //
            Move.EAST //
        ), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(6, 7), // cur pos
                Arrays.asList(rowCol(5, 6)), // treasures
                1, // obtained treasures
                4, // max steps
                1), // remaining steps
            board -> {
            }, //
            Move.EAST //
        ) //
    ));


    IPlayer player = new TestPlayerFixedMoves(turns, "when_one_of_two_treasures_then_points");
    MapBoard board = turns.get(0).getStartState().asBoard();
    TestResult result = runBoard("non-jar", player, board);

    Assert.assertNull(result.getException());
    Assert.assertTrue(result.getScore() > 0);
  }

  @Test
  void when_walk_over_obstacle_then_success() {
    final String[][] test10x10Board = {//
        // 0...1....2....3....4....5....6....7....8....9
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}, // 0
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 1
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 2
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 3
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 4
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 5
        {"W", " ", " ", " ", " ", " ", "T", " ", " ", "W"}, // 6
        {"W", " ", " ", " ", " ", " ", "t", " ", " ", "W"}, // 7
        {"W", " ", " ", " ", " ", " ", "P", " ", " ", "W"}, // 8
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"} // 9
    };


    List<AssertableTurn> turns = new LinkedList<>(Arrays.asList(//
        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(8, 6), // cur pos
                Arrays.asList(rowCol(6, 6)), // treasures
                0, // obtained treasures
                40, // max steps
                40), // remaining steps
            board -> {
            }, //
            Move.NORTH //
        ), //

        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(7, 6), // cur pos
                Arrays.asList(rowCol(6, 6)), // treasures
                0, // obtained treasures
                40, // max steps
                40 - TREE_COST), // remaining steps
            board -> {
            }, //
            Move.NORTH //
        )));


    IPlayer player = new TestPlayerFixedMoves(turns, "when_walk_over_obstacle_then_success");
    MapBoard board = turns.get(0).getStartState().asBoard();
    TestResult result = runBoard("non-jar", player, board);

    Assert.assertNull(result.getException());
    Assert.assertTrue(result.getScore() > 0);
  }

  @Test
  void when_excpetion_on_move_then_exception_returned() {
    final String[][] test10x10Board = {//
        // 0...1....2....3....4....5....6....7....8....9
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"}, // 0
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 1
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 2
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 3
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 4
        {"W", " ", " ", " ", " ", " ", " ", " ", " ", "W"}, // 5
        {"W", " ", " ", " ", " ", " ", "T", " ", " ", "W"}, // 6
        {"W", " ", " ", " ", " ", " ", "t", " ", " ", "W"}, // 7
        {"W", " ", " ", " ", " ", " ", "P", " ", " ", "W"}, // 8
        {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"} // 9
    };


    List<AssertableTurn> turns = new LinkedList<>(Arrays.asList(//
        new AssertableTurn( //
            new ExpectedBoard(test10x10Board, //
                rowCol(8, 6), // cur pos
                Arrays.asList(rowCol(6, 6)), // treasures
                0, // obtained treasures
                40, // max steps
                40), // remaining steps
            board -> {
              throw new RuntimeException("Some code bug");
            }, //
            Move.NORTH //
        ) //
    ));


    IPlayer player =
        new TestPlayerFixedMoves(turns, "when_excpetion_on_move_then_exception_returned");
    MapBoard board = turns.get(0).getStartState().asBoard();
    TestResult result = runBoard("non-jar", player, board);

    Assert.assertNotNull(result.getException());
    Assert.assertEquals(0, result.getScore());
  }

}
