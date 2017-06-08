/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.battlesnake;

import com.battlesnake.data.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@RestController
public class RequestController {

  static String SNAKE_NAME = "All Your Snake";
  Snake ourSnake;
  Snake otherSnake;
  int height;
  int width;

  @RequestMapping(value="/start", method=RequestMethod.POST, produces="application/json")
  public StartResponse start(@RequestBody StartRequest request) {
    return new StartResponse()
      .setName(SNAKE_NAME)
      .setColor("#00ff00")
      .setHeadUrl("http://vignette1.wikia.nocookie.net/nintendo/images/6/61/Bowser_Icon.png/revision/latest?cb=20120820000805&path-prefix=en")
      .setHeadType(HeadType.FANG)
      .setTailType(TailType.CURLED)
      .setTaunt("BELONG TO US!");
  }

  @RequestMapping(value="/move", method=RequestMethod.POST, produces = "application/json")
  public MoveResponse move(@RequestBody MoveRequest request) {
    height = request.getHeight();
    width = request.getWidth();
    getSnakes(request.getSnakes());
    int food[][] = request.getFood();

    int head[] = ourSnake.getCoords()[0];
    int x = head[0];
    int y = head[1];

    ArrayList<Move> safeDirections = getSafeDirections(x, y, request.getSnakes());
    ArrayList<Snake> snakes = request.getSnakes();

    Move foodMove = getDirectFoodMove(request.getFood()[0]);

    Move getMove = bestMove(foodMove, safeDirections);

    return new MoveResponse()
      .setMove(foodMove)
      .setTaunt("Going Somewhere!");
  }

  private Move bestMove(Move foodMove, ArrayList<Move> safeDirections) {
    if (safeDirections.contains(foodMove)) {
      return foodMove;
    }
    if (foodMove == Move.UP) {
      if (safeDirections.contains(Move.LEFT)) {
        return Move.LEFT;
      }
      if (safeDirections.contains(Move.RIGHT)) {
        return Move.RIGHT;
      }
      return Move.DOWN;
    }
    if (foodMove == Move.DOWN) {
      if (safeDirections.contains(Move.LEFT)) {
        return Move.LEFT;
      }
      if (safeDirections.contains(Move.RIGHT)) {
        return Move.RIGHT;
      }
      return Move.UP;
    }

    if (foodMove == Move.LEFT) {
      if (safeDirections.contains(Move.UP)) {
        return Move.UP;
      }
      if (safeDirections.contains(Move.DOWN)) {
        return Move.DOWN;
      }
      return Move.RIGHT;
    }
    if (foodMove == Move.RIGHT) {
      if (safeDirections.contains(Move.UP)) {
        return Move.UP;
      }
      if (safeDirections.contains(Move.DOWN)) {
        return Move.DOWN;
      }
      return Move.LEFT;
    }
    return Move.UP;
  }

  private ArrayList<Move> getSafeDirections(int xhead, int yhead, ArrayList<Snake> snakes) {
    ArrayList<Move> directions = new ArrayList<>();
    directions.add(Move.UP);
    directions.add(Move.DOWN);
    directions.add(Move.LEFT);
    directions.add(Move.RIGHT);
    for (Snake snake: snakes) {
      for (int[] coords : snake.getCoords()) {
        if (coords == null) {
          continue;
        }
        //Check to the left
        if ((coords[0] == xhead -1 && coords[1] == yhead) || xhead == 0) {
          directions.remove(Move.LEFT);
        }
        //check to the right
        if ((coords[0] == xhead + 1 && coords[1] == yhead) || xhead == width - 1) {
          directions.remove(Move.RIGHT);
        }
        //check to the top
        if ((coords[1] == yhead - 1 && coords[0] == xhead) || yhead == 0) {
          directions.remove(Move.UP);
        }
        //check to the bottom
        if ((coords[1] == yhead + 1 && coords[0] == xhead) || yhead == height - 1) {
          directions.remove(Move.DOWN);
        }
      }
    }
    return directions;
  }

  private Move getDirectFoodMove(int[] foodPoint) {
    int x = ourSnake.getCoords()[0][0];
    int y = ourSnake.getCoords()[0][1];
    x -= foodPoint[0];
    y -= foodPoint[1];

    if (x != 0 && Math.abs(x) < Math.abs(y)) {
      if (x > 0) {
        return Move.LEFT;
      } else {
        return Move.RIGHT;
      }
    } else {
      if (y > 0) {
        return Move.UP;
      } else {
        return Move.DOWN;
      }
    }
  }

  private void getSnakes(ArrayList<Snake> snakes) {
    for (Snake snake : snakes) {
        if (snake.getName().equals(SNAKE_NAME)) {
          ourSnake = snake;
        } else {
          otherSnake = snake;
        }
    }
  }
    
  @RequestMapping(value="/end", method=RequestMethod.POST)
  public Object end() {
      // No response required
      Map<String, Object> responseObject = new HashMap<String, Object>();
      return responseObject;
  }

}
