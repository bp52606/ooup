package notepad.action;

public interface ILocalizationProvider {
	
	String getString(String key);
	void addLocalizationListener(ILocalizationListener l);
	void removeLocalizationListener(ILocalizationListener l);
	String getCurrentLanguage();

}
