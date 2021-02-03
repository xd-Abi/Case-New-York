package old.cny.player;

import old.cny.image.Image;
import old.cny.scenegraph.Node;

public class Player extends Node
{


    public Player()
    {
          super(new Image("icon.png"));

          AddComponent(new PlayerMovement());
    }
}
