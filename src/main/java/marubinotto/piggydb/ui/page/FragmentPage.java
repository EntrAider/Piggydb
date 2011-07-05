package marubinotto.piggydb.ui.page;

import marubinotto.piggydb.model.Fragment;
import marubinotto.piggydb.model.Tag;
import marubinotto.piggydb.model.TagRepository;
import marubinotto.piggydb.ui.page.common.AbstractFragmentsPage;
import marubinotto.piggydb.ui.page.common.PageUrl;
import marubinotto.piggydb.ui.page.common.Utils;
import marubinotto.piggydb.ui.page.control.FragmentFormPanel;
import marubinotto.piggydb.ui.page.control.TagTree;
import marubinotto.piggydb.ui.page.control.form.SingleTagForm;
import marubinotto.piggydb.ui.page.model.RecentlyViewed;
import marubinotto.util.Assert;
import net.sf.click.control.HiddenField;
import net.sf.click.extras.tree.Tree;

import org.apache.commons.lang.StringUtils;

public class FragmentPage extends AbstractFragmentsPage {

	@Override
	protected PageUrl createThisPageUrl() {
		PageUrl pageUrl = super.createThisPageUrl();
		if (this.fragment != null) 
			pageUrl.parameters.put(PN_FRAGMENT_ID, this.fragment.getId());
		return pageUrl;
	}

	//
	// Input
	//

	public static final String PN_FRAGMENT_ID = "id";
	public Long id;

	public Fragment fragment;

	public Fragment getFragment() {
		return this.fragment;
	}

	@Override
	protected boolean onPreInit() throws Exception {
		initControls();
		setTargetFragment();
		if (this.fragment != null || getContext().isAjaxRequest()) {
			return true;
		}
		else {
			getLogger().debug("Missing a target fragment.");
			if (this.id != null) {
				setRedirectWithMessage(HomePage.class, getMessage("no-such-fragment", this.id));
			}
			else {
				setRedirect(HomePage.class);
			}
			return false;
		}
	}

	private void setTargetFragment() throws Exception {
		if (this.id != null) {
			this.fragment = getDomain().getFragmentRepository().get(this.id.longValue());
		}
	}

	//
	// Control
	//

	private FragmentFormPanel fragmentFormPanel;
	private FragmentFormPanel subFragmentFormPanel;

	@Override
	public void onInit() {
		super.onInit();
		if (this.fragment != null) applyTargetFragmentToControls();
	}

	private void initControls() {
		// Fragment
		this.fragmentFormPanel = createFragmentFormPanel();
		this.fragmentFormPanel.setTitle(getMessage("FragmentPage-edit-this-fragment"));
		if (StringUtils.isNotBlank(getContext().getRequestParameter("edit"))) {
			this.fragmentFormPanel.setActive(true);
		}

		// Super tag
		this.superTagForm.setListenerForAdd("onAddSuperTagClick");
		this.superTagForm.setListenerForDelete("onRemoveSuperTagClick");
		this.superTagForm.initialize();
		this.superTags = new TagTree("superTags", this.resources, this.html);
		addControl(this.superTags);

		// Sub fragment
		this.subFragmentFormPanel = createFragmentFormPanel("subFragmentFormPanel");
		this.subFragmentFormPanel.setTitle(getMessage("FragmentPage-create-new-related-fragment"));
		this.subFragmentFormPanel.setRestoresScrollTop(true);
	}

	private void applyTargetFragmentToControls() {
		Assert.Property.requireNotNull(fragment, "fragment");

		this.fragmentFormPanel.setBaseData(this.fragment);
		this.fragmentFormPanel.setRedirectPathAfterRegistration(this.thisPageUrl.getPagePath());

		for (Tag tag : this.fragment.getClassification()) {
			if (isInheritedByDefault(tag)) this.subFragmentFormPanel.addDefaultTag(tag);
		}
		this.subFragmentFormPanel.setRedirectPathAfterRegistration(this.thisPageUrl.getPagePath());
		this.subFragmentFormPanel.setParentId(this.fragment.getId());

		TagTree.restoreTagTree(this.superTags, this.fragment, getUser());
	}

	private static boolean isInheritedByDefault(Tag tag) {
		if (tag.getName().startsWith("#")) return false;
		return true;
	}

	// Super tag

	public SingleTagForm superTagForm = new SingleTagForm(this);
	private Tree superTags;

	public boolean onAddSuperTagClick() throws Exception {
		Assert.Property.requireNotNull(fragment, "fragment");

		if (!this.superTagForm.isValid()) {
			return true;
		}

		String tagName = this.superTagForm.tagField.getValue();
		if (StringUtils.isBlank(tagName)) {
			return true;
		}

		getLogger().info("Adding a super-tag: " + tagName + " to: " + this.fragment.getId());
		TagRepository tagRepository = getDomain().getTagRepository();
		try {
			this.fragment.addTagByUser(tagName, tagRepository, getUser());
		}
		catch (Exception e) {
			Utils.handleFormError(e, this.superTagForm, this);
			return true;
		}
		saveFragment(this.fragment);

		highlightFragment(this.fragment.getId());
		setRedirectToThisPage();
		return false;
	}

	public boolean onRemoveSuperTagClick() throws Exception {
		Assert.Property.requireNotNull(fragment, "fragment");

		String tagToRemove = this.superTagForm.tagToDeleteField.getValue();
		if (StringUtils.isBlank(tagToRemove)) {
			return true;
		}

		this.fragment.removeTagByUser(tagToRemove, getUser());
		saveFragment(this.fragment);

		highlightFragment(this.fragment.getId());
		setRedirectToThisPage(getMessage("completed-remove-tag",
			new Object[]{this.html.linkToTag(tagToRemove), this.html.fragmentInMessage(this.fragment)}, false));
		return false;
	}

	//
	// Model
	//

	@Override
	protected void setModels() throws Exception {
		super.setModels();
		Assert.Property.requireNotNull(fragment, "fragment");

		String fragmentTitle = this.fragment.makeHeadline();
		this.htmlTitle = this.htmlTitle + HTML_TITLE_SEP + (fragmentTitle != null ? fragmentTitle : "");

		importCssFile("style/piggydb-fragment.css", true, null);
		importJsFile("scripts/piggydb-fragment.js", true);

		getRecentlyViewed().add(
			new RecentlyViewed.Entity(RecentlyViewed.TYPE_FRAGMENT, this.fragment.getId()));

		setCommonSidebarModels();
	}

	@Override
	public void onRender() {
		super.onRender();
		embedCurrentStateInParameters();
	}

	private void embedCurrentStateInParameters() {
		Assert.Property.requireNotNull(fragment, "fragment");

		// For forms
		this.fragmentFormPanel.fragmentForm.add(new HiddenField(PN_FRAGMENT_ID, this.fragment.getId()));
		this.subFragmentFormPanel.fragmentForm.add(new HiddenField(PN_FRAGMENT_ID, this.fragment.getId()));
		this.superTagForm.add(new HiddenField(PN_FRAGMENT_ID, this.fragment.getId()));

		addParameterToCommonForms(PN_FRAGMENT_ID, this.fragment.getId());
	}
}
