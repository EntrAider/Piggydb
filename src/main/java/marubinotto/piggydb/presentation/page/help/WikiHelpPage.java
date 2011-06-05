package marubinotto.piggydb.presentation.page.help;

import marubinotto.piggydb.presentation.page.AbstractPage;

public class WikiHelpPage extends AbstractPage {

	public String lang;
	public String localizedPath;
	
	@Override 
	public String getPath() {
		if (this.lang == null) {
			return super.getPath();
		}
		
		if (this.localizedPath != null) {
			return this.localizedPath;
		}
		
		this.localizedPath = super.getPath().replaceAll("\\.htm", "-" + lang + ".htm");
		getLogger().debug("this.localizedPath: " + this.localizedPath);
		return this.localizedPath;
	}
	
	@Override 
	public void onRender() {
		disableClientCaching();
	}
}
