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

  @RequestMapping(value="/start", method=RequestMethod.POST, produces="application/json")
  public StartResponse start(@RequestBody StartRequest request) {
    return new StartResponse()
      .setName(SNAKE_NAME)
      .setColor("#FFAA00")
      .setHeadUrl("http://vignette1.wikia.nocookie.net/nintendo/images/6/61/Bowser_Icon.png/revision/latest?cb=20120820000805&path-prefix=en")
      .setHeadType(HeadType.FANG)
      .setTailType(TailType.CURLED)
      .setTaunt("BELONG TO US!");
  }

  @RequestMapping(value="/move", method=RequestMethod.POST, produces = "application/json")
  public MoveResponse move(@RequestBody MoveRequest request) {
    int height = request.getHeight();
    int width = request.getWidth();
    getSnakes(request.getSnakes());
    int food[][] = request.getFood();

    int head[] = ourSnake.getCoords()[0];
    int x = head[0];
    int y = head[1];

    ArrayList<Move> safeDirections = getSafeDirections(x, y, request.getSnakes());


    ArrayList<Snake> snakes = request.getSnakes();
    return new MoveResponse()
      .setMove(Move.DOWN)
      .setTaunt("Going Down!");
  }

  private ArrayList<Move> getSafeDirections(int xhead, int yhead, ArrayList<Snake> snakes) {
    ArrayList<Move> directions = new ArrayList<>();
    for (Snake snake: snakes) {
      for (int[] coords : snake.getCoords()) {

      }
    }
    return directions;
  }

  private void getSnakes(ArrayList<Snake> snakes) {
    for (Snake snake : snakes) {
        if (snake.getName().equals("All Your Base Snake")) {
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
