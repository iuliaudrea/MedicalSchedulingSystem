package ro.unibuc.pao.persistence;

import ro.unibuc.pao.domain.Client;
import java.util.Vector;

public class ClientRepository implements GenericRepository<Client>{

    // vector static, deoarece vreau ca celelalte repository-uri sa-l poata accesa
    private static final Vector<Client> storage = new Vector<Client>();

    @Override
    public void add(Client entity) {
        storage.add(entity);
    }

    @Override
    public Client get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Client entity) {
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

    public boolean clientExistsInRepo(Client client) {
        for(int i = 0; i < storage.size(); i++)
            if(storage.get(i).equals(client))
                return true;
        return false;
    }
}
