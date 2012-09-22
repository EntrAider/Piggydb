package marubinotto.piggydb.model.base;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import marubinotto.piggydb.model.auth.User;
import marubinotto.piggydb.model.entity.RawEntity;
import marubinotto.piggydb.model.entity.RawEntityFactory;
import marubinotto.piggydb.model.exception.BaseDataObsoleteException;
import marubinotto.util.Assert;

public interface Repository<E extends Entity> {

	public long size() throws Exception;

	public long register(E entity) throws Exception;
	
	public E get(long id) throws Exception;
	
	public boolean update(E entity) 
	throws BaseDataObsoleteException, Exception;
	
	public void delete(long id, User user) throws Exception;
	
	public Map<Long, String> getNames(Set<Long> ids) throws Exception;
	
	public Query<E> getQuery(Class<? extends Query<E>> queryType)
	throws Exception;
	
	
	public static abstract class Base<E extends Entity, R extends RawEntity> 
	implements Repository<E>, RawEntityFactory<R> {
		
		public void delete(long id, User user) throws Exception {
			Assert.Arg.notNull(user, "user");
			
			E entity = get(id);
			if (entity == null) return;
			entity.ensureCanDelete(user);
			doDelete(entity, user);
		}
		
		protected abstract void doDelete(E entity, User user) throws Exception;
		
		
		// Query
		
		private LinkedList<Class<? extends Query<E>>> queryImpls = 
			new LinkedList<Class<? extends Query<E>>>();
		
		public void registerQuery(Class<? extends Query<E>> queryImpl) {
			Assert.Arg.notNull(queryImpl, "queryImpl");
			
			this.queryImpls.addFirst(queryImpl);
		}
		
		public Query<E> getQuery(Class<? extends Query<E>> queryType) 
		throws InstantiationException, IllegalAccessException {
			Assert.Arg.notNull(queryType, "queryType");
			
			for (Class<? extends Query<E>> impl : this.queryImpls) {
				if (queryType.isAssignableFrom(impl)) {
					Query<E> query = impl.newInstance();
					query.setRepository(this);
					return query;
				}
			}
			return null;
		}
	}
}
