package marubinotto.piggydb.ui.page;

import marubinotto.piggydb.ui.page.common.AbstractBorderPage;
import marubinotto.util.time.DateTime;

public class AboutPage extends AbstractBorderPage {

	@Override
	protected boolean needsAuthentication() {
		return false;
	}

	public String cacheControlToken;

	@Override
	protected void setModels() throws Exception {
		super.setModels();

		importCss("style/piggydb-about.css", true, null);
		this.cacheControlToken = DateTime.getCurrentTime().format("yyyyMMdd");
	}
}
