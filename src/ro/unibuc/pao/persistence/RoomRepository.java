package ro.unibuc.pao.persistence;

import ro.unibuc.pao.domain.Room;

import java.util.Vector;

public class RoomRepository implements GenericRepository<Room>{

    // vector static, deoarece vreau ca celelalte repository-uri sa-l poata accesa
    private static final Vector<Room> storage = new Vector<Room>();

    @Override
    public void add(Room entity) {
        storage.add(entity);
    }

    @Override
    public Room get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Room entity) {
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

//    public boolean RoomExistsInRepo(Room room) {
//        for(int i = 0; i < storage.size(); i++)
//            if(storage.get(i).equals(room))
//                return true;
//        return false;
//    }
}
