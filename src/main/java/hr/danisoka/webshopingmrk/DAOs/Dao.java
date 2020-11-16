package hr.danisoka.webshopingmrk.DAOs;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {

	List<T> getAll();
	Optional<T> getById(ID id) throws Exception;
	T save(T entity) throws Exception;
	T update(T entity, ID id) throws Exception;
	T deleteById(ID id) throws Exception;
	void deleteAll();	
}
