package repository;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import validation.*;

public abstract class AbstractCrudRepository<ID, E extends HasId<ID>> implements CrudRepository<ID, E> {
    protected Map<ID, E> entities;
    private IValidation<E> validator;
    protected String filename;
    private String objtype;

    protected void writeData() throws IOException {
        try {
            BufferedWriter writer=new BufferedWriter(new FileWriter(filename));
            for(E item: entities.values()) {
                writer.write(item.toString());
                writer.newLine();
            }
            writer.close();
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void readData() throws Exception {
        entities.clear();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = br.readLine()) != null){
                Class<?> itemClass = Class.forName(objtype);
                Object item = itemClass.newInstance();
                Method method = itemClass.getDeclaredMethod("buildObject", String.class);
                E obj = (E)method.invoke(item, line);
                entities.put(obj.getId(),obj);
            }
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    public AbstractCrudRepository(IValidation<E> validator,String filename,String objtype) throws Exception {
        this.objtype = objtype;
        this.entities = new HashMap<>();
        this.validator = validator;
        this.filename = filename;
        readData();
    }

    @Override
    public E findOne(ID id) throws Exception {
        readData();
        return this.entities.get(id);
    }

    @Override
    public Iterable<E> findAll() throws Exception {
        readData();
        return this.entities.values();
    }

    @Override
    public void save(E entity) throws Exception {
        this.validator.validate(entity);
        if (this.entities.putIfAbsent(entity.getId(),entity)!=null) {
            throw new Exception("Duplicate entity");
        }
        writeData();
    }

    @Override
    public void delete(ID id) throws Exception {
        this.entities.remove(id);
        writeData();
    }

    @Override
    public E update(E entity) throws Exception {
        try {
            this.validator.validate(entity);
            if(findOne(entity.getId()) == null){
                //throw new Exception("Student does not exist");
                return null;
            }
            else {
                this.entities.replace(entity.getId(), entity);
                writeData();
                return entity;
            }
        }
        catch (ValidationException exc){
            throw new Exception("Invalid entity.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long size() throws Exception {
        readData();
        return this.entities.size();
    }
}
