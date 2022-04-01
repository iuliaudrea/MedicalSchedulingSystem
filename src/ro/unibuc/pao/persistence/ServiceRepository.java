package ro.unibuc.pao.persistence;

import ro.unibuc.pao.domain.Service;

import java.util.Vector;

public class ServiceRepository implements GenericRepository<Service>{

    // vector static, deoarece vreau ca celelalte repository-uri sa-l poata accesa
    private static final Vector<Service> storage = new Vector<Service>();

    @Override
    public void add(Service entity) {
        storage.add(entity);
    }

    @Override
    public Service get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Service entity) {
        storage.set(index, entity);
    }

    @Override
    public void delete(int index) {
        storage.remove(index);
    }

    @Override
    public int getSize() {
        return storage.size();
    }

    public boolean serviceExistsInRepo(Service service) {
        for(int i = 0; i < storage.size(); i++)
            if(storage.get(i).equals(service))
                return true;
        return false;
    }
}
