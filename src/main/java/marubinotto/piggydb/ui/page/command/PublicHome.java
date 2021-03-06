package marubinotto.piggydb.ui.page.command;

import marubinotto.piggydb.model.Filter;
import marubinotto.piggydb.model.Fragment;
import marubinotto.piggydb.model.Tag;
import marubinotto.piggydb.model.entity.RawFilter;
import marubinotto.piggydb.model.query.FragmentsByFilter;
import marubinotto.piggydb.ui.page.DocumentViewPage;
import marubinotto.util.paging.Page;

public class PublicHome extends AbstractCommand {
	
	@Override
	protected boolean needsAuthentication() {
		return false;
	}

	@Override 
	protected void execute() throws Exception {
		Filter filter = createPublicHomeFilter();
		if (filter == null) return;
		
		FragmentsByFilter query = (FragmentsByFilter)
			getDomain().getFragmentRepository().getQuery(FragmentsByFilter.class);
		query.setFilter(filter);	
		Page<Fragment> fragments = query.getPage(1, 0);
		
		if (fragments.isEmpty()) {
			getLogger().debug("Public home fragments not found");
			return;
		}
		
		Long publicHomeId = fragments.get(0).getId();
		getLogger().info("publicHomeId: " + publicHomeId);
		
		DocumentViewPage documentView = (DocumentViewPage) 
			getContext().createPage(DocumentViewPage.class);
		documentView.id = publicHomeId;
		setForward(documentView);
	}
	
	private Filter createPublicHomeFilter() throws Exception {
		Tag publicTag = getDomain().getTagRepository().getByName(Tag.NAME_PUBLIC);
		Tag homeTag = getDomain().getTagRepository().getByName(Tag.NAME_HOME);
		if (publicTag == null || homeTag == null) {
			getLogger().debug("Missing needed tags");
			return null;
		}
		
		RawFilter filter = new RawFilter();
		filter.getClassification().addTag(publicTag);
		filter.getClassification().addTag(homeTag);
		return filter;
	}
}
