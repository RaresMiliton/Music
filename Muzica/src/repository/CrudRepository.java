package repository;

public interface CrudRepository<ID, E> {
    E findOne(ID id) throws Exception;
    Iterable<E> findAll() throws Exception;
    void save(E entity) throws Exception;
    void delete(ID id) throws Exception;
    E update(E entity) throws Exception;
    long size() throws Exception;
}