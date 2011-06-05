package marubinotto.piggydb.model.fragments;

import static marubinotto.util.time.DateTime.setCurrentTimeForTest;
import static org.junit.Assert.assertEquals;
import marubinotto.piggydb.model.Fragment;
import marubinotto.piggydb.model.FragmentRepository;
import marubinotto.piggydb.model.FragmentsOptions;
import marubinotto.util.paging.Page;

import org.junit.Before;
import org.junit.Test;

/**
 * Get fragments order by update date desc.
 */
public class GetFragmentsTest extends FragmentRepositoryTestBase {
	
	protected long id1;
	protected long id2;
	protected long id3;
	
	public GetFragmentsTest(RepositoryFactory<FragmentRepository> factory) {
		super(factory);
	}
	
	@Before
	public void given() throws Exception {
		super.given();

		setCurrentTimeForTest(2008, 1, 1);
		this.id1 = this.object.register(newFragmentWithTitle("title1"));
		
		setCurrentTimeForTest(2008, 1, 2);
		this.id2 = this.object.register(newFragmentWithTitle("title2"));
		
		setCurrentTimeForTest(2008, 1, 3);
		this.id3 = this.object.register(newFragmentWithTitle("title3"));
		
		setCurrentTimeForTest(null);
	}

	@Test
	public void orderByUpdateDateDesc() throws Exception {
		// When
		Page<Fragment> page = this.object.getFragments(
			new FragmentsOptions(3, 0, false));
		
		// Then
		assertEquals(3, page.size());
		assertEquals(this.id3, page.get(0).getId().longValue());
		assertEquals(this.id2, page.get(1).getId().longValue());
		assertEquals(this.id1, page.get(2).getId().longValue());
	}
	
	@Test
	public void pageIndexOutOfBounds() throws Exception {
		this.object.getFragments(new FragmentsOptions(3, -1, false));
		this.object.getFragments(new FragmentsOptions(3, 10, false));
		// should not throw any exception
	}
	
	@Test
	public void shouldChangeOrderWhenUpdated() throws Exception {
		// Given
		setCurrentTimeForTest(2008, 1, 4);
		Fragment fragment = this.object.get(this.id1);
		fragment.touch(getPlainUser(), true);
		this.object.update(fragment);
		
		// When
		Page<Fragment> page = this.object.getFragments(
			new FragmentsOptions(3, 0, false));
		
		// Then
		assertEquals(3, page.size());
		assertEquals(this.id1, page.get(0).getId().longValue());
		assertEquals(this.id3, page.get(1).getId().longValue());
		assertEquals(this.id2, page.get(2).getId().longValue());
	}
}
