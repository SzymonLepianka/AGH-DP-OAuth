package Group28.OAuth.DAO;

public interface IDataBaseAccessObject<T> {
    T create(T object);
    T read(T object);
    T update(T object);
    void delete(T object);
}
