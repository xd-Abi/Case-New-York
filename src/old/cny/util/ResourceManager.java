package old.cny.util;

public class ResourceManager 
{
	
	public static class MainMenu 
	{
		public static final String BACKGROUND = "menu/main/background.png";
		public static final String BACKGROUND_1 = "menu/main/background.png";
		public static final String TITLE = "menu/main/title.png";
		public static final String PLAY_BUTTON = "menu/main/play-button.png";
		public static final String SETTINGS_BUTTON = "menu/main/settings-button.png";
		public static final String QUIT_BUTTON = "menu/main/quit-button.png";

		public static final String BACKGROUND_AUDIO = "menu/main/background_audio.wav";
	}

	public static class PauseMenu
	{
		public static final String RESUME_BUTTON = "menu/pause/resume-button.png";
		public static final String BACKGROUND = "menu/pause/background.png";
	}

	public static class ButtonAudio
	{
		public static final String CLICK_SOUND = "audio/button/click_sound.wav";
	}
	
	// Default Object and Texture Shader:
	
	public static final String DEFAULT_VERTEX_SHADER = "shader/default-vertex.net";
	public static final String DEFAULT_FRAGMENT_SHADER = "shader/default-fragment.net";
}