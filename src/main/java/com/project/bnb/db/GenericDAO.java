package com.project.bnb.db;

import java.util.Collection;

import org.hibernate.SessionFactory;

import com.google.common.base.Optional;
import com.project.bnb.core.AbstractModel;

import io.dropwizard.hibernate.AbstractDAO;

public abstract class GenericDAO<T extends AbstractModel> extends AbstractDAO<T> {

	protected GenericDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public void saveOrUpdate(T model) {
		persist(model);
	}

	public void saveOrUpdate(Collection<T> models) {
		models.forEach(m -> persist(m));
	}

	public Optional<T> findById(Long id) {
		return Optional.fromNullable(get(id));
	}

	public void delete(T object) {
		if (object != null) {
			currentSession().delete(object);
		}
	}

	public int delete(Collection<T> objects) {
		objects.forEach(o -> delete(o));
		return objects.size();
	}

}
