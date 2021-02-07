package net.cny.scenegraph;

import java.util.HashMap;
import java.util.Map;

import net.cny.core.scenegraph.Node;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;

public class SecondLevel extends Scenegraph{

	@Override
	public Map<String, Node> CreateMap() {
		
		Map<String, Node> map = new HashMap<>();
		map.put("test", new GuiBackground("menu/main/background.png"));
		return map;
	}

}
