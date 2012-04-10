package marubinotto.piggydb.ui.page;

import marubinotto.piggydb.ui.page.common.AbstractBorderPage;

public class TagsPage extends AbstractBorderPage {

	public long tagCount = 0;;

	@Override 
	protected void setModels() throws Exception {
		super.setModels();
		
		importCss("style/piggydb-tags.css", true, null);
		importCss("click/tree/tree.css", false, null);		
		importBottomJs("js/piggydb.widget.tags.js", true);
		
		this.tagCount = getDomain().getTagRepository().size();
	}
}
