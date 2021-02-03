package old.cny.player;
/*
import net.cny.platform.Keyboard;
import net.cny.scenegraph.NodeComponent;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class PlayerMovement extends NodeComponent
{

    private final float speed;

    public PlayerMovement()
    {
        speed = 0.4f;
    }

    @Override
    public void Update(float delta)
    {
        Vector3f position = getParent().GetTranslation();

        if (Keyboard.IsKeyHold(GLFW.GLFW_KEY_A) && !(position.x <= -1))
        {
            position.set(position.x -= speed * delta, position.y, position.z);
        }

        if (Keyboard.IsKeyHold(GLFW.GLFW_KEY_D) && !(position.x >= 1))
        {
            position.set(position.x += speed * delta, position.y, position.z);
        }

        getParent().SetTranslation(position);
    }

    @Override
    public String GetType() {
        return "player-movement-component";
    }
}
*/