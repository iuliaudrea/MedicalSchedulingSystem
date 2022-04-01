package ro.unibuc.pao.persistence;

import ro.unibuc.pao.domain.Medic;

import java.util.Vector;

public class MedicRepository implements GenericRepository<Medic>{

    // vector static, deoarece vreau ca celelalte repository-uri sa-l poata accesa
    private static final Vector<Medic> storage = new Vector<Medic>();

    @Override
    public void add(Medic entity) {
        storage.add(entity);
    }

    @Override
    public Medic get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Medic entity) {
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

    public boolean medicExistsInRepo(Medic medic) {
        for(int i = 0; i < storage.size(); i++)
            if(storage.get(i).equals(medic))
                return true;
        return false;
    }
}
