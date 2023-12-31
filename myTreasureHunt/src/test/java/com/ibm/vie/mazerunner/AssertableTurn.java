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

import java.util.function.Consumer;

public class AssertableTurn {
  private final ExpectedBoard startState;
  private final Move move;
  private final Consumer<IBoard> assertions;


  public AssertableTurn(ExpectedBoard startState, Consumer<IBoard> assertions, Move move) {
    this.startState = startState;
    this.move = move;
    this.assertions = assertions;
  }

  public void assertTurn(IBoard other) {
    startState.assertBoardIsAsExpected(other);
    assertions.accept(other);
  }

  public Move getMove() {
    return move;
  }

  public ExpectedBoard getStartState() {
    return startState;
  }
}
