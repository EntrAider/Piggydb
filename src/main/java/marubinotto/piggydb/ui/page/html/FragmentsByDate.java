package marubinotto.piggydb.ui.page.html;

import marubinotto.piggydb.model.enums.FragmentField;
import marubinotto.piggydb.ui.page.control.CalendarFocus;

public class FragmentsByDate extends AbstractFragments {

	public String date;

	@Override 
	protected void setFragments() throws Exception {
		CalendarFocus calendarFocus = CalendarFocus.parseString(this.date);
		if (calendarFocus != null) {
			this.fragments = getFragmentRepository().findByTime(
				calendarFocus.toInterval(),
				FragmentField.UPDATE_DATETIME,
				this.options);
		}
		else {
			this.fragments = getFragmentRepository().getFragments(this.options);
		}
	}
}
