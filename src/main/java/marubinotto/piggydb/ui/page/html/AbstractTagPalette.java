package marubinotto.piggydb.ui.page.html;

public abstract class AbstractTagPalette extends AbstractHtmlFragment {

	public String jsPaletteRef;
	public boolean enableClose = false;
	public String sessionName = null;
	
	public String viewType;
	
	@Override 
	protected void setModels() throws Exception {
		super.setModels();
		
		this.viewType = getViewType();
		if (this.sessionName != null) {
			getSession().getUiState().put(this.sessionName + ".viewType", this.viewType);
		}
	}
	
	protected abstract String getViewType();
}
