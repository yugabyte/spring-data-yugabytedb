package com.yugabyte.data.relational.core.dialect;

import java.util.List;

import org.springframework.data.relational.core.dialect.LockClause;
import org.springframework.data.relational.core.dialect.PostgresDialect;
import org.springframework.data.relational.core.sql.IdentifierProcessing;
import org.springframework.data.relational.core.sql.LockOptions;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.data.relational.core.sql.Table;

/**
 * An SQL dialect for YugabyteDB.
 *
 * @author Nikhil Chandrappa
 * @since 2.3
 */
public class YugabyteDbDialect extends PostgresDialect {
	
	public static final YugabyteDbDialect INSTANCE = new YugabyteDbDialect();
	
	protected YugabyteDbDialect() {}
	
	private final YugabyteDbLockClause LOCK_CLAUSE = new YugabyteDbLockClause(this.getIdentifierProcessing());
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.relational.core.dialect.Dialect#lock()
	 */
	@Override
	public LockClause lock() {
		return LOCK_CLAUSE;
	}
	
	static class YugabyteDbLockClause implements LockClause {

		private final IdentifierProcessing identifierProcessing;

		YugabyteDbLockClause(IdentifierProcessing identifierProcessing) {
			this.identifierProcessing = identifierProcessing;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.core.dialect.LockClause#getLock(LockOptions)
		 */
		@Override
		public String getLock(LockOptions lockOptions) {

			List<Table> tables = lockOptions.getFrom().getTables();
			if (tables.isEmpty()) {
				return "";
			}

			// get the first table and obtain last part if the identifier is a composed one.
			SqlIdentifier identifier = tables.get(0).getName();
			SqlIdentifier last = identifier;

			for (SqlIdentifier sqlIdentifier : identifier) {
				last = sqlIdentifier;
			}

			// without schema
			String tableName = last.toSql(this.identifierProcessing);

			switch (lockOptions.getLockMode()) {

				case PESSIMISTIC_WRITE:
					return "FOR UPDATE OF " + tableName;

				case PESSIMISTIC_READ:
					return "FOR SHARE OF " + tableName;

				default:
					return "";
			}
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.relational.core.dialect.LockClause#getClausePosition()
		 */
		@Override
		public Position getClausePosition() {
			return Position.AFTER_ORDER_BY;
		}
	};
	

}
