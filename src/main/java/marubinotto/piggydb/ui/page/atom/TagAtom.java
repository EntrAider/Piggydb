package marubinotto.piggydb.ui.page.atom;

import java.util.List;

import marubinotto.piggydb.model.Fragment;
import marubinotto.piggydb.model.Tag;
import marubinotto.piggydb.model.entity.RawFilter;
import marubinotto.piggydb.ui.page.common.BorderPage;

public class TagAtom extends Atom {
	
	public Long id;
	
	private Tag tag;
	
	@Override
	protected void setFeedInfo() throws Exception {
		super.setFeedInfo();
		
		if (this.id == null) return;
		
		this.feedId = this.feedId + PARAM_PREFIX_IN_ID + this.id;
		appendQueryToUrls("?id=" + this.id);
			
		this.tag = getDomain().getTagRepository().get(this.id.longValue());
		if (this.tag == null) return;
		
		this.feedTitle = this.feedTitle + BorderPage.HTML_TITLE_SEP + this.tag.getName();
	}

	@Override
	protected List<Fragment> getFragments() throws Exception {
		if (this.tag == null) return null;
		
		RawFilter filter = new RawFilter();
		filter.getClassification().addTag(this.tag);
		return getDomain().getFragmentRepository().findByFilter(filter, this.fragmentsOptions);
	}
}
