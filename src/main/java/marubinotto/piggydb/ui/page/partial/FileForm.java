package marubinotto.piggydb.ui.page.partial;

import marubinotto.util.message.CodedException;

public class FileForm extends AbstractFragmentForm {

	public String title;
	
	@Override 
	protected void setModels() throws Exception {
		super.setModels();
		
		if (!canUploadFile())
			throw new CodedException("no-authority-for-page");
		
		if (this.fragment.getId() != null) {
			this.title = getMessage("edit-fragment");
		}
		else {
			this.title = getMessage("add-file");
		}
	}
}
