package net.cny.gui;

public class GuiAnimatedButton extends GuiButton
{

    private final float p1;
    private float xPos;
    private boolean isFinished;

    public GuiAnimatedButton(String texturePath, float p1, float p2, float s1, float s2, float xPos)
    {
        super(texturePath, xPos, p2, s1, s2);
        this.p1 = p1;
        this.xPos = xPos;
        isFinished = false;
    }

    @Override
    public void Update()
    {

        super.Update();

        if (xPos != p1 && !(xPos > p1))
        {
            isFinished = false;
            xPos += 0.05f;
        }

        SetTranslation(xPos + GetScaling().x  / 2, GetTranslation().y + GetScaling().y  / 2, 0);
    }
}
