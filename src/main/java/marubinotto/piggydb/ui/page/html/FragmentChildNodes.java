package marubinotto.piggydb.ui.page.html;

import java.util.List;

import marubinotto.piggydb.model.Fragment;
import marubinotto.piggydb.model.FragmentRelation;

public class FragmentChildNodes extends AbstractHtmlFragment {

	public Long id;
	public Long contextParentId;
	
	public Fragment fragment;
	public List<FragmentRelation> childRelations;
	
	@Override 
	protected void setModels() throws Exception {
		super.setModels();
		
		if (this.id == null) return;
		
		this.fragment = getDomain().getFragmentRepository().get(this.id.longValue());		
		this.childRelations = contextParentId != null ? 
			this.fragment.getChildRelations(this.contextParentId) :
			this.fragment.getChildRelations();
		
		setSelectedFragments();
	}
}
