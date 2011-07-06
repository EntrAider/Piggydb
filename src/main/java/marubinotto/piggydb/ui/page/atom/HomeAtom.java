package marubinotto.piggydb.ui.page.atom;

import java.util.List;

import marubinotto.piggydb.model.Fragment;

public class HomeAtom extends Atom {

	@Override
	protected List<Fragment> getFragments() throws Exception {
		return getDomain().getFragmentRepository().getFragments(this.fragmentsOptions);
	}
}
