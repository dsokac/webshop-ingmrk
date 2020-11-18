package hr.danisoka.webshopingmrk;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import hr.danisoka.webshopingmrk.models.Order;

public class MyEnumConverter implements UserType{

	@Override
	public int[] sqlTypes() {
		return new int[]{Types.OTHER};
	}

	@Override
	public Class returnedClass() {
		return Order.Status.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		String name = rs.getString(names[0]);
		return rs.wasNull() ? null : Order.Status.valueOf(Order.Status.class, name);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if(value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			st.setObject(index, ((Order.Status)value) , Types.OTHER);
		}
		
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Order.Status)value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

}
