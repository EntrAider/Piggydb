package marubinotto.piggydb.ui.page.partial;

import marubinotto.piggydb.model.Filter;
import marubinotto.piggydb.ui.page.FilterPage;

public class FragmentsByFilter extends AbstractFragments {

	public Long id;
	
	public Filter filter;
	
	@Override 
	protected void setFragments() throws Exception {
		if (this.id != null)
			this.filter = getDomain().getFilterRepository().get(this.id);
		else
			this.filter = (Filter)getContext().getSessionAttribute(FilterPage.SK_NEW_FILTER);
		if (this.filter == null) return;
		
		this.contextTags = this.filter.getClassification();
		
		marubinotto.piggydb.model.query.FragmentsByFilter query = 
			(marubinotto.piggydb.model.query.FragmentsByFilter)getQuery(
				marubinotto.piggydb.model.query.FragmentsByFilter.class);
		query.setFilter(this.filter);
		this.fragments = getPage(query);
	}
}
