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

import java.util.List;
import org.junit.Assert;

class TestPlayerFixedMoves implements IPlayer {

  private List<AssertableTurn> turns;
  private int turnNumber = 0;
  private final String name;

  public TestPlayerFixedMoves(List<AssertableTurn> turns, String name) {
    this.turns = turns;
    this.name = name;
  }

  @Override
  public String getName() {
    return "test player";
  }

  @Override
  public void analyzeBoard(IAnalysisBoard board) {}

  @Override
  public Move selectMove(IBoard board) {
    System.err.println(name + ": Selecting move number " + this.turnNumber++);
    if (!turns.isEmpty()) {
      AssertableTurn turn = turns.remove(0);
      turn.assertTurn(board);
      return turn.getMove();
    } else {
      throw new RuntimeException("No more moves!");
    }
  }

  @Override
  public void gameCompleted(IBoard board) {
    System.err.println("Game completed");
    Assert.assertTrue(turns.isEmpty());
  }

}
